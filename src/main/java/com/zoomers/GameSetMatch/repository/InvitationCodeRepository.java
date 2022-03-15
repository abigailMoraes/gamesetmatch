package com.zoomers.GameSetMatch.repository;

import com.zoomers.GameSetMatch.entity.InvitationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationCodeRepository extends JpaRepository<InvitationCode, String> {

}
