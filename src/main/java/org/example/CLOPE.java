package org.example;

import org.example.transaction.Transaction;

import java.util.*;

/**
 * Класс, реализующий алгоритм кластеризации CLOPE
 */
public class CLOPE {

    /**
     * Выполняет алгоритм кластеризации CLOPE
     *
     * @param transactions список транзакций
     * @param r коэффициент отталкивания
     *
     * @return список кластеров
     */
    public List<Cluster> execute(List<Transaction> transactions, double r) {
        List<Cluster> clusters = initialization(transactions, r);
        return iteration(clusters, transactions, r);
    }

    /**
     * Инициализирует кластеры
     *
     * @param transactions список транзакций
     * @param r коэффициент отталкивания
     *
     * @return список инициализированных кластеров
     */
    private List<Cluster> initialization(List<Transaction> transactions, double r) {
        List<Cluster> clusters = new ArrayList<>();
        for (Transaction transaction : transactions) {
            int bestIndex = findBestClusterIndex(clusters, transaction, r);
            if (bestIndex == -1) {
                continue;
            } else if (bestIndex == clusters.size()) {
                clusters.add(new Cluster());
            }

            transaction.setClusterIndex(bestIndex);
            clusters.get(bestIndex).addTransaction(transaction);
        }

        showResult(transactions);
        return clusters;
    }

    /**
     * Проводит итерации с кластерами
     *
     * @param clusters список кластеров для итерации
     * @param transactions список транзакций
     * @param r коэффициент отталкивания
     *
     * @return список кластеров после завершения итераций
     */
    private List<Cluster> iteration(List<Cluster> clusters, List<Transaction> transactions, double r) {
        boolean moved;
        do {
            moved = false;
            for (Transaction transaction : transactions) {
                int bestIndex = findBestClusterIndex(clusters, transaction, r);
                if (bestIndex == -1) {
                    continue;
                } else if (bestIndex == clusters.size()) {
                    clusters.add(new Cluster());
                }

                clusters.get(transaction.getClusterIndex()).removeTransaction(transaction);
                transaction.setClusterIndex(bestIndex);
                clusters.get(bestIndex).addTransaction(transaction);
                moved = true;
            }
        } while (moved);

        // Удаление пустых кластеров
        for (int i = clusters.size()-1; i >= 0; i--) {
            if (clusters.get(i).getWidth() == 0) {
                clusters.remove(i);
            }
        }

        showResult(transactions);
        return clusters;
    }

    /**
     * Находит индекс наилучшего кластера для данной транзакции
     *
     * @param clusters список текущих кластеров
     * @param transaction текущая транзакция для анализа
     * @param r коэффициент отталкивания
     *
     * @return индекс лучшего кластера для данной транзакции
     */
    private int findBestClusterIndex(List<Cluster> clusters, Transaction transaction, double r) {
        int bestIndex = -1;
        double maxDelta = 0;

        int oldClusterIndex = transaction.getClusterIndex();

        double deltaRem = 0;
        if (oldClusterIndex != -1) {
            Cluster oldCluster = clusters.get(oldClusterIndex);
            deltaRem = deltaRemove(oldCluster, transaction, r);
        }

        for (int indexC = 0; indexC < clusters.size(); indexC++) {
            if (indexC == oldClusterIndex) continue;
            Cluster cluster = clusters.get(indexC);
            double deltaAdd = deltaAdd(cluster, transaction, r);
            if (deltaAdd + deltaRem > maxDelta) {
                bestIndex = indexC;
                maxDelta = deltaAdd;
            }
        }

        if (deltaAdd(new Cluster(), transaction, r) + deltaRem > maxDelta) {
            bestIndex = clusters.size();
        }
        return bestIndex;
    }

    /**
     * Рассчитывает изменение delta при добавлении транзакции в кластер
     *
     * @param cluster кластер
     * @param transaction транзакция
     * @param r коэффициент отталкивания
     *
     * @return изменение delta при добавлении
     */
    private double deltaAdd(Cluster cluster, Transaction transaction, double r) {
        int newItemsSum = cluster.getItemsSum() + transaction.getItems().size();
        int newWidth = cluster.getWidth();
        for (Object item : transaction.getItems()) {
            if (!cluster.getHistogram().containsKey(item)) {
                newWidth++;
            }
        }

        double newDelta = 0;
        double oldDelta = 0;
        if (newWidth != 0) {
            newDelta = newItemsSum * (cluster.getTransactionCount() + 1) / Math.pow(newWidth, r);
        }
        if (cluster.getWidth() != 0) {
            oldDelta = cluster.getItemsSum() * cluster.getTransactionCount() / Math.pow(cluster.getWidth(), r);
        }
        return newDelta - oldDelta;
    }

    /**
     * Рассчитывает изменение delta при удалении транзакции из кластера
     *
     * @param cluster кластер
     * @param transaction транзакция
     * @param r коэффициент отталкивания
     *
     * @return изменение delta при удалении
     */
    private double deltaRemove(Cluster cluster, Transaction transaction, double r) {
        int newItemsSum = cluster.getItemsSum() - transaction.getItems().size();
        int newWidth = cluster.getWidth();

        for (Object item : transaction.getItems()) {
            if (cluster.getHistogram().get(item) - 1 == 0) {
                newWidth--;
            }
        }

        double newDelta = 0;
        double oldDelta = cluster.getItemsSum() * cluster.getTransactionCount() / Math.pow(cluster.getWidth(), r);
        if (newWidth != 0) {
            newDelta = newItemsSum * (cluster.getTransactionCount() - 1) / Math.pow(newWidth, r);
        }
        return newDelta - oldDelta;
    }

    /**
     * Отображает результаты кластеризации транзакций
     *
     * @param transactions список транзакций для отображения результатов
     */
    public void showResult(List<Transaction> transactions) {
        Map<Integer, Map<Object, Integer>> result = new HashMap<>();
        Map<Object, Integer> groups = new HashMap<>();
        for (Transaction transaction : transactions) {
            int index = transaction.getClusterIndex();
            Object group = transaction.getGroup();
            groups.put(group, groups.getOrDefault(group, 0)+1);

            result.putIfAbsent(index, new HashMap<>());
            result.get(index).put(group, result.get(index).getOrDefault(group, 0)+1);
        }

        StringBuilder title = new StringBuilder("Кластер\t\t");
        StringBuilder total = new StringBuilder("Итоги:\t\t");

        groups.entrySet()
                .forEach(entry -> {
                    title.append(entry.getKey()).append("\t\t\t");
                    total.append(entry.getValue()).append("\t\t");
                });
        System.out.println(title);

        for (Map.Entry<Integer, Map<Object, Integer>> entry : result.entrySet()) {
            StringBuilder row = new StringBuilder(entry.getKey() + 1 + "\t\t\t");
            for (Object group : groups.keySet()) {
                row.append(
                        (entry.getValue().get(group) != null) ?
                            (entry.getValue().get(group) >= 1000) ?
                                entry.getValue().get(group) + "\t\t" : entry.getValue().get(group) + "\t\t\t"
                        : 0 + "\t\t\t");
            }
            System.out.println(row);
        }
        System.out.println(total + "\n");
    }
}
