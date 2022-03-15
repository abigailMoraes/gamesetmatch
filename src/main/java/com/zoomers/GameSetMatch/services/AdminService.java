package com.zoomers.GameSetMatch.services;

import com.zoomers.GameSetMatch.entity.Tournament;
import com.zoomers.GameSetMatch.repository.AdminRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
public class AdminService implements AdminRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveTournament(Tournament tournament) {
        entityManager.persist(tournament);
    }
}
