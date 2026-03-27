package com.example.controllers;

import com.example.domain.Campaign;
import com.example.domain.Party;
import com.example.domain.Score;
import com.example.domain.User;
import com.example.services.CampaignService;

public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    public Campaign startCampaign(User user, Party party) {
        return campaignService.startCampaign(user, party);
    }

    public void moveToInn(Campaign campaign) {
        campaignService.moveToInn(campaign);
    }

    public void moveToBattle(Campaign campaign) {
        campaignService.moveToBattle(campaign);
    }

    public void completeBattleAndAdvance(Campaign campaign) {
        campaignService.completeBattleAndAdvance(campaign);
    }

    public void saveProgress(int userId, Campaign campaign) {
        campaignService.saveProgress(userId, campaign);
    }

    public Campaign loadCampaign(int userId) {
        return campaignService.loadProgress(userId);
    }

    public boolean canExitCampaign(boolean battleInProgress, Campaign campaign) {
        return campaignService.canExitCampaign(battleInProgress, campaign);
    }

    public Score endCampaign(User user, Campaign campaign, boolean keepParty, Integer replacePartyId) {
        return campaignService.endCampaign(user, campaign, keepParty, replacePartyId);
    }
}