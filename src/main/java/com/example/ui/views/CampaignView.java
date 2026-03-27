package com.example.ui.views;

import com.example.Main;
import com.example.controllers.CampaignController;
import com.example.domain.Hero;
import com.example.domain.Inventory;
import com.example.domain.Item;
import com.example.domain.Party;
import com.example.domain.RoomType;
import com.example.domain.Score;
import com.example.ui.UICommands;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CampaignView extends JFrame implements UICommands {

    private final CampaignController controller;
    private final AppState state;

    private JTextArea output;
    private JComboBox<String> classBox;
    private JTextField heroNameField;

    public CampaignView(AppState state, CampaignController controller) {
        this.state = state;
        this.controller = controller;
        init();
        refreshOutput();
    }

    private void init() {
        setTitle("Campaign");
        setSize(780, 540);
        setLocationRelativeTo(null);

        output = new JTextArea();
        output.setEditable(false);

        classBox = new JComboBox<>(new String[]{"Warrior", "Mage", "Order", "Chaos"});
        heroNameField = new JTextField("Hero", 10);

        JButton start = new JButton("Start New Campaign");
        JButton toInn = new JButton("Go to Inn");
        JButton toBattle = new JButton("Continue to Battle");
        JButton useItem = new JButton("Use Item");
        JButton save = new JButton("Save Progress");
        JButton endCampaign = new JButton("End Campaign");
        JButton exit = new JButton("Save & Exit");

        JPanel top = new JPanel();
        top.add(new JLabel("Hero Name"));
        top.add(heroNameField);
        top.add(new JLabel("Class"));
        top.add(classBox);
        top.add(start);

        JPanel bottom = new JPanel();
        bottom.add(toInn);
        bottom.add(toBattle);
        bottom.add(useItem);
        bottom.add(save);
        bottom.add(endCampaign);
        bottom.add(exit);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        start.addActionListener(e -> startCampaign());
        toInn.addActionListener(e -> goToInn());
        toBattle.addActionListener(e -> continueToBattle());
        useItem.addActionListener(e -> useItemOnHero());
        save.addActionListener(e -> saveProgress());
        endCampaign.addActionListener(e -> endCampaignFlow());
        exit.addActionListener(e -> saveAndExit());
    }

    private void startCampaign() {
        String heroType = (String) classBox.getSelectedItem();
        String heroName = heroNameField.getText().trim();
        if (heroName.isBlank()) {
            heroName = heroType;
        }

        Party party = new Party();
        party.setName(state.currentUser.getUsername() + "'s Party");
        party.addHero(new Hero(heroName, heroType));

        state.currentParty = party;
        state.currentInventory = new Inventory();
        state.currentCampaign = controller.startCampaign(state.currentUser, party);

        state.currentUser.getCampaigns().clear();
        state.currentUser.addCampaign(state.currentCampaign);

        state.currentlyInInn = true;
        state.currentlyInBattle = false;
        state.battleInProgress = false;

        append("Started new campaign.");
        append("The first room is the inn.");
        refreshOutput();

        new InnView(state, Main.innController).start();
    }

    private void goToInn() {
        if (state.currentCampaign == null) {
            append("No active campaign.");
            return;
        }

        controller.loadCampaign(state.currentUser.getUserId());
        state.currentlyInInn = true;
        state.currentlyInBattle = false;
        state.battleInProgress = false;
        state.currentCampaign.setLastRoomType(RoomType.INN);
        controller.saveProgress(state.currentUser.getUserId(), state.currentCampaign);

        append("Returned to the inn.");
        refreshOutput();
        new InnView(state, Main.innController).start();
    }

    private void continueToBattle() {
        if (state.currentCampaign == null || state.currentParty == null) {
            append("No active campaign.");
            return;
        }

        if (!isPartyAtFullHealth()) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Your party is not at full health. Continue to battle anyway?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choice != JOptionPane.YES_OPTION) {
                append("Stayed out of battle.");
                return;
            }
        }

        state.currentlyInInn = false;
        state.currentlyInBattle = true;
        state.battleInProgress = true;
        state.currentCampaign.setLastRoomType(RoomType.BATTLE);
        controller.saveProgress(state.currentUser.getUserId(), state.currentCampaign);

        append("Continuing to battle.");
        refreshOutput();
        new BattleView(state, Main.battleController).start();
    }

    private boolean isPartyAtFullHealth() {
        for (Hero hero : state.currentParty.getHeroes()) {
            if (hero.getHp() < hero.getMaxHp()) {
                return false;
            }
        }
        return true;
    }

    private void saveProgress() {
        if (state.currentCampaign == null) {
            append("No campaign to save.");
            return;
        }

        controller.saveProgress(state.currentUser.getUserId(), state.currentCampaign);
        append("Campaign progress saved.");
        refreshOutput();
    }

    private void useItemOnHero() {
        if (state.currentCampaign == null || state.currentInventory == null || state.currentParty == null) {
            append("No active campaign.");
            return;
        }

        List<Item> items = state.currentInventory.getItems();
        if (items.isEmpty()) {
            append("Inventory is empty.");
            return;
        }

        List<Hero> heroes = state.currentParty.getHeroes();
        if (heroes.isEmpty()) {
            append("No heroes in party.");
            return;
        }

        String[] itemOptions = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            itemOptions[i] = item.getName() + " (" + item.getEffectType() + " " + item.getEffectValue() + ")";
        }

        String itemChoice = (String) JOptionPane.showInputDialog(
                this,
                "Choose an item:",
                "Use Item",
                JOptionPane.PLAIN_MESSAGE,
                null,
                itemOptions,
                itemOptions[0]
        );

        if (itemChoice == null) return;

        int itemIndex = findSelectedIndex(itemOptions, itemChoice);
        if (itemIndex < 0) return;
        Item selectedItem = items.get(itemIndex);

        String[] heroOptions = new String[heroes.size()];
        for (int i = 0; i < heroes.size(); i++) {
            Hero h = heroes.get(i);
            heroOptions[i] = h.getName() + " [" + h.getType() + "] HP " + h.getHp() + "/" + h.getMaxHp();
        }

        String heroChoice = (String) JOptionPane.showInputDialog(
                this,
                "Choose a hero:",
                "Use Item",
                JOptionPane.PLAIN_MESSAGE,
                null,
                heroOptions,
                heroOptions[0]
        );

        if (heroChoice == null) return;

        int heroIndex = findSelectedIndex(heroOptions, heroChoice);
        if (heroIndex < 0) return;
        Hero selectedHero = heroes.get(heroIndex);

        boolean used = state.currentInventory.useItem(selectedItem, selectedHero);
        append(used
                ? "Used " + selectedItem.getName() + " on " + selectedHero.getName() + "."
                : "Could not use item.");
        refreshOutput();
    }

    private void endCampaignFlow() {
        if (state.currentCampaign == null) {
            append("No active campaign.");
            return;
        }

        boolean keepParty = JOptionPane.showConfirmDialog(
                this,
                "Do you want to keep this party after the campaign ends?",
                "Keep Party?",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;

        Integer replacePartyId = null;

        if (keepParty && state.currentUser.getSavedParties().size() >= 5) {
            String[] options = new String[state.currentUser.getSavedParties().size()];
            for (int i = 0; i < state.currentUser.getSavedParties().size(); i++) {
                Party p = state.currentUser.getSavedParties().get(i);
                String name = p.getName() == null ? "Party " + p.getId() : p.getName();
                options[i] = p.getId() + " - " + name;
            }

            String selected = (String) JOptionPane.showInputDialog(
                    this,
                    "You already have 5 saved parties. Choose one to replace:",
                    "Replace Party",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (selected == null) {
                append("End campaign cancelled.");
                return;
            }

            String idPart = selected.split(" - ")[0].trim();
            replacePartyId = Integer.parseInt(idPart);
        }

        Score score = controller.endCampaign(state.currentUser, state.currentCampaign, keepParty, replacePartyId);

        if (keepParty) {
            if (replacePartyId != null) {
                final Integer finalReplacePartyId = replacePartyId;
                state.currentUser.getSavedParties().removeIf(p -> p.getId() == finalReplacePartyId);
            }
            state.currentUser.addParty(state.currentCampaign.getParty());
        }

        state.currentUser.getCampaigns().clear();
        state.currentCampaign = null;
        state.currentlyInInn = false;
        state.currentlyInBattle = false;
        state.battleInProgress = false;

        append("Campaign ended. Final score: " + score.getValue());
        refreshOutput();
    }

    private void saveAndExit() {
        if (state.currentCampaign == null) {
            append("No active campaign.");
            dispose();
            return;
        }

        boolean canExit = controller.canExitCampaign(state.battleInProgress, state.currentCampaign);
        if (!canExit) {
            append("You cannot exit while a battle is in progress.");
            return;
        }

        controller.saveProgress(state.currentUser.getUserId(), state.currentCampaign);
        append("Campaign saved. Closing campaign window.");
        dispose();
    }

    private void refreshOutput() {
        StringBuilder sb = new StringBuilder();

        sb.append("=== PROFILE ===\n");
        sb.append("User: ").append(state.currentUser.getUsername()).append("\n");
        sb.append("Score: ").append(state.currentUser.getScore()).append("\n");
        sb.append("Ranking: ").append(state.currentUser.getRanking()).append("\n");
        sb.append("Saved parties: ").append(state.currentUser.getSavedParties().size()).append("\n\n");

        if (state.currentCampaign == null) {
            sb.append("No active campaign.\n");
        } else {
            sb.append("=== CAMPAIGN ===\n");
            sb.append("Current room: ").append(state.currentCampaign.getCurrentRoom()).append("\n");
            sb.append("Last room type: ").append(state.currentCampaign.getLastRoomType()).append("\n");
            sb.append("Complete: ").append(state.currentCampaign.isComplete()).append("\n");
            sb.append("Campaign score: ").append(state.currentCampaign.getScore()).append("\n\n");
        }

        if (state.currentlyInInn) {
            sb.append("Current location: INN\n\n");
        } else if (state.currentlyInBattle) {
            sb.append("Current location: BATTLE\n\n");
        }

        if (state.currentParty != null) {
            sb.append("=== PARTY ===\n");
            sb.append("Gold: ").append(state.currentParty.getGold()).append("\n");
            sb.append("Heroes:\n");
            for (Hero h : state.currentParty.getHeroes()) {
                sb.append("- ")
                        .append(h.getName()).append(" [").append(h.getType()).append("]")
                        .append(" L").append(h.getLevel())
                        .append(" HP ").append(h.getHp()).append("/").append(h.getMaxHp())
                        .append(" Mana ").append(h.getMana())
                        .append(" ATK ").append(h.getAttack())
                        .append(" DEF ").append(h.getDefense())
                        .append(h.isAlive() ? "" : " (DEAD)")
                        .append("\n");
            }
            sb.append("\n");
        }

        if (state.currentInventory != null) {
            sb.append("=== INVENTORY ===\n");
            if (state.currentInventory.getItems().isEmpty()) {
                sb.append("No items.\n");
            } else {
                for (Item item : state.currentInventory.getItems()) {
                    sb.append("- ")
                            .append(item.getName())
                            .append(" / cost ").append(item.getCost())
                            .append(" / ").append(item.getEffectType())
                            .append(" ").append(item.getEffectValue())
                            .append("\n");
                }
            }
        }

        output.setText(sb.toString());
    }

    private void append(String message) {
        output.append("\n" + message + "\n");
    }

    private int findSelectedIndex(String[] options, String selected) {
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(selected)) return i;
        }
        return -1;
    }

    public void start() {
        setVisible(true);
    }
}