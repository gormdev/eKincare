package com.message.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.ekincare.R;
import com.oneclick.ekincare.vo.ChatFeaturesItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by RaviTejaN on 01-12-2016.
 */

public class ChatListFeatures extends  ArrayAdapter<ChatFeaturesItem> {

        Context context;
    private LayoutInflater inflator;

public ChatListFeatures(Context context, int resourceId,
        List<ChatFeaturesItem> items) {
        super(context, resourceId, items);
        this.context = context;
    inflator = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ChatFeaturesItem rowItem = getItem(position);
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.chat_box_additional_features_list, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.chat_text_lable_list);
            holder.imageView = (ImageView) convertView.findViewById(R.id.chat_list_dialog_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(rowItem.getTitle().equalsIgnoreCase("Show my father's health status")){
            holder.txtTitle.setText(Html.fromHtml("Show my <b>father's</b> health status"));
        }else if(rowItem.getTitle().equalsIgnoreCase("Show blood glucose results")){
            holder.txtTitle.setText(Html.fromHtml("Show <b>blood glucose</b> results"));
        }else if(rowItem.getTitle().equalsIgnoreCase("Book Video call with a doctor")){
            holder.txtTitle.setText(Html.fromHtml("Book <b>video call</b> with a doctor"));
        }else {
            holder.txtTitle.setText(rowItem.getTitle()) ;
         }

        if (holder.imageView != null)
            holder.imageView.setImageResource(rowItem.getImageId());

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
    }
}