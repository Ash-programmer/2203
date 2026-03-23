package com.example.domain;

import java.util.ArrayList;
import java.util.List;

public class BattleState {

    private Party playerParty;
    private Party enemyParty;

    private List<Hero> turnOrder;
    private int turnIndex;

    private boolean finished;

    public BattleState(Party playerParty, Party enemyParty) {
        this.playerParty = playerParty;
        this.enemyParty = enemyParty;

        this.turnOrder = new ArrayList<>();
        this.turnIndex = 0;
        this.finished = false;

        createTurnOrder();
    }

    private void createTurnOrder() {

        turnOrder.addAll(playerParty.getHeroes());
        turnOrder.addAll(enemyParty.getHeroes());
    }

    public Hero getCurrentHero() {

        if (turnOrder.isEmpty()) return null;

        return turnOrder.get(turnIndex);
    }

    public void nextTurn() {

        turnIndex++;

        if (turnIndex >= turnOrder.size()) {
            turnIndex = 0;
        }
    }

    public Party getPlayerParty() {
        return playerParty;
    }

    public Party getEnemyParty() {
        return enemyParty;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void checkBattleEnd() {

        if (!playerParty.hasLivingHeroes() || !enemyParty.hasLivingHeroes()) {
            finished = true;
        }
    }
}