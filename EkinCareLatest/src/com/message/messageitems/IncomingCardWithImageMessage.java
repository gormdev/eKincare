package com.message.messageitems;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.ekincare.R;
import com.message.custominterface.CardWithImageClickEvent;
import com.message.custominterface.HorizontalCardListItemClickEvent;
import com.message.model.CardViewModel;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.viewholder.IncomingCardWithImageViewHolder;
import com.message.viewholder.IncomingHorizontalCardListViewHolder;
import com.message.viewholder.MessageViewHolder;

/**
 * Created by matthewpage on 6/21/16.
 */
public class IncomingCardWithImageMessage extends MessageItem{

    CardWithImageClickEvent cardWithImageClickEvent;
    private CardViewModel cardViewModel;
    private Context context;

    public IncomingCardWithImageMessage(CardViewModel cardViewModel, Context context,CardWithImageClickEvent cardWithImageClickEvent)
    {
        super();
        this.cardViewModel = cardViewModel;
        this.context=context;
        this.cardWithImageClickEvent=cardWithImageClickEvent;
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder)
    {
        if (messageViewHolder != null && messageViewHolder instanceof IncomingCardWithImageViewHolder)
        {
            final IncomingCardWithImageViewHolder incomingCardWithImageViewHolder = (IncomingCardWithImageViewHolder) messageViewHolder;


            if(!cardViewModel.getUrl().isEmpty()){
                Glide.with(context).load(cardViewModel.getUrl()).into(incomingCardWithImageViewHolder.imageView);
            }else{
                incomingCardWithImageViewHolder.imageView.setImageResource(cardViewModel.getResource());
            }
            incomingCardWithImageViewHolder.textViewDiscription.setText(cardViewModel.getSubTitle());
            incomingCardWithImageViewHolder.textViewTitle.setText(cardViewModel.getTitle());
            incomingCardWithImageViewHolder.textLanguage.setText(cardViewModel.getButtonText2());
            incomingCardWithImageViewHolder.textSpecilites.setText(cardViewModel.getButtonText1());
            incomingCardWithImageViewHolder.textViewButton1.setText(cardViewModel.getButtonAction1()+" "+context.getResources().getString(R.string.Rs));
            incomingCardWithImageViewHolder.textViewButton2.setText(cardViewModel.getButtonAction2()+" "+context.getResources().getString(R.string.Rs));


           /* incomingCardWithImageViewHolder.textViewButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardWithImageClickEvent.onCardWithImagePositiveClick(incomingCardWithImageViewHolder.textViewButton1.getText().toString());
                }
            });

            incomingCardWithImageViewHolder.textViewButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardWithImageClickEvent.onCardWithImageNegativeClick(incomingCardWithImageViewHolder.textViewButton2.getText().toString());
                }
            });*/
        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.INCOMING_CARD_WITH_IMAGE;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.BOT;
    }


    public CardViewModel getCardData() {
        return cardViewModel;
    }
}