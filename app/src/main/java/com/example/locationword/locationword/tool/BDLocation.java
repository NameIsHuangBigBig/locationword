package com.example.locationword.locationword.tool;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.locationword.locationword.MapActivity;
import com.example.locationword.locationword.event.LocationEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.logging.Handler;

public class BDLocation {
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private LocationClientOption locationOption = new LocationClientOption();
    private double lat=0;
    private double lon=0;
    public BDLocation(Context context){
        mLocationClient = new LocationClient(context);
        mLocationClient.registerLocationListener(myListener);
        initLocationOption();
    }
    private void initLocationOption() {
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("bd0911");
//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(0);
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
        public void onReceiveLocation(com.baidu.location.BDLocation location) {

            if (location.getLocType() == com.baidu.location.BDLocation.TypeGpsLocation){// GPS定位结果

                Log.i("location","gps定位成功");
            } else if (location.getLocType() == com.baidu.location.BDLocation.TypeNetWorkLocation){// 网络定位结果
                Log.i("location","网络定位成功");
            } else if (location.getLocType() == com.baidu.location.BDLocation.TypeOffLineLocation) {// 离线定位结果
                Log.i("location","离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == com.baidu.location.BDLocation.TypeServerError) {
                Log.i("location","服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == com.baidu.location.BDLocation.TypeNetWorkException) {
                Log.i("location","网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == com.baidu.location.BDLocation.TypeCriteriaException) {
                Log.i("location","无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f
            Log.i("firstLocation","纬度："+latitude+"\t经度："+longitude+"\t精度："+radius);
            EventBus.getDefault().post(new LocationEvent(latitude,longitude));
//            lat=latitude;
//            lon=longitude;
            //  float direction = location.getDirection();
            //sendLocation(radius,longitude,latitude);
        }

        public void onReceivePoi(com.baidu.location.BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }
    public double getFirstLat(){
        return lat;
    }
    public double getFirstLon(){
        return lon;
    }
    public void stop(){
        mLocationClient.unRegisterLocationListener(myListener);
        mLocationClient.stop();
    }
}
