package com.example.domain;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private List<Item> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean hasItem(Item item) {
        return items.contains(item);
    }

    public boolean useItem(Item item, Hero hero) {

        if (!items.contains(item)) {
            return false;
        }

        item.applyEffect(hero);
        items.remove(item);

        return true;
    }

    public int getSize() {
        return items.size();
    }
}