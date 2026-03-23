package com.example.ui.views;

import com.example.controllers.AuthController;
import com.example.domain.*;
import com.example.ui.UICommands;
import com.example.Main;

import javax.swing.*;
import java.awt.*;

public class AuthView extends JFrame implements UICommands {

    private AuthController controller;
    private AppState state;

    JTextField userField;
    JPasswordField passField;

    public AuthView(AuthController controller) {
        this.controller = controller;
        this.state = new AppState();
        init();
    }

    private void init() {

        setTitle("Login");
        setSize(350,200);
        setLocationRelativeTo(null);

        userField = new JTextField();
        passField = new JPasswordField();

        JButton login = new JButton("Login");
        JButton register = new JButton("Register");

        JPanel p = new JPanel(new GridLayout(3,2));

        p.add(new JLabel("Username"));
        p.add(userField);

        p.add(new JLabel("Password"));
        p.add(passField);

        p.add(login);
        p.add(register);

        add(p);

        login.addActionListener(e -> login());
        register.addActionListener(e -> register());
    }

    private void login() {

        User u =
                controller.login(
                        userField.getText(),
                        new String(passField.getPassword())
                );

        if(u != null) {

            state.currentUser = u;

            state.currentParty = new Party();
            state.currentParty.addHero(new Hero("Knight","Warrior"));

            state.currentInventory = new Inventory();

            dispose();

            MainMenuView menu = new MainMenuView(
                    state,
                    Main.campaignController,
                    Main.battleController,
                    Main.innController,
                    Main.pvpController,
                    Main.continueController
            );
            menu.start();

        } else {

            JOptionPane.showMessageDialog(this,"Login failed");

        }
    }

    private void register() {

        boolean ok =
                controller.register(
                        userField.getText(),
                        new String(passField.getPassword())
                );

        JOptionPane.showMessageDialog(this,
                ok ? "Registered" : "Failed");
    }

    public void start() {
        setVisible(true);
    }
}