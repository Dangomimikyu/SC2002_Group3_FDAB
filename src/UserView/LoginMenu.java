package UserView;
import User.SystemUser;

import java.util.InputMismatchException;

public class LoginMenu extends Menu
{
    public static void start()
    {
        Display();

    }

    private static void Display()
    {
        int choice = -1;
        while (choice != 4)
        {
            System.out.println("=============================");
            System.out.println("|   HDB management system   |");
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
                System.out.println("Invalid character");
                sc.nextLine(); // ignore and move the cursor to next line
                continue;
            }

            switch(choice)
            {
                case 1:
                    AuthMenu.Login(SystemUser.usertype.APPLICANT);
                    return;

                case 2:
                    AuthMenu.Login(SystemUser.usertype.MANAGER);
                    return;

                case 3:
                    AuthMenu.Login(SystemUser.usertype.OFFICER);
                    return;

                case 4:
                    System.out.println("Exiting system");
                    return;

                default:
                    System.out.println("Invalid number");
                    break;
            }
        }
    }
}
