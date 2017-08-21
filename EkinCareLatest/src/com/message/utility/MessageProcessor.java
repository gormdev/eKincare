package com.message.utility;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.message.FragmentChat;
import com.message.model.ChatMessage;
import com.openNoteScanner.helpers.OpenNoteMessage;

/**
 * Created by Ajay on 13-12-2016.
 */

public class MessageProcessor extends Handler
{

    private static final String TAG = "ImageProcessor";
    private final FragmentChat fragmentChat;

    public MessageProcessor (Looper looper , Handler uiHandler , FragmentChat fragmentChat) {
        super(looper);
        this.fragmentChat = fragmentChat;
    }

    public void handleMessage ( Message msg ) {

        if (msg.obj.getClass() == OpenNoteMessage.class) {

            OpenNoteMessage obj = (OpenNoteMessage) msg.obj;

            String command = obj.getCommand();

            Log.d(TAG, "Message Received: " + command + " - " + obj.getObj().toString() );

            if ( command.equalsIgnoreCase("add")) {
                addMessage((ChatMessage) obj.getObj());
            }else if(command.equalsIgnoreCase("show edittext")){
                showEdittext();
            }
        }
    }

    private void showEdittext() {
        fragmentChat.showEditText();
    }

    private void addMessage(final ChatMessage chatMessage) {
        fragmentChat.generateIncomingMessageAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentChat.removeMessage(fragmentChat.getRecyclerCount()-1);
                fragmentChat.addNewMessage(chatMessage,fragmentChat.getRecyclerCount());
                fragmentChat.recyclerViewChat.smoothScrollToPosition(fragmentChat.getRecyclerCount());
            }
        },700);
    }
}
