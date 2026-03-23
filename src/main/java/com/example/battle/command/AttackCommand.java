package com.example.battle.command;

import com.example.domain.Hero;

public class AttackCommand implements Command {

    private Hero actor;
    private Hero target;

    public AttackCommand(Hero actor, Hero target) {
        this.actor = actor;
        this.target = target;
    }

    @Override
    public void execute() {

        if (target != null) {
            target.takeDamage(actor.getAttack());
        }

    }
}