package TTT.RUSH.Basic.service;

import TTT.RUSH.JDBC.dao.PartyBoardPostDao;
import TTT.RUSH.JDBC.entity.PartyBoardComment;
import TTT.RUSH.JDBC.entity.PartyBoardPost;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PartyBoardService {

    private final PartyBoardPostDao postDao;

    public PartyBoardService(JdbcTemplate jdbcTemplate) {
        this.postDao = new PartyBoardPostDao(jdbcTemplate);
    }

    // 게시글 관련 메서드
    // -----------------------------

    // 특정 partyId와 페이지 번호로 게시글 가져오기
    public List<PartyBoardPost> getPostsByPartyIdAndPage(Long partyId, int page, int pageSize) {
        return postDao.getPostsByPartyIdAndPage(partyId, page, pageSize);
    }

    // 특정 partyId의 전체 게시글 수 가져오기
    public int getPostCountByPartyId(Long partyId) {
        return postDao.getPostCountByPartyId(partyId);
    }

    // 특정 게시글 상세 조회
    public PartyBoardPost getPostById(Long postId) {
        return postDao.getPostById(postId);
    }

    // 게시글 작성
    public void createPost(PartyBoardPost post) {
        postDao.createPost(post);
    }

    // 게시글 수정
    public void updatePost(PartyBoardPost post) {
        postDao.updatePost(post);
    }

    // 게시글 작성자인지 확인
    public boolean isAuthor(long postId, String username) {
        PartyBoardPost post = postDao.getPostById(postId);
        return post != null && post.getAuthor().equals(username);
    }
    
    // 게시글 삭제 (댓글 먼저 삭제 후 게시글 삭제)
    @Transactional
    public void deletePostWithComments(Long postId) {
        // 댓글 삭제
    	postDao.deleteCommentsByPostId(postId);
        
        // 게시글 삭제
    	postDao.deletePostById(postId);
    }

    // 댓글 관련 메서드
    // -----------------------------

    // 댓글 생성
    public void createComment(PartyBoardComment comment) {
        postDao.createComment(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId) {
        postDao.deleteComment(commentId);
    }

    // 특정 댓글 조회
    public PartyBoardComment getCommentById(Long commentId) {
        return postDao.getCommentById(commentId);
    }

    // 특정 게시글의 댓글 목록 조회
    public List<PartyBoardComment> getCommentsByPostId(Long postId) {
        return postDao.getCommentsByPostId(postId);
    }
}

