package UserView;
import Main.MainApp;
import Service.Authenticator;
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
                MainApp.SetUser(ret);
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
