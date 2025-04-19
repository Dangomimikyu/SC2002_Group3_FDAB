package UserView;
import Main.MainApp;
import Service.Authenticator;
import User.Applicant;
import User.HDB_Manager;
import User.HDB_Officer;
import User.SystemUser;

public class AuthMenu extends Menu
{
    public static void Login(SystemUser.usertype type) {
        for (int i = 2; i >= 0; --i) {
            String id = "", pass = "";

            System.out.print("Enter ID: ");
            id = sc.nextLine();
            System.out.print("Enter Password: ");
            pass = sc.nextLine();

            Authenticator auth = new Authenticator();
            SystemUser ret = auth.login(id, pass);

            if (ret != null) {
                System.out.print("Welcome ");
                System.out.println(ret.name);
                if (type == SystemUser.usertype.MANAGER) {
                    ManagerMenu.SetUser((HDB_Manager)ret);
                    ManagerMenu.start();
                }
                else if (type == SystemUser.usertype.OFFICER) {
                    OfficerMenu.SetUser((HDB_Officer)ret);
                    OfficerMenu.start();
                }
                else if (type == SystemUser.usertype.APPLICANT) {
                    ApplicantMenu.SetUser((Applicant)ret);
                    ApplicantMenu.start();
                }
                return;
            } else {
                System.out.print("Wrong credentials. you have ");
                System.out.print(i);
                System.out.print(" attempts left");
                System.out.println();
            }
        }
    }
}
