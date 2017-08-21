package com.message.messageitems;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.Glide;
import com.message.custominterface.HorizontalCardListItemClickEvent;
import com.message.custominterface.SchemaClickEvent;
import com.message.model.CardViewModel;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.viewholder.IncomingCardWithTextViewHolder;
import com.message.viewholder.MessageViewHolder;

/**
 * Created by matthewpage on 6/21/16.
 */
public class IncomingCardWithTextAndButtonMessage extends MessageItem {

    HorizontalCardListItemClickEvent horizontalCardListItemClickEvent;
    SchemaClickEvent schemaClickEvent;
    private CardViewModel cardViewModel;
    private Context context;

    public IncomingCardWithTextAndButtonMessage(CardViewModel cardViewModel, Context context, HorizontalCardListItemClickEvent horizontalCardListItemClickEvent,SchemaClickEvent schemaClickEvent)
    {
        super();
        this.cardViewModel = cardViewModel;
        this.schemaClickEvent = schemaClickEvent;
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

            incomingCardWithTextViewHolder.textViewButton2.setText(cardViewModel.getButtonText2());

            incomingCardWithTextViewHolder.textViewButton1.setText(cardViewModel.getButtonText1());

            incomingCardWithTextViewHolder.textViewAdditional.setText(cardViewModel.getAdditional());

            incomingCardWithTextViewHolder.textViewDiscription.setText(cardViewModel.getSubTitle());
            System.out.println("IncomingCardWithTextAndButtonMessage.buildMessageItem============"+cardViewModel.getTitle());
            try{
                if(cardViewModel.getTitle().length()>1){
                    System.out.println("IncomingCardWithTextAndButtonMessage.buildMessageItem============"+"IN");
                    incomingCardWithTextViewHolder.titleView.setVisibility(View.VISIBLE);
                    incomingCardWithTextViewHolder.textViewTitle.setText(cardViewModel.getTitle());
                }else{
                    System.out.println("IncomingCardWithTextAndButtonMessage.buildMessageItem============"+"OUT");

                    incomingCardWithTextViewHolder.titleView.setVisibility(View.GONE);
                }

            }catch (Exception e){
                e.printStackTrace();
                incomingCardWithTextViewHolder.textViewTitle.setText("eKincare");
            }



            incomingCardWithTextViewHolder.textViewButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isSchemaPresent(cardViewModel.getButtonAction1())){
                        System.out.println("IncomingCardWithTextAndButtonMessage.onClick========"+"YEs");
                        String id = cardViewModel.getButtonAction1().substring(cardViewModel.getButtonAction1().indexOf("@")+1,cardViewModel.getButtonAction1().indexOf("/"));
                        String transitionScreen = cardViewModel.getButtonAction1().substring(cardViewModel.getButtonAction1().indexOf("/")+1);

                        schemaClickEvent.onSchemaItemClick(id,transitionScreen);
                    }else{
                        System.out.println("IncomingCardWithTextAndButtonMessage.onClick========"+"No");

                        horizontalCardListItemClickEvent.onHorizontalCardListItemClick(cardViewModel.getButtonAction1());
                    }
                }
            });

            incomingCardWithTextViewHolder.textViewButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isSchemaPresent(cardViewModel.getButtonAction1())){
                        String id = cardViewModel.getButtonAction1().substring(cardViewModel.getButtonAction1().indexOf("@")+1,cardViewModel.getButtonAction1().indexOf("/"));
                        String transitionScreen = cardViewModel.getButtonAction1().substring(cardViewModel.getButtonAction1().indexOf("/")+1);
                        schemaClickEvent.onSchemaItemClick(id,transitionScreen);
                    }else{
                        horizontalCardListItemClickEvent.onHorizontalCardListItemClick(cardViewModel.getButtonAction2());
                    }
                }
            });

           /* if(cardViewModel.getButtonText2().isEmpty()){
                hideView(incomingCardWithTextViewHolder.textViewButton2);
            }else{
                showView(incomingCardWithTextViewHolder.textViewButton2);
            }

            if(cardViewModel.getButtonText1().isEmpty()){
                hideView(incomingCardWithTextViewHolder.textViewButton1);
            }else{
                showView(incomingCardWithTextViewHolder.textViewButton1);
            }*/

            /*if(cardViewModel.getSubTitle().isEmpty()){
                hideView(incomingCardWithTextViewHolder.textViewDiscountedPrice);
            }else{
                showView(incomingCardWithTextViewHolder.textViewDiscountedPrice);
            }

            if(cardViewModel.getTitle().isEmpty()){
                hideView(incomingCardWithTextViewHolder.textViewDiscountedPrice);
            }else{
                showView(incomingCardWithTextViewHolder.textViewTitle);
            }*/

            /*if(cardViewModel.getAdditional().isEmpty()){
                hideView(incomingCardWithTextViewHolder.textViewDiscountedPrice);
            }else{
                showView(incomingCardWithTextViewHolder.textViewOriginalPrice);
            }*/

        }
    }

    private boolean isSchemaPresent(String text) {
        if(text.startsWith("@")){
            return true;
        }
        return false;
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
}