package Task5;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Object lock = new Object();
        System.out.println("Choose the type of output:");
        System.out.println("1. Synchronized output");
        System.out.println("2. Simple output");
        System.out.print("Enter your choice (1 or 2): ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        if (choice == 1) {
            Thread thread1 = new Thread(new SymbolPrinter(lock, '-'));
            Thread thread2 = new Thread(new SymbolPrinter(lock, '|'));
            thread1.start();
            thread2.start();
        } else if (choice == 2) {
            Thread simpleThread1 = new Thread(new SimpleSymbolPrinter('-'));
            Thread simpleThread2 = new Thread(new SimpleSymbolPrinter('|'));
            simpleThread1.start();
            simpleThread2.start();
        } else {
            System.out.println("Invalid choice. Please enter 1 or 2.");
        }
    }
}
