import java.io.File;
import java.util.Scanner;

public class Main {
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
        }
    }
}
