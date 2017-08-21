package com.message.messageitems;

import android.content.Context;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.viewholder.OutgoingMapViewHolder;
import com.message.viewholder.MessageViewHolder;

/**
 * Created by matthewpage on 6/21/16.
 */
public class OutgoingMapMessage extends MessageItem
{

    private final Context context;
    private LatLng location;

    public OutgoingMapMessage(Context context, LatLng location)
    {
        super();
        this.context = context;
        this.location=location;
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder)
    {
        if(messageViewHolder != null && (messageViewHolder instanceof OutgoingMapViewHolder))
        {
            System.out.println("IncomingMapViewHolder.buildMessageItem = "+ location.latitude) ;
            final OutgoingMapViewHolder outgoingMapViewHolder = (OutgoingMapViewHolder) messageViewHolder;

            outgoingMapViewHolder.mapViewListItemViewOnResume();
            outgoingMapViewHolder.setMapLocation(location);
        }


    }

    @Override
    public MessageType getMessageType() {
        return MessageType.OUTGOING_MAP;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.SELF;
    }

    public double getLat() {
        return location.latitude;
    }

    public double getLong() {
        return location.longitude;
    }
}