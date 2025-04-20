//LoginMenu allows users to choose what type of user to log-in as 

package UserView;

import User.SystemUser;
import java.util.InputMismatchException;

public class LoginMenu extends Menu
{
    public static void start() { Display(); }

    private static void Display()
    {
        int choice = -1;
        while (choice != 4)
        {
            System.out.println("\n=============================");
            System.out.println("|   HDB management system   |");
            System.out.println("=============================");
            System.out.println("| 1. Log in as Applicant    |");
            System.out.println("| 2. Log in as HDB Manager  |");
            System.out.println("| 3. Log in as HDB Officer  |");
            System.out.println("| 4. Exit system            |");
            System.out.println("=============================");
            System.out.print("Enter choice: ");
            try
            {
                choice = sc.nextInt();
            }
            catch (final InputMismatchException e)
            {
                System.out.println("\nError: Invalid character!");
                sc.nextLine();
                continue;
            }

            switch(choice)
            {
                case 1 -> AuthMenu.Login(SystemUser.usertype.APPLICANT);
                case 2 -> AuthMenu.Login(SystemUser.usertype.MANAGER);
                case 3 -> AuthMenu.Login(SystemUser.usertype.OFFICER);
                case 4 -> { System.out.println("\nExiting system..."); break; }
                default -> System.out.println("\nError: Invalid character!");
            }
        }
    }
}
