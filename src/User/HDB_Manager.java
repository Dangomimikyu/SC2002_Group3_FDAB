package User;
//HDB Manager

public class HDB_Manager extends SystemUser{

    public HDB_Manager(String nric, String p, String n) {
        super(nric, p, n);
        this.ApplicantPerms = usertype.MANAGER;
    }

}