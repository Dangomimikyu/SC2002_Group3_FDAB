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
        u.InitialiseAssignedProject(u.project_name);
        ApplicantMenu.SetUser(user);
    }
    private static void Display()
    {
        System.out.println("\nWelcome " + user.name + " !\nWhat would you like to do today?");
        int choice = -1;
        while (choice != 12) {
            System.out.println("\n====================================");
            System.out.println("|            Officer menu          |");
            System.out.println("====================================");
            System.out.println("| 1.  Go to Applicant Menu         |");
            System.out.println("| 2.  Join a project as an Officer |");
            System.out.println("| 3.  View Joinable Projects       |");
            System.out.println("| 4.  View Joined Project Details  |");
            System.out.println("| 5.  Check Request Status         |");
            System.out.println("| 6.  View handled bookings        |");
            System.out.println("| 7.  Resolve a Booking            |");
            System.out.println("| 8.  Update Flat Selection        |");
            System.out.println("| 9.  View Handled Enquiries       |");
            System.out.println("| 10. Reply to handled Enquiry     |");
            System.out.println("| 11. Change Password              |");
            System.out.println("| 12. Log out                      |");
            System.out.println("====================================");
            System.out.print("Enter choice: ");
            try {
                choice = sc.nextInt();
            } catch (final InputMismatchException e) {
                System.out.println("Invalid character");
                continue;
            }
            finally { sc.nextLine(); }

            switch (choice)
            {
                case 1 -> { // go to applicant view
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
                    user.viewAllHandledRequests();
                    int index = GetIntInput("\nPlease enter the index of the officer application you wish to handle: ");
                    int approve = -1;
                    while (approve != 0 && approve != 1) {
                        approve = GetIntInput("Would you like to approve(1) or reject(0) it ?: ");
                    }
                    user.ResolveHandledRequest(index, (approve == 1));
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

                //view handled enquiries
                case 9 -> user.ViewEnquiries();

                case 10 -> { //Reply to Enquiry
                    user.ViewEnquiries();
                    int index = GetIntInput("\nPlease enter the index of the withdrawal you wish to handle: ");
                    sc.nextLine();
                    String reply = GetStringInput("Please enter your reply: ");
                    user.ReplyToEnquiry(index, reply);
                }

                case 11 -> {
                        String new_password = GetStringInput("Please enter your new password: ");
                        if (authenticator.changePassword( user.userID, user.password, new_password)) { return; }
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
