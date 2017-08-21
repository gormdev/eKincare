package com.message.messageitems;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ekincare.R;
import com.message.model.CardViewModel;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.model.PackageItem;
import com.message.utility.Config;
import com.message.viewholder.IncomingCardWithListViewHolder;
import com.message.viewholder.IncomingHorizontalCardListViewHolder;
import com.message.viewholder.MessageViewHolder;
import com.oneclick.ekincare.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewpage on 6/21/16.
 */
public class IncomingHorizontalPackageCardListMessage extends MessageItem {

    List<PackageItem> list;
    private Context context;
    private Adapter adapter;
    IncomingCardWithListViewHolder incomingCardWithListViewHolder;

    public IncomingHorizontalPackageCardListMessage(List<PackageItem> list, Context context)
    {
        super();
        this.context = context;
        this.list = list;
        adapter = new Adapter();
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder) {
        if (messageViewHolder != null && messageViewHolder instanceof IncomingCardWithListViewHolder)
        {
            incomingCardWithListViewHolder = (IncomingCardWithListViewHolder) messageViewHolder;

            incomingCardWithListViewHolder.listView.setAdapter(adapter);
            setListViewHeightBasedOnChildren(incomingCardWithListViewHolder.listView);

            incomingCardWithListViewHolder.dividerBottom.setVisibility(View.GONE);
            incomingCardWithListViewHolder.textViewButton1.setVisibility(View.GONE);
            incomingCardWithListViewHolder.textViewButton2.setVisibility(View.GONE);
            incomingCardWithListViewHolder.textViewHeader.setText("Packages");
            incomingCardWithListViewHolder.textViewHeader.setTextColor(Color.parseColor("#4a4a4a"));
            incomingCardWithListViewHolder.textViewHeader.setAllCaps(true);
        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.INCOMING_PACKAGE;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.GENERAL;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        android.widget.ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
        listView.setLayoutParams(params);
    }

    public List<PackageItem> getList() {
        return list;
    }

    public class Adapter extends BaseAdapter {

        ViewHolder holder = null;

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                convertView = inflater.inflate( R.layout.item_message_incoming_package, null );

                holder.textviewTitle = (TextView) convertView.findViewById(R.id.textview_title);
                holder.textviewTestNumber = (TextView) convertView.findViewById(R.id.textview_test_number);
                holder.textviewOriginal = (TextView) convertView.findViewById(R.id.textview_original);
                holder.textviewDiscounted = (TextView) convertView.findViewById(R.id.textview_discounted);
                holder.imageInfo = (ImageView) convertView.findViewById(R.id.image_info);
                holder.divider = (View) convertView.findViewById(R.id.divider);
                convertView.setTag(holder);
                System.out.println("Adapter.getView pos="+position);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if(position == (list.size()-1)){
                holder.divider.setVisibility(View.GONE);
            }else{
                holder.divider.setVisibility(View.VISIBLE);
            }

            holder.textviewTitle.setText(list.get(position).getName());
            if(list.get(position).getTests_count().equalsIgnoreCase("0")){
                holder.textviewTestNumber.setVisibility(View.INVISIBLE);
            }else{
                holder.textviewTestNumber.setVisibility(View.VISIBLE);
                holder.textviewTestNumber.setText(list.get(position).getTests_count() + " test");
            }

            holder.textviewOriginal.setText(context.getResources().getString(R.string.Rs) +list.get(position).getMrp());
            holder.textviewOriginal.setPaintFlags(holder.textviewOriginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textviewDiscounted.setText(context.getResources().getString(R.string.Rs) +list.get(position).getSelling_price());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(list.get(position).getPackage_info(),list.get(position).getName());
                }
            });

            return convertView;
        }

        private void showDialog(String errorMessage, String title) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle(title);
            builder.setMessage(errorMessage);

            String positiveText = "Okay";
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            // display dialog
            dialog.show();
        }

        public class ViewHolder {
            TextView textviewTitle;
            TextView textviewTestNumber;
            TextView textviewOriginal;
            TextView textviewDiscounted;
            ImageView imageInfo;
            View divider;
        }

    }

}