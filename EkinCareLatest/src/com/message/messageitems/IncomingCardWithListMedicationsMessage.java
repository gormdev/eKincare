package com.message.messageitems;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ekincare.R;
import com.message.custominterface.CardWithListButtonClickEvent;
import com.message.custominterface.HorizontalCardListItemClickEvent;
import com.message.custominterface.SchemaClickEvent;
import com.message.model.CardViewModel;
import com.message.model.MedicationsChatModel;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.model.TestComponentData;
import com.message.viewholder.IncomingCardWithListViewHolder;
import com.message.viewholder.MessageViewHolder;

import org.joda.time.Period;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.R.id.list;

/**
 * Created by RaviTejaN on 08-12-2016.
 */

public class IncomingCardWithListMedicationsMessage extends MessageItem {

    ArrayList<MedicationsChatModel> mTestList;
    private Context context;
    private Adapter adapter;
    private CardWithListButtonClickEvent cardWithListButtonClickEvent;
    private CardViewModel cardViewModel;
    HorizontalCardListItemClickEvent horizontalCardListItemClickEvent;
    SchemaClickEvent schemaClickEvent;


    public IncomingCardWithListMedicationsMessage(Context context, ArrayList<MedicationsChatModel> list, CardWithListButtonClickEvent cardWithListButtonClickEvent, CardViewModel cardViewModel,SchemaClickEvent schemaClickEvent,HorizontalCardListItemClickEvent horizontalCardListItemClickEvent)
    {
        super();
        this.context = context;
        this.mTestList = list;
        adapter = new Adapter();
        this.cardWithListButtonClickEvent = cardWithListButtonClickEvent;
        this.cardViewModel=cardViewModel;
        this.schemaClickEvent = schemaClickEvent;
        this.horizontalCardListItemClickEvent=horizontalCardListItemClickEvent;

           }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder)
    {
        System.out.println("IncomingCardWithListMessage.buildMessageItem =="+ (messageViewHolder instanceof IncomingCardWithListViewHolder));
        if (messageViewHolder != null && messageViewHolder instanceof IncomingCardWithListViewHolder) {
            System.out.println("IncomingCardWithListMessage.buildMessageItem");
            final IncomingCardWithListViewHolder incomingCardWithListViewHolder = (IncomingCardWithListViewHolder) messageViewHolder;
            System.out.println("IncomingCardWithListMessage.IncomingCardWithListMessage list"+mTestList.size());
            incomingCardWithListViewHolder.listView.setAdapter(adapter);
            setListViewHeightBasedOnChildren(incomingCardWithListViewHolder.listView);
            incomingCardWithListViewHolder.textViewButton1.setVisibility(View.VISIBLE);
            incomingCardWithListViewHolder.textViewButton1.setText(cardViewModel.getButtonText1());
            incomingCardWithListViewHolder.textViewButton2.setVisibility(View.VISIBLE);
            incomingCardWithListViewHolder.textViewHeader.setVisibility(View.GONE);
            incomingCardWithListViewHolder.textViewHeader.setText(cardViewModel.getTitle());
            incomingCardWithListViewHolder.divider.setVisibility(View.GONE);
            incomingCardWithListViewHolder.textViewButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isSchemaPresent(cardViewModel.getButtonAction1())){
                        String id = cardViewModel.getButtonAction1().substring(cardViewModel.getButtonAction1().indexOf("@")+1,cardViewModel.getButtonAction1().indexOf("/"));
                        String transitionScreen = cardViewModel.getButtonAction1().substring(cardViewModel.getButtonAction1().indexOf("/")+1);

                        schemaClickEvent.onSchemaItemClick(id,transitionScreen);
                    }else{
                        horizontalCardListItemClickEvent.onHorizontalCardListItemClick(cardViewModel.getButtonAction1());
                    }
                }
            });
        }
    }
    private boolean isSchemaPresent(String text) {
        if(text.startsWith("@")){
            return true;
        }
        return false;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.INCOMING_CARD_WITH_LIST_MEDICATION;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.GENERAL;
    }

    public class Adapter extends BaseAdapter {
        ViewHolder holder = null;
        String afterMealBeforeMeal;
        String quantity = "0";
        String medicationType;
        int id,resourceImage;

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
                convertView = inflater.inflate( R.layout.item_message_medication_row, null );

                holder.textViewMedicationName =  (TextView) convertView.findViewById(R.id.text_medication_name);
                holder.relativeLayoutContainer =  (RelativeLayout) convertView.findViewById(R.id.container_medication);
                //holder.textViewMedicationNotTaken = (TextView)convertView.findViewById(R.id.text_take);
                holder.imageViewTaken = (ImageView)convertView.findViewById(R.id.image_done) ;
                holder.imageViewMedicationtype = (ImageView)convertView.findViewById(R.id.image_medication) ;
                holder.textViewMedicationQuantity = (TextView)convertView.findViewById(R.id.text_medication_quantity);
                holder.textViewMedicationDays =  (TextView)convertView.findViewById(R.id.text_medication_time);
                holder.viewDivider = (View)convertView.findViewById(R.id.divider);
                holder.morningLable=(TextView)convertView.findViewById(R.id.morning_lable);
                holder.eveningLable=(TextView)convertView.findViewById(R.id.evening_lable);
                holder.nightLable=(TextView)convertView.findViewById(R.id.night_lable);
                convertView.setTag(holder);
                System.out.println("Adapter.getView pos="+position);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            if(position == mTestList.size()-1){
                holder.viewDivider.setVisibility(View.GONE);
            }else{
                holder.viewDivider.setVisibility(View.VISIBLE);
            }
            if(mTestList.get(position).getBefore_meal()!=null){
                if(mTestList.get(position).getBefore_meal().equalsIgnoreCase("true")){
                    afterMealBeforeMeal  = "before meal";
                }else{
                    afterMealBeforeMeal  = "after meal";
                }
            }else{
                afterMealBeforeMeal  = "after meal";
            }
            holder.textViewMedicationName.setText(mTestList.get(position).getName() + ", " + mTestList.get(position).getDose_quantity());
            holder.textViewMedicationQuantity.setText( mTestList.get(position).getMedication_type() + ", " + afterMealBeforeMeal);
            if(mTestList.get(position).getMedication_type().equalsIgnoreCase("Tablet")){
                resourceImage = R.drawable.tablets;
            }else if(mTestList.get(position).getMedication_type().equalsIgnoreCase("Capsule")){
                resourceImage = R.drawable.capsule;
            }else if(mTestList.get(position).getMedication_type().equalsIgnoreCase("Syrup")){
                resourceImage = R.drawable.ic_syrup_24px;
            }else if(mTestList.get(position).getMedication_type().equalsIgnoreCase("Injection")){
                resourceImage = R.drawable.injection;
            }else{
                resourceImage = R.drawable.fluides;
            }

            System.out.println("ListAdapter.getView" + mTestList.get(position).getId());

            holder.imageViewMedicationtype.setImageResource(resourceImage);

            if(mTestList.get(position).getMorning().equalsIgnoreCase("true")){
                holder.morningLable.setVisibility(View.VISIBLE);
            }else {
                holder.morningLable.setVisibility(View.GONE);
            }

            if(mTestList.get(position).getEvening().equalsIgnoreCase("true")){
                holder.nightLable.setVisibility(View.VISIBLE);
            }else {
                holder.nightLable.setVisibility(View.GONE);
            }

            if(mTestList.get(position).getAfternoon().equalsIgnoreCase("true")){
                holder.eveningLable.setVisibility(View.VISIBLE);
            }else {
                holder.eveningLable.setVisibility(View.GONE);
            }


            int difference = differenceBtwDate(mTestList.get(position).getCreated_at(),new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));


            boolean isDaily = false;

            if(mTestList.get(position).getRecurring() != null){
                if(mTestList.get(position).getRecurring().equalsIgnoreCase("true")){
                    isDaily = true;
                }
            }

            if(isDaily){
                holder.textViewMedicationDays.setText(difference + getSuffix(difference)  + " day of daily medication." );
            }else{
                String day = "";
                if(mTestList.get(position).getNumber_of_days()!=null){
                    if(Integer.parseInt(mTestList.get(position).getNumber_of_days())>1){
                        day = "days";
                    }else{
                        day = day;
                    }
                    //System.out.println("ListAdapter.getView " + " getCreated_at()="+listData.get(position).getCreated_at());
                    holder.textViewMedicationDays.setText( difference + getSuffix(difference) + " day of " + mTestList.get(position).getNumber_of_days() + " " +day + " medication");

                }else{
                    holder.textViewMedicationDays.setText( difference + getSuffix(difference) + " day of " + "0" + " medication");

                }
            }




            return convertView;
        }

    }

    public class ViewHolder {
        private TextView textViewMedicationName;
        private TextView textViewMedicationQuantity;
        private ImageView imageViewMedicationtype;
        private ImageView imageViewTaken;
        //private TextView textViewMedicationNotTaken;
        private TextView textViewMedicationDays;
        private RelativeLayout relativeLayoutContainer;
        private View viewDivider;
        private TextView morningLable,eveningLable,nightLable;
    }

    @SuppressLint("SimpleDateFormat")
    public int differenceBtwDate(String strFirstDates, String strSecondDate) {
        int returnDate = 0;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = format2.parse(strFirstDates);
            secondDate = format.parse(strSecondDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //System.out.println("ListAdapter.differenceBtwDate firstDate="+firstDate + " secondDate= "+secondDate);

        if (firstDate != null && secondDate != null) {

            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(firstDate);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(secondDate);

            Period p = new Period(firstDate.getTime(), secondDate.getTime());
            int diffYear = p.getYears();
            int diffMonth = p.getMonths();
            int diffDay = p.getDays();
            int hours = p.getHours();
            int minutes = p.getMinutes();

            diffDay+=1;

            returnDate = diffDay;
        }
        return returnDate;
    }

    public String getSuffix(int i) {
        int  j = i % 10,
                k = i % 100;
        if (j == 1 && k != 11) {
            return "st";
        }
        if (j == 2 && k != 12) {
            return "nd";
        }
        if (j == 3 && k != 13) {
            return "rd";
        }
        return "th";
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