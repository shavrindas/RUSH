package TTT.RUSH.Basic.controller;

import TTT.RUSH.Basic.service.PartyFileService;
import TTT.RUSH.JDBC.entity.Party;
import TTT.RUSH.JDBC.entity.Party_file;
import TTT.RUSH.JDBC.entity.Users;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PartyTEMPController {
	 private final PartyFileService partyFileService;
	    public PartyTEMPController(PartyFileService partyFileService) {
	        this.partyFileService = partyFileService;
	    }
	    

    @GetMapping("/partyGameMainPage/{partyId}")
    public String partyGameMainPage(@PathVariable Long partyId, HttpSession session, Model model) {
        Party party = (Party) session.getAttribute("currentParty");
        Users user = (Users) session.getAttribute("user");

        model.addAttribute("party", party);
        model.addAttribute("user", user);
        return "partyGameMainPage";
    }

    @GetMapping("/partyGameSnake/{partyId}")
    public String partyGameSnake(@PathVariable Long partyId, HttpSession session, Model model) {
        Party party = (Party) session.getAttribute("currentParty");
        Users user = (Users) session.getAttribute("user");

        model.addAttribute("party", party);
        model.addAttribute("user", user);
        return "partyGameSnake"; // Snake 게임 페이지 뷰 이름
    }

    @GetMapping("/partyGameMissile/{partyId}")
    public String partyGameMissile(@PathVariable Long partyId, HttpSession session, Model model) {
        Party party = (Party) session.getAttribute("currentParty");
        Users user = (Users) session.getAttribute("user");

        model.addAttribute("party", party);
        model.addAttribute("user", user);
        return "partyGameMissile"; // Missile 게임 페이지 뷰 이름
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

        // 현재 파티의 파일 목록 가져오기
        List<Party_file> fileList = partyFileService.getFilesByPartyId(partyId);

        model.addAttribute("party", party);
        model.addAttribute("user", user);
        model.addAttribute("fileList", fileList);
        return "partyFileMainPage";
    }

    

    @PostMapping("/party/file/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, 
                             @RequestParam("partyId") Long partyId,
                             HttpSession session, Model model) {
        try {
            Party_file partyFile = new Party_file();
            partyFile.setFileName(file.getOriginalFilename());
            partyFile.setFileContent(file.getBytes());
            partyFile.setPartyId(partyId);

            boolean isUploaded = partyFileService.uploadFile(partyFile);
            if (!isUploaded) {
                model.addAttribute("errorMessage", "파일 업로드 실패");
            }

            // 업로드 후 해당 파티의 파일 페이지로 리다이렉트
            return "redirect:/partyFileMainPage/" + partyId;

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "에러 발생: " + e.getMessage());
            return "partyFileMainPage"; // 업로드 실패 시 현재 페이지로 유지
        }
    }
    
    @GetMapping("/party/file/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) {
        // 파일 정보 가져오기
        Party_file file = partyFileService.getFileById(fileId);

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        // HTTP 응답 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(file.getFileName())
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(file.getFileContent());
    }

    
    @DeleteMapping("/party/file/delete/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long fileId) {
        try {
            partyFileService.deleteFile(fileId);
            return ResponseEntity.ok("File deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to delete the file: " + e.getMessage());
        }
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

