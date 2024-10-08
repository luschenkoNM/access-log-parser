package ru.courses;

import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

@Getter
public class Statistics {
    private long totalTraffic;
    private int countRequestOfUsers;
    private int countFailedRequestOfUsers;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private HashSet<String> setExistingPages;
    private HashSet<String> setNonExistingPages;
    private HashMap<String, Integer> countOS;
    private HashMap<String, Integer> countBrowsers;
    private HashSet<String> setIpAddr;
    private HashMap<LocalDateTime, Integer> countOfRequestPerSecond;
    private HashSet<String> setDomainAddr;

    public Statistics() {
        this.minTime = LocalDateTime.MAX;
        this.maxTime = LocalDateTime.MIN;
        this.totalTraffic = 0;
        this.setExistingPages = new HashSet<>();
        this.setNonExistingPages = new HashSet<>();
        this.countOS = new HashMap<>();
        this.countBrowsers = new HashMap<>();
        this.countRequestOfUsers = 0;
        this.countFailedRequestOfUsers = 0;
        this.setIpAddr = new HashSet<>();
        this.countOfRequestPerSecond = new HashMap<>();
        this.setDomainAddr = new HashSet<>();
    }

    public void addEntry(LogEntry logEntry) {
        this.totalTraffic = getTotalTraffic() + logEntry.getResponseSize();
        this.countOS = getCountOS(logEntry);
        this.countBrowsers = getCountBrowser(logEntry);
        if (checkExistDomainAddr(logEntry))
            this.setDomainAddr.add(getDomainAdrr(logEntry));
        if (logEntry.getTime().isBefore(getMinTime()))
            this.minTime = logEntry.getTime();
        if (logEntry.getTime().isAfter(getMaxTime()))
            this.maxTime = logEntry.getTime();
        if (logEntry.getResponseCode() == 200)
            this.setExistingPages.add(logEntry.getPath());
        if (logEntry.getResponseCode() == 404)
            this.setNonExistingPages.add(logEntry.getPath());
        if (!logEntry.getUserAgent().isBot())
            this.countRequestOfUsers++;
        if (checkFailedRequestOfUsers(logEntry))
            this.countFailedRequestOfUsers++;
        if (!logEntry.getUserAgent().isBot())
            this.setIpAddr.add(logEntry.getIpAddr());
        if (!logEntry.getUserAgent().isBot())
            this.countOfRequestPerSecond = getCountRequestInSeconds(logEntry);
    }

    /**
     * Метод проверяет наличие доменного имени в refer
     */
    private Boolean checkExistDomainAddr(LogEntry logEntry) {
        boolean result = false;
        String[] split = logEntry.getRefer().split("/");
        if (split.length > 1)
            result = true;
        return result;
    }

    /**
     * Метод возвращает доменное имя
     */
    private String getDomainAdrr(LogEntry logEntry) {
        String[] split = logEntry.getRefer().split("/");
        return split[2];
    }

    /**
     * Метод проверяет является ли запрос пользователя ошибочным
     */
    private boolean checkFailedRequestOfUsers(LogEntry logEntry) {
        boolean result = false;
        String codeToString = String.valueOf(logEntry.getResponseCode());
        if (!logEntry.getUserAgent().isBot() && (codeToString.startsWith("4") || codeToString.startsWith("5")))
            result = true;
        return result;
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
     * Метод возвращает количество запросов пользователей за каждую секунду в виде Map, ключ: LocalDateTime, значение: количество запросов
     */
    private HashMap<LocalDateTime, Integer> getCountRequestInSeconds(LogEntry logEntry) {
        if (countOfRequestPerSecond.containsKey(logEntry.getTime()))
            countOfRequestPerSecond.put(logEntry.getTime(), countOfRequestPerSecond.get(logEntry.getTime()) + 1);
        else countOfRequestPerSecond.put(logEntry.getTime(), 1);
        return countOfRequestPerSecond;
    }

    /**
     * Метод возвращает пиковое значение посещаемости сайта в секунду одним пользователем
     */
    public Integer getMaxCountRequestInSeconds() {
        return countOfRequestPerSecond.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();
    }

    /**
     * Метод возвращает часовой размер трафика из лог-файла
     */
    public long getTrafficRate() {
        Period period = Period.between(getMinTime().toLocalDate(), getMaxTime().toLocalDate());
        Duration duration = Duration.between(getMinTime().toLocalTime(), getMaxTime().toLocalTime());
        return this.totalTraffic / getPeriodOfTimeInHours();
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

    /**
     * Метод возвращает среднюю посещаемость сайта одним пользователем
     */
    public int getCountUsers() {
        return getCountRequestOfUsers() / setIpAddr.size();
    }


    /**
     * Метод возвращает среднее количество посещений сайта за час
     */
    public int getCountVisitsPerHour() {
        return this.countRequestOfUsers / getPeriodOfTimeInHours();
    }

    /**
     * Метод возвращает среднее количество ошибочных запросов пользователями сайта за час
     */
    public int getCountFailedRequestOfUsersPerHour() {
        return this.countFailedRequestOfUsers / getPeriodOfTimeInHours();
    }

    /**
     * Метод возвращает период времени в часах, за который имеются записи в логе
     */
    private int getPeriodOfTimeInHours() {
        Period period = Period.between(getMinTime().toLocalDate(), getMaxTime().toLocalDate());
        Duration duration = Duration.between(getMinTime().toLocalTime(), getMaxTime().toLocalTime());
        return period.getDays() * 24 + duration.toHoursPart();
    }


}
