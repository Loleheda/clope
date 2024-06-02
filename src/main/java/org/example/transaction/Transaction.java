package org.example.transaction;

import java.util.List;

/**
 * Интерфейс транзакции
 */
public interface Transaction {
    /**
     * Получить список элементов
     *
     * @return список элементов
     */
    List<Object> getItems();

    /**
     * Получить индекс кластера
     *
     * @return индекс кластера транзакции
     */
    int getClusterIndex();

    /**
     * Установить индекс кластера
     *
     * @param clusterIndex индекс кластера
     */
    void setClusterIndex(int clusterIndex);

    /**
     * Получить группу, связанную с транзакцией
     *
     * @return группа
     */
    Object getGroup();

    /**
     * Установить группу, связанную с транзакцией
     *
     * @param group группа для установки
     */
    void setGroup(Object group);
}
