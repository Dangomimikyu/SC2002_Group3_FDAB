//AuthMenu authenticates user's log-in and password to initiate OfficerMenu, ManagerMenu or ApplicantMenu

package UserView;

import java.util.regex.Pattern;
import Database.UserInfoDB;
import Service.*;
import User.*;
import User.SystemUser.usertype;

public class AuthMenu extends Menu
{

    public static void Login(usertype type)
    {
        Authenticator authenticator = new Authenticator();
        SystemUser user = null;
        int attempts = 0;
        sc.nextLine();

        while (attempts < 3)
        {
            try {

                String id = "", pass = "";

                System.out.print("\nEnter ID: ");
                id = sc.nextLine();

                //if nric is in wrong format
                if (!Pattern.compile("^[STFG]\\d{7}[A-Z]$").matcher(id).matches()) 
                { throw new Error("\nError: NRIC is in wrong format!"); }

                System.out.print("Enter Password: ");
                pass = sc.nextLine();

                //if account with given id exists in database but corresponding password is wrong
                if (UserInfoDB.getInstance().SearchDB(id) != null && authenticator.login(id,pass) == null) 
                { attempts++; throw new Error("\nError: Wrong password entered! You have "+(3-attempts)+" left."); }

                // testing
                // id = "T8765432F";
                // pass = "password";
                user = authenticator.login(id,pass);

                //could not find account with given id and password
                if (user == null) { throw new Error("\nError: Could not find user"); }

                //can find account but the perm type to log in as does not correspond with actual account user perms
                else if ((!(user instanceof HDB_Manager) && type == usertype.MANAGER) ||
                    ( !(user instanceof HDB_Officer) && type == usertype.OFFICER) ||
                    ( !(user instanceof Applicant) && type == usertype.APPLICANT) ||
                    (   user instanceof HDB_Officer && type == usertype.APPLICANT)) {
                    throw new Error("\nError: Chosen user type to access does not match searched user type!"); }
                    
                break;
            }
            catch (Error e) { System.out.println(e.getMessage()); }
        }
        if (attempts == 3) { System.out.println("\nToo many password attempts! Returning to Login Menu..."); return; }
        
        switch (type) {
            case MANAGER -> { ManagerMenu.SetUser((HDB_Manager)user); ManagerMenu.start(); }
            case OFFICER -> { OfficerMenu.SetUser((HDB_Officer)user); OfficerMenu.start(); }
            case APPLICANT -> { ApplicantMenu.SetUser((Applicant)user); ApplicantMenu.start(); }
        }

    }
}