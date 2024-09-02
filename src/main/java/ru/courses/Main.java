package ru.courses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final String PATH = "access-log-parser/src/main/resources/access.log";
    public static final String PATH_TEST = "access-log-parser/src/main/resources/file.txt";
    public static String str = "219.94.241.169 - - [25/Sep/2022:07:08:22 +0300] \"GET /housekeeping/?rss=1&t=2&p=53&c=205&s=114&lg=1 HTTP/1.0\" 200 2562 \"-\" \"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0";

    public static void main(String[] args) {
        int count = 1;
        while (true) {
            System.out.print("Введите путь к файлу: ");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExist = file.exists();
            boolean isDirectory = file.isDirectory();
            if (!fileExist) {
                System.out.println("Указанный файл не существует.");
                continue;
            }
            if (isDirectory) {
                System.out.println("Указанный путь является путём к папке, а не к файлу.");
                continue;
            }
            System.out.println("Путь указан верно. Это файл номер: " + count);
            count++;

            parseFileLogEntry(file);
        }

    }

    public static List parseFileLogEntry(File file) {
        List<LogEntry> list = new ArrayList<>();
        Statistics statistics = new Statistics();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int countStr = 0;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 1024)
                    throw new LineLengthException("В файле имеется строка превышающая 1024 символа");
                list.add(new LogEntry(line));
                statistics.addEntry(list.get(countStr));
                countStr++;
            }
            System.out.println("Общий трафик: " + statistics.getTotalTraffic());
            System.out.println("Минимальное время: " + statistics.getMinTime());
            System.out.println("Максимальное время: " + statistics.getMaxTime());
            System.out.println("Часовой трафик: " + statistics.getTrafficRate());
            System.out.println("Общее количество строк = " + countStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static List parseFile(File file, FragmentLog fragmentLog) {
        List list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int countStr = 0;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 1024)
                    throw new LineLengthException("В файле имеется строка превышающая 1024 символа");
                list.add(fragmentLog.getParameters(line));
                countStr++;
            }
            System.out.println("Общее количество строк = " + countStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static void printCountBots(List list) {
        int countYandexBot = 0;
        int countGooglebot = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals("YandexBot"))
                countYandexBot++;
            if (list.get(i).equals("Googlebot"))
                countGooglebot++;
        }
        System.out.println("Количество запросов от YandexBot = " + countYandexBot);
        System.out.println("Количество запросов от Googlebot = " + countGooglebot);
    }
}
