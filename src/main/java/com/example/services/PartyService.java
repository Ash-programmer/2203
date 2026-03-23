package com.example.services;

import com.example.domain.Hero;
import com.example.domain.Party;

public class PartyService {

    public boolean addHero(Party party, Hero hero) {
        return party.addHero(hero);
    }

    public boolean removeHero(Party party, Hero hero) {
        return party.removeHero(hero);
    }

    public void applyRewards(Party party, int exp, int gold) {

        party.addGold(gold);

        int heroCount = party.getHeroes().size();

        if (heroCount == 0) return;

        int expPerHero = exp / heroCount;

        for (Hero h : party.getHeroes()) {
            h.gainExperience(expPerHero);
        }
    }

    public boolean hasLivingHeroes(Party party) {
        return party.hasLivingHeroes();
    }

    public Hero getHighestLevelHero(Party party) {
        return party.getHighestLevelHero();
    }
}