package com.example.locationword.locationword.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.locationword.locationword.R;

public class MeFragment extends Fragment {
    public View onCreateView(LayoutInflater layoutinflater, ViewGroup vg, Bundle bundle){
        View v= View.inflate(getContext(), R.layout.me_fragment,null);
        return v;
    }
}
