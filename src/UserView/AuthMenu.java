//AuthMenu authenticates user's log-in and password to initiate OfficerMenu, ManagerMenu or ApplicantMenu

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
        while (true)
        {
            String id = "", pass = "";

            System.out.print("\nEnter ID: ");
            id = sc.nextLine();
            System.out.print("Enter Password: ");
            pass = sc.nextLine();

            user = authenticator.login(id,pass);
            if (user == null) { System.out.println("\nError: Could not find user! Please re-enter your id and password."); }
            if ((!(user instanceof HDB_Manager) && type == SystemUser.usertype.MANAGER) ||
                (!(user instanceof HDB_Officer) && type == SystemUser.usertype.OFFICER) ||
                (!(user instanceof Applicant) && type == SystemUser.usertype.APPLICANT)) {
                System.out.println("\nError: User credentials are correct but wrong user type was picked!");
                continue;
            }
            break;
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
