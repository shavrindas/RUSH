package TTT.RUSH.JDBC.entity;

import java.sql.Timestamp;

public class Party {
    private int partyId;
    private String partyName;
    private String partyCode;
    private String partyInvite;
    private Timestamp partyCreatedAt;
    private boolean partyActive;

    public int getPartyId() {
        return partyId;
    }

    public void setPartyId(int partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public String getPartyInvite() {
        return partyInvite;
    }

    public void setPartyInvite(String partyInvite) {
        this.partyInvite = partyInvite;
    }

    public Timestamp getPartyCreatedAt() {
        return partyCreatedAt;
    }

    public void setPartyCreatedAt(Timestamp partyCreatedAt) {
        this.partyCreatedAt = partyCreatedAt;
    }

    public boolean isPartyActive() {
        return partyActive;
    }

    public void setPartyActive(boolean partyActive) {
        this.partyActive = partyActive;
    }
}
