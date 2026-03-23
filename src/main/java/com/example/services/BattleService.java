package com.example.services;

import com.example.domain.Action;
import com.example.domain.ActionType;
import com.example.domain.BattleResult;
import com.example.domain.BattleState;
import com.example.domain.Hero;
import com.example.domain.Party;

public class BattleService {

    public BattleState startBattle(Party player, Party enemy) {
        return new BattleState(player, enemy);
    }

    public BattleResult executeTurn(BattleState state, Action action) {

        Hero actor = action.getActor();
        Hero target = action.getTarget();

        if (actor == null) {
            return new BattleResult(false, 0, 0, "Invalid actor");
        }

        if (!actor.isAlive()) {
            return new BattleResult(false, 0, 0, "Actor is dead");
        }

        if (action.getType() == ActionType.ATTACK) {

            if (target != null) {
                target.takeDamage(actor.getAttack());
            }
        }

        if (action.getType() == ActionType.DEFEND) {
            // simple defend (no change yet)
        }

        if (action.getType() == ActionType.WAIT) {
            // do nothing
        }

        if (action.getType() == ActionType.SPECIAL) {
            if (target != null) {
                target.takeDamage(actor.getAttack() + 5);
            }
        }

        state.checkBattleEnd();

        if (state.isFinished()) {

            boolean playerWon = state.getEnemyParty().hasLivingHeroes() == false;

            if (playerWon) {
                return new BattleResult(true, 50, 30, "Player won");
            } else {
                return new BattleResult(false, 10, 5, "Player lost");
            }
        }

        state.nextTurn();

        return new BattleResult(false, 0, 0, "Turn complete");
    }
}