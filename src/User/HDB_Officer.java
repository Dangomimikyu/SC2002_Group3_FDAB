package User;
//HDB Officer possess all applicant’s capabilities.

public class HDB_Officer extends Applicant{

    public HDB_Officer(String n, String nric, int a, String m, String p, String AP, String APS) {
        super(n, nric, a, m, p, AP, APS);
        this.UserPerms = usertype.OFFICER;
    }

}