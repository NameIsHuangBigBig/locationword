package com.example.locationword.locationword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.adapter.IBRoutePlanListener;
import com.baidu.mapapi.bikenavi.model.BikeRoutePlanError;
import com.baidu.mapapi.bikenavi.params.BikeNaviLaunchParam;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.example.locationword.locationword.Thread.GetLocationThread;
import com.example.locationword.locationword.bean.LocationItem;
import com.example.locationword.locationword.clusterutil.clustering.ClusterManager;
import com.example.locationword.locationword.event.GroupUpdateEvent;
import com.example.locationword.locationword.event.MapEvent;
import com.example.locationword.locationword.http.API;
import com.example.locationword.locationword.http.HttpUtil;
import com.example.locationword.locationword.myview.MapInfoView;
import com.example.locationword.locationword.tool.Constant;
import com.example.locationword.locationword.tool.JSONChange;
import com.example.locationword.locationword.tool.ShowUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends AppCompatActivity {
    private MapView mMapView = null;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private LocationClientOption locationOption = new LocationClientOption();
    private BaiduMap bm;
    //ClusterManager
    private String InGroupMan;
    private boolean isFirst=true;
    private Context context = MapActivity.this;
    private String TAG = "MapActivity";
    private double Jundgelat=0;
    private double Jundgelon=0;
    private double tolat=0;
    private double tolon=0;
    private WalkNaviLaunchParam param;
    private BikeNaviLaunchParam bparam;
    private boolean lastLocation=false;
    private String userId;
    ClusterManager mClusterManager;
    List<LocationItem> items = new ArrayList<>();
    private List<OverlayOptions> options = new ArrayList<OverlayOptions>();
    private List<GetLocationThread>locationArray = new ArrayList<>();
    private InfoWindow minfoWindow;
    private boolean myLocation=true;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1000:
                    String result = (String)msg.obj;
                    JsonObject jo =JSONChange.StringToJsonObject(result);
                    Log.i(TAG,jo.get("message").getAsString());
                    break;
                case 1001:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ShowUtil.showText(context,"网络异常！");
                        }
                    });
                    break;
                case 500:
                    String getResult = (String)msg.obj;
                    Log.i("ssss",getResult+"");
                    if(getResult.indexOf("message")<0){
                        JsonArray Getja = JSONChange.StringToJsonArrary(getResult);
                        analysisLocationA(Getja);
                    }

//                    if (Getjo.get("message")!=null){
//                        if(Getjo.get("message").equals("用户不在线")){
//                            if(!lastLocation){
//                                final double lat = Getjo.get("lat").getAsDouble();
//                                final double lon = Getjo.get("lon").getAsDouble();
//                               // addUserLocationImage(lat,lon);
//                                lastLocation=true;
//                                Log.i(TAG,"用户离线：最后一次位置！");
//                            }
//                        }
//                        Log.i(TAG,"用户"+Getjo.get("userId").getAsString()+"定位：失败，不存在");
//                    }else{
//                        final double lat = Getjo.get("lat").getAsDouble();
//                        final double lon = Getjo.get("lon").getAsDouble();
//                        final String aAuserId = Getjo.get("userId").getAsString();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                addUserLocationImage(lat,lon);
//                                //addUserLocationL(1.0,lat,lon);
//                            }
//                        });
//                        Log.i(TAG,"用户"+Getjo.get("userId").getAsString()+"定位：经度："+Getjo.get("lon").getAsString()+"纬度："+Getjo.get("lat").getAsString());
//                    }
                   break;
            }
        }
    };
    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.map_activity);
        //SDKInitializer.initialize(getApplicationContext());
        initView();

        addListener();
        initBapMap();
        initLocationOption();
        requestUserLocation();
    }
    protected void requestUserLocation(){
        JsonArray ja = JSONChange.StringToJsonArrary(InGroupMan);
        GetLocationThread glt = new GetLocationThread(ja,handler,userId);
        glt.startLocation();
        locationArray.add(glt);
    }
    protected void addListener(){
        mLocationClient.registerLocationListener(myListener);
        bm.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
               // Log.i("mapstatus", "==-->滚动状态开始");
            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
                Log.i("mapstatus", "==-->滚动状态改变开始");
                myLocation=false;

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                Log.i("mapstatus", "==-->滚动状态中");
                myLocation=false;
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
              //  Log.i("mapstatus", "==-->滚动状态结束");
                myLocation=true;
            }
        });
        bm.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                myLocation=false;
                tolat=marker.getPosition().latitude;
                tolon=marker.getPosition().longitude;
                locationArray.get(0).setIsstoplocation(true);
                Log.i("ClickMarker",marker.getTitle()+
                        "lat"+marker.getPosition().latitude);
                LatLng pt = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                minfoWindow = new InfoWindow(new MapInfoView(context,marker.getTitle()), pt, -47);
                bm.showInfoWindow(minfoWindow);
                //bm.hideInfoWindow();
