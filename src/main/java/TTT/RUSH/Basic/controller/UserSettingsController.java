package TTT.RUSH.Basic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import TTT.RUSH.JDBC.entity.Party;
import TTT.RUSH.JDBC.entity.Users;
import TTT.RUSH.Basic.service.PartyService;
import TTT.RUSH.JDBC.dao.UsersDao;

@Controller
public class UserSettingsController {

    @Autowired
    private UsersDao usersDao;
    @Autowired
    private PartyService partyService;
    
    
    // [Fixed] 설정 페이지
    @GetMapping("/userPersonalSettingPage")
    public String personalSettingsPage(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            return "userPersonalSettingPage"; 
        }
        return "redirect:/login"; 
    }

    // [Fixed] 개인정보 수정 페이지
    @GetMapping("/userPersonalInfoEditPage")
    public String personalInfoEditPage(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            return "userPersonalInfoEditPage"; 
        }
        return "redirect:/login"; 
    }

    // [Fixed] 개인정보 수정 처리
    @PostMapping("/updatePersonalInfo")
    public String updatePersonalInfo(@RequestParam("username") String username,
                                      @RequestParam("email") String email,
                                      @RequestParam(value = "password", required = false) String password,
                                      HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            user.setUsername(username);
            user.setEmail(email);
            if (password != null && !password.isEmpty()) {
                user.setPassword(password); 
            }
            usersDao.updateUser(user); 
            session.setAttribute("user", user); 
            return "redirect:/userPersonalSettingPage";
        }
        return "redirect:/login"; 
    }



    // 사용자 개인 페이지
    @GetMapping("/userPersonalPage")
    public String userPersonalPage(Model model, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("user");
        int userId = (user != null) ? user.getId() : -1; // 로그인된 경우 userId 설정

        // 사용자 정보가 있을 경우
        if (user != null) {
            model.addAttribute("user", user); 
        } else {
            return "redirect:/login";
        }
        // 사용자 파티 정보 가져오기
        if (userId != -1) {
            List<Party> parties = partyService.getUserParties(userId);
            model.addAttribute("parties", parties);  
        }

        return "userPersonalPage";  
    }

    // [Fixed] 파티 생성 및 참가 페이지
    @GetMapping("/userPersonalPartyCratePage")
    public String userPersonalPartyCratePage() {
        return "userPersonalPartyCratePage";
    }

    // [Fixed] 새로운 파티 생성
    @PostMapping("/createParty")
    public String createParty(HttpSession session, Model model, @RequestParam String partyName) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        int userId = user.getId(); 

        Party newParty = new Party();
        newParty.setPartyName(partyName); 
        partyService.createParty(partyName, userId);
        return "redirect:/userPersonalPage"; 
    }

    // [Fixed] 새로운 파티에 참가하는 메서드
    @PostMapping("/joinParty")
    public String joinParty(HttpSession session, @RequestParam("party_code") String partyCode, Model model) {
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        boolean isJoined = partyService.joinParty(user.getId(), partyCode);

        if (!isJoined) {
            model.addAttribute("error", "파티 코드가 잘못되었거나 이미 참가한 파티입니다.");
            return "redirect:/userPersonalPartyCratePage"; 
        }

        return "redirect:/userPersonalPage"; 
    }


    
    
}
