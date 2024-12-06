package TTT.RUSH.Basic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TTT.RUSH.JDBC.dao.PartyDao;
import TTT.RUSH.JDBC.entity.Party;

@Service
public class PartyService {

    @Autowired
    private PartyDao partyDao;

    public List<Party> getUserParties(int userId) {
        return partyDao.getPartiesByUserId(userId);
    }
    
    // [Fixed] 파티 생성 처리 메서드
    public void createParty(String partyName, int userId) {
        int partyId = partyDao.createParty(partyName);
        partyDao.createDefaultRoles(partyId);
        partyDao.addUserToParty(userId, partyId, "admin");
    }

    // [Fixed] 파티 참가 처리 메서드
    public boolean joinParty(int userId, String partyCode) {
        Party party = partyDao.getPartyByCode(partyCode);
        if (party == null) {
            return false;
        }
        // 사용자 정보를 추가, 기본 역할은 editor
        partyDao.addUserToParty(userId, party.getPartyId(), "editor");

        return true;
    }
    
    public Party getPartyById(Long partyId) {
        return partyDao.findPartyById(partyId);
    }
    
    public void deleteParty(int userId, int partyId) {
        partyDao.removeUserFromParty(partyId, userId);

    }
    
}
