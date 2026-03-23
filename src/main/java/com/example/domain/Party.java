package com.example.domain;

import java.util.ArrayList;
import java.util.List;

public class Party {

    private List<Hero> heroes;
    private int gold;
    private int maxSize;

    public Party() {
        this.heroes = new ArrayList<>();
        this.gold = 100;
        this.maxSize = 3;
    }

    public Party(int maxSize) {
        this.heroes = new ArrayList<>();
        this.gold = 100;
        this.maxSize = maxSize;
    }

    public boolean addHero(Hero hero) {
        if (heroes.size() >= maxSize) {
            return false;
        }

        heroes.add(hero);
        return true;
    }

    public boolean removeHero(Hero hero) {
        return heroes.remove(hero);
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public int getSize() {
        return heroes.size();
    }

    public boolean isFull() {
        return heroes.size() >= maxSize;
    }

    public boolean hasLivingHeroes() {
        for (Hero h : heroes) {
            if (h.isAlive()) {
                return true;
            }
        }
        return false;
    }

    public void addGold(int amount) {
        gold += amount;
    }

    public boolean spendGold(int amount) {
        if (gold < amount) {
            return false;
        }

        gold -= amount;
        return true;
    }

    public int getGold() {
        return gold;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public Hero getHighestLevelHero() {

        if (heroes.isEmpty()) return null;

        Hero best = heroes.get(0);

        for (Hero h : heroes) {
            if (h.getLevel() > best.getLevel()) {
                best = h;
            }
        }

        return best;
    }
}