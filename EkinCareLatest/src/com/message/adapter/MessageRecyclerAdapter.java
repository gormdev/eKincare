package com.message.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ekincare.R;
import com.message.model.ChatMessage;
import com.message.model.MessageItem;
import com.message.model.MessageType;
import com.message.view.MapViewListItemView;
import com.message.viewholder.IncomingCardGraphWithListViewHolder;
import com.message.viewholder.IncomingCardVitalViewHolder;
import com.message.viewholder.IncomingCardWithImageViewHolder;
import com.message.viewholder.IncomingCardWithListViewHolder;
import com.message.viewholder.IncomingCardWithTextViewHolder;
import com.message.viewholder.IncomingCardWithVaccaniationViewHolder;
import com.message.viewholder.IncomingDatePickerViewHolder;
import com.message.viewholder.IncomingHorizontalCardListViewHolder;
import com.message.viewholder.IncomingHorizontalListViewHolder;
import com.message.viewholder.IncomingHraFamilyHistoryCardViewHolder;
import com.message.viewholder.OutgoingMapViewHolder;
import com.message.viewholder.IncomingMemberProfileViewHolder;
import com.message.viewholder.IncomingTextProgressViewHolder;
import com.message.viewholder.IncomingTextViewHolder;
import com.message.viewholder.IncomingTimePickerViewHolder;
import com.message.viewholder.IntroductionViewHolder;
import com.message.viewholder.MessageGeneralTextViewHolder;
import com.message.viewholder.MessageViewHolder;
import com.message.viewholder.OutgoingDcCardViewHolder;
import com.message.viewholder.OutgoingImageViewHolder;
import com.message.viewholder.OutgoingTextViewHolder;
import com.message.viewholder.SpinnerViewHolder;

import java.util.List;

/**
 * Created by John C. Hunchar on 5/12/16.
 */
public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private static final String TAG = MessageRecyclerAdapter.class.getName();

    private List<ChatMessage> chatMessageList;
    private FragmentActivity  context;

    public MessageRecyclerAdapter(List<ChatMessage> chatMessageItems, FragmentActivity context) {
        chatMessageList = chatMessageItems;
        this.context = context;
        //System.out.println("MessageRecyclerAdapter.MessageRecyclerAdapter messageList.size="+ messageList.size());
    }

    public List<ChatMessage> getChatMessageList(){
        return  chatMessageList;
    }

    public void add(int location, ChatMessage chatMessage)
    {
        try{
            chatMessageList.add(location, chatMessage);
            notifyItemInserted(location);
            notifyItemRangeChanged(location, chatMessageList.size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void update(int location, ChatMessage chatMessage)
    {
        //System.out.println("MessageRecyclerAdapter.update");
        notifyItemChanged(location);
    }

    public void delete(int position) { //removes the row
        chatMessageList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, chatMessageList.size());
    }



    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MessageViewHolder viewHolder = null;

        MessageType messageType = MessageType.values()[viewType];
        //System.out.println("MessageRecyclerAdapter.onCreateViewHolder messageItemType="+messageType);
        switch (messageType) {
            case INCOMING_TEXT:
                View incomingTextView = inflater.inflate(R.layout.item_message_incoming_text, parent, false);
                viewHolder = new IncomingTextViewHolder(incomingTextView);
                break;

            case OUTGOING_TEXT:
                View scoutTextView = inflater.inflate(R.layout.item_message_outgoing_text, parent, false);
                viewHolder = new OutgoingTextViewHolder(scoutTextView);
                break;

            case INCOMING_IMAGE:
                View userMediaView = inflater.inflate(R.layout.item_message_incoming_image, parent, false);
                viewHolder = new IncomingTextViewHolder(userMediaView);
                break;

            case OUTGOING_IMAGE:
                View userTextView = inflater.inflate(R.layout.item_message_outgoing_image, parent, false);
                viewHolder = new OutgoingImageViewHolder(userTextView);
                break;

            case INCOMING_HORIZONTAL_CARD_LIST:
                View horizontalCardListView = inflater.inflate(R.layout.item_message_list_horizontal_card, parent, false);
                viewHolder = new IncomingHorizontalCardListViewHolder(horizontalCardListView);
                break;

            case INCOMING_HORIZONTAL_DOCTOR_CARD_LIST:
                View horizontalDoctorCardListView = inflater.inflate(R.layout.item_message_list_horizontal_card, parent, false);
                viewHolder = new IncomingHorizontalCardListViewHolder(horizontalDoctorCardListView);
                break;

            case INCOMING_HORIZONTAL_LIST:
                System.out.println("MessageRecyclerAdapter.onCreateViewHolder INCOMING_HORIZONTAL_LIST");
                View horizontalListView = inflater.inflate(R.layout.item_message_list, parent, false);
                viewHolder = new IncomingHorizontalListViewHolder(horizontalListView);
                break;

            case INCOMING_MULTISELECT_LIST:
                System.out.println("MessageRecyclerAdapter.onCreateViewHolder INCOMING_HORIZONTAL_LIST");
                View horizontalMultiSelectListView = inflater.inflate(R.layout.item_message_card_with_multi_select_list, parent, false);
                viewHolder = new IncomingCardWithListViewHolder(horizontalMultiSelectListView);
                break;

            case INCOMING_CARD_WITH_LIST:
                System.out.println("MessageRecyclerAdapter.onCreateViewHolderINCOMING_CARD_WITH_LIST  IncomingCardWithListViewHolder");
                View cardWithList = inflater.inflate(R.layout.item_message_card_with_list, parent, false);
                viewHolder = new IncomingCardWithListViewHolder(cardWithList);
                break;
            case INCOMING_CART_WITH_LIST:
                System.out.println("MessageRecyclerAdapter.onCreateViewHolderINCOMING_CART_WITH_LIST  IncomingCardWithListViewHolder");
                View cartWithList = inflater.inflate(R.layout.item_message_cart_with_list, parent, false);
                viewHolder = new IncomingCardWithListViewHolder(cartWithList);
                break;
            case INCOMING_CARD_WITH_WELLNESS_LIST:
                System.out.println("MessageRecyclerAdapter.onCreateViewHolderINCOMING_CARD_WITH_LIST  IncomingCardWithListViewHolder");
                View cardWithWellnessList = inflater.inflate(R.layout.item_message_card_with_list, parent, false);
                viewHolder = new IncomingCardWithListViewHolder(cardWithWellnessList);
                break;
            case INCOMING_CARD_WITH_LIST_MEDICATION:
                View cardWithListMedication = inflater.inflate(R.layout.item_message_card_with_list, parent, false);
                viewHolder = new IncomingCardWithListViewHolder(cardWithListMedication);
                break;
            case  INCOMING_CARD_LIST_WITH_GRAPH:
                View cardGraphWIthListTrends = inflater.inflate(R.layout.item_message_card_list_with_graph, parent, false);
                viewHolder = new IncomingCardGraphWithListViewHolder(cardGraphWIthListTrends);
                break;
            case INCOMING_CARD_WITH_LIST_VACCINATION:
                View cardWithListvaccination = inflater.inflate(R.layout.item_message_vaccaniation_card, parent, false);
                viewHolder = new IncomingCardWithVaccaniationViewHolder(cardWithListvaccination);
                break;

            case INCOMING_CARD_WITH_IMAGE:
                View incomingCardWithImage = inflater.inflate(R.layout.item_message_card_with_image, parent, false);
                viewHolder = new IncomingCardWithImageViewHolder(incomingCardWithImage);
                break;

            case INCOMING_CARD_WITH_TEXT:
                View incomingCardWithText = inflater.inflate(R.layout.item_message_card_with_text, parent, false);
                viewHolder = new IncomingCardWithTextViewHolder(incomingCardWithText);
                break;

            case INCOMING_DATE_PICKER:
                View incomingDatePickerView = inflater.inflate(R.layout.item_message_card_with_datepicker, parent, false);
                viewHolder = new IncomingDatePickerViewHolder(incomingDatePickerView);
                break;

            case INCOMING_TIME_PICKER:
                View incomingTimePickerView = inflater.inflate(R.layout.item_message_card_with_timepicker, parent, false);
                viewHolder = new IncomingTimePickerViewHolder(incomingTimePickerView);
                break;

            case GENERAL_TEXT:
                View generalTextView = inflater.inflate(R.layout.item_message_general_text, parent, false);
                viewHolder = new MessageGeneralTextViewHolder(generalTextView);
                break;

            case INTRODUCTION_TEXT:
                View generalMediaView = inflater.inflate(R.layout.item_message_introduction, parent, false);
                viewHolder = new IntroductionViewHolder(generalMediaView);
                break;
            case SPINNER:
                View spinnerView = inflater.inflate(R.layout.item_spinner, parent, false);
                viewHolder = new SpinnerViewHolder(spinnerView);
                break;
            case INCOMING_VITAL_VALUES:
                View incomingVitalCard = inflater.inflate(R.layout.item_message_incomeing_vital, parent, false);
                viewHolder = new IncomingCardVitalViewHolder(incomingVitalCard);
                break;
            case INCOMING_CARD_HRA_FAMILY_HISTORY:
                View incomingHraCard = inflater.inflate(R.layout.item_message_hra_family_history, parent, false);
                viewHolder = new IncomingHraFamilyHistoryCardViewHolder(incomingHraCard);
                break;
            case INCOMING_MEMBER_PROFILE_CARD:
                View incomingProfileCard = inflater.inflate(R.layout.item_message_profile_card, parent, false);
                viewHolder = new IncomingMemberProfileViewHolder(incomingProfileCard);
                break;
            case INCOMING_TEXT_PROGRESS:
                View incomingTextProgress = inflater.inflate(R.layout.item_message_incoming_waiting_animation, parent, false);
                viewHolder = new IncomingTextProgressViewHolder(incomingTextProgress);
                break;
            case OUTGOING_MAP:
                MapViewListItemView mapViewListItemView = new MapViewListItemView(context);
                mapViewListItemView.mapViewOnCreate(null);
                viewHolder = new OutgoingMapViewHolder(mapViewListItemView,context);
                //View incomingMap = inflater.inflate(R.layout.item_message_incoming_map, parent, false);
                //viewHolder = new IncomingMapViewHolder(incomingMap,context);
                break;
            case INCOMING_PACKAGE:
                View incomingPackage = inflater.inflate(R.layout.item_message_card_with_list, parent, false);
                viewHolder = new IncomingCardWithListViewHolder(incomingPackage);
                break;
            case INCOMING_DC_LIST:
                View incomingDc = inflater.inflate(R.layout.item_message_list_horizontal_card, parent, false);
                viewHolder = new IncomingHorizontalCardListViewHolder(incomingDc);
                break;
            case OUTGOING_DC_CARD:
                View outgoingDc = inflater.inflate(R.layout.item_message_outgoing_dc_card, parent, false);
                viewHolder = new OutgoingDcCardViewHolder(outgoingDc);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder messageViewHolder, int position) {
        if (messageViewHolder == null) {
            return;
        }
        // Build the item
        MessageItem messageItem = getMessageItemByPosition(position);
        if (messageItem != null) {

            ////System.out.println("MessageRecyclerAdapter.onBindViewHolder source="+messageItem.getMessageSource());
            //System.out.println("MessageRecyclerAdapter.onBindViewHolder type="+messageItem.getMessageType());

            messageItem.buildMessageItem(messageViewHolder);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageList != null ? chatMessageList.size() : 0;
    }

    @Override
    public int getItemViewType(int position)
    {
        // Get the item type
        Integer itemType = getMessageItemType(position);
        if (itemType != null) {
            return itemType;
        }

        return super.getItemViewType(position);
    }

    public MessageItem getMessageItemByPosition(int position) {
        if (chatMessageList != null && !chatMessageList.isEmpty()) {
            if (position >= 0 && position < chatMessageList.size()) {
                MessageItem messageItem = chatMessageList.get(position).getMessageItem();
                if (messageItem != null) {
                    return messageItem;
                }
            }
        }

        return null;
    }

    private Integer getMessageItemType(int position) {
        MessageItem messageItem = getMessageItemByPosition(position);
        if (messageItem != null) {
            return messageItem.getMessageItemTypeOrdinal();
        }

        return null;
    }

}
