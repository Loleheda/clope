package org.example.transaction;

import java.util.ArrayList;
import java.util.List;

public class MushroomTransaction implements Transaction {
    private List<String> items;
    private int clusterIndex;
    private String group;

    public MushroomTransaction(String group, List<String> items) {
        this.group = group;
        this.items = items;
        this.clusterIndex = -1;
    }

    @Override
    public List<Object> getItems() {
        return new ArrayList<>(items);
    }

    @Override
    public int getClusterIndex() {
        return clusterIndex;
    }

    @Override
    public void setClusterIndex(int clusterIndex) {
        this.clusterIndex = clusterIndex;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public void setGroup(Object group) {
        this.group = group.toString();
    }
}
