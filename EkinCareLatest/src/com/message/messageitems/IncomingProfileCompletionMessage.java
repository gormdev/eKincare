package com.message.messageitems;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.ekincare.R;
import com.ekincare.ui.custom.ProgressItem;
import com.message.custominterface.HorizontalCardListItemClickEvent;
import com.message.custominterface.SchemaClickEvent;
import com.message.model.CardViewModel;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.viewholder.IncomingCardWithTextViewHolder;
import com.message.viewholder.IncomingMemberProfileViewHolder;
import com.message.viewholder.MessageViewHolder;

import java.util.ArrayList;

/**
 * Created by matthewpage on 6/21/16.
 */
public class IncomingProfileCompletionMessage extends MessageItem {

    private CardViewModel cardViewModel;
    private Context context;
    private ProgressItem mProgressItem;
    SchemaClickEvent schemaClickEvent;

    public IncomingProfileCompletionMessage(CardViewModel cardViewModel, Context context,SchemaClickEvent schemaClickEvent)
    {
        super();
        this.cardViewModel = cardViewModel;
        this.context=context;
        this.schemaClickEvent = schemaClickEvent;
        System.out.println("IncomingCardWithTextMessage.IncomingCardWithTextMessage");
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder) {
        if (messageViewHolder != null && messageViewHolder instanceof IncomingMemberProfileViewHolder)
        {
            final IncomingMemberProfileViewHolder incomingMemberProfileViewHolder = (IncomingMemberProfileViewHolder) messageViewHolder;

            ArrayList<ProgressItem> progressItemList = new ArrayList<ProgressItem>();
            // red span
            mProgressItem = new ProgressItem();
            mProgressItem.progressItemPercentage = 33;
            mProgressItem.color = R.color.progress_first;
            progressItemList.add(mProgressItem);
            // blue span
            mProgressItem = new ProgressItem();
            mProgressItem.progressItemPercentage = 33;
            mProgressItem.color = R.color.progress_second;
            progressItemList.add(mProgressItem);
            // green span
            mProgressItem = new ProgressItem();
            mProgressItem.progressItemPercentage = 33;
            mProgressItem.color = R.color.progress_three;
            progressItemList.add(mProgressItem);
            // yellow span
            incomingMemberProfileViewHolder.textViewProfileText.setText(cardViewModel.getTitle());

            incomingMemberProfileViewHolder.seekbar.initData(progressItemList);
            incomingMemberProfileViewHolder.seekbar.invalidate();

            incomingMemberProfileViewHolder.textViewOptimalHs.setText(cardViewModel.getSubTitle());
            try{
                incomingMemberProfileViewHolder.seekbarOptimalHealthScore.setProgress(Integer.parseInt((cardViewModel.getSubTitle())));
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
            float optimalX = incomingMemberProfileViewHolder.seekbarOptimalHealthScore.getThumb().getBounds().left;
            incomingMemberProfileViewHolder.frameLayoutTextOptimalHealthScore.setX(optimalX);
            incomingMemberProfileViewHolder.seekbarOptimalHealthScore.setEnabled(false);
            incomingMemberProfileViewHolder.seekbar.setEnabled(false);
            incomingMemberProfileViewHolder.seekbar.getThumb().mutate().setAlpha(0);

            if(cardViewModel.getButtonText2().length()<=2){
                incomingMemberProfileViewHolder.seekbarHealthScore.setMax(100);
                incomingMemberProfileViewHolder.seekbarOptimalHealthScore.setMax(100);
                incomingMemberProfileViewHolder.textProgressLimitRange.setText("100");
            }else if(cardViewModel.getButtonText2().length()>=3){
                incomingMemberProfileViewHolder.seekbarHealthScore.setMax(1000);
                incomingMemberProfileViewHolder.seekbarOptimalHealthScore.setMax(1000);
                incomingMemberProfileViewHolder.textProgressLimitRange.setText("1000");
            }

            incomingMemberProfileViewHolder.textViewOrigionalHs.setText(cardViewModel.getButtonText2());
            try{
                incomingMemberProfileViewHolder.seekbarHealthScore.setProgress(Integer.parseInt(cardViewModel.getButtonText2()));
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
            float x = incomingMemberProfileViewHolder.seekbarHealthScore.getThumb().getBounds().left;
            //set the left value to textview x value
            incomingMemberProfileViewHolder.frameLayoutTextHealthScore.setX(x);
            incomingMemberProfileViewHolder.seekbarHealthScore.setEnabled(false);

            incomingMemberProfileViewHolder.textViewViewInsight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isSchemaPresent(cardViewModel.getButtonAction1())){
                        String id = cardViewModel.getButtonAction1().substring(cardViewModel.getButtonAction1().indexOf("@")+1,cardViewModel.getButtonAction1().indexOf("/"));
                        String transitionScreen = cardViewModel.getButtonAction1().substring(cardViewModel.getButtonAction1().indexOf("/")+1);
                        schemaClickEvent.onSchemaItemClick(id,transitionScreen);
                    }else{
                        Toast.makeText(context,"Action not available",Toast.LENGTH_LONG).show();
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

    public void showView(View v){
        v.setVisibility(View.GONE);
    }
    public void hideView(View v){
        v.setVisibility(View.GONE);
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.INCOMING_MEMBER_PROFILE_CARD;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.BOT;
    }

    public CardViewModel getCardData() {
        return cardViewModel;
    }
}