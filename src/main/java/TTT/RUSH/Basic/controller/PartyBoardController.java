package TTT.RUSH.Basic.controller;

import TTT.RUSH.Basic.service.PartyBoardService;
import TTT.RUSH.Basic.service.PartyService;
import TTT.RUSH.JDBC.entity.Party;
import TTT.RUSH.JDBC.entity.PartyBoardComment;
import TTT.RUSH.JDBC.entity.PartyBoardPost;
import TTT.RUSH.JDBC.entity.Users;
import jakarta.servlet.http.HttpSession;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PartyBoardController {
    private final PartyBoardService boardService;
    private final PartyService partyService;

    public PartyBoardController(PartyBoardService boardService, PartyService partyService) {
        this.boardService = boardService;
        this.partyService = partyService;
    }

    // 파티 게시판 페이지 조회
    @GetMapping("/partyBoardMainPage/{partyId}")
    public String viewPartyBoard(
            @PathVariable("partyId") Long partyId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            HttpSession session, Model model) {

        int pageSize = 30;
        int totalPosts = boardService.getPostCountByPartyId(partyId);
        int totalPages = (int) Math.ceil((double) totalPosts / pageSize);

        if (page < 1 || page > totalPages) {
            return "redirect:/partyBoardMainPage/" + partyId + "?page=1";
        }

        List<PartyBoardPost> posts = boardService.getPostsByPartyIdAndPage(partyId, page, pageSize);

        Party currentParty = (Party) session.getAttribute("currentParty");
        if (currentParty == null || currentParty.getPartyId() != partyId) {
            currentParty = partyService.getPartyById(partyId);
            if (currentParty == null) {
                return "redirect:/userPersonalPage";
            }
            session.setAttribute("currentParty", currentParty);
        }

        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/userPersonalPage";
        }

        model.addAttribute("party", currentParty);
        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "partyBoardMainPage";
    }

    // 게시글 상세 조회 페이지
    @GetMapping("/partyBoardViewPage/{postId}")
    public String viewPostDetail(@PathVariable("postId") Long postId, 
                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                 HttpSession session, Model model) {

        PartyBoardPost post = boardService.getPostById(postId);
        Party party = (Party) session.getAttribute("currentParty");
        Users user = (Users) session.getAttribute("user");

        if (post == null || party == null || user == null) {
            return "redirect:/partyBoardMainPage";
        }

        int postsPerPage = 30;
        int totalPosts = boardService.getPostCountByPartyId((long)party.getPartyId());
        int totalPages = (int) Math.ceil((double) totalPosts / postsPerPage);
        List<PartyBoardPost> posts = boardService.getPostsByPartyIdAndPage((long)party.getPartyId(), page, postsPerPage);
        List<PartyBoardComment> comments = boardService.getCommentsByPostId(postId);

        model.addAttribute("post", post);
        model.addAttribute("party", party);
        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        model.addAttribute("comments", comments);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "partyBoardViewPage";
    }

    // 게시글 삭제 요청 처리
    @PostMapping("/deletePost")
    public String deletePost(@RequestParam("postId") Long postId, 
                             @RequestParam("partyId") int partyId,
                             RedirectAttributes redirectAttributes) {
        try {
            boardService.deletePostWithComments(postId);
            redirectAttributes.addFlashAttribute("message", "Post deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete post.");
        }
        return "redirect:/partyBoardMainPage/" + partyId;
    }
    
    // 게시글 작성 페이지
    @GetMapping("/partyBoardCreatePage")
    public String createPostPage(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        Party party = (Party) session.getAttribute("currentParty");

        if (party == null || user == null) {
            return "redirect:/userPersonalPage";
        }

        model.addAttribute("party", party);
        model.addAttribute("user", user);
        return "partyBoardCreatePage";
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

/*package TTT.RUSH.Basic.controller;

import TTT.RUSH.Basic.service.PartyBoardService;
import TTT.RUSH.Basic.service.PartyService;
import TTT.RUSH.JDBC.entity.Party;
import TTT.RUSH.JDBC.entity.PartyBoardComment;
import TTT.RUSH.JDBC.entity.PartyBoardPost;
import TTT.RUSH.JDBC.entity.Users;
import jakarta.servlet.http.HttpSession;

import java.sql.Timestamp;
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
    public String viewPartyBoard(
            @PathVariable("partyId") Long partyId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            HttpSession session, Model model) {

        // 페이지네이션 설정
        int pageSize = 30; 
        int totalPosts = boardService.getPostCountByPartyId(partyId); 
        int totalPages = (int) Math.ceil((double) totalPosts / pageSize); 

        if (page < 1 || page > totalPages) {
            return "redirect:/partyBoardMainPage/" + partyId + "?page=1";
        }

        // 게시글 목록 가져오기
        List<PartyBoardPost> posts = boardService.getPostsByPartyIdAndPage(partyId, page, pageSize);

        // 세션에서 현재 파티 정보 가져오기
        Party currentParty = (Party) session.getAttribute("currentParty");
        if (currentParty == null || currentParty.getPartyId() != partyId) {
            currentParty = partyService.getPartyById(partyId);
            if (currentParty == null) {
                return "redirect:/userPersonalPage"; 
            }
            session.setAttribute("currentParty", currentParty); 
        }
        model.addAttribute("party", currentParty);

        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/userPersonalPage";
        }
        model.addAttribute("user", user);

        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "partyBoardMainPage";
    }


    // 게시글 상세 조회 페이지
    @GetMapping("/partyBoardViewPage/{postId}")
    public String viewPostDetail(@PathVariable("postId") Long postId, 
                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                 HttpSession session, 
                                 Model model) {
        PartyBoardPost post = boardService.getPostById(postId);
        Party party = (Party) session.getAttribute("currentParty");
        Users user = (Users) session.getAttribute("user");
        
        // 게시글 목록 페이징 처리
        int totalPosts = boardService.getPostCountByPartyId((long) party.getPartyId());
        int postsPerPage = 30;  // 한 페이지에 보여줄 게시글 수
        int totalPages = (int) Math.ceil((double) totalPosts / postsPerPage);

        List<PartyBoardPost> posts = boardService.getPostsByPartyIdAndPage((long)party.getPartyId(), page, postsPerPage);
        List<PartyBoardComment> comments = boardService.getCommentsByPostId(postId);

        if (post != null) {
            model.addAttribute("post", post);
            model.addAttribute("party", party);
            model.addAttribute("user", user);
            model.addAttribute("posts", posts);
            model.addAttribute("comments", comments);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
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
*/
