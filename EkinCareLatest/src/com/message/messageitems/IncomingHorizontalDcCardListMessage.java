package com.message.messageitems;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekincare.R;
import com.google.android.gms.maps.GoogleMap;
import com.message.custominterface.CardDcClick;
import com.message.model.Labs;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.viewholder.IncomingHorizontalCardListViewHolder;
import com.message.viewholder.MessageViewHolder;

import java.util.List;

/**
 * Created by matthewpage on 6/21/16.
 */
public class IncomingHorizontalDcCardListMessage extends MessageItem {

    List<Labs> list;
    private Context context;
    private Adapter adapter;
    CardDcClick cardDcClick;
    LinearLayoutManager layoutManager;
    GoogleMap mGoogleMap;
    private IncomingHorizontalCardListViewHolder incomingHorizontalCardListViewHolder;


    public IncomingHorizontalDcCardListMessage(List<Labs> list, Context context, CardDcClick cardDcClick) {
        super();
        this.context = context;
        this.list = list;
        this.cardDcClick = cardDcClick;
        adapter = new Adapter();
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder) {
        if (messageViewHolder != null && messageViewHolder instanceof IncomingHorizontalCardListViewHolder) {
            incomingHorizontalCardListViewHolder = (IncomingHorizontalCardListViewHolder) messageViewHolder;

            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

            incomingHorizontalCardListViewHolder.recyclerView.setLayoutManager(layoutManager);
            //incomingHorizontalListViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            incomingHorizontalCardListViewHolder.recyclerView.setAdapter(adapter);

            incomingHorizontalCardListViewHolder.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    int currentFirstVisible = layoutManager.findFirstVisibleItemPosition();

                    if (dx < 0) {
                        incomingHorizontalCardListViewHolder.avatarContainer.setVisibility(isFirstConsecutiveMessageFromSource ? View.VISIBLE : View.GONE);

                    } else if (dx > 0) {
                        incomingHorizontalCardListViewHolder.avatarContainer.setVisibility(View.GONE);

                    }
                }
            });

        }

        incomingHorizontalCardListViewHolder.avatarContainer.setVisibility(isFirstConsecutiveMessageFromSource ? View.VISIBLE : View.INVISIBLE);

    }

    @Override
    public MessageType getMessageType() {
        return MessageType.INCOMING_DC_LIST;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.BOT;
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {


        public class ViewHolder extends RecyclerView.ViewHolder {
            public LinearLayout cardView;
            public TextView textViewTitle, textViewAddress1,textViewAddress2,textViewAddress3, textViewHomeCollection,textViewPhone,changeLocation;
            public WebView mapView;

            public ViewHolder(View view) {
                super(view);
                cardView = (LinearLayout) itemView.findViewById(R.id.card_view);
                textViewTitle = (TextView) itemView.findViewById(R.id.textview_title);
                textViewAddress1 = (TextView) itemView.findViewById(R.id.textview_address1);
                textViewAddress2 = (TextView) itemView.findViewById(R.id.textview_address2);
                textViewAddress3 = (TextView) itemView.findViewById(R.id.textview_address3);
                textViewHomeCollection = (TextView) itemView.findViewById(R.id.textview_home_collection);
                textViewPhone = (TextView) itemView.findViewById(R.id.textview_phone);
                changeLocation=(TextView)itemView.findViewById(R.id.textview_select_dc_item);
                //mapView = (WebView) itemView.findViewById(R.id.webview);

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cardDcClick.onDcSelected(list.get(getAdapterPosition()));
                    }
                });
                changeLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cardDcClick.onDcSelected(list.get(getAdapterPosition()));
                    }
                });


            }


        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_incoming_dc, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textViewTitle.setText(list.get(position).getEnterprise_name());
            System.out.println("Adapter.onBindViewHolder======="+list.get(position).getLocality());
            holder.textViewAddress1.setText(list.get(position).getLocality() + ",");
            holder.textViewAddress2.setText(list.get(position).getCity());
            holder.textViewAddress3.setVisibility(View.GONE);

            holder.textViewPhone.setText(list.get(position).getDistance() + " km away");

            try{
                if(list.get(position).getHome_collection_enabled().equalsIgnoreCase("true")){
                    holder.textViewHomeCollection.setText("Home collection available");
                    holder.textViewHomeCollection.setTextColor(context.getResources().getColor(R.color.light_green));
                }else{
                    holder.textViewHomeCollection.setText("Home collection not available");
                    holder.textViewHomeCollection.setTextColor(context.getResources().getColor(R.color.red));
                }
            }catch (NullPointerException e){
                holder.textViewHomeCollection.setText("Home collection not available");
                holder.textViewHomeCollection.setTextColor(context.getResources().getColor(R.color.red));
            }

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

}