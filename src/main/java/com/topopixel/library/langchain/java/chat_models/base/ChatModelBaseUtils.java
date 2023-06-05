package com.topopixel.library.langchain.java.chat_models.base;

import com.theokanning.openai.completion.chat.ChatMessage;
import com.topopixel.library.langchain.java.schema.*;

public class ChatModelBaseUtils {

    public static ChatMessage convertToChatMessage(BaseMessage message) {
        if (message instanceof HumanMessage) {
            return new ChatMessage("user", message.getContent());
        } else if (message instanceof AIMessage) {
            return new ChatMessage("assistant", message.getContent());
        } else if (message instanceof SystemMessage) {
            return new ChatMessage("system", message.getContent());
        }
        return null;
    }

    public static BaseMessage convertToBaseMessage(ChatMessage chat) {
        String role = chat.getRole();
        switch (role) {
            case "user":
                return new HumanMessage(chat.getContent());
            case "assistant":
                return new AIMessage(chat.getContent());
            case "system":
                return new SystemMessage(chat.getContent());
            default:
                return null;
        }
    }
}
