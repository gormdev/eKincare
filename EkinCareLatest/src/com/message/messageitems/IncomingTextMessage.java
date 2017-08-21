package com.message.messageitems;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Parcel;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.ekincare.R;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.viewholder.IncomingTextViewHolder;
import com.message.viewholder.MessageViewHolder;

import net.frakbot.jumpingbeans.JumpingBeans;

/**
 * Created by matthewpage on 6/21/16.
 */
public class IncomingTextMessage extends MessageItem {

    private Context context;
    private String text;

    public IncomingTextMessage(String text, Context context)
    {
        super();
        this.context = context;
        this.text=text;
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder) {
        if (messageViewHolder != null && messageViewHolder instanceof IncomingTextViewHolder)
        {
            final IncomingTextViewHolder incomingTextViewHolder = (IncomingTextViewHolder) messageViewHolder;

            incomingTextViewHolder.text.setText(text);

            incomingTextViewHolder.bubble.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context,"bot message Clicked", Toast.LENGTH_SHORT).show();
                }
            });

            incomingTextViewHolder.bubble.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager)
                            context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("simple text", text);
                    clipboard.setPrimaryClip(clip);
                    String toastMessage = "message copied";
                    //Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            //System.out.println("IncomingTextMessage.buildMessageItem isFirstConsecutiveMessageFromSource="+isFirstConsecutiveMessageFromSource);

            incomingTextViewHolder.avatar.setVisibility(isFirstConsecutiveMessageFromSource ? View.VISIBLE : View.INVISIBLE);
            incomingTextViewHolder.avatarContainer.setVisibility(isFirstConsecutiveMessageFromSource ? View.VISIBLE : View.INVISIBLE);

        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.INCOMING_TEXT;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.BOT;
    }

    public String getText() {
        return text;
    }
}