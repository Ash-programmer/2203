package com.example.controllers;

import com.example.domain.Campaign;
import com.example.services.CampaignService;

public class ContinueCampaignController {

    private CampaignService campaignService;

    public ContinueCampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    public Campaign loadCampaign(int userId) {
        return campaignService.loadProgress(userId);
    }
}