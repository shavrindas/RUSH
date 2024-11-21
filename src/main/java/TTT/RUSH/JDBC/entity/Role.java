package TTT.RUSH.JDBC.entity;

public class Role {
    private int roleId;
    private Integer partyId;
    private String roleName;
    private Boolean projectDelete;
    private Boolean adminDelete;
    private Boolean basicDelete;
    private Boolean basicWrite;
    private Boolean fileWrite;
    private Boolean commentWrite;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public Integer getPartyId() {
        return partyId;
    }

    public void setPartyId(Integer partyId) {
        this.partyId = partyId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getProjectDelete() {
        return projectDelete;
    }

    public void setProjectDelete(Boolean projectDelete) {
        this.projectDelete = projectDelete;
    }

    public Boolean getAdminDelete() {
        return adminDelete;
    }

    public void setAdminDelete(Boolean adminDelete) {
        this.adminDelete = adminDelete;
    }

    public Boolean getBasicDelete() {
        return basicDelete;
    }

    public void setBasicDelete(Boolean basicDelete) {
        this.basicDelete = basicDelete;
    }

    public Boolean getBasicWrite() {
        return basicWrite;
    }

    public void setBasicWrite(Boolean basicWrite) {
        this.basicWrite = basicWrite;
    }

    public Boolean getFileWrite() {
        return fileWrite;
    }

    public void setFileWrite(Boolean fileWrite) {
        this.fileWrite = fileWrite;
    }

    public Boolean getCommentWrite() {
        return commentWrite;
    }

    public void setCommentWrite(Boolean commentWrite) {
        this.commentWrite = commentWrite;
    }
}
