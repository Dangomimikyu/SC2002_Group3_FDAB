package User;
//SystemUser is the superclass for Applicant, HDBManager and HDBOfficer

public class SystemUser{

    public enum usertype {
        APPLICANT, OFFICER, MANAGER
    }

    public usertype UserPerms;
    public final String userID;
    public String password;
    public final String name;

    public SystemUser(String nric, String p, String n) {
        this.userID = nric;
        this.password = p;
        this.name = n;
    }

    public String getUserDetails() {
        return  "User Perms: " + this.UserPerms.toString()
                + "\nUserID: " + userID
                + "\nName: " + name;
    }
    
}
