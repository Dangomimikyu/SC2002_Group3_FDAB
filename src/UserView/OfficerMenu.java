package UserView;

import User.HDB_Officer;

import java.util.InputMismatchException;

public class OfficerMenu extends Menu
{
    private static HDB_Officer user;
    public static void start()
    {
        Display();
    }
    public static void SetUser(HDB_Officer u)
    {
        user = u;
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
                case 1: // go to applicant view
                    ApplicantMenu.start();
                    break;

                case 2: // apply to join a project
                    break;

                case 3: // check project status
                    // show list of projects
                    // then print project details
                    break;

                case 4: // show applicant list
                    break;

                case 5: // update applicant flat type
                    break;

                case 6: // get receipt
                    // show applicant list again
                    // use receiptgenerator
                    break;

                case 7: // log out
                    System.out.println("Logging out");
                    return;

                default:
                    System.out.println("Invalid number");
                    break;

            }
        }
    }
}
