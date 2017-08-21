package com.message.messageitems;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.util.Base64;
import android.view.View;

import com.bumptech.glide.Glide;
import com.ekincare.R;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.utility.Config;
import com.message.viewholder.MessageViewHolder;
import com.message.viewholder.OutgoingImageViewHolder;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

/**
 * Created by matthewpage on 6/21/16.
 */
public class OutgoingImageMessage extends MessageItem {

    private String path;
    private  Bitmap bitmap;
    private int sendingStatus;
    OutgoingImageViewHolder incomingImageViewHolder;
    boolean flag = false;

    public OutgoingImageMessage(String path, Bitmap bitmap)
    {
        super();
        this.path=path;
        this.bitmap=bitmap;
    }
    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder) {
        if(messageViewHolder != null && (messageViewHolder instanceof OutgoingImageViewHolder)){
            incomingImageViewHolder = (OutgoingImageViewHolder) messageViewHolder;

            if(bitmap!=null){
                incomingImageViewHolder.glideRoundedImageView.setImageBitmap(bitmap);
            }

            if(flag){
                incomingImageViewHolder.progressBar.setVisibility(View.VISIBLE);
            }else {
                incomingImageViewHolder.progressBar.setVisibility(View.GONE);
            }

            switch (sendingStatus){
                case Config.MESSAGE_SENT:
                    incomingImageViewHolder.circleImageViewTick.setImageResource((R.drawable.ic_done_white_24dp));
                    break;
                case Config.MESSAGE_DEIVERED:
                    incomingImageViewHolder.circleImageViewTick.setImageResource((R.drawable.ic_done_all_white_24px));
                    break;
                case Config.MESSAGE_FAILED:
                    incomingImageViewHolder.circleImageViewTick.setImageResource((R.drawable.ic_cloud_off_black_24px));
                    break;
                default:
                    break;
            }
        }
    }

    public void showProgress(boolean flag){
        this.flag = flag;
    }

    public void setStatus(int status){
        this.sendingStatus = status;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.OUTGOING_IMAGE;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.SELF;
    }

    public String getBitmap() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}