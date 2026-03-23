package com.example.domain.factory;

import com.example.domain.Hero;

public class HeroFactory {

    public static Hero createWarrior(String name) {
        return new Hero(name, "Warrior");
    }

    public static Hero createMage(String name) {
        return new Hero(name, "Mage");
    }

    public static Hero createRogue(String name) {
        return new Hero(name, "Rogue");
    }

}