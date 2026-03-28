package com.example.ui.views;

import com.example.controllers.BattleController;
import com.example.controllers.CampaignController;
import com.example.controllers.ContinueCampaignController;
import com.example.controllers.InnController;
import com.example.controllers.PvPInviteController;
import com.example.domain.Party;
import com.example.ui.UICommands;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends JFrame implements UICommands {

    private final AppState state;
    private final CampaignController campaignController;
    private final BattleController battleController;
    private final InnController innController;
    private final PvPInviteController pvpController;
    private final ContinueCampaignController continueController;

    private JTextArea profileArea;

    public MainMenuView(
            AppState state,
            CampaignController campaignController,
            BattleController battleController,
            InnController innController,
            PvPInviteController pvpController,
            ContinueCampaignController continueController
    ) {
        this.state = state;
        this.campaignController = campaignController;
        this.battleController = battleController;
        this.innController = innController;
        this.pvpController = pvpController;
        this.continueController = continueController;

        init();
        refreshProfile();
    }

    private void init() {
        setTitle("Main Menu");
        setSize(560, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        profileArea = new JTextArea();
        profileArea.setEditable(false);

        JButton campaign = new JButton("Start / View Campaign");
        JButton continueBtn = new JButton("Continue Campaign");
        JButton pvp = new JButton("PvP Invite");
        JButton refresh = new JButton("Refresh Profile");

        JPanel buttons = new JPanel(new GridLayout(4, 1, 5, 5));
        buttons.add(campaign);
        buttons.add(continueBtn);
        buttons.add(pvp);
        buttons.add(refresh);

        add(new JScrollPane(profileArea), BorderLayout.CENTER);
        add(buttons, BorderLayout.EAST);

        campaign.addActionListener(e -> new CampaignView(state, campaignController).start());
        continueBtn.addActionListener(e -> new ContinueCampaignView(state, continueController).start());
        pvp.addActionListener(e -> new PvPInviteView(state, pvpController).start());
        refresh.addActionListener(e -> refreshProfile());
    }

    private void refreshProfile() {
        StringBuilder sb = new StringBuilder();

        sb.append("=== PROFILE ===\n");
        sb.append("Username: ").append(state.currentUser.getUsername()).append("\n");
        sb.append("Score: ").append(state.currentUser.getScore()).append("\n");
        sb.append("Ranking: ").append(state.currentUser.getRanking()).append("\n");
        sb.append("Active Campaign: ").append(state.currentUser.hasCampaign() ? "yes" : "no").append("\n");
        sb.append("Saved Parties: ").append(state.currentUser.getSavedParties().size()).append("\n\n");

        if (!state.currentUser.getSavedParties().isEmpty()) {
            sb.append("=== SAVED PARTIES ===\n");
            for (Party p : state.currentUser.getSavedParties()) {
                String name = p.getName() == null ? "Unnamed Party" : p.getName();
                sb.append("- ")
                        .append("ID ").append(p.getId())
                        .append(" | ").append(name)
                        .append(" | Heroes: ").append(p.getSize())
                        .append(" | Gold: ").append(p.getGold())
                        .append("\n");
            }
        }

        profileArea.setText(sb.toString());
    }

    public void start() {
        setVisible(true);
    }
}