package com.message.viewholder;

import android.content.Context;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekincare.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.message.view.MapViewListItemView;


/**
 * Created by John C. Hunchar on 5/12/16.
 */
public class OutgoingMapViewHolder extends MessageViewHolder implements OnMapReadyCallback {
    protected GoogleMap mGoogleMap;

    private Context mContext;
    private LatLng mMapLocation;

    private MapViewListItemView mMapViewListItemView;

    public OutgoingMapViewHolder(MapViewListItemView itemView, FragmentActivity context) {
        super(itemView);
        mContext = context;

        mMapViewListItemView = itemView;

        //System.out.println("IncomingTextViewHolder.IncomingTextViewHolder");
        mMapViewListItemView.mMapView.getMapAsync(this);
    }

    public void mapViewListItemViewOnResume() {
        if (mMapViewListItemView != null) {
            mMapViewListItemView.mapViewOnResume();
        }
    }

    public void setMapLocation(LatLng mapLocation) {
        //System.out.println("IncomingMapViewHolder.setMapLocation mapLocation="+mapLocation.latitude);
        mMapLocation = mapLocation;

        // If the map is ready, update its content.
        if (mGoogleMap != null) {
            updateMapContents();
        }
    }

    protected void updateMapContents() {
        //System.out.println("IncomingMapViewHolder.updateMapContents");
        mGoogleMap.clear();
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mMapLocation.latitude,mMapLocation.longitude)));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(mMapLocation.latitude,mMapLocation.longitude), 17f);
        mGoogleMap.moveCamera(cameraUpdate);
        //mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //System.out.println("IncomingMapViewHolder.onMapReady");
        mGoogleMap = googleMap;

        MapsInitializer.initialize(mContext);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        if (mMapLocation != null) {
            updateMapContents();
        }
    }
}
