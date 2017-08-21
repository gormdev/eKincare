package com.message.messageitems;

import android.content.Context;
import android.view.View;

import com.message.custominterface.HorizontalCardListItemClickEvent;
import com.message.model.CardViewModel;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.viewholder.IncomingCardWithTextViewHolder;
import com.message.viewholder.MessageViewHolder;

/**
 * Created by matthewpage on 6/21/16.
 */
public class IncomingCardWithTextMessage extends MessageItem {

    HorizontalCardListItemClickEvent horizontalCardListItemClickEvent;
    private CardViewModel cardViewModel;
    private Context context;

    public IncomingCardWithTextMessage(CardViewModel cardViewModel, Context context, HorizontalCardListItemClickEvent horizontalCardListItemClickEvent)
    {
        super();
        this.cardViewModel = cardViewModel;
        this.context=context;
        this.horizontalCardListItemClickEvent =horizontalCardListItemClickEvent;
        System.out.println("IncomingCardWithTextMessage.IncomingCardWithTextMessage");
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder) {
        if (messageViewHolder != null && messageViewHolder instanceof IncomingCardWithTextViewHolder)
        {
            final IncomingCardWithTextViewHolder incomingCardWithTextViewHolder = (IncomingCardWithTextViewHolder) messageViewHolder;

            System.out.println("IncomingCardWithTextMessage.buildMessageItem"+ cardViewModel.getButtonText2() + cardViewModel.getButtonText1() + cardViewModel.getAdditional());
            System.out.println("IncomingCardWithTextMessage.buildMessageItem"+ cardViewModel.getTitle() + cardViewModel.getSubTitle());

            incomingCardWithTextViewHolder.textViewButton1.setVisibility(View.GONE);
            incomingCardWithTextViewHolder.textViewButton2.setVisibility(View.GONE);
            incomingCardWithTextViewHolder.textViewAdditional.setText(cardViewModel.getAdditional());

            incomingCardWithTextViewHolder.textViewDiscription.setText(cardViewModel.getSubTitle());

            incomingCardWithTextViewHolder.textViewTitle.setText(cardViewModel.getTitle());

        }
    }


    public void showView(View v){
        v.setVisibility(View.GONE);
    }
    public void hideView(View v){
        v.setVisibility(View.GONE);
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.INCOMING_CARD_WITH_TEXT;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.BOT;
    }

    public CardViewModel getCardData() {
        return cardViewModel;
    }
}