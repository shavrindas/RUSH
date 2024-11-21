package TTT.RUSH.Basic.service;

import TTT.RUSH.JDBC.dao.PartyBoardPostDao;
import TTT.RUSH.JDBC.entity.PartyBoardComment;
import TTT.RUSH.JDBC.entity.PartyBoardImage;
import TTT.RUSH.JDBC.entity.PartyBoardPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartyBoardService {

    private final PartyBoardPostDao postDao;
    
    @Autowired
    public PartyBoardService(JdbcTemplate jdbcTemplate) {
        this.postDao = new PartyBoardPostDao(jdbcTemplate);  
    }
    
    // 게시글 리스트 조회
    public List<PartyBoardPost> getAllPosts() {
        return postDao.getAllPosts();
    }

    // partyId에 해당하는 게시글 목록 가져오기
    public List<PartyBoardPost> getPostsByPartyId(Long partyId) {
        return postDao.getPostsByPartyId(partyId);
    }
    
    // 게시글 상세 조회
	public PartyBoardPost getPostById(Long postId) {
        return postDao.getPostsById(postId);
	}

    // 게시글 작성
    public void createPost(PartyBoardPost post) {
        postDao.createPost(post);
    }
	
    public boolean isAuthor(long postId, String username) {
        PartyBoardPost post = postDao.getPostsById(postId);
        return post != null && post.getAuthor().equals(username);
    }

    // 게시글 수정
    public void updatePost(PartyBoardPost post) {
        postDao.updatePost(post);
    }
    
    
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

    // 게시글에 속한 댓글 리스트 조회
    public List<PartyBoardComment> getCommentsByPostId(Long postId) {
        return postDao.getCommentsByPostId(postId);
    }
	
}
