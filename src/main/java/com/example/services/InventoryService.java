package com.example.services;

import com.example.domain.Inventory;
import com.example.domain.Item;
import com.example.domain.Hero;
import com.example.domain.Party;
import com.example.domain.ActionResult;

public class InventoryService {

    public void addItem(Inventory inventory, Item item) {
        inventory.addItem(item);
    }

    public ActionResult buyItem(Party party, Inventory inventory, Item item) {

        if (party.getGold() < item.getCost()) {
            return ActionResult.fail("Not enough gold");
        }

        party.spendGold(item.getCost());
        inventory.addItem(item);

        return ActionResult.success("Item purchased");
    }

    public ActionResult useItem(Inventory inventory, Item item, Hero hero) {

        if (!inventory.hasItem(item)) {
            return ActionResult.fail("Item not found");
        }

        boolean used = inventory.useItem(item, hero);

        if (!used) {
            return ActionResult.fail("Could not use item");
        }

        return ActionResult.success("Item used");
    }

    public boolean hasItem(Inventory inventory, Item item) {
        return inventory.hasItem(item);
    }
}