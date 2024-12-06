package TTT.RUSH.JDBC.entity;

import java.sql.Timestamp;
/*
drop table party_file;
drop table party_file_version; 

CREATE TABLE party_file (
	    file_id INT AUTO_INCREMENT PRIMARY KEY,      -- 파일 아이디 (Primary Key)
	    file_name VARCHAR(255) NOT NULL,             -- 파일 이름
	    file_content LONGBLOB,                       -- 파일 내용 (이진 데이터 저장)
	    party_id INT,                                -- 파티 테이블과 연결된 아이디 (외래 키)
	    file_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,   -- 파일 생성일
	    file_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 파일 수정일
	    CONSTRAINT fk_party FOREIGN KEY (party_id) REFERENCES party(party_id) -- 외래 키 설정
	);

*/

public class Party_file {
    private long fileId;           // 파일 아이디
    private String fileName;      // 파일 이름
    private byte[] fileContent;   // 파일 내용 (이진 데이터)
    private long partyId;          // 파티 ID (연결된 Party ID)
    private Timestamp fileCreatedAt; // 파일 생성일
    private Timestamp fileUpdatedAt; // 파일 수정일

    // Getter 및 Setter
    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public long getPartyId() {
        return partyId;
    }

    public void setPartyId(long partyId) {
        this.partyId = partyId;
    }

    public Timestamp getFileCreatedAt() {
        return fileCreatedAt;
    }

    public void setFileCreatedAt(Timestamp fileCreatedAt) {
        this.fileCreatedAt = fileCreatedAt;
    }

    public Timestamp getFileUpdatedAt() {
        return fileUpdatedAt;
    }

    public void setFileUpdatedAt(Timestamp fileUpdatedAt) {
        this.fileUpdatedAt = fileUpdatedAt;
    }


}
