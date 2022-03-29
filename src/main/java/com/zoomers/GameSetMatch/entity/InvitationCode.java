package com.zoomers.GameSetMatch.entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@ToString

@Entity
@Table(name = "Invitation_Code")
public class InvitationCode {
    @Column(name = "invitation_code")
    private @Id String invitationCode;
    @Column(name = "is_valid")
    private boolean isValid;
    @Column(name = "created_on")
    private String createdOn;
    private static final int expireDurationInDays = 7;

    public InvitationCode() {
        this.invitationCode = randomString();
        this.isValid = true;
        this.createdOn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    // using code from https://stackoverflow.com/a/157202
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final int lengthOfCode = 10;
    static SecureRandom rnd = new SecureRandom();

    String randomString(){
        StringBuilder sb = new StringBuilder(lengthOfCode);
        for(int i = 0; i < lengthOfCode; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
