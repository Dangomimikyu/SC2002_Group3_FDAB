package User;
//SystemUser is the superclass for Applicant, HDBManager and HDBOfficer

public class SystemUser{

    public enum usertype {
        APPLICANT, OFFICER, MANAGER
    }

    public usertype ApplicantPerms;
    public final String userID;
    public final String password;
    public final String name;

    public SystemUser(String nric, String p, String n) {
        this.userID = nric;
        this.password = p;
        this.name = n;
    }
    
}
