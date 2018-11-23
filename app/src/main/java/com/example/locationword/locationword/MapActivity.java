package com.example.locationword.locationword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class MapActivity extends AppCompatActivity {
    private MapView mMapView = null;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private LocationClientOption locationOption = new LocationClientOption();
    private BaiduMap bm;
    private boolean isFirst=true;
    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.map_activity);
        initView();
        addListener();
        initBapMap();
        initLocationOption();

    }
    protected void addListener(){
        mLocationClient.registerLocationListener(myListener);
    }
    protected void initView(){
        mMapView = (MapView) findViewById(R.id.bmapView);
        bm =mMapView.getMap();
        bm.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类

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
        MyLocationData locData = new MyLocationData.Builder().accuracy(radius)
                .direction(100).latitude(lat).longitude(lon).build();

        bm.setMyLocationData(locData);//给地图设置定位数据，这样地图就显示位置了
// 开启定位图层
        if (isFirst){
            isFirst=false;
            LatLng ll = new LatLng(lat,lon);
            MapStatus.Builder builder = new MapStatus.Builder();
            //设置缩放中心点；缩放比例；
            builder.target(ll).zoom(10.0f);
            //给地图设置状态
            bm.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mLocationClient.stop();
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
        locationOption.setNeedDeviceDirect(false);
//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
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

}