//在地图上添加该文字对象并显示
               // bm.addOverlay(textOption);
                return true;
            }
        });

    }
    protected void initView(){
        userId = getSharedPreferences(Constant.logindata,MODE_PRIVATE)
                .getString(Constant.UserId,"");
        mMapView = (MapView) findViewById(R.id.bmapView);
        bm =mMapView.getMap();
        mClusterManager = new ClusterManager<>(this, bm);
        bm.setMyLocationEnabled(true);
        // 初始化点聚合管理类
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        InGroupMan = getIntent().getStringExtra("InGroupMan");

    }
    protected void initBapMap(){

// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        bm.setMyLocationConfigeration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.COMPASS,
                true,
                null));
        //bm.setMyLocationEnabled(true);
    }
    protected void sendLocation(float radius,double lon,double lat){

       //给地图设置定位数据，这样地图就显示位置了
// 开启定位图层
        if(Jundgelat!=lat||Jundgelon!=lon){
            MyLocationData locData = new MyLocationData.Builder().accuracy(radius)
                    .direction(100).latitude(lat).longitude(lon).build();
            if(myLocation){
                bm.setMyLocationData(locData);
            }
            Log.i(TAG,"检测位置结果：不一样");
            HashMap<String,String> map = new HashMap<>();
            map.put("userId",getSharedPreferences(Constant.logindata,MODE_PRIVATE
            ).getString(Constant.UserId,""));
            map.put("lat",lat+"");
            map.put("lon",lon+"");
            HttpUtil.getInstence().doPost(API.updateUserLocation,map,handler);
        }else{
            Log.i(TAG,"检测位置结果：一样");
        }
        Jundgelat = lat ;
        Jundgelon = lon ;
        if (isFirst){
            Log.i("first","第一次定位");

            isFirst=false;
            LatLng ll = new LatLng(lat,lon);
            MapStatus.Builder builder = new MapStatus.Builder();
            //设置缩放中心点；缩放比例；
            builder.target(ll).zoom(100.0f);
            //给地图设置状态

            bm.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
        // 当不需要定位图层时关闭定位图层

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//订阅
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mLocationClient.unRegisterLocationListener(myListener);
        mLocationClient.stop();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<locationArray.size();i++){
                    locationArray.get(i).setIsstoplocation(true);
                    locationArray.get(i).stopLocation();
                }
            }
        }).start();
        bm.setMyLocationEnabled(false);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    private void initLocationOption() {
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("bd0911");
//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(3000);
//可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
//可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);
//可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(true);
//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(false);
//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true);
//可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false);
//可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationOption.setIsNeedAltitude(false);
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000,1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        mLocationClient.setLocOption(locationOption);
        mLocationClient.start();
        mLocationClient.requestLocation();
    }
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果

                Log.i("location","gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
                Log.i("location","网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                Log.i("location","离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                Log.i("location","服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                Log.i("location","网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                Log.i("location","无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f
            Log.i("location","纬度："+latitude+"\t经度："+longitude+"\t精度："+radius);
            float direction = location.getDirection();
            sendLocation(radius,longitude,latitude);
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }
    public void addUserLocationImage(double lat,double lon,String userId){

        LatLng point = new LatLng(lat, lon);
//构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.ease_icon_marka);

//构建MarkerOption，用于在地图上添加Marker
        Bundle b = new Bundle();
        b.putString("userId",userId);
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .perspective(true)
                .anchor(1.0f,10.0f)
                .title(userId)
                .extraInfo(b);
        //在地图上添加Marker，并显示
        Log.i(TAG,"添加");
        options.add(option);




        items.add(new LocationItem(point));

        //mClusterManager.addItems(items);
       // bm.clear();
      //  bm.addOverlay(options);
    }
    protected void analysisLocationA(JsonArray ja){
        options.clear();
        items.clear();
        for (int i = 0;i<ja.size();i++){
            JsonObject jo = ja.get(i).getAsJsonObject();
             if(!jo.get("userId").getAsString().equals(userId)){
                if(jo.get("isOnLine").getAsString().equals("1")){
                    addUserLocationImage(jo.get("lat").getAsDouble(),
                            jo.get("lon").getAsDouble(),
                            jo.get("userId").getAsString());
                }
            }

        }
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
        if(locationArray.get(0).getLocation()){
            bm.clear();
        }

//            }
//        });

        Log.i("clear","clear");
        bm.addOverlays(options);
        mClusterManager.addItems(items);
    }
    protected void iniBDaohanEngine(){
        try {
            BikeNavigateHelper.getInstance().initNaviEngine(this, new IBEngineInitListener() {
                @Override
                public void engineInitSuccess() {
                    Log.d(TAG, "BikeNavi engineInitSuccess");
                    routePlanWithBikeParam();
                }

                @Override
                public void engineInitFail() {
                    Log.d(TAG, "BikeNavi engineInitFail");
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "startBikeNavi Exception");
            e.printStackTrace();
        }
    }
    protected void routePlanWithBikeParam(){
        BikeNavigateHelper.getInstance().routePlanWithParams(bparam, new IBRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Log.d("bdaohan", "BikeNavi onRoutePlanStart");
            }

            @Override
            public void onRoutePlanSuccess() {
                Log.d("bdaohan", "BikeNavi onRoutePlanSuccess");
                Intent intent = new Intent();
                intent.setClass(MapActivity.this, BNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(BikeRoutePlanError error) {
                Log.d("bdaohan", "BikeNavi onRoutePlanFail");
            }

        });
    }
    protected void iniDaoHanEngine(){
        WalkNavigateHelper.getInstance().initNaviEngine(MapActivity.this, new IWEngineInitListener() {
            @Override
            public void engineInitSuccess() {
                Log.d("ENGINE", "引擎初始化成功");
                routePlanWithParam();
            }

            @Override
            public void engineInitFail() {
                Log.d("ENGINE", "引擎初始化失败");
            }
        });
    }
    public void routePlanWithParam() {
        WalkNavigateHelper.getInstance().routePlanWithParams(param, new IWRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Log.d("ENGINE", "开始算路");
            }

            @Override
            public void onRoutePlanSuccess() {
                Log.d("ENGINE", "算路成功,跳转至诱导页面");
                Intent intent = new Intent();
                intent.setClass(MapActivity.this, WNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError error) {
                Log.d("ENGINE", "算路失败");
            }

        });
    }
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);//订阅
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(MapEvent event) {
        if (event.isClose().equals("close")){
            bm.hideInfoWindow();
            myLocation=true;
            locationArray.get(0).setIsstoplocation(false);
        }else if(event.isClose().equals("daohan")){
            LatLng  startPt = new LatLng(Jundgelat,Jundgelon);
            LatLng  endPt = new LatLng(tolat, tolon);
            param = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
            iniDaoHanEngine();
        }else if (event.isClose().equals("qxdaohan")){
            LatLng  startPt = new LatLng(Jundgelat,Jundgelon);
            LatLng  endPt = new LatLng(tolat, tolon);
            bparam = new BikeNaviLaunchParam().stPt(startPt).endPt(endPt);
            iniBDaohanEngine();
        }
    }
}
