package com.example.domain;

public class Action {

    private ActionType type;
    private Hero actor;
    private Hero target;

    public Action(ActionType type, Hero actor, Hero target) {
        this.type = type;
        this.actor = actor;
        this.target = target;
    }

    public ActionType getType() {
        return type;
    }

    public Hero getActor() {
        return actor;
    }

    public Hero getTarget() {
        return target;
    }
}