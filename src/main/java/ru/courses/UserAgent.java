package ru.courses;

import lombok.Getter;

@Getter
public class UserAgent {
    private final String osName;
    private final String browserName;
    private final boolean isBot;


    public UserAgent(String str) {
        this.osName = getOsName(FragmentLog.USER_AGENT.getParameters(str));
        this.browserName = getBrowserName(FragmentLog.USER_AGENT.getParameters(str));
        this.isBot = isBot(FragmentLog.USER_AGENT.getParameters(str));
    }

    private boolean isBot(String str){
        return str.contains("bot");
    }

    private String getOsName(String str) {
        String osName = "Other";
        String[] parts = str.split("\\)");
        if (parts[0].contains("Windows"))
            osName = "Windows";
        if (parts[0].contains("Mac"))
            osName = "macOS";
        if (parts[0].contains("Linux"))
            osName = "Linux";
        return osName;
    }

    private String getBrowserName(String str) {
        String browserName = "Other";
        String[] parts = str.split("\\(");
        if (parts.length > 1) {
            if (str.contains("Gecko/20100101 Firefox/")) {
                browserName = "Firefox";
                return browserName;
            }
            if (str.contains("KHTML, like Gecko) Chrome/")) {
                browserName = "Chrome";
                return browserName;
            }
            if (str.contains("OPR/") || parts[1].contentEquals("Opera/")) {
                browserName = "Opera";
                return browserName;
            }
            if (str.contains("Edg/")) {
                browserName = "Edge";
                return browserName;
            }
            if (str.contains("Safari/")) {
                browserName = "Safari";
                return browserName;
            }
        }
        return browserName;
    }

    @Override
    public String toString() {
        return "UserAgent{" +
                "Операционная система: " + osName +
                ", Браузер: " + browserName +
                '}';
    }
}
