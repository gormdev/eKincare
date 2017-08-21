package com.message.messageitems;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.bumptech.glide.Glide;
import com.ekincare.R;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.viewholder.IncomingImageViewHolder;
import com.message.viewholder.MessageViewHolder;
import com.squareup.picasso.Picasso;

/**
 * Created by matthewpage on 6/21/16.
 */
public class IncomingImageMessage extends MessageItem
{

    private final Context context;
    private String url;

    public IncomingImageMessage(String url, Context context)
    {
        super();
        this.context = context;
        this.url=url;
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder)
    {
        if(messageViewHolder != null && (messageViewHolder instanceof IncomingImageViewHolder))
        {
            System.out.println("IncomingImageMessage.buildMessageItem = "+ url) ;
            final IncomingImageViewHolder incomingImageViewHolder = (IncomingImageViewHolder) messageViewHolder;

                Glide.with(context)
                        .load(Uri.parse(url))
                        .asGif().placeholder(R.drawable.ic_launcher).crossFade()
                        .into(incomingImageViewHolder.glideRoundedImageView);

            System.out.println("IncomingImageMessage.buildMessageItem isFirstConsecutiveMessageFromSource="+isFirstConsecutiveMessageFromSource);

            incomingImageViewHolder.avatar.setVisibility(isFirstConsecutiveMessageFromSource ? View.VISIBLE : View.INVISIBLE);
            incomingImageViewHolder.avatarContainer.setVisibility(isFirstConsecutiveMessageFromSource ? View.VISIBLE : View.INVISIBLE);

        }


    }

    @Override
    public MessageType getMessageType() {
        return MessageType.INCOMING_IMAGE;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.BOT;
    }
}