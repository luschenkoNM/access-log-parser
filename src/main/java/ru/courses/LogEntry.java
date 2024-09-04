package ru.courses;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
public class LogEntry {
    private final String ipAddr, path, refer;
    private final LocalDateTime time;
    private final HttpMethod method;
    private final int responseCode, responseSize;
    private final UserAgent userAgent;

    public LogEntry(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
        this.ipAddr = FragmentLog.IP.getParameters(str);
        this.path = FragmentLog.PATH.getParameters(str);
        this.refer = FragmentLog.REFERER.getParameters(str);
        this.time = LocalDateTime.parse(FragmentLog.DATA.getParameters(str), formatter);
        this.method = getMethod(str);
        this.responseCode = Integer.parseInt(FragmentLog.RESPONSE_CODE.getParameters(str));
        this.responseSize = Integer.parseInt(FragmentLog.RESPONSE_SIZE.getParameters(str));
        this.userAgent = new UserAgent(str);
    }

    private HttpMethod getMethod(String str) {
        HttpMethod httpMethod = HttpMethod.OTHER;
        if (FragmentLog.METHOD.getParameters(str).equals("GET"))
            return HttpMethod.GET;
        if (FragmentLog.METHOD.getParameters(str).equals("POST"))
            return HttpMethod.POST;
        if (FragmentLog.METHOD.getParameters(str).equals("DELETE"))
            return HttpMethod.DELETE;
        if (FragmentLog.METHOD.getParameters(str).equals("PUT"))
            return HttpMethod.PUT;
        return HttpMethod.OTHER;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "ipAddr='" + ipAddr + '\'' +
                ", path='" + path + '\'' +
                ", refer='" + refer + '\'' +
                ", method=" + method +
                ", responseCode=" + responseCode +
                ", responseSize=" + responseSize +
                ", userAgent=" + userAgent +
                '}';
    }
}
