package com.example.controllers;

import com.example.domain.Campaign;
import com.example.services.CampaignService;

public class ExitCampaignController {

    private CampaignService campaignService;

    public ExitCampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    public boolean exitCampaign(Campaign campaign) {

        if (campaign == null) {
            return false;
        }

        campaignService.saveProgress(campaign);

        return true;
    }
}