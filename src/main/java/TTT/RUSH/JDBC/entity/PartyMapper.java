package TTT.RUSH.JDBC.entity;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PartyMapper implements RowMapper<Party> {

    @Override
    public Party mapRow(ResultSet rs, int rowNum) throws SQLException {
        Party party = new Party();
        party.setPartyId(rs.getInt("party_id"));
        party.setPartyName(rs.getString("party_name"));
        party.setPartyCode(rs.getString("party_code"));
        party.setPartyInvite(rs.getString("party_invite"));
        party.setPartyCreatedAt(rs.getTimestamp("party_created_at"));
        party.setPartyActive(rs.getBoolean("party_active"));
        return party;
    }
}
