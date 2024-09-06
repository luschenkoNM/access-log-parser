package ru.courses;

import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Getter
public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private HashSet<String> setExistingPages;
    private HashSet<String> setNonExistingPages;
    private HashMap<String, Integer> countOS;
    private HashMap<String, Integer> countBrowsers;

    public Statistics() {
        this.minTime = LocalDateTime.MAX;
        this.maxTime = LocalDateTime.MIN;
        this.totalTraffic = 0;
        this.setExistingPages = new HashSet<>();
        this.setNonExistingPages = new HashSet<>();
        this.countOS = new HashMap<>();
        this.countBrowsers = new HashMap<>();
    }

    public void addEntry(LogEntry logEntry) {
        this.totalTraffic = getTotalTraffic() + logEntry.getResponseSize();
        this.countOS = getCountOS(logEntry);
        this.countBrowsers = getCountBrowser(logEntry);
        if (logEntry.getTime().isBefore(getMinTime()))
            this.minTime = logEntry.getTime();
        if (logEntry.getTime().isAfter(getMaxTime()))
            this.maxTime = logEntry.getTime();
        if (logEntry.getResponseCode() == 200)
            this.setExistingPages.add(logEntry.getPath());
        if (logEntry.getResponseCode() == 404)
            this.setNonExistingPages.add(logEntry.getPath());
    }

    /**
     * Метод возвращает количество использования операционных систем из лог-файла
     */
    private HashMap<String, Integer> getCountOS(LogEntry logEntry) {
        if (countOS.containsKey(logEntry.getUserAgent().getOsName()))
            countOS.put(logEntry.getUserAgent().getOsName(), countOS.get(logEntry.getUserAgent().getOsName()) + 1);
        else countOS.put(logEntry.getUserAgent().getOsName(), 1);
        return countOS;
    }

    /**
     * Метод возвращает количество использования браузеров из лог-файла
     */
    private HashMap<String, Integer> getCountBrowser(LogEntry logEntry) {
        if (countBrowsers.containsKey(logEntry.getUserAgent().getBrowserName()))
            countBrowsers.put(logEntry.getUserAgent().getBrowserName(), countBrowsers.get(logEntry.getUserAgent().getBrowserName()) + 1);
        else countBrowsers.put(logEntry.getUserAgent().getBrowserName(), 1);
        return countBrowsers;
    }

    /**
     * Метод возвращает часовой размер трафика из лог-файла
     */
    public long getTrafficRate() {
        Period period = Period.between(getMinTime().toLocalDate(), getMaxTime().toLocalDate());
        Duration duration = Duration.between(getMinTime().toLocalTime(), getMaxTime().toLocalTime());
        long result = this.totalTraffic / (period.getDays() * 24 + duration.toHoursPart());
        return result;
    }

    /**
     * Метод возвращает статистику использования операционных систем в виде Map, ключ: название ОС, значение: доли ОС в логе(от 0 до 1)
     */
    public HashMap<String, Double> getStatisticsOS() {
        HashMap<String, Double> map = new HashMap<>();
        double allCountOS = 0;
        for (Map.Entry<String, Integer> entry : this.countOS.entrySet()) {
            Integer value = entry.getValue();
            allCountOS += value;
        }
        for (Map.Entry<String, Integer> entry : this.countOS.entrySet()) {
            map.put(entry.getKey(), entry.getValue() / allCountOS);
        }
        return map;
    }

    /**
     * Метод возвращает статистику использования браузера в виде Map, ключ: название Браузера, значение: доли браузера в логе(от 0 до 1)
     */
    public HashMap<String, Double> getStatisticsBrowsers() {
        HashMap<String, Double> map = new HashMap<>();
        double allCountOS = 0;
        for (Map.Entry<String, Integer> entry : this.countBrowsers.entrySet()) {
            Integer value = entry.getValue();
            allCountOS += value;
        }
        for (Map.Entry<String, Integer> entry : this.countBrowsers.entrySet()) {
            map.put(entry.getKey(), entry.getValue() / allCountOS);
        }
        return map;
    }
}
