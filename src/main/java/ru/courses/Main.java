package ru.courses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static final String PATH = "access-log-parser/src/main/resources/access.log";

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
            parseFile(file);
        }

    }

    public static void parseFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int countStr = 0;
            int maxLengthStr = 0;
            int minLengthStr = Integer.MAX_VALUE;
            while ((line = reader.readLine()) != null) {
                int length = line.length();
                if (line.length() > 1024)
                    throw new LineLengthException("В файле имеется строка превышающая 1024 символа");
                if (length > maxLengthStr) maxLengthStr = length;
                if (length < minLengthStr) minLengthStr = length;
                countStr++;
            }
            System.out.println("Общее количество строк = " + countStr);
            System.out.println("Длина самой длинной строки = " + maxLengthStr);
            System.out.println("Длина самой короткой строки = " + minLengthStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
