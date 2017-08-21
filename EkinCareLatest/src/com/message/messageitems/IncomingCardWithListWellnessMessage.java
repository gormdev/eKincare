package com.message.messageitems;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ekincare.R;
import com.message.custominterface.CardWithListButtonClickEvent;
import com.message.model.CardViewModel;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.model.TestComponentData;
import com.message.model.VaccaniationDataModel;
import com.message.model.WellnessDataModel;
import com.message.viewholder.IncomingCardWithListViewHolder;
import com.message.viewholder.IncomingCardWithVaccaniationViewHolder;
import com.message.viewholder.MessageViewHolder;

import org.joda.time.Period;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by RaviTejaN on 12-12-2016.
 */

public class IncomingCardWithListWellnessMessage extends MessageItem {

    ArrayList<WellnessDataModel> mTestList;
    private Context context;
    private Adapter adapter;
    private CardWithListButtonClickEvent cardWithListButtonClickEvent;
    private CardViewModel cardViewModel;

    public IncomingCardWithListWellnessMessage(Context context, ArrayList<WellnessDataModel> list, CardWithListButtonClickEvent cardWithListButtonClickEvent, CardViewModel cardViewModel)
    {
        super();
        this.context = context;
        this.mTestList = list;
        adapter = new Adapter();
        this.cardWithListButtonClickEvent = cardWithListButtonClickEvent;
        this.cardViewModel=cardViewModel;
        System.out.println("IncomingCardWithListMessage.IncomingCardWithListMessage list"+list.size());
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder)
    {
        System.out.println("IncomingCardWithListMessage.buildMessageItem =="+ (messageViewHolder instanceof IncomingCardWithListViewHolder));
        if (messageViewHolder != null && messageViewHolder instanceof IncomingCardWithListViewHolder) {
            System.out.println("IncomingCardWithListMessage.buildMessageItem");
            final IncomingCardWithListViewHolder incomingCardWithListViewHolder = (IncomingCardWithListViewHolder) messageViewHolder;

            incomingCardWithListViewHolder.listView.setAdapter(adapter);
            setListViewHeightBasedOnChildren(incomingCardWithListViewHolder.listView);

            incomingCardWithListViewHolder.textViewHeader.setText(cardViewModel.getTitle());
        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.INCOMING_CARD_WITH_WELLNESS_LIST;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.BOT;
    }

    public class Adapter extends BaseAdapter {

        ViewHolder holder = null;

        @Override
        public int getCount() {
            return mTestList.size();
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
                convertView = inflater.inflate( R.layout.item_message_wellness_row, null );
                holder.typeName = (TextView) convertView.findViewById(R.id.type_name);
                holder.typeDescription = (TextView) convertView.findViewById(R.id.type_description);
                convertView.setTag(holder);
                System.out.println("Adapter.getView pos="+position);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.typeName.setText(mTestList.get(position).getType());
            holder.typeDescription.setText(mTestList.get(position).getValue());
            if(mTestList.get(position).getValue().equalsIgnoreCase("Never")){
                holder.typeDescription.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            }else if(mTestList.get(position).getValue().equalsIgnoreCase("Occasionally")){
                holder.typeDescription.setTextColor(ContextCompat.getColor(context, R.color.red));

            }else if(mTestList.get(position).getValue().equalsIgnoreCase("Frequently")){
                holder.typeDescription.setTextColor(ContextCompat.getColor(context, R.color.yellow));
            }

            return convertView;
        }

        public class ViewHolder {
            TextView typeName;
            TextView typeDescription;
        }

    }

    private void startCountAnimation(int value,final TextView textview) {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, value);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textview.setText("" + (int) animation.getAnimatedValue());
            }
        });
        animator.start();
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