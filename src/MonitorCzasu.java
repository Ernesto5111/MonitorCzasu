import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MonitorCzasu {

private static LocalDateTime start = null;
private static final String FILE_NAME = "sesje.txt";
private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);
    boolean running = true;

    while (running) {
        System.out.println("\n--- Monitor czasu ---");
        System.out.println("1. Rozpocznij nową sesję");
        System.out.println("2. Zakończ sesję");
        System.out.println("3. Pokaż historię sesji");
        System.out.println("4. Wyjdź");
        System.out.print("Wybór: ");

        try {
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    startSesji();
                    break;
                case "2":
                    koniecSesji();
                    break;
                case "3":
                    pokazHistorie();
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    System.out.println("Nieprawidłowy wybór!");
            }

        } catch (Exception e) {
            System.out.println("Błąd: " + e.getMessage());
        } finally {
            System.out.println("Operacja zakończona.");
        }
    }

    scanner.close();
}


private static void startSesji() {
    if (start != null) {
        throw new IllegalStateException("Sesja już trwa!");
    }
    start = LocalDateTime.now();
    System.out.println("Sesja rozpoczęta o: " + start.format(FORMAT));
}


private static void koniecSesji() {
    if (start == null) {
        throw new IllegalStateException("Nie rozpoczęto wcześniej sesji!");
    }

    LocalDateTime end = LocalDateTime.now();
    long minutes = Duration.between(start, end).toMinutes();

    try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
        writer.write("[" + start.format(FORMAT) + "] Sesja trwała: " + minutes + " minut\n");
        System.out.println("Sesja zakończona. Czas: " + minutes + " minut.");
    } catch (IOException e) {
        System.out.println("Błąd zapisu do pliku: " + e.getMessage());
    }

    start = null;
}

private static void pokazHistorie() {
    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {

        String line;
        boolean empty = true;

        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            empty = false;
        }

        if (empty) {
            System.out.println("Brak zapisanych sesji.");
        }

    } catch (IOException e) {

        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            System.out.println("Brak zapisanych sesji.");
        } catch (IOException ex) {
            System.out.println("Błąd pracy z plikiem: " + ex.getMessage());
        }
    }
}
}
