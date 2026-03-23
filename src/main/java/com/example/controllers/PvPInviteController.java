package com.example.controllers;

import com.example.domain.User;
import com.example.services.InvitationService;

public class PvPInviteController {

    private InvitationService invitationService;

    public PvPInviteController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    public boolean sendInvite(User from, String toUsername) {

        return invitationService.sendInvite(from, toUsername);
    }

    public boolean respondInvite(int inviteId, boolean accept) {

        return invitationService.respondInvite(inviteId, accept);
    }
}