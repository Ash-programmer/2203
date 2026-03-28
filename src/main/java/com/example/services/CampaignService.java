package com.example.services;

import com.example.domain.Campaign;
import com.example.domain.Party;
import com.example.domain.RoomType;
import com.example.domain.Score;
import com.example.domain.User;
import com.example.persistence.repositories.CampaignRepo;
import com.example.persistence.repositories.PartyRepo;
import com.example.persistence.repositories.UserRepo;

import java.util.List;

public class CampaignService {

    private final CampaignRepo campaignRepo;
    private final PartyRepo partyRepo;
    private final UserRepo userRepo;

    public CampaignService(CampaignRepo campaignRepo, PartyRepo partyRepo, UserRepo userRepo) {
        this.campaignRepo = campaignRepo;
        this.partyRepo = partyRepo;
        this.userRepo = userRepo;
    }

    public Campaign startCampaign(User user, Party party) {
        Campaign campaign = new Campaign(party);
        campaign.setCurrentRoom(1);
        campaign.setLastRoomType(RoomType.INN);

        user.getCampaigns().clear();
        user.addCampaign(campaign);
        campaignRepo.save(user.getUserId(), campaign);
        return campaign;
    }

    public void moveToInn(Campaign campaign) {
        campaign.setLastRoomType(RoomType.INN);
    }

    public void moveToBattle(Campaign campaign) {
        campaign.setLastRoomType(RoomType.BATTLE);
    }

    public void completeBattleAndAdvance(Campaign campaign) {
        campaign.advanceRoom();
        campaign.setLastRoomType(RoomType.BATTLE);
    }

    public void saveProgress(int userId, Campaign campaign) {
        campaignRepo.save(userId, campaign);
    }

    public Campaign loadProgress(int userId) {
        return campaignRepo.loadByUserId(userId);
    }

    public boolean canExitCampaign(boolean battleInProgress, Campaign campaign) {
        if (campaign == null || battleInProgress) {
            return false;
        }

        RoomType type = campaign.getLastRoomType();
        return type == RoomType.INN || type == RoomType.BATTLE;
    }

    public boolean isCampaignComplete(Campaign campaign) {
        return campaign != null && campaign.getCurrentRoom() > 30 || (campaign != null && campaign.isComplete());
    }

    public Score endCampaign(User user, Campaign campaign, boolean keepParty, Integer replacePartyId) {
        campaign.calculateFinalScore();
        Score score = Score.calculate(user, campaign);

        user.setScore(score.getValue());
        user.setRanking(score.getValue());
        userRepo.save(user);

        if (keepParty && campaign.getParty() != null) {
            List<Party> savedParties = partyRepo.loadForUser(user.getUserId());

            if (savedParties.size() < 5) {
                partyRepo.save(user.getUserId(), campaign.getParty());
            } else if (replacePartyId != null) {
                partyRepo.delete(replacePartyId);
                partyRepo.save(user.getUserId(), campaign.getParty());
            }
        }

        campaign.setComplete(true);
        campaignRepo.deleteByUserId(user.getUserId());
        return score;
    }
}