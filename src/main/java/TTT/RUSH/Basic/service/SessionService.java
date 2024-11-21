package TTT.RUSH.Basic.service;

import TTT.RUSH.JDBC.entity.Party;
import TTT.RUSH.JDBC.entity.Users;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    public Party getPartyFromSession(HttpSession session) {
        return (Party) session.getAttribute("currentParty");
    }

    public Users getUserFromSession(HttpSession session) {
        return (Users) session.getAttribute("user");
    }
}
