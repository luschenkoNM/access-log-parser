package ru.courses;

import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

@Getter
public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

    public Statistics() {
        this.minTime = LocalDateTime.MAX;
        this.maxTime = LocalDateTime.MIN;
        this.totalTraffic = 0;
    }

    public void addEntry(LogEntry logEntry) {
        this.totalTraffic = getTotalTraffic() + logEntry.getResponseSize();
        if (logEntry.getTime().isBefore(getMinTime()))
            this.minTime = logEntry.getTime();
        if (logEntry.getTime().isAfter(getMaxTime()))
            this.maxTime = logEntry.getTime();

    }

    public long getTrafficRate() {
        Period period = Period.between(getMinTime().toLocalDate(), getMaxTime().toLocalDate());
        Duration duration = Duration.between(getMinTime().toLocalTime(), getMaxTime().toLocalTime());
        long result = this.totalTraffic / (period.getDays() * 24 + duration.toHoursPart());
        return result;
    }
}
