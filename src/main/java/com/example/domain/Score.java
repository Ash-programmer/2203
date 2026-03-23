package com.example.domain;

public class Score {

    private int value;
    private String username;

    public Score(String username, int value) {
        this.username = username;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getUsername() {
        return username;
    }

    public void add(int amount) {
        value += amount;
    }

    public static Score calculate(User user, Campaign campaign) {

        int total = 0;

        total += campaign.getParty().getGold() / 10;

        for (Hero h : campaign.getParty().getHeroes()) {
            total += h.getLevel() * 100;
        }

        return new Score(user.getUsername(), total);
    }
}