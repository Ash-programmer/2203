package com.example.battle.staratgy;

import com.example.domain.Hero;

public interface BattleStrategy {

    void execute(Hero actor, Hero target);

}