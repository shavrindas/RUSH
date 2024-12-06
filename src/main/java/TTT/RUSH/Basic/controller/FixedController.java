package TTT.RUSH.Basic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import TTT.RUSH.JDBC.dao.UsersDao;
import TTT.RUSH.JDBC.entity.Users;
import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;

@Controller
public class FixedController {

    @Autowired
    private UsersDao usersDao;

    // index 페이지는 네비게이션 역할
    @GetMapping("/index")
    public String index() {
        return "index"; 
    }
    
    // 유저 생성 
    @GetMapping("/userCreate")
    public String userCreate() {
        return "userCreate"; 
    }
    
    // 유저 찾기
    @GetMapping("/userFind")
    public String userFind() {
        return "userFind"; 
    }
    
    // User Details 페이지를 위한 매핑 , 이건 디버그용이라 나중에 삭제 
    @GetMapping("/userDetails")
    public String userDetails() {
        return "userDetails";
    }
    
    // 유저 생성 처리
    @PostMapping("/createUser")
    public String createUser(@RequestParam("username") String username, 
                             @RequestParam("password") String password, 
                             @RequestParam("email") String email,
                             Model model) {
        try {
            Users newUser = new Users();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setEmail(email);

            // 사용자 생성
            usersDao.createUser(newUser);
            model.addAttribute("message", "User created successfully!");
            return "index";
        } catch (SQLException e) {
            model.addAttribute("message", "Error occurred while creating user: " + e.getMessage());
            return "userCreate"; // 오류 발생 시 다시 userCreate 페이지로 리다이렉트
        }
    }

    // ID를 입력받아 사용자를 조회하고, UserDetails 페이지로 이동 , 이건 디버그용이라 나중에 삭제 
    @PostMapping("/searchUser")
    public String searchUser(@RequestParam("id") int id, Model model) {
        try {
            Users user = usersDao.getUserById(id);
            if (user != null) {
                model.addAttribute("user", user); 
            } else {
                model.addAttribute("message", "User not found.");
            }
        } catch (Exception e) {
            model.addAttribute("message", "Error occurred: " + e.getMessage());
        }
        return "userDetails"; 
    }

    // 이메일로 사용자 검색
    @PostMapping("/findUser")
    public String findUser(@RequestParam("email") String email, Model model) {
        try {
            Users user = usersDao.getUserByEmailAndActive(email);
            if (user != null) {
                model.addAttribute("user", user);
                return "userNewPass"; 
            } else {
                model.addAttribute("message", "No active user found with this email.");
                return "userFind"; 
            }
        } catch (Exception e) {
            model.addAttribute("message", "Error occurred: " + e.getMessage());
            return "userFind"; 
        }
    }

    // 비밀번호 재설정 처리
    @PostMapping("/updatePassword")
    public String updatePassword(@RequestParam("id") int id, 
                                 @RequestParam("password") String password, 
                                 Model model) {
        try {
            Users user = usersDao.getUserById(id);
            if (user != null) {
                user.setPassword(password);
                usersDao.updateUser(user); // 비밀번호 업데이트
                model.addAttribute("message", "Password updated successfully!");
                return "index"; 
            } else {
                model.addAttribute("message", "User not found.");
                return "userFind";
            }
        } catch (Exception e) {
            model.addAttribute("message", "Error occurred: " + e.getMessage());
            return "userFind"; 
        }
    }
    
    // 로그인 폼 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "userLogin";  
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, RedirectAttributes redirectAttributes) {
        Users user = usersDao.findByEmailAndPassword(email, password);  // 이메일과 비밀번호로 사용자 확인
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/userPersonalPage";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/login"; 
        }
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션 종료
        session.invalidate();
        return "redirect:/login";  // 로그아웃 후 로그인 페이지로 리디렉션
    }

}

