package com.message.async;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import com.message.model.ChatMessage;
import com.message.adapter.MessageRecyclerAdapter;
import com.message.utility.MessageUtils;

public class ReplaceMessagesTask extends AsyncTask {
    private List<ChatMessage> mChatMessages;
    private MessageRecyclerAdapter mRecyclerAdapter;
    private Context context;
    private int upTo;

    public ReplaceMessagesTask(List<ChatMessage> chatMessages, MessageRecyclerAdapter mRecyclerAdapter, Context context, int upTo) {
        this.mChatMessages = chatMessages;
        this.mRecyclerAdapter = mRecyclerAdapter;
        this.upTo = upTo;
        this.context = context;
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        for (int i = mChatMessages.size() - 1; i >= 0; i--) {
            mChatMessages.remove(i);
        }

        for (ChatMessage chatMessage : mChatMessages) {
            if (context == null)
                return null;
            mChatMessages.add(chatMessage); // this call is why we need the AsyncTask
        }

        for (int i = 0; i < mChatMessages.size(); i++) {
            MessageUtils.markMessageItemAtIndexIfFirstOrLastFromSource(i, mChatMessages);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if (o != null) {
            return;
        }


        if (upTo >= 0 && upTo < mChatMessages.size()) {
            mRecyclerAdapter.notifyItemRangeInserted(0, upTo);
            mRecyclerAdapter.notifyItemChanged(upTo);
        } else {
            mRecyclerAdapter.notifyDataSetChanged();
        }

    }
}
