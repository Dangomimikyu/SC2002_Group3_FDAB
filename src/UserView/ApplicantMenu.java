package UserView;

import Managers.EnquiryManager;
import User.Applicant;

import java.util.InputMismatchException;

public class ApplicantMenu extends Menu
{
    private static Applicant user;

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
            System.out.println("| 1. View projects         |");
            System.out.println("| 2. Apply for a project   |");
            System.out.println("| 3. Create an enquiry     |");
            System.out.println("| 4. View your enquiries   |");
            System.out.println("| 5. Edit your enquiries   |");
            System.out.println("| 6. Delete an enquiry     |");
            System.out.println("| 7. Book a flat           |");
            System.out.println("| 8. Log out               |");
            System.out.println("============================");
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

            switch (choice)
            {
                case 1:
                    // filter by applicant's status and stuff
                    //ArrayList<Project> pList = ProjectListingDB.getInstance().ViewDB();
                    break;

                case 2:
                    // use ApplicationManager to make a request
                    // request will be sent to RequestDB to be stored
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

                case 8:
                    break;

                default:
                    System.out.println("Invalid number");
                    break;

            }
        }
    }

    public static void SetUser(Applicant a)
    {
        user = a;
    }
}
