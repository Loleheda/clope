package org.example;


import org.example.db.MushroomDB;
import org.example.db.DB;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        double r = 0;
        do {
            System.out.print("Введите коэффициент отталкивания (целая и дробная часть разделяются запятой): ");
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextDouble()) {
                r = scanner.nextDouble();
            }
        } while (r == 0);

        DB db = new MushroomDB();
        CLOPE clope = new CLOPE();
        clope.execute(db.create(), r);
    }
}