package TTT.RUSH.Basic.controller;

import TTT.RUSH.Basic.service.PartyBoardService;
import TTT.RUSH.Basic.service.PartyService;
import TTT.RUSH.JDBC.entity.Party;
import TTT.RUSH.JDBC.entity.PartyBoardComment;
import TTT.RUSH.JDBC.entity.PartyBoardPost;
import TTT.RUSH.JDBC.entity.Users;
import jakarta.servlet.http.HttpSession;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PartyBoardController {
    private PartyBoardService boardService;
    private PartyService partyService;

    public PartyBoardController(PartyBoardService boardService, PartyService partyService) {
        this.boardService = boardService;
        this.partyService = partyService;
    }

    // 파티 게시판 페이지를 조회
    @GetMapping("/partyBoardMainPage/{partyId}")
    public String viewPartyBoard(@PathVariable("partyId") Long partyId, HttpSession session, Model model) {
        Party currentParty = (Party) session.getAttribute("currentParty");
        // 세션에서 currentParty가 존재하는지 확인
        if (currentParty != null && (long) currentParty.getPartyId() == (partyId)) {
            model.addAttribute("party", currentParty); 
        } else {
            Party party = partyService.getPartyById(partyId);
            if (party != null) {
                session.setAttribute("currentParty", party); 
                model.addAttribute("party", party); 
            } else {
                return "redirect:/userPersonalPage"; 
            }
        }
        // 사용자 정보 및 게시글 목록 처리
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);  
            List<PartyBoardPost> posts = boardService.getPostsByPartyId(partyId);
            model.addAttribute("posts", posts != null && !posts.isEmpty() ? posts : new ArrayList<>());
            return "partyBoardMainPage";
        }
        return "redirect:/userPersonalPage";
    }

    // 게시글 상세 조회 페이지
    @GetMapping("/partyBoardViewPage/{postId}")
    public String viewPostDetail(@PathVariable("postId") Long postId, HttpSession session, Model model) {
        PartyBoardPost post = boardService.getPostById(postId);
        Party party = (Party) session.getAttribute("currentParty");
        Users user = (Users) session.getAttribute("user");
        List<PartyBoardComment> comments = boardService.getCommentsByPostId(postId);

        if (post != null) {
            model.addAttribute("post", post);
            model.addAttribute("party", party);
            model.addAttribute("user", user);
            model.addAttribute("posts", boardService.getPostsByPartyId((long)party.getPartyId()));
            model.addAttribute("comments", comments);
            return "partyBoardViewPage"; 
        }
        return "redirect:/partyBoardMainPage";
    }

    // 게시글 작성 페이지
    @GetMapping("/partyBoardCreatePage")
    public String createPostPage(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        Party party = (Party) session.getAttribute("currentParty");
        if (party != null) {
            model.addAttribute("party", party);
            model.addAttribute("user", user);
            return "partyBoardCreatePage";
        }
        return "redirect:/userPersonalPage";
    }

    @PostMapping("/partyBoardCreate")
    public String createPost(PartyBoardPost post, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        Party party = (Party) session.getAttribute("currentParty");

        if (party != null && user != null) {
            post.setPartyId(party.getPartyId());
            post.setAuthor(user.getUsername());
            boardService.createPost(post);
            return "redirect:/partyBoardMainPage/" + party.getPartyId();
        }
        return "redirect:/userPersonalPage";
    }

    // 게시글 수정 페이지
    @GetMapping("/partyBoardEditPage/{postId}")
    public String editPostPage(@PathVariable("postId") Long postId, HttpSession session, Model model) {
        PartyBoardPost post = boardService.getPostById(postId);
        Users user = (Users) session.getAttribute("user");
        Party party = (Party) session.getAttribute("currentParty");

        if (post != null && user != null && post.getAuthor().equals(user.getUsername())) {
            model.addAttribute("post", post);
            model.addAttribute("party", party);
            model.addAttribute("user", user);
            return "partyBoardEditPage";
        }
        return "redirect:/partyBoardViewPage/" + postId;
    }

    // 게시글 수정 요청 처리
    @PostMapping("/partyBoardEdit")
    public String editPost(PartyBoardPost post, HttpSession session) {
        Users user = (Users) session.getAttribute("user");

        if (user != null && boardService.isAuthor(post.getPostId(), user.getUsername())) {
            post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            boardService.updatePost(post);
            return "redirect:/partyBoardViewPage/" + post.getPostId();
        }
        return "redirect:/partyBoardMainPage";
    }

    // 댓글 작성
    @PostMapping("/partyBoardComment")
    public String createComment(@RequestParam("postId") Long postId, @RequestParam("content") String content, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            PartyBoardComment comment = new PartyBoardComment();
            comment.setPostId(postId);
            comment.setAuthor(user.getUsername());
            comment.setContent(content);
            boardService.createComment(comment);
        }
        return "redirect:/partyBoardViewPage/" + postId;
    }

    // 댓글 삭제
    @PostMapping("/partyBoardCommentDelete")
    public String deleteComment(@RequestParam("commentId") Long commentId, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        PartyBoardComment comment = boardService.getCommentById(commentId);

        if (comment != null && user != null && user.getUsername().equals(comment.getAuthor())) {
            boardService.deleteComment(commentId);
        }
        return "redirect:/partyBoardViewPage/" + comment.getPostId();
    }
}

