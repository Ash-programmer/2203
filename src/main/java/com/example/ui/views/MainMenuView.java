package com.example.ui.views;

import com.example.ui.UICommands;
import com.example.controllers.*;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends JFrame implements UICommands {

    private AppState state;

    private CampaignController campaignController;
    private BattleController battleController;
    private InnController innController;
    private PvPInviteController pvpController;
    private ContinueCampaignController continueController;

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
    }

    private void init() {

        setTitle("Main Menu");
        setSize(400,300);
        setLocationRelativeTo(null);

        JButton campaign = new JButton("Campaign");
        JButton battle = new JButton("Battle");
        JButton inn = new JButton("Inn");
        JButton pvp = new JButton("PvP");
        JButton continueBtn = new JButton("Continue");

        JPanel p = new JPanel(new GridLayout(5,1));

        p.add(campaign);
        p.add(battle);
        p.add(inn);
        p.add(pvp);
        p.add(continueBtn);

        add(p);

        campaign.addActionListener(e -> {
            CampaignView v = new CampaignView(state, campaignController);
            v.start();
        });

        battle.addActionListener(e -> {
            BattleView v = new BattleView(state, battleController);
            v.start();
        });

        inn.addActionListener(e -> {
            InnView v = new InnView(state, innController);
            v.start();
        });

        pvp.addActionListener(e -> {
            PvPInviteView v = new PvPInviteView(state, pvpController);
            v.start();
        });

        continueBtn.addActionListener(e -> {
            ContinueCampaignView v = new ContinueCampaignView(state, continueController);
            v.start();
        });
    }

    public void start() {
        setVisible(true);
    }
}