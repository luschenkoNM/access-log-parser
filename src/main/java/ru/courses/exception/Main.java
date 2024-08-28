package ru.courses.exception;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Main {
    public static final String PATH = "access-log-parser/src/main/resources/access.log";

    public static void main(String[] args) {
        File file = new File(PATH); // проверка существования файла осуществляется классом File

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
