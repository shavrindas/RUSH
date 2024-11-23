package TTT.RUSH.Basic.controller;
/*
import TTT.RUSH.JDBC.entity.Party;
import TTT.RUSH.JDBC.entity.Users;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
@Controller
*/
public class PartyChatController {
/*
    // Party Chat 페이지로 이동
    @GetMapping("/partyChatMainPage")
    public String partyChatMainPage(HttpSession session, Model model) {
        // 세션에서 현재 사용자와 파티 정보를 가져옴
        Party currentParty = (Party) session.getAttribute("currentParty");
        Users currentUser = (Users) session.getAttribute("user");

        // 현재 사용자나 파티 정보가 없는 경우 리다이렉트
        if (currentParty == null || currentUser == null) {
            return "redirect:/userPersonalPage";
        }

        // 모델에 사용자 이름과 현재 파티 정보를 추가
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("party", currentParty);

        return "partyChatMainPage"; // HTML 파일 이름 변경
    }
*/
}
