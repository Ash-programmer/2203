package com.example.controllers;

import com.example.domain.Action;
import com.example.domain.BattleResult;
import com.example.domain.BattleState;
import com.example.domain.Party;
import com.example.services.BattleService;

public class BattleController {

    private BattleService battleService;

    public BattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    public BattleState startBattle(Party player, Party enemy) {
        return battleService.startBattle(player, enemy);
    }

    public BattleResult executeTurn(BattleState state, Action action) {
        return battleService.executeTurn(state, action);
    }
}