package com.example.locationword.locationword.bean;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.model.LatLng;
import com.example.locationword.locationword.clusterutil.clustering.ClusterItem;

public class LocationItem implements ClusterItem {
    LatLng la;

    public LatLng getLa() {
        return la;
    }

    public void setLa(LatLng la) {
        this.la = la;
    }

    public LocationItem(LatLng la) {
        this.la = la;
    }

    @Override
    public LatLng getPosition() {
        return la;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        return null;
    }
}
