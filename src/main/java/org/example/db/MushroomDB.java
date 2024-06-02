package org.example.db;

import org.example.transaction.Transaction;
import org.example.transaction.MushroomTransaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MushroomDB implements DB {

    private List<Transaction> transactions;

    public MushroomDB() {
        this.transactions = new ArrayList<>();
    }

    @Override
    public List<Transaction> create() {
        try {
            File myObj = new File("agaricus-lepiota.data");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                List<String> items = new ArrayList<>(List.of(myReader.nextLine().split(",")));
                String group = items.get(0);
                items = items.subList(1, items.size());
                // Так как в транзакции одинаковые буквы на разных позициях имеют разное значение, поэтому приписываем к объекту его позицию
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).equals("?")) continue;
                    items.set(i, items.get(i)+i);
                }
                items.remove("?");
                transactions.add(new MushroomTransaction(group, items));
            }
            myReader.close();
            return transactions;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
