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

            printCountBots(parseFile(file, FragmentLog.USER_AGENT_BOT));
        }

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
