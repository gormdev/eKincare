package com.message.messageitems;

import android.animation.ValueAnimator;
import android.content.Context;
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
import com.message.viewholder.IncomingCardWithListViewHolder;
import com.message.viewholder.MessageViewHolder;

import java.util.ArrayList;

/**
 * Created by matthewpage on 6/21/16.
 */
public class IncomingCardWithListMessage extends MessageItem {

    ArrayList<TestComponentData> mTestList;
    private Context context;
    private Adapter adapter;
    private CardWithListButtonClickEvent cardWithListButtonClickEvent;
    private CardViewModel cardViewModel;

    public IncomingCardWithListMessage(Context context, ArrayList<TestComponentData> list, CardWithListButtonClickEvent cardWithListButtonClickEvent, CardViewModel cardViewModel)
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
        //System.out.println("IncomingCardWithListMessage.buildMessageItem =="+ (messageViewHolder instanceof IncomingCardWithListViewHolder));
        if (messageViewHolder != null && messageViewHolder instanceof IncomingCardWithListViewHolder) {
            //System.out.println("IncomingCardWithListMessage.buildMessageItem");
            final IncomingCardWithListViewHolder incomingCardWithListViewHolder = (IncomingCardWithListViewHolder) messageViewHolder;

            incomingCardWithListViewHolder.listView.setAdapter(adapter);
            setListViewHeightBasedOnChildren(incomingCardWithListViewHolder.listView);

            incomingCardWithListViewHolder.textViewButton1.setText(cardViewModel.getButtonText1());
            incomingCardWithListViewHolder.textViewButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardWithListButtonClickEvent.onCardWithListButtonItemClick(cardViewModel.getButtonAction1());
                }
            });

            incomingCardWithListViewHolder.textViewButton2.setText(cardViewModel.getButtonText2());
            incomingCardWithListViewHolder.textViewButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardWithListButtonClickEvent.onCardWithListButtonItemClick(cardViewModel.getButtonAction2());
                }
            });

            incomingCardWithListViewHolder.textViewHeader.setText(cardViewModel.getTitle());
        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.INCOMING_CARD_WITH_LIST;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.GENERAL;
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
                convertView = inflater.inflate( R.layout.raw_assessment_new_test, null );
                holder.mTextViewLabComponentNameText = (TextView) convertView.findViewById(R.id.fragment_assessment_new_test_component_name_text);
                holder.mTextViewResultValueText = (TextView) convertView.findViewById(R.id.fragment_assessment_new_result_value_text);
                holder.mTextViewPressureUnit = (TextView) convertView.findViewById(R.id.fragment_assessment_new_result_unit_text);
                holder.mTextViewLabNameText = (TextView) convertView.findViewById(R.id.fragment_assessment_new_lab_test_name_text);
                holder.mLinearHeaderLayout = (LinearLayout) convertView.findViewById(R.id.fragment_assessment_new_header_layout);
                holder.mTextViewIdealRangeText = (TextView) convertView.findViewById(R.id.fragment_assessment_ideal_range_value);
                holder.mLinearRowLayout = (LinearLayout) convertView.findViewById(R.id.fragment_assessment_new_linear_card);
                holder.divider = (ImageView) convertView.findViewById(R.id.divider);
                convertView.setTag(holder);
                System.out.println("Adapter.getView pos="+position);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if(position == mTestList.size()-1){
                holder.divider.setVisibility(View.GONE);
            }

            if (mTestList.get(position).getTest_component_name().length() > 21) {
                holder.mTextViewLabComponentNameText.setText(mTestList.get(position).getTest_component_name().subSequence(0, 21) + "...");
            } else {
                holder.mTextViewLabComponentNameText.setText(mTestList.get(position).getTest_component_name());
            }

            holder.mTextViewPressureUnit.setText(mTestList.get(position).getUnits());

            holder.mTextViewResultValueText.setText(mTestList.get(position).getResult());

            // holder.mTextViewResultValueText.setText(cardViewModelArrayList.get(position).getResult());
            holder.mTextViewIdealRangeText.setText(mTestList.get(position).getIdealRange());

            if (mTestList.get(position).getHeader() != null && !mTestList.get(position).getHeader().equalsIgnoreCase("")) {
                holder.mTextViewLabNameText.setText(mTestList.get(position).getHeader());
                holder.mLinearHeaderLayout.setVisibility(View.VISIBLE);
            } else {
                holder.mLinearHeaderLayout.setVisibility(View.GONE);
            }

            if (mTestList.get(position).getColor() != null && !mTestList.get(position).getColor().equalsIgnoreCase("")) {
                if (mTestList.get(position).getColor().contains("success")) {
                    holder.mTextViewResultValueText.setTextColor(context.getResources().getColor(R.color.green));
                    holder.mTextViewPressureUnit.setTextColor(context.getResources().getColor(R.color.green));

                } else if (mTestList.get(position).getColor().contains("danger")) {
                    holder.mTextViewResultValueText.setTextColor(context.getResources().getColor(R.color.red));
                    holder.mTextViewPressureUnit.setTextColor(context.getResources().getColor(R.color.red));

                } else if(mTestList.get(position).getColor().contains("warning")) {
                    holder.mTextViewResultValueText.setTextColor(context.getResources().getColor(R.color.yellow));
                    holder.mTextViewPressureUnit.setTextColor(context.getResources().getColor(R.color.yellow));

                }else{
                    holder.mTextViewResultValueText.setTextColor(context.getResources().getColor(R.color.yellow));
                    holder.mTextViewPressureUnit.setTextColor(context.getResources().getColor(R.color.yellow));
                }
            }
            return convertView;
        }

        public class ViewHolder {
            TextView mTextViewLabComponentNameText;
            TextView mTextViewResultValueText;
            TextView mTextViewIdealRangeText;
            TextView mTextViewPressureUnit;
            TextView mTextViewLabNameText;

            LinearLayout mLinearHeaderLayout;
            LinearLayout mLinearRowLayout;

            ImageView divider;
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