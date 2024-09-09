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

//            System.out.println("Общий трафик: " + getStatistics(file).getTotalTraffic());
//            System.out.println("Часовой трафик: " + getStatistics(file).getTrafficRate());
//            System.out.println("Минимальное время: " + getStatistics(file).getMinTime());
//            System.out.println("Максимальное время: " + getStatistics(file).getMaxTime());
//            System.out.println("Запросы с кодом 200: " + getStatistics(file).getSetExistingPages());
//            System.out.println("Количество ОС: " + getStatistics(file).getCountOS());
//            System.out.println("Статистика ОС: " + getStatistics(file).getStatisticsOS());
//            System.out.println("Запросы с кодом 404: " + getStatistics(file).getSetNonExistingPages());
//            System.out.println("Количество используемых браузеров" + getStatistics(file).getCountBrowsers());
//            System.out.println("Статистика по используемым браузерам" + getStatistics(file).getStatisticsBrowsers());
//            System.out.println("Среднее количество посещений сайта за час: " + getStatistics(file).getCountVisitsPerHour());
//            System.out.println("Количество ошибочных запросов: " + getStatistics(file).getCountFailedRequestOfUsers());
//            System.out.println("Среднее количество ошибочных запросов в час: " + getStatistics(file).getCountFailedRequestOfUsersPerHour());
            System.out.println("Средняя посещаемость одним пользователем: " + getStatistics(file).getCountUsers());
        }

    }
    /**
     * Метод возвращает экземляр класса Statistics для возможности получение статистики из переданноло лог-файла
     */
    public static Statistics getStatistics(File file) {
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
            // System.out.println("Количесвто строк: " + countStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return statistics;
    }

    /**
     * Метод возвращает список распарсенный на элементы предусмотренные в классе LogEntry
     */
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * Метод возвращает список фрагментов лог-файла
     * @param file - лог
     * @param fragmentLog - фрагмент лога
     */
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

    /**
     * Метод выводит в консоль количество запросов от YandexBot и YandexBot в переданном списке
     */
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
        System.out.println("Количество запросов от YandexBot = " + countGooglebot);
    }
}
