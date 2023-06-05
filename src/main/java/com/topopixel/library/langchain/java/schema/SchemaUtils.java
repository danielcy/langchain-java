package com.topopixel.library.langchain.java.schema;

import java.util.List;

public class SchemaUtils {

    public static String getBufferString(List<BaseMessage> messages) {
        return getBufferString(messages, null, null);
    }

    public static String getBufferString(List<BaseMessage> messages, String humanPrefix,
        String aiPrefix) {
        String humanP = humanPrefix == null ? "Human" : humanPrefix;
        String aiP = aiPrefix == null ? "AI" : aiPrefix;
        StringBuilder sb = new StringBuilder();
        for (BaseMessage m : messages) {
            String role = "";
            if (m instanceof HumanMessage) {
                role = humanPrefix;
            }
            if (m instanceof AIMessage) {
                role = aiPrefix;
            }
            if (m instanceof SystemMessage) {
                role = "System";
            }
            sb.append(String.format("%s: %s", role, m.getContent()));
            sb.append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
