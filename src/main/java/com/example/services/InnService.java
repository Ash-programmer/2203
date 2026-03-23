package com.example.services;

import com.example.domain.ActionResult;
import com.example.domain.Hero;
import com.example.domain.Inventory;
import com.example.domain.Item;
import com.example.domain.Party;
import com.example.domain.StatusReport;

public class InnService {

    private InventoryService inventoryService;
    private PartyService partyService;

    public InnService(InventoryService inventoryService,
                      PartyService partyService) {

        this.inventoryService = inventoryService;
        this.partyService = partyService;
    }

    public StatusReport getStatus(Party party) {

        StatusReport report = new StatusReport();

        for (Hero h : party.getHeroes()) {

            if (!h.isAlive()) {
                h.heal(50);
                report.addRevivedHero(h);
            } else {
                h.heal(20);
                report.addHealedHero(h);
            }
        }

        report.setMessage("Party rested at inn");

        return report;
    }

    public ActionResult purchaseItem(Party party,
                                     Inventory inventory,
                                     Item item) {

        return inventoryService.buyItem(party, inventory, item);
    }

    public ActionResult recruitHero(Party party,
                                    Hero hero) {

        if (party.isFull()) {
            return ActionResult.fail("Party is full");
        }

        boolean added = partyService.addHero(party, hero);

        if (!added) {
            return ActionResult.fail("Could not add hero");
        }

        return ActionResult.success("Hero recruited");
    }
}