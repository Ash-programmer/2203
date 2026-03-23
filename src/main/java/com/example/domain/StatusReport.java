package com.example.domain;

import java.util.ArrayList;
import java.util.List;

public class StatusReport {

    private List<Hero> healedHeroes;
    private List<Hero> revivedHeroes;
    private String message;

    public StatusReport() {
        this.healedHeroes = new ArrayList<>();
        this.revivedHeroes = new ArrayList<>();
        this.message = "";
    }

    public void addHealedHero(Hero hero) {
        healedHeroes.add(hero);
    }

    public void addRevivedHero(Hero hero) {
        revivedHeroes.add(hero);
    }

    public List<Hero> getHealedHeroes() {
        return healedHeroes;
    }

    public List<Hero> getRevivedHeroes() {
        return revivedHeroes;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}