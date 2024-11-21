package TTT.RUSH.JDBC.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;
import TTT.RUSH.JDBC.entity.Party;
import TTT.RUSH.JDBC.entity.PartyMapper;

@Repository
public class PartyDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 사용자 ID로 파티 목록 가져오기
    public List<Party> getPartiesByUserId(int userId) {
        String sql = "SELECT p.* FROM party p JOIN party_users pu ON p.party_id = pu.party_id WHERE pu.user_id = ?";
        return jdbcTemplate.query(sql, new PartyMapper(), userId);
    }

    // 파티 생성
    public int createParty(String partyName) {
        String sql = "INSERT INTO party (party_name, party_code, party_invite, party_active) VALUES (?, ?, ?, 1)";
        String partyCode = generatePartyCode();
        jdbcTemplate.update(sql, partyName, partyCode, partyCode); // 처음 생성은 party_code와 party_invite를 동일하게 설정 
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    // 기본 역할 생성 (admin, editor, ban) <- 롤을 수정할 수 있어도 이거 기본형은 못지우게 처리해둬야함
    public void createDefaultRoles(int partyId) {
        String sql = "INSERT INTO role (party_id, role_name, project_delete, admin_delete, basic_delete, basic_write, file_write, comment_write) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, partyId, "admin", 1, 1, 1, 1, 1, 1);
        jdbcTemplate.update(sql, partyId, "editor", 0, 0, 1, 1, 1, 1);
        jdbcTemplate.update(sql, partyId, "ban", 0, 0, 0, 0, 0, 0);
    }

    // 사용자와 파티를 연결하고 기본 역할을 editor로 추가
    public void addUserToParty(int userId, int partyId, String roleName) {
        // 기본 역할 editor의 role_id 찾기
        int roleId = getRoleIdByName(partyId, roleName);
        // 파티에 사용자 추가 (editor 역할 부여)
        String sql = "INSERT INTO party_users (party_id, user_id, role_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, partyId, userId, roleId);
    }

    // 사용자 파티 참가 및 기본 역할 editor 부여
    public void addUserToParty(int userId, int partyId) {
        // 기본 역할 "editor"의 role_id 찾기
        int roleId = getRoleIdByName(partyId, "editor");
        // 파티에 사용자 추가 (editor 역할 부여)
        String sql = "INSERT INTO party_users (party_id, user_id, role_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, partyId, userId, roleId);
    }

    // 파티 코드로 파티 찾기
    public Party getPartyByCode(String partyCode) {
        String sql = "SELECT * FROM party WHERE party_code = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new PartyMapper(), partyCode);
        } catch (EmptyResultDataAccessException e) {
            return null; 
        }
    }

    public Party findPartyById(Long partyId) {
        String sql = "SELECT * FROM party WHERE party_id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Party.class), partyId);
    }
    
    // 파티의 역할 이름으로 role_id 찾기
    private int getRoleIdByName(int partyId, String roleName) {
        String sql = "SELECT role_id FROM role WHERE party_id = ? AND role_name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, partyId, roleName);
    }

    // 파티 코드 생성 (UUID)
    private String generatePartyCode() {
        String partyCode;
        String sql = "SELECT COUNT(*) FROM party WHERE party_code = ?";
        do {
            partyCode = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        } while (jdbcTemplate.queryForObject(sql, Integer.class, partyCode) > 0);
        return partyCode;
    }

}

