package com.example.domain;

public class BattleResult {

    private boolean playerWon;
    private int expGained;
    private int goldGained;
    private String message;

    public BattleResult(boolean playerWon, int expGained, int goldGained, String message) {
        this.playerWon = playerWon;
        this.expGained = expGained;
        this.goldGained = goldGained;
        this.message = message;
    }

    public boolean didPlayerWin() {
        return playerWon;
    }

    public int getExpGained() {
        return expGained;
    }

    public int getGoldGained() {
        return goldGained;
    }

    public String getMessage() {
        return message;
    }
}