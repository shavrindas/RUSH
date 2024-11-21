// PartyBoardImage.java
package TTT.RUSH.JDBC.entity;

import java.sql.Timestamp;

public class PartyBoardImage {
    private long imageId;
    private long postId;
    private String url;
    private Timestamp uploadedAt;
	public long getImageId() {
		return imageId;
	}
	public void setImageId(long imageId) {
		this.imageId = imageId;
	}
	public long getPostId() {
		return postId;
	}
	public void setPostId(long postId) {
		this.postId = postId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Timestamp getUploadedAt() {
		return uploadedAt;
	}
	public void setUploadedAt(Timestamp uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

    // Getters and setters
}
