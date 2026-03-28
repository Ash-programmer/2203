package com.example.services;

import com.example.domain.ActionResult;
import com.example.domain.Hero;
import com.example.domain.Inventory;
import com.example.domain.Item;
import com.example.domain.Party;
import com.example.domain.StatusReport;

public class InnService {

    private static final int RECRUIT_COST = 50;

    private final InventoryService inventoryService;
    private final PartyService partyService;

    public InnService(InventoryService inventoryService,
                      PartyService partyService) {
        this.inventoryService = inventoryService;
        this.partyService = partyService;
    }

    public StatusReport getStatus(Party party) {
        StatusReport report = new StatusReport();

        for (Hero h : party.getHeroes()) {
            if (!h.isAlive()) {
                h.heal(h.getMaxHp());
                h.addMana(h.getMaxMana());
                report.addRevivedHero(h);
            } else {
                h.heal(h.getMaxHp());
                h.addMana(h.getMaxMana());
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

        if (!party.spendGold(RECRUIT_COST)) {
            return ActionResult.fail("Not enough gold to recruit hero");
        }

        boolean added = partyService.addHero(party, hero);

        if (!added) {
            party.addGold(RECRUIT_COST);
            return ActionResult.fail("Could not add hero");
        }

        return ActionResult.success("Hero recruited for " + RECRUIT_COST + " gold");
    }
}