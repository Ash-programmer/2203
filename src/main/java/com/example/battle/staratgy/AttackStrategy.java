package com.example.battle.staratgy;

import com.example.domain.Hero;

public class AttackStrategy implements BattleStrategy {

    @Override
    public void execute(Hero actor, Hero target) {

        if (target != null) {
            target.takeDamage(actor.getAttack());
        }

    }
}