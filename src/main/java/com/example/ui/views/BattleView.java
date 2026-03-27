package com.example.ui.views;

import com.example.Main;
import com.example.controllers.BattleController;
import com.example.domain.ActionType;
import com.example.domain.BattleResult;
import com.example.domain.BattleState;
import com.example.domain.Hero;
import com.example.domain.Party;
import com.example.ui.UICommands;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BattleView extends JFrame implements UICommands {

    private final BattleController controller;
    private final AppState appState;

    private JTextArea output;
    private JComboBox<String> targetBox;

    private BattleState battleState;
    private boolean rewardsApplied = false;

    public BattleView(AppState state, BattleController controller) {
        this.appState = state;
        this.controller = controller;
        init();
    }

    private void init() {
        setTitle("Battle");
        setSize(800, 560);
        setLocationRelativeTo(null);

        output = new JTextArea();
        output.setEditable(false);

        targetBox = new JComboBox<>();

        JButton start = new JButton("Start Battle");
        JButton attack = new JButton("Attack");
        JButton defend = new JButton("Defend");
        JButton wait = new JButton("Wait");
        JButton special = new JButton("Special");
        JButton refresh = new JButton("Refresh");

        JPanel top = new JPanel();
        top.add(start);
        top.add(new JLabel("Target"));
        top.add(targetBox);
        top.add(attack);
        top.add(defend);
        top.add(wait);
        top.add(special);
        top.add(refresh);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);

        start.addActionListener(e -> startBattle());
        attack.addActionListener(e -> playerAction(ActionType.ATTACK));
        defend.addActionListener(e -> playerAction(ActionType.DEFEND));
        wait.addActionListener(e -> playerAction(ActionType.WAIT));
        special.addActionListener(e -> playerAction(ActionType.SPECIAL));
        refresh.addActionListener(e -> refreshBattleView());
    }

    private void startBattle() {
        Party playerParty;

        if (appState.currentCampaign != null && appState.currentCampaign.getParty() != null) {
            playerParty = appState.currentCampaign.getParty();
        } else if (appState.currentParty != null) {
            playerParty = appState.currentParty;
        } else {
            playerParty = new Party();
            playerParty.addHero(new Hero("DemoHero", "Warrior"));
        }

        if (playerParty.getHeroes().isEmpty()) {
            append("Your party has no heroes.");
            return;
        }

        Party enemyParty = buildEnemyParty();

        battleState = controller.startBattle(playerParty, enemyParty);
        appState.battleInProgress = true;
        appState.currentlyInBattle = true;
        appState.currentlyInInn = false;
        rewardsApplied = false;

        append("Battle started.");
        refreshBattleView();
        processEnemyTurnsIfNeeded();
    }

    private Party buildEnemyParty() {
        Party enemy = new Party();
        int room = appState.currentCampaign != null ? appState.currentCampaign.getCurrentRoom() : 1;

        if (room < 10) {
            enemy.addHero(new Hero("Goblin", "Chaos"));
        } else if (room < 20) {
            enemy.addHero(new Hero("Bandit", "Warrior"));
            enemy.addHero(new Hero("Cultist", "Chaos"));
        } else {
            enemy.addHero(new Hero("Knight", "Order"));
            enemy.addHero(new Hero("Warlock", "Mage"));
            enemy.addHero(new Hero("Brute", "Warrior"));
        }

        return enemy;
    }

    private void playerAction(ActionType type) {
        if (battleState == null) {
            append("Start a battle first.");
            return;
        }

        if (battleState.isFinished()) {
            append("Battle is already over.");
            return;
        }

        Hero actor = battleState.getCurrentHero();
        if (actor == null) {
            append("No current actor.");
            return;
        }

        if (!isPlayerHero(actor)) {
            append("It is not your team's turn.");
            processEnemyTurnsIfNeeded();
            return;
        }

        Hero target = null;
        int manaCost = 0;

        if (type == ActionType.ATTACK || type == ActionType.SPECIAL) {
            target = selectedEnemyTarget();
            if (target == null && type == ActionType.ATTACK) {
                append("Choose a target.");
                return;
            }
            if (type == ActionType.SPECIAL) {
                manaCost = manaCostFor(actor);
            }
        }

        com.example.domain.Action action =
                new com.example.domain.Action(type, actor, target, manaCost);

        BattleResult result = controller.executeTurn(battleState, action);
        append(actor.getName() + " used " + type + ". " + result.getMessage());

        if (battleState.isFinished()) {
            applyBattleOutcome(result);
            refreshBattleView();
            postBattleChoice();
            return;
        }

        refreshBattleView();
        processEnemyTurnsIfNeeded();
    }

    private void processEnemyTurnsIfNeeded() {
        while (battleState != null && !battleState.isFinished()) {
            Hero actor = battleState.getCurrentHero();
            if (actor == null || isPlayerHero(actor)) {
                break;
            }

            Hero target = firstLivingHero(battleState.getPlayerParty());
            if (target == null) {
                battleState.checkBattleEnd();
                break;
            }

            com.example.domain.Action action =
                    new com.example.domain.Action(ActionType.ATTACK, actor, target, 0);

            BattleResult result = controller.executeTurn(battleState, action);
            append(actor.getName() + " attacks " + target.getName() + ". " + result.getMessage());

            if (battleState.isFinished()) {
                applyBattleOutcome(result);
                refreshBattleView();
                postBattleChoice();
                break;
            }
        }

        refreshBattleView();
    }

    private void postBattleChoice() {
        appState.battleInProgress = false;
        appState.currentlyInBattle = false;

        String[] options = {"Head Back to Inn", "Go to Next Battle"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Battle finished. What do you want to do next?",
                "After Battle",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            appState.currentlyInInn = true;
            if (appState.currentCampaign != null) {
                appState.currentCampaign.setLastRoomType(com.example.domain.RoomType.INN);
                Main.campaignController.saveProgress(appState.currentUser.getUserId(), appState.currentCampaign);
            }
            new InnView(appState, Main.innController).start();
            return;
        }

        if (!isPartyAtFullHealth()) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Your heroes are not at full health. Continue to the next battle anyway?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm != JOptionPane.YES_OPTION) {
                appState.currentlyInInn = true;
                if (appState.currentCampaign != null) {
                    appState.currentCampaign.setLastRoomType(com.example.domain.RoomType.INN);
                    Main.campaignController.saveProgress(appState.currentUser.getUserId(), appState.currentCampaign);
                }
                new InnView(appState, Main.innController).start();
                return;
            }
        }

        if (appState.currentCampaign != null) {
            appState.currentCampaign.advanceRoom();
            appState.currentCampaign.setLastRoomType(com.example.domain.RoomType.BATTLE);
            Main.campaignController.saveProgress(appState.currentUser.getUserId(), appState.currentCampaign);
        }

        appState.currentlyInBattle = true;
        appState.battleInProgress = true;
        startBattle();
    }

    private boolean isPartyAtFullHealth() {
        Party party = battleState.getPlayerParty();
        for (Hero hero : party.getHeroes()) {
            if (hero.getHp() < hero.getMaxHp()) {
                return false;
            }
        }
        return true;
    }

    private void applyBattleOutcome(BattleResult result) {
        if (rewardsApplied) return;
        rewardsApplied = true;

        Party playerParty = battleState.getPlayerParty();

        if (result.didPlayerWin()) {
            playerParty.addGold(result.getGoldGained());

            for (Hero hero : playerParty.getHeroes()) {
                if (hero.isAlive()) {
                    hero.gainExperience(result.getExpGained());
                }
            }

            if (appState.currentCampaign != null) {
                appState.currentCampaign.addScore(result.getGoldGained() + result.getExpGained());
                Main.campaignController.saveProgress(appState.currentUser.getUserId(), appState.currentCampaign);
            }

            append("Victory rewards applied.");
        } else {
            int goldLoss = playerParty.getGold() / 10;
            playerParty.spendGold(goldLoss);

            for (Hero hero : playerParty.getHeroes()) {
                hero.loseExperiencePercent(0.30);
            }

            if (appState.currentCampaign != null) {
                Main.campaignController.saveProgress(appState.currentUser.getUserId(), appState.currentCampaign);
            }

            append("Defeat penalties applied.");
        }
    }

    private void refreshBattleView() {
        if (battleState == null) {
            output.setText("No battle started.\n");
            targetBox.setModel(new DefaultComboBoxModel<>(new String[0]));
            return;
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (Hero enemy : livingHeroes(battleState.getEnemyParty())) {
            model.addElement(enemy.getName());
        }
        targetBox.setModel(model);

        StringBuilder sb = new StringBuilder();

        sb.append("=== TURN ===\n");
        Hero current = battleState.getCurrentHero();
        if (current != null) {
            sb.append("Current actor: ").append(current.getName())
                    .append(" [").append(current.getType()).append("]\n");
            sb.append("Side: ").append(isPlayerHero(current) ? "Player" : "Enemy").append("\n");
            sb.append("Status: ").append(current.isAlive() ? "Ready" : "Dead").append("\n\n");
        }

        sb.append("=== PLAYER TEAM ===\n");
        appendParty(sb, battleState.getPlayerParty());

        sb.append("\n=== ENEMY TEAM ===\n");
        appendParty(sb, battleState.getEnemyParty());

        output.setText(sb.toString());
    }

    private void appendParty(StringBuilder sb, Party party) {
        sb.append("Gold: ").append(party.getGold()).append("\n");
        for (Hero h : party.getHeroes()) {
            sb.append("- ")
                    .append(h.getName())
                    .append(" [").append(h.getType()).append("]")
                    .append(" L").append(h.getLevel())
                    .append(" HP ").append(h.getHp()).append("/").append(h.getMaxHp())
                    .append(" Mana ").append(h.getMana())
                    .append(" ATK ").append(h.getAttack())
                    .append(" DEF ").append(h.getDefense())
                    .append(h.isAlive() ? "" : " (DEAD)")
                    .append("\n");
        }
    }

    private boolean isPlayerHero(Hero hero) {
        return battleState.getPlayerParty().getHeroes().contains(hero);
    }

    private Hero selectedEnemyTarget() {
        Object selected = targetBox.getSelectedItem();
        if (selected == null) return null;

        String name = selected.toString();
        for (Hero h : battleState.getEnemyParty().getHeroes()) {
            if (h.isAlive() && h.getName().equals(name)) {
                return h;
            }
        }
        return null;
    }

    private Hero firstLivingHero(Party party) {
        for (Hero h : party.getHeroes()) {
            if (h.isAlive()) return h;
        }
        return null;
    }

    private List<Hero> livingHeroes(Party party) {
        List<Hero> result = new ArrayList<>();
        for (Hero h : party.getHeroes()) {
            if (h.isAlive()) result.add(h);
        }
        return result;
    }

    private int manaCostFor(Hero hero) {
        return switch (hero.getType()) {
            case "Order" -> 25;
            case "Chaos" -> 30;
            case "Warrior" -> 60;
            case "Mage" -> 80;
            default -> 20;
        };
    }

    private void append(String message) {
        output.append("\n" + message + "\n");
    }

    public void start() {
        setVisible(true);
    }
}
