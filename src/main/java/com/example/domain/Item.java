package com.example.domain;

public class Item {

    private String name;
    private int cost;
    private String effectType;
    private int effectValue;

    public Item(String name, int cost, String effectType, int effectValue) {
        this.name = name;
        this.cost = cost;
        this.effectType = effectType;
        this.effectValue = effectValue;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public String getEffectType() {
        return effectType;
    }

    public int getEffectValue() {
        return effectValue;
    }

    public void applyEffect(Hero hero) {
        if (effectType.equals("heal")) {
            hero.heal(effectValue);
        }

        if (effectType.equals("mana")) {
            hero.addMana(effectValue);
        }

        if (effectType.equals("buff")) {
            hero.addShield(effectValue);
        }
    }
}