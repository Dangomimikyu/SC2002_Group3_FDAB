package UserView;

import User.HDB_Officer;
import User.Enum_FlatType;
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
            System.out.println("================================");
            System.out.println("|      Officer menu            |");
            System.out.println("| 1. Go to Applicant view      |");
            System.out.println("| 2. Join a project            |");
            System.out.println("| 3. View joinable Projects    |");
            System.out.println("| 4. View Project status       |");
            System.out.println("| 5. View Project details      |");
            System.out.println("| 6. View handled bookings     |");
            System.out.println("| 7. Resolve a Booking         |");
            System.out.println("| 8. Generate flat receipt     |");
            System.out.println("| 9. Log out                   |");
            System.out.println("================================");
            System.out.print("Enter choice: ");
            try {
                choice = sc.nextInt();
            } catch (final InputMismatchException e) {
                System.out.println("Invalid character");
                sc.nextLine(); // ignore and move the cursor to next line
                continue;
            }

            sc.nextLine();
            switch (choice)
            {
                case 1: // go to applicant view
                    System.out.println("\nNOTE: to get back to applicant menu, simply log out from that menu");
                    ApplicantMenu.start();
                    break;

                case 2: // apply to join a project as an officer
                    System.out.print("Enter name of project to apply for: ");
                    String projname = sc.nextLine();
                    user.RegisterForProject(projname);
                    break;

                case 3: // show all projects open to officer for management position
                    user.viewProjectList();
                    break;

                case 4: //show status of Officer's registration status for their applied project
                    user.ViewApplicationStatus();
                    break;

                case 5: // show details of officer's applied project
                    user.ViewProjectDetails();
                    break;

                case 6: // show all applicant handled requests (only bookings)
                    user.viewAllHandledRequests();
                    break;

                case 7: // update applicant flat type   
                    System.out.print("Enter the userID of the applicant you wish to book for: ");
                    String applicantID = sc.nextLine();
                    System.out.print("Enter the flat type to which to book the applicant: ");
                    String flatTypeToBook = sc.nextLine();
                    user.UpdateFlatSelection(applicantID,Enum_FlatType.valueOf(flatTypeToBook));
                    break;

                case 8: //generate receipt with applicant userID
                    System.out.print("Enter the userID of the applicant you wish to generate a receipt for: ");
                    String userid = sc.nextLine();
                    user.GenerateReceipt(userid);
                    break;

                case 9:// log out
                    System.out.println("Logging out");
                    return;

                default:
                    System.out.println("Invalid number");
                    break;

            }
        }
    }
}
