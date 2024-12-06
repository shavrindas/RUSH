package TTT.RUSH.Basic.service;

import TTT.RUSH.JDBC.dao.PartyFileDao;
import TTT.RUSH.JDBC.entity.Party_file;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PartyFileService {
    private final PartyFileDao partyFileDao;

    public PartyFileService(PartyFileDao partyFileDao) {
        this.partyFileDao = partyFileDao;
    }

    // 파일 업로드 로직
    public boolean uploadFile(Party_file file) {
        int rowsInserted = partyFileDao.saveFile(file);
        return rowsInserted > 0;
    }

    public List<Party_file> getFilesByPartyId(Long partyId) {
        return partyFileDao.findByPartyId(partyId);
    }
    
    public Party_file getFileById(Long fileId) {
        return partyFileDao.findById(fileId);
    }

    public void deleteFile(Long fileId) {
        try {
            partyFileDao.deleteById(fileId);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to delete the file with ID: " + fileId, e);
        }
    }



}
