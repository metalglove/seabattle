package common;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageLogger {
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_GREEN = "\u001B[92m";
    private final String messagePrefix;
    private static AtomicInteger prefixLength = new AtomicInteger();

    public MessageLogger(String messagePrefix) {
        this.messagePrefix = messagePrefix;
        if (messagePrefix.length() > MessageLogger.prefixLength.get()) {
            MessageLogger.prefixLength.set(messagePrefix.length());
        }
        System.out.println(ANSI_GREEN + "Registered logger: [" + messagePrefix + "]" + ANSI_RESET);
    }

    public void info(String message) {
        System.out.println(ANSI_CYAN + getMessagePrefix() + ANSI_YELLOW + " [INFO ] " + message + ANSI_RESET);
    }

    public void error(String message) {
        String strippedMessage = message.replace("\r\n","");
        System.out.println(ANSI_CYAN + getMessagePrefix() + ANSI_RED + " [ERROR] " + strippedMessage + ANSI_RESET);
    }

    public void list(List<?> list) {
        list.forEach(obj -> info(obj.toString()));
    }

    private String getMessagePrefix() {
        if (messagePrefix.length() >= MessageLogger.prefixLength.get()) {
            return '[' + messagePrefix + ']';
        }
        StringBuilder sb = new StringBuilder();
        sb.append(messagePrefix);
        while (sb.length() < MessageLogger.prefixLength.get()) {
            sb.append(' ');
        }
        sb.insert(0, '[');
        sb.append("]");
        return sb.toString();
    }

    public void overwritableInfo(String message) {
        System.out.print(ANSI_CYAN + getMessagePrefix() + ANSI_YELLOW + " [INFO ] " + message + ANSI_RESET + "\r");
    }
}
