package TTT.RUSH.Basic.controller;

import TTT.RUSH.JDBC.entity.Party;
import TTT.RUSH.JDBC.entity.Users;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PartyTEMPController {

    @GetMapping("/partyGameMainPage/{partyId}")
    public String partyGameMainPage(@PathVariable Long partyId, HttpSession session, Model model) {
        Party party = (Party) session.getAttribute("currentParty");
        Users user = (Users) session.getAttribute("user");

        model.addAttribute("party", party);
        model.addAttribute("user", user);
        return "partyGameMainPage";
    }

    @GetMapping("/partyChatMainPage/{partyId}")
    public String partyChatMainPage(@PathVariable Long partyId, HttpSession session, Model model) {
        Party party = (Party) session.getAttribute("currentParty");
        Users user = (Users) session.getAttribute("user");

        model.addAttribute("party", party);
        model.addAttribute("user", user);
        return "partyChatMainPage";
    }

    @GetMapping("/partyFileMainPage/{partyId}")
    public String partyFileMainPage(@PathVariable Long partyId, HttpSession session, Model model) {
        Party party = (Party) session.getAttribute("currentParty");
        Users user = (Users) session.getAttribute("user");

        model.addAttribute("party", party);
        model.addAttribute("user", user);
        return "partyFileMainPage";
    }

    @GetMapping("/partySettingPage/{partyId}")
    public String partySettingPage(@PathVariable Long partyId, HttpSession session, Model model) {
        Party party = (Party) session.getAttribute("currentParty");
        Users user = (Users) session.getAttribute("user");

        model.addAttribute("party", party);
        model.addAttribute("user", user);
        return "partySettingPage";
    }
}

