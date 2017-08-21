package com.message.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;

import com.message.adapter.MessageRecyclerAdapter;
import com.message.model.ChatMessage;
import com.message.model.MessageSource;
import com.message.utility.MessageUtils;
import com.message.utility.ScrollUtils;

import java.util.List;

public class AddNewMessageTask extends AsyncTask<Object, Void, Object> {
    private List<ChatMessage> chatMessages;
    private MessageRecyclerAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private int count;

    public AddNewMessageTask(
            List<ChatMessage> chatMessages,
            MessageRecyclerAdapter mRecyclerAdapter,
            RecyclerView mRecyclerView,
            int count) {
        this.chatMessages = chatMessages;
        this.mRecyclerAdapter = mRecyclerAdapter;
        this.mRecyclerView = mRecyclerView;
        this.count=count;
        System.out.println("AddNewMessageTask.AddNewMessageTask chatMessages="+chatMessages.size());

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        if(chatMessages.size()>count){
            for (int i = chatMessages.size()-count; i < chatMessages.size(); i++) {
                try{
                    MessageUtils.markMessageItemAtIndexIfFirstOrLastFromSource(i, chatMessages);
                }catch (Exception e){
                    System.out.println("AddNewMessageTask.doInBackground");
                }
            }
        }else{
            for (int i = 0; i < chatMessages.size(); i++) {
                try{
                    MessageUtils.markMessageItemAtIndexIfFirstOrLastFromSource(i, chatMessages);
                }catch (Exception e){
                    System.out.println("AddNewMessageTask.doInBackground");
                }
            }
        }



        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (o != null)
            return;
        boolean isAtBottom = !mRecyclerView.canScrollVertically(1);
        boolean isAtTop = !mRecyclerView.canScrollVertically(-1);

        //mRecyclerAdapter.notifyItemRangeInserted(0, chatMessages.size() - 1);
        //mRecyclerAdapter.notifyItemChanged(chatMessages.size() - 1);

        new Handler(Looper.getMainLooper()).post(new Runnable() { // Tried new Handler(Looper.myLopper()) also
            @Override
            public void run() {
                try{
                    mRecyclerAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    System.out.println("AddNewMessageTask.onPostExecute exception e="+e.toString());
                    e.printStackTrace();
                }
            }
        });

        if(count>5){
            try{
                if (isAtBottom
                        || chatMessages.get(chatMessages.size() - 1).getMessageItem().getMessageSource() == MessageSource.SELF
                        || chatMessages.get(chatMessages.size() - 1).getMessageItem().getMessageSource() == MessageSource.BOT)
                    mRecyclerView.smoothScrollToPosition(mRecyclerAdapter.getItemCount() - 1);
                else {
                    if (isAtTop)
                    {
                        ScrollUtils.scrollToTopAfterDelay(mRecyclerView, mRecyclerAdapter);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }
}
