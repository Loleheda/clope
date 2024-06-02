package org.example;

import org.example.transaction.Transaction;

import java.util.*;

/**
 * Класс, представляющий кластер с транзакциями
 */
public class Cluster {
    private Map<Object, Integer> histogram;
    private int transactionCount;
    private int itemsSum;
    private int width;

    public Cluster() {
        this.histogram = new HashMap<>();
    }

    /**
     * Добавляет транзакцию к кластеру
     *
     * @param transaction транзакция для добавления
     */
    public void addTransaction(Transaction transaction) {
        List<Object> items = transaction.getItems();
        Map<Object, Integer> newHistogram = getHistogram();
        for (Object item : items) {
            newHistogram.put(item, newHistogram.getOrDefault(item, 0)+1);
        }
        setHistogram(newHistogram);
        setWidth(newHistogram.size());
        setItemsSum(getItemsSum()+items.size());
        setTransactionCount(getTransactionCount()+1);
    }

    /**
     * Удаляет транзакцию из кластера
     *
     * @param transaction транзакция для удаления
     */
    public void removeTransaction(Transaction transaction)  {
        List<Object> items = transaction.getItems();
        Map<Object, Integer> newHistogram = getHistogram();
        for (Object item : items) {
            newHistogram.put(item, newHistogram.get(item)-1);
            if (newHistogram.get(item) == 0) {
                newHistogram.remove(item);
            }
        }
        setWidth(newHistogram.size());
        setItemsSum(getItemsSum()-itemsSum);
        setTransactionCount(getTransactionCount()-1);
        setHistogram(newHistogram);
    }

    /**
     * Получает гистограмму кластера
     *
     * @return гистограмма кластера
     */
    public Map<Object, Integer> getHistogram() {
        return histogram;
    }

    /**
     * Устанавливает гистограмму кластера
     *
     * @param histogram новая гистограмма
     */
    public void setHistogram(Map<Object, Integer> histogram) {
        this.histogram = histogram;
    }

    /**
     * Возвращает количество транзакций в кластере
     *
     * @return количество транзакций
     */
    public int getTransactionCount() {
        return transactionCount;
    }

    /**
     * Устанавливает количество транзакций в кластере
     *
     * @param transactionCount новое количество транзакций
     */
    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }

    /**
     * Возвращает суммарное количество элементов в кластере
     *
     * @return сумма элементов
     */
    public int getItemsSum() {
        return itemsSum;
    }

    /**
     * Устанавливает суммарное количество элементов в кластере
     *
     * @param itemsSum новая сумма элементов
     */
    public void setItemsSum(int itemsSum) {
        this.itemsSum = itemsSum;
    }

    /**
     * Возвращает ширину кластера (количество уникальных элементов)
     *
     * @return ширина кластера
     */
    public int getWidth() {
        return width;
    }

    /**
     * Устанавливает ширину кластера (количество уникальных элементов)
     *
     * @param width новая ширина кластера
     */
    public void setWidth(int width) {
        this.width = width;
    }
}
