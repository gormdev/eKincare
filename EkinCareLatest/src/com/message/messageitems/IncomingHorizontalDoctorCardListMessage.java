package com.message.messageitems;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ekincare.R;
import com.message.FragmentChat;
import com.message.custominterface.CardDoctorConsultClick;
import com.message.custominterface.HorizontalCardListItemClickEvent;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.utility.ServerRequests;
import com.message.viewholder.IncomingHorizontalCardListViewHolder;
import com.message.viewholder.MessageViewHolder;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.vo.DoctorModelData;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by RaviTejaN on 13-12-2016.
 */

public class IncomingHorizontalDoctorCardListMessage extends MessageItem {

    CardDoctorConsultClick cardDoctorConsultClick;
    List<DoctorModelData> list;
    private Context context;
    private Adapter adapter;
    PreferenceHelper prefs;

   /* public IncomingHorizontalDoctorCardListMessage(Context context , ArrayList<DoctorModelData> list, HorizontalCardListItemClickEvent horizontalCardListItemClickEvent)
    {
        super();
        this.context = context;
        this.list = list;
        adapter = new Adapter();
        this.horizontalCardListItemClickEvent = horizontalCardListItemClickEvent;
    }*/

    public IncomingHorizontalDoctorCardListMessage(Context context, List<DoctorModelData> list, CardDoctorConsultClick cardDoctorConsultClick) {
        super();
        this.context = context;
        this.list = list;
        adapter = new Adapter();
        this.cardDoctorConsultClick = cardDoctorConsultClick;
        prefs=new PreferenceHelper(context);
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder) {
        if (messageViewHolder != null && messageViewHolder instanceof IncomingHorizontalCardListViewHolder)
        {
            final IncomingHorizontalCardListViewHolder incomingHorizontalListViewHolder = (IncomingHorizontalCardListViewHolder) messageViewHolder;

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            incomingHorizontalListViewHolder.recyclerView.setLayoutManager(layoutManager);
            incomingHorizontalListViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            incomingHorizontalListViewHolder.recyclerView.setAdapter(adapter);

        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.INCOMING_HORIZONTAL_DOCTOR_CARD_LIST;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.GENERAL;
    }

    public List<DoctorModelData> getList() {
        return list;
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {
            public TextView textViewHeader,textViewSubHeader,button1,button2;
            public CircleImageView imageView;
            public CardView cardView;
            public TextView experience_doctor,pricedoctor,profiledoctor;


            public ViewHolder(View view) {
                super(view);
                textViewHeader = (TextView) view.findViewById(R.id.textview_title);
                textViewSubHeader = (TextView) view.findViewById(R.id.textview_discription);
                button1 = (TextView) view.findViewById(R.id.button1);
                button2 = (TextView) view.findViewById(R.id.button2);
                imageView = (CircleImageView) view.findViewById(R.id.imageview_background);
                cardView = (CardView) view.findViewById(R.id.card_view);
                pricedoctor=(TextView)view.findViewById(R.id.rate_doctor);
                profiledoctor=(TextView)view.findViewById(R.id.profile_doctor);
                cardView.setOnClickListener(this);
                button1.setOnClickListener(this);
                button2.setOnClickListener(this);
                pricedoctor.setOnClickListener(this);
                profiledoctor.setOnClickListener(this);

                System.out.println("ViewHolder.ViewHolder");
            }

            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.rate_doctor){
                    prefs.setPREF_DOCTORID(list.get(getAdapterPosition()).getId());

                    cardDoctorConsultClick.onConsultDoctorSelected(list.get(getAdapterPosition()).getName());

                }else if(v.getId()==R.id.profile_doctor){
                    cardDoctorConsultClick.onDoctorProfileSelected(list.get(getAdapterPosition()).getId());
                }
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_list_item_doctor_card, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(Adapter.ViewHolder holder, int position) {

            holder.button2.setVisibility(View.GONE);
            holder.button1.setVisibility(View.GONE);


            if(!list.get(position).getPhoto().isEmpty()){
                Glide.with(context).load(list.get(position).getPhoto()).into(holder.imageView);
            }else{
               // holder.imageView.setImageResource(totalDoctorPackages.get(position).getPhoto());
            }

            if(list.get(position).getSpeciality()!=null && list.get(position).getSpeciality().isEmpty()){
                holder.textViewSubHeader.setVisibility(View.GONE);
            }
            holder.textViewSubHeader.setText(list.get(position).getSpeciality());


            holder.textViewHeader.setText(list.get(position).getName());
           // holder.pricedoctor.setText(list.get(position).getConsult().getInr_val() +"Rs");

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}