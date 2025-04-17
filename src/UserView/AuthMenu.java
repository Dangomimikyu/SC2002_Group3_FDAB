package UserView;
import User.SystemUser;

public class AuthMenu extends Menu
{
    public static void Login(SystemUser.usertype type)
    {
        for (int i = 0; i < 3; ++i)
        {
            String id = "", pass = "";

            System.out.print("Enter ID: ");
            id = sc.nextLine();
            System.out.print("Enter Password: ");
            pass = sc.nextLine();

            // SystemUser ret = authenticator.Authenticate(id, pass)
            // if (ret != null) print (welcome, "name"); MainApp.SetUser(ret);
            // else print "invalid details, you have 3 - i attempts left
        }
    }
}
