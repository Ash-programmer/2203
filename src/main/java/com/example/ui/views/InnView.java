package com.example.ui.views;

import com.example.controllers.InnController;
import com.example.domain.*;
import com.example.ui.UICommands;
import com.example.ui.views.AppState;

import javax.swing.*;
import java.awt.*;

public class InnView extends JFrame implements UICommands {

    private AppState state;
    private InnController controller;

    JTextArea output;

    public InnView(AppState state, InnController controller) {

        this.state = state;
        this.controller = controller;

        init();
    }

    private void init() {

        setTitle("Inn");
        setSize(500,400);

        output = new JTextArea();

        JButton rest = new JButton("Rest");
        JButton buy = new JButton("Buy Potion");
        JButton recruit = new JButton("Recruit");

        JPanel p = new JPanel();

        p.add(rest);
        p.add(buy);
        p.add(recruit);

        add(new JScrollPane(output),BorderLayout.CENTER);
        add(p,BorderLayout.SOUTH);

        rest.addActionListener(e -> rest());
        buy.addActionListener(e -> buy());
        recruit.addActionListener(e -> recruit());
    }

    private void rest() {

        StatusReport r =
                controller.getStatus(state.currentParty);

        output.append(r.getMessage()+"\n");
    }

    private void buy() {

        Item i =
                new Item("Potion",10,"heal",20);

        ActionResult r =
                controller.purchaseItem(
                        state.currentParty,
                        state.currentInventory,
                        i
                );

        output.append(r.getMessage()+"\n");
    }

    private void recruit() {

        Hero h =
                new Hero("Mage","Mage");

        ActionResult r =
                controller.recruitHero(state.currentParty,h);

        output.append(r.getMessage()+"\n");
    }

    public void start() {
        setVisible(true);
    }
}