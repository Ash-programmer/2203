package com.example.services;

import com.example.domain.User;
import com.example.persistence.repositories.UserRepo;

public class InvitationService {

    private UserRepo userRepo;

    public InvitationService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean sendInvite(User from, String toUsername) {

        if (from == null) {
            return false;
        }

        User to = userRepo.findByUsername(toUsername);

        if (to == null) {
            return false;
        }

        if (!from.hasSavedParty()) {
            return false;
        }

        if (!to.hasSavedParty()) {
            return false;
        }

        return true;
    }

    public boolean respondInvite(int inviteId, boolean accept) {

        if (!accept) {
            return false;
        }

        return true;
    }
}