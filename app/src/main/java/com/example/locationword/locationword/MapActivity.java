package com.example.locationword.locationword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;

public class MapActivity extends AppCompatActivity {
    private MapView mMapView = null;
    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.map_activity);
        mMapView = (MapView) findViewById(R.id.bmapView);
        BaiduMap bm=mMapView.getMap();
        bm.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        bm.setMyLocationEnabled(true);
        //23.0978918427,113.3096980622
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(10)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(113.3096980622)
                .longitude(23.0978918427).build();
        // 设置定位数据
        bm.setMyLocationData(locData);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
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
}
