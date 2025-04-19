package UserView;
import Service.*;
import User.*;

public class AuthMenu extends Menu
{

    public static void Login(SystemUser.usertype type)
    {
        Authenticator authenticator = new Authenticator();
        SystemUser user = null;
        sc.nextLine();
        while (user == null)
        {
            String id = "", pass = "";

            System.out.print("Enter ID: ");
            id = sc.nextLine();
            System.out.print("Enter Password: ");
            pass = sc.nextLine();

            user = authenticator.login(id,pass);
        }
        if (type == SystemUser.usertype.MANAGER) {
            ManagerMenu.SetUser((HDB_Manager)user);
            ManagerMenu.start();
        }
        else if (type == SystemUser.usertype.OFFICER) {
            OfficerMenu.SetUser((HDB_Officer)user);
            OfficerMenu.start();
        }
        else if (type == SystemUser.usertype.APPLICANT) {
            ApplicantMenu.SetUser((Applicant)user);
            ApplicantMenu.start();
        }
    }
}
