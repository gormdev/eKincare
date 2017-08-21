package com.message.view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ekincare.R;
import com.google.android.gms.maps.MapView;

/**
 * Created by Ajay on 18-01-2017.
 */

public class MapViewListItemView extends LinearLayout {

    public MapView mMapView;

    public MapViewListItemView(Context context) {
        this(context, null);
    }

    public MapViewListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_message_incoming_map, this);
        mMapView = (MapView) view.findViewById(R.id.map);
        setOrientation(VERTICAL);
    }

    public void mapViewOnCreate(Bundle savedInstanceState) {
        if (mMapView != null) {
            mMapView.onCreate(savedInstanceState);
        }
    }
    public void mapViewOnResume() {
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    public void mapViewOnDestroy() {
        if (mMapView != null) {
            try {
                mMapView.onDestroy();
            } catch (NullPointerException e) {
                Log.e("MAP", "Error while attempting MapView.onDestroy(), ignoring exception", e);
            }
        }
    }

    public void mapViewOnLowMemory() {
        if (mMapView != null) {
            mMapView.onLowMemory();
        }
    }

    public void mapViewOnSaveInstanceState(Bundle outState) {
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }
}
