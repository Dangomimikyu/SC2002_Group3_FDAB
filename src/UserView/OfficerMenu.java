package UserView;

import User.HDB_Officer;
import User.Enum_FlatType;
import java.util.InputMismatchException;
import InteractableAttributePackage.Request.ApplicationStatus;

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
        u.InitialiseAssignedProject(u.project_name);
        ApplicantMenu.SetUser(user);
    }
    private static void Display()
    {
        System.out.println("\nWelcome " + user.name + " !\nWhat would you like to do today?");
        int choice = -1;
        while (choice != 12) {
            System.out.println("\n==================================");
            System.out.println("|            Officer menu          |");
            System.out.println("====================================");
            System.out.println("| 1. Go to Applicant Menu          |");
            System.out.println("| 2. Join a project as an Officer  |");
            System.out.println("| 3. View Joinable Projects        |");
            System.out.println("| 4. View Joined Project Details   |");
            System.out.println("| 5. Check Request Status          |");
            System.out.println("| 6. View handled bookings         |");
            System.out.println("| 7. Resolve a Booking             |");
            System.out.println("| 8. Update Flat Selection         |");
            System.out.println("| 9. Generate flat receipt         |");
            System.out.println("| 10. View Handled Enquiries       |");
            System.out.println("| 11. Reply to handled Enquiry     |");
            System.out.println("| 12. Log out                      |");
            System.out.println("====================================");
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
                case 1 -> { // go to applicant view
                    System.out.println("\nNOTE: to get back to applicant menu, simply log out from that menu");
                    ApplicantMenu.start();
                }

                case 2 -> { // apply to join a project as an officer
                    System.out.print("Enter name of project to apply as an officer for: ");
                    user.RegisterForProject(sc.nextLine());
                }

                // show all projects open to officer for management position
                case 3 -> user.viewProjectListForOfficers();
                //show status of Officer's registration status for their applied project
                case 4 -> user.viewAppliedProjectStatusOfficer();
                //check officer request status
                case 5 -> user.checkRequestStatus();
                // show all applicant handled requests (only bookings)
                case 6 -> { user.viewAllHandledRequests();}


                //resolve a booking
                case 7 -> {
                    System.out.print("Enter the userID of the applicant you wish to book for: ");
                    String applicantID = sc.nextLine();

                    int flat_choice = -1;
                    while (flat_choice != 1 && flat_choice != 2) {
                        System.out.println("Enter the flat type to update applicant: \n1. TWO_ROOM\n2. THREE_ROOM");
                        flat_choice = GetIntInput("\nEnter your choice: ");
                    }

                    int status_choice = -1;
                    while (status_choice != 1 && status_choice != 2) {
                        System.out.print("Enter the status to which to book the applicant:\n1. APPROVED\n2. REJECTED");
                        status_choice = GetIntInput("\nEnter your choice: ");    
                    }

                    Enum_FlatType flattype;
                    ApplicationStatus status;
                    if (flat_choice == 1) { flattype = Enum_FlatType.TWO_ROOM; }
                    else { flattype = Enum_FlatType.THREE_ROOM; }

                    if (status_choice == 1) { status = ApplicationStatus.APPROVED; }
                    else { status = ApplicationStatus.APPROVED; }

                    user.ResolveHandledRequest(applicantID, status, flattype);     
                }

                // update applicant flat type   
                case 8 -> {
                    int flat_choice = -1;
                    while (flat_choice != 1 && flat_choice != 2) {
                        System.out.println("Enter the flat type to update in your project: \n1. TWO_ROOM\n2. THREE_ROOM");
                        flat_choice = GetIntInput("Enter your choice: ");
                    }
                    if (flat_choice == 1) { user.UpdateFlatSelection(Enum_FlatType.TWO_ROOM); }
                    else if (flat_choice == 2) { user.UpdateFlatSelection(Enum_FlatType.THREE_ROOM); }
                }

                //generate flat receipt with userID
                case 9 -> {
                    System.out.print("Enter the userID of the applicant you wish to generate a receipt for: ");
                    user.GenerateReceipt(sc.nextLine());
                }

                //view handled enquiries
                case 10 -> user.ViewEnquiries();

                case 11 -> { //Reply to Enquiry
                    System.out.print("Enter the title of the enquiry you wish to reply to: ");
                    String title = sc.nextLine();
                    System.out.print("Enter the enquirer's id of the enquiry you wish to reply to: ");
                    String app_id = sc.nextLine();             
                    System.out.print("Enter your reply: ");
                    String reply = sc.nextLine();
                    user.ReplyToEnquiry(title, app_id, reply);
                }

                case 12 -> { //logout
                    System.out.println("Logging out");
                    return;
                }

                default -> System.out.println("\nError: Invalid number");

            }
        }
    }
}
