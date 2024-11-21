package TTT.RUSH.JDBC.dao;

import TTT.RUSH.JDBC.entity.PartyBoardComment;
import TTT.RUSH.JDBC.entity.PartyBoardImage;
import TTT.RUSH.JDBC.entity.PartyBoardPost;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PartyBoardPostDao {

    private final JdbcTemplate jdbcTemplate;

    // JdbcTemplate을 주입받아서 사용
    public PartyBoardPostDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 모든 게시글 가져오기
    public List<PartyBoardPost> getAllPosts() {
        String sql = "SELECT * FROM party_board_post";
        return jdbcTemplate.query(sql, new RowMapper<PartyBoardPost>() {
            @Override
            public PartyBoardPost mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                PartyBoardPost post = new PartyBoardPost();
                post.setPostId(rs.getInt("post_id"));
                post.setPartyId(rs.getInt("party_id"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                post.setAuthor(rs.getString("author"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                post.setUpdatedAt(rs.getTimestamp("updated_at"));
                return post;
            }
        });
    }

    // partyId에 해당하는 게시글만 가져오기
    public List<PartyBoardPost> getPostsByPartyId(Long partyId) {
        String sql = "SELECT * FROM party_board_post WHERE party_id = ?";
        return jdbcTemplate.query(sql, new Object[]{partyId}, new RowMapper<PartyBoardPost>() {
            @Override
            public PartyBoardPost mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                PartyBoardPost post = new PartyBoardPost();
                post.setPostId(rs.getInt("post_id"));
                post.setPartyId(rs.getInt("party_id"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                post.setAuthor(rs.getString("author"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                post.setUpdatedAt(rs.getTimestamp("updated_at"));
                return post;
            }
        });
    }
    
    public PartyBoardPost getPostsById(Long postId) {
        String sql = "SELECT * FROM party_board_post WHERE post_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{postId}, new RowMapper<PartyBoardPost>() {
            @Override
            public PartyBoardPost mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                PartyBoardPost post = new PartyBoardPost();
                post.setPostId(rs.getInt("post_id"));
                post.setPartyId(rs.getInt("party_id"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                post.setAuthor(rs.getString("author"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                post.setUpdatedAt(rs.getTimestamp("updated_at"));
                return post;
            }
        });
    }

	
	
    // 게시글 생성
    public void createPost(PartyBoardPost post) {
        String sql = "INSERT INTO party_board_post (party_id, title, content, author) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, post.getPartyId(), post.getTitle(), post.getContent(), post.getAuthor());
    }

    // 게시글 수정
    public void updatePost(PartyBoardPost post) {
        String sql = "UPDATE party_board_post SET title = ?, content = ?, updated_at = ? WHERE post_id = ?";
        jdbcTemplate.update(sql, post.getTitle(), post.getContent(), post.getUpdatedAt(), post.getPostId());
    }
    
    
    // 댓글 생성
    public void createComment(PartyBoardComment comment) {
        String sql = "INSERT INTO party_board_comment (post_id, author, content) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, comment.getPostId(), comment.getAuthor(), comment.getContent());
    }

    // 댓글 삭제
    public void deleteComment(Long commentId) {
        String sql = "DELETE FROM party_board_comment WHERE comment_id = ?";
        jdbcTemplate.update(sql, commentId);
    }

    // 특정 댓글 조회
    public PartyBoardComment getCommentById(Long commentId) {
        String sql = "SELECT * FROM party_board_comment WHERE comment_id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(PartyBoardComment.class), commentId);
    }

    // 특정 게시글에 속한 댓글 리스트 조회
    public List<PartyBoardComment> getCommentsByPostId(Long postId) {
        String sql = "SELECT * FROM party_board_comment WHERE post_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PartyBoardComment.class), postId);
    }
    
	
	
}



