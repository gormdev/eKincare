package com.message.utility;

import com.message.model.ChatMessage;
import com.message.model.MessageItem;
import com.message.model.MessageType;

import java.util.List;

public class MessageUtils {

    public static void markMessageItemAtIndexIfFirstOrLastFromSource(int i, List<ChatMessage> chatMessageList) {
        //System.out.println("1.MessageUtils.markMessageItemAtIndexIfFirstOrLastFromSource");
        if (i < 0 || i >= chatMessageList.size()) {
            return;
        }

        ChatMessage chatMessage = chatMessageList.get(i);
        MessageItem messageItem = chatMessage.getMessageItem();

        //System.out.println("2.MessageUtils.markMessageItemAtIndexIfFirstOrLastFromSource messageItem="+messageItem.getMessageSource());

        messageItem.setFirstConsecutiveMessageFromSource(false);
        messageItem.setLastConsecutiveMessageFromSource(false);

        //System.out.println("3.MessageUtils.markMessageItemAtIndexIfFirstOrLastFromSource previousMessageIsNotFromSameSource(i, messageList)="+previousMessageIsNotFromSameSource(i, messageList));

        if (previousMessageIsNotFromSameSource(i, chatMessageList)) {
            messageItem.setFirstConsecutiveMessageFromSource(true);
        }

        //System.out.println("4.MessageUtils.markMessageItemAtIndexIfFirstOrLastFromSource  true messageItem.isFirstConsecutiveMessageFromSource"+messageItem.isFirstConsecutiveMessageFromSource());

        //System.out.println("5.MessageUtils.markMessageItemAtIndexIfFirstOrLastFromSource isTheLastConsecutiveMessageFromSource(i, messageList)="+isTheLastConsecutiveMessageFromSource(i, messageList));

        if (isTheLastConsecutiveMessageFromSource(i, chatMessageList)) {
            messageItem.setLastConsecutiveMessageFromSource(true);
            //System.out.println("6.MessageUtils.markMessageItemAtIndexIfFirstOrLastFromSource  true messageItem.isLastConsecutiveMessageFromSource"+messageItem.isLastConsecutiveMessageFromSource());

        }
    }

    private static boolean previousMessageIsNotFromSameSource(int i, List<ChatMessage> chatMessage) {
        //System.out.println("MessageUtils.previousMessageIsNotFromSameSource previousMessageIsSpinner(i, message)="+previousMessageIsSpinner(i, message));
        //System.out.println("MessageUtils.previousMessageIsNotFromSameSource previousMessageIsFromAnotherSender(i, message)="+previousMessageIsFromAnotherSender(i, message));

        return i == 0 || previousMessageIsSpinner(i, chatMessage) || previousMessageIsFromAnotherSender(i, chatMessage);
    }

    private static boolean previousMessageIsFromAnotherSender(int i, List<ChatMessage> chatMessageItems) {
        //System.out.println("MessageUtils.previousMessageIsFromAnotherSender messageItems.get(i - 1).getMessageItem().getMessageSource() ="
                //+messageItems.get(i - 1).getMessageItem().getMessageSource() );
        //System.out.println("MessageUtils.previousMessageIsFromAnotherSender messageItems.get(i).getMessageItem().getMessageSource() ="
               // + messageItems.get(i).getMessageItem().getMessageSource() );

        return chatMessageItems.get(i - 1).getMessageItem().getMessageSource() != chatMessageItems.get(i).getMessageItem().getMessageSource();
    }

    private static boolean previousMessageIsSpinner(int i, List<ChatMessage> chatMessage) {
        return isSpinnerMessage(i - 1, chatMessage);
    }

    private static boolean isTheLastConsecutiveMessageFromSource(int i, List<ChatMessage> chatMessageItems) {
        if (isSpinnerMessage(i, chatMessageItems)) {
            return false;
        }

        return i == chatMessageItems.size() - 1 ||
                nextMessageHasDifferentSource(i, chatMessageItems) ||
                chatMessageItems.get(i) == null ||
                chatMessageItems.get(i + 1) == null ||
                nextMessageWasAtLeastAnHourAfterThisOne(i, chatMessageItems);
    }

    private static boolean nextMessageWasAtLeastAnHourAfterThisOne(int i, List<ChatMessage> chatMessageItems) {
        return chatMessageItems.get(i + 1).getDate() - chatMessageItems.get(i).getDate() > 1000 * 60 * 60; // one hour
    }

    private static boolean nextMessageHasDifferentSource(int i, List<ChatMessage> chatMessageItems) {
        return chatMessageItems.get(i).getMessageItem().getMessageSource() != chatMessageItems.get(i + 1).getMessageItem().getMessageSource();
    }

    private static boolean isSpinnerMessage(int i, List<ChatMessage> chatMessageItems) {
        return chatMessageItems.get(i).getMessageItem().getMessageType() == MessageType.SPINNER;
    }
}
