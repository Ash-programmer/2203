package com.example.ui.views;

import com.example.controllers.PvPInviteController;
import com.example.ui.UICommands;

import javax.swing.*;
import java.awt.*;

public class PvPInviteView extends JFrame implements UICommands {

    private PvPInviteController controller;
    private AppState state;

    JTextField opponent;

    public PvPInviteView(AppState state, PvPInviteController controller) {

        this.state = state;
        this.controller = controller;

        init();
    }

    private void init() {

        setTitle("PvP");
        setSize(400,200);

        opponent = new JTextField();

        JButton send = new JButton("Send Invite");

        add(opponent,BorderLayout.CENTER);
        add(send,BorderLayout.SOUTH);

        send.addActionListener(e -> send());
    }

    private void send() {

        boolean ok =
                controller.sendInvite(
                        state.currentUser,
                        opponent.getText()
                );

        JOptionPane.showMessageDialog(this,
                ok ? "Invite Sent" : "Invite Failed");
    }

    public void start() {
        setVisible(true);
    }
}