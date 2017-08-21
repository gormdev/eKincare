package com.message.messageitems;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.ekincare.R;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.utility.Config;
import com.message.viewholder.MessageViewHolder;
import com.message.viewholder.OutgoingTextViewHolder;

/**
 * Created by matthewpage on 6/21/16.
 */
public class OutgoingTextMessage extends MessageItem {

    private Context context;
    private int sendingStatus;
    private String text;

    public OutgoingTextMessage(String text, Context context)
    {
        super();
        this.context = context;
        this.text=text;
    }

    public String getText(){
        return text;
    }

    public void setStatus(int status){
        this.sendingStatus = status;
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder) {
        if (messageViewHolder != null && messageViewHolder instanceof OutgoingTextViewHolder)
        {
            final OutgoingTextViewHolder messageGeneralTextViewHolder = (OutgoingTextViewHolder) messageViewHolder;

            switch (sendingStatus){
                case Config.MESSAGE_SENT:
                    messageGeneralTextViewHolder.circleImageViewTick.setImageResource((R.drawable.ic_done_white_24dp));
                    break;
                case Config.MESSAGE_DEIVERED:
                    messageGeneralTextViewHolder.circleImageViewTick.setImageResource((R.drawable.ic_done_all_white_24px));
                    break;
                case Config.MESSAGE_FAILED:
                    messageGeneralTextViewHolder.circleImageViewTick.setImageResource((R.drawable.ic_cloud_off_black_24px));
                    break;
                default:
                    break;
            }

            messageGeneralTextViewHolder.text.setText(text);

            messageGeneralTextViewHolder.bubble.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // Toast.makeText(context,"user message Clicked", Toast.LENGTH_SHORT).show();
                }
            });

            messageGeneralTextViewHolder.bubble.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager)
                            context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("simple text", text);
                    clipboard.setPrimaryClip(clip);
                    //Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    //v.vibrate(150);
                    String toastMessage = "message copied";
                    //Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.OUTGOING_TEXT;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.SELF;
    }
}