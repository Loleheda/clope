package org.example.db;

import org.example.transaction.Transaction;

import java.util.List;

/**
 * Интерфейс, представляющий базу данных транзакций
 */
public interface DB {

    /**
     * Создает список транзакций
     *
     * @return список транзакций
     */
    List<Transaction> create();
}
