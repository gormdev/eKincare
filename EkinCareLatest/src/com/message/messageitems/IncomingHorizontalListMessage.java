package com.message.messageitems;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ekincare.R;
import com.message.animators.FadeInAnimator;
import com.message.animators.LandingAnimator;
import com.message.custominterface.HorizontalListItemClickEvent;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.viewholder.IncomingHorizontalListViewHolder;
import com.message.viewholder.MessageViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

//import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by matthewpage on 6/21/16.
 */
public class IncomingHorizontalListMessage extends MessageItem {

    List<String> list;
    private final Context context;
    private Adapter adapter;
    private HorizontalListItemClickEvent horizontalListItemClickEvent;
    private int width = 82;
    LinearLayoutManager layoutManager;
    private  int firstVisibleInListview;


    public IncomingHorizontalListMessage(Context context , List<String> list, HorizontalListItemClickEvent horizontalListItemClickEvent)
    {
        super();
        this.list = new ArrayList<>();
        this.list.clear();
        this.context = context;
        this.list = list;
        adapter = new Adapter();
        this.horizontalListItemClickEvent = horizontalListItemClickEvent;
        System.out.println("IncomingHorizontalListMessage.IncomingHorizontalListMessage list"+list.size());
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder) {
        System.out.println("IncomingHorizontalListMessage.buildMessageItem");
        if (messageViewHolder != null && messageViewHolder  instanceof IncomingHorizontalListViewHolder)
        {
            final IncomingHorizontalListViewHolder incomingHorizontalListViewHolder = (IncomingHorizontalListViewHolder) messageViewHolder;

            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            firstVisibleInListview = layoutManager.findFirstVisibleItemPosition();

            incomingHorizontalListViewHolder.recyclerView.setLayoutManager(layoutManager);
            //incomingHorizontalListViewHolder.recyclerView.setItemAnimator(new LandingAnimator());
            incomingHorizontalListViewHolder.recyclerView.setAdapter(adapter);
            incomingHorizontalListViewHolder.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    int currentFirstVisible = layoutManager.findFirstVisibleItemPosition();

                    if(dx < 0)
                    {
                        incomingHorizontalListViewHolder.avatar.setVisibility(isFirstConsecutiveMessageFromSource ? View.VISIBLE : View.GONE);
                        incomingHorizontalListViewHolder.avatarContainer.setVisibility(isFirstConsecutiveMessageFromSource ? View.VISIBLE : View.GONE);

                        System.out.println("Scrolled Right " + " currentFirstVisible="+currentFirstVisible+
                                " firstVisibleInListview= "+firstVisibleInListview);

                    }else if(dx>0)
                    {
                        incomingHorizontalListViewHolder.avatarContainer.setVisibility(View.GONE);

                        System.out.println("Scrolled Left " + " currentFirstVisible="+currentFirstVisible+
                                " firstVisibleInListview= "+firstVisibleInListview);

                    }
                }
            });

            incomingHorizontalListViewHolder.avatar.setVisibility(isFirstConsecutiveMessageFromSource ? View.VISIBLE : View.INVISIBLE);
            incomingHorizontalListViewHolder.avatarContainer.setVisibility(isFirstConsecutiveMessageFromSource ? View.VISIBLE : View.INVISIBLE);

        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.INCOMING_HORIZONTAL_LIST;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.BOT;
    }

    public String getText() {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i<list.size();i++){
            sb.append(list.get(i));
            if(i<=(list.size()-1)){
                sb.append(",");
            }
        }
        return sb.toString();
    }


    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView textView;
            public LinearLayout linearLayout;

            public ViewHolder(View view) {
                super(view);
                textView = (TextView) view.findViewById(R.id.text_view_message_list_item);
                linearLayout = (LinearLayout) view.findViewById(R.id.container_layout);
                textView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                System.out.println("IncomingHorizontalListMessage.onClick list="+list.size());
                horizontalListItemClickEvent.onHorizontalListItemClick(list.get(getAdapterPosition()));
                /**/
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_list_item_text, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(list.get(position));
            //animate(holder.linearLayout, position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        private void animate(View view, final int pos) {
            view.animate().cancel();
            view.setTranslationY(100);
            view.setAlpha(0);
            view.animate().alpha(1.0f).translationY(0).setDuration(100).setStartDelay(pos * 100);
        }
    }

}

