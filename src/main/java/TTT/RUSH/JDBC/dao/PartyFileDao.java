package TTT.RUSH.JDBC.dao;

import TTT.RUSH.JDBC.entity.Party_file;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PartyFileDao {
    private final JdbcTemplate jdbcTemplate;

    public PartyFileDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 파일 저장
    public int saveFile(Party_file file) {
        String sql = "INSERT INTO party_file (file_name, file_content, party_id) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, file.getFileName(), file.getFileContent(), file.getPartyId());
    }

    // 파일 조회 (예제: ID로 조회)
    public Party_file getFileById(long fileId) {
        String sql = "SELECT * FROM party_file WHERE file_id = ?";
        RowMapper<Party_file> rowMapper = (rs, rowNum) -> {
            Party_file partyFile = new Party_file();
            partyFile.setFileId(rs.getLong("file_id"));
            partyFile.setFileName(rs.getString("file_name"));
            partyFile.setFileContent(rs.getBytes("file_content"));
            partyFile.setPartyId(rs.getLong("party_id"));
            partyFile.setFileCreatedAt(rs.getTimestamp("file_created_at"));
            partyFile.setFileUpdatedAt(rs.getTimestamp("file_updated_at"));
            return partyFile;
        };
        return jdbcTemplate.queryForObject(sql, rowMapper, fileId);
    }
    
    
    @SuppressWarnings("deprecation")
	public List<Party_file> findByPartyId(Long partyId) {
        String sql = "SELECT * FROM party_file WHERE party_id = ?";
        return jdbcTemplate.query(sql, new Object[]{partyId}, (rs, rowNum) -> {
            Party_file file = new Party_file();
            file.setFileId(rs.getLong("file_id"));
            file.setFileName(rs.getString("file_name"));
            file.setFileContent(rs.getBytes("file_content"));
            file.setPartyId(rs.getLong("party_id"));
            file.setFileCreatedAt(rs.getTimestamp("file_created_at"));
            file.setFileUpdatedAt(rs.getTimestamp("file_updated_at"));
            return file;
        });
    }

	@SuppressWarnings("deprecation")
	public Party_file findById(Long fileId) {
	    String sql = "SELECT * FROM party_file WHERE file_id = ?";
	    return jdbcTemplate.queryForObject(sql, new Object[]{fileId}, (rs, rowNum) -> {
	        Party_file file = new Party_file();
	        file.setFileId(rs.getLong("file_id"));
	        file.setFileName(rs.getString("file_name"));
	        file.setFileContent(rs.getBytes("file_content"));
	        file.setPartyId(rs.getLong("party_id"));
	        file.setFileCreatedAt(rs.getTimestamp("file_created_at"));
	        file.setFileUpdatedAt(rs.getTimestamp("file_updated_at"));
	        return file;
	    });
	}

	public void deleteById(long fileId) {
	    String checkSql = "SELECT COUNT(*) FROM party_file WHERE file_id = ?";
	    Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, fileId);

	    if (count == null || count == 0) {
	        throw new RuntimeException("File not found with ID: " + fileId);
	    }

	    String sql = "DELETE FROM party_file WHERE file_id = ?";
	    jdbcTemplate.update(sql, fileId);
	}



}

