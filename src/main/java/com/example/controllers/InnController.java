package com.example.controllers;

import com.example.domain.ActionResult;
import com.example.domain.Hero;
import com.example.domain.Inventory;
import com.example.domain.Item;
import com.example.domain.Party;
import com.example.domain.StatusReport;
import com.example.services.InnService;

public class InnController {

    private InnService innService;

    public InnController(InnService innService) {
        this.innService = innService;
    }

    public StatusReport getStatus(Party party) {
        return innService.getStatus(party);
    }

    public ActionResult purchaseItem(Party party,
                                     Inventory inventory,
                                     Item item) {

        return innService.purchaseItem(party, inventory, item);
    }

    public ActionResult recruitHero(Party party,
                                    Hero hero) {

        return innService.recruitHero(party, hero);
    }
}