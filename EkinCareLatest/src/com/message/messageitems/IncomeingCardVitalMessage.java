package com.message.messageitems;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.Glide;
import com.message.custominterface.CardWithImageClickEvent;
import com.message.model.CardViewModel;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.viewholder.IncomingCardVitalViewHolder;
import com.message.viewholder.IncomingCardWithImageViewHolder;
import com.message.viewholder.MessageViewHolder;

/**
 * Created by RaviTejaN on 02-12-2016.
 */

public class IncomeingCardVitalMessage extends MessageItem {

    private CardViewModel cardViewModel;
    private Context context;

    public IncomeingCardVitalMessage(CardViewModel cardViewModel, Context context) {
        super();
        this.cardViewModel = cardViewModel;
        this.context = context;
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder) {
        if (messageViewHolder != null && messageViewHolder instanceof IncomingCardVitalViewHolder) {
            final IncomingCardVitalViewHolder incomingcardvitalviewholder = (IncomingCardVitalViewHolder) messageViewHolder;
            System.out.println("IncomeingCardVitalMessage.buildMessageItem======"+cardViewModel.getTitle()+cardViewModel.getSubTitle()+cardViewModel.getButtonText1());
            incomingcardvitalviewholder.vitalTitle.setText(cardViewModel.getTitle());
            incomingcardvitalviewholder.vitalUpdateTime.setText(cardViewModel.getSubTitle());
            incomingcardvitalviewholder.vitalUpdateValue.setText(cardViewModel.getButtonText1());
            incomingcardvitalviewholder.vitalUpdateDimension.setText(cardViewModel.getButtonAction1());
            incomingcardvitalviewholder.vitalValueInches.setText(cardViewModel.getButtonText2());
            incomingcardvitalviewholder.vitalUpdateDimensionInches.setText(cardViewModel.getButtonAction2());



        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.INCOMING_VITAL_VALUES;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.GENERAL;
    }
}
