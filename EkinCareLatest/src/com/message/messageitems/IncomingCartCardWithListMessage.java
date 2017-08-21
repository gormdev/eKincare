package com.message.messageitems;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ekincare.R;
import com.message.model.CardViewModel;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.model.PackageItem;
import com.message.viewholder.IncomingCardWithListViewHolder;
import com.message.viewholder.MessageViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewpage on 6/21/16.
 */
public class IncomingCartCardWithListMessage extends MessageItem {

    List<PackageItem> cardViewModelArrayList;
    private Context context;
    private Adapter adapter;

    public IncomingCartCardWithListMessage(Context context, List<PackageItem> list)
    {
        super();
        this.context = context;
        this.cardViewModelArrayList = list;
        adapter = new Adapter();
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder)
    {
        System.out.println("IncomingCardWithListMessage.buildMessageItem =="+ (messageViewHolder instanceof IncomingCardWithListViewHolder));
        if (messageViewHolder != null && messageViewHolder instanceof IncomingCardWithListViewHolder)
        {
            final IncomingCardWithListViewHolder incomingCardWithListViewHolder = (IncomingCardWithListViewHolder) messageViewHolder;

            incomingCardWithListViewHolder.listView.setAdapter(adapter);
            setListViewHeightBasedOnChildren(incomingCardWithListViewHolder.listView);

            int total = 0;
            for(int i=0;i<cardViewModelArrayList.size();i++){
                int tempamount = Integer.parseInt(cardViewModelArrayList.get(i).getSelling_price());
                total = total + tempamount;
            }

            incomingCardWithListViewHolder.textViewButton2.setText("Total Amount: ");
            incomingCardWithListViewHolder.textViewButton1.setText(context.getResources().getString(R.string.Rs) + total);

            incomingCardWithListViewHolder.textViewHeader.setText("Your cart:");
        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.INCOMING_CART_WITH_LIST;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.GENERAL;
    }

    public List<PackageItem> getList() {
        return cardViewModelArrayList;
    }

    public class Adapter extends BaseAdapter {

        ViewHolder holder = null;

        @Override
        public int getCount() {
            return cardViewModelArrayList.size();
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
                convertView = inflater.inflate( R.layout.item_message_cart_item, null );
                holder.mTextViewName = (TextView) convertView.findViewById(R.id.textview_cart_item);
                holder.view=(View) convertView.findViewById(R.id.divider_for_med);
                holder.mTextViewPrice = (TextView) convertView.findViewById(R.id.textview_cart_price);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.mTextViewName.setText(cardViewModelArrayList.get(position).getName());
            holder.mTextViewPrice.setText(context.getResources().getString(R.string.Rs) + cardViewModelArrayList.get(position).getSelling_price());

            if(position == (cardViewModelArrayList.size()-1)){
                holder.view.setVisibility(View.INVISIBLE);
            }else{
                holder.view.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        public class ViewHolder {
            TextView mTextViewName;
            TextView mTextViewPrice;
            View view;
        }

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
}