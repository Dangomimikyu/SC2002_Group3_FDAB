package UserView;

import User.HDB_Manager;

import java.util.InputMismatchException;

public class ManagerMenu extends Menu
{
    private static HDB_Manager user;
    public static void start()
    {
        Display();
    }

    private static void Display()
    {
        int choice = -1;
        while (choice != 7) {
            System.out.println("============================");
            System.out.println("|      Applicant menu      |");
            System.out.println("| 1. Go to Applicant view  |");
            System.out.println("| 2. Join a project        |");
            System.out.println("| 3. View Project status   |");
            System.out.println("| 4. View all applicants   |");
            System.out.println("| 5. Update applicant flat |");
            System.out.println("| 6. Generate flat receipt |");
            System.out.println("| 7. Log out               |");
            System.out.println("============================");
            System.out.print("Enter choice: ");
            try {
                choice = sc.nextInt();
            } catch (final InputMismatchException e) {
                System.out.println("Invalid character");
                sc.nextLine(); // ignore and move the cursor to next line
                continue;
            }

            switch (choice)
            {
                case 1:
                    break;

                case 2:
                    break;

                case 3:
                    break;

                case 4:
                    break;

                case 5:
                    break;

                case 6:
                    break;

                case 7:
                    break;

                default:
                    System.out.println("Invalid number");
                    break;

            }
        }
    }

    public static void SetUser(HDB_Manager u)
    {
        user = u;
    }
}
