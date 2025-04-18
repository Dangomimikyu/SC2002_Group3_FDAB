package UserView;

import Database.ProjectListingDB;
import InteractableAttributePackage.Enquiry;
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
    public static void SetUser(Applicant a)
    {
        user = a;
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
                case 1: // view available projects based on the applicant's stats
                    ProjectListingDB.getInstance().ViewDB();
                    break;

                case 2: // apply for project
                    System.out.print("Enter name of project to apply for: ");
                    user.ApplyProject(sc.nextLine());
                    break;

                case 3: // make a new enquiry
                    user.CreateEnquiry();
                    break;

                case 4: // view applicant's own enquiries
                    user.ViewEnquiry();
                    break;

                case 5: // edit applicant's own enquiries
                    user.EditEnquiry();
                    break;

                case 6: // delete an enquiry
                    user.DeleteEnquiry();
                    break;

                case 7: // book a flat
                    user.BookFlat();
                    break;

                case 8: // log out
                    System.out.println("Logging out");
                    return;

                default:
                    System.out.println("Invalid number");
                    break;

            }
        }
    }
}
