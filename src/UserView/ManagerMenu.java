package UserView;

import Filter.*;
import InteractableAttributePackage.Project;
import InteractableAttributePackage.ProjectDetails;
import InteractableAttributePackage.Request;
import Service.ReportGenerator;
import User.Applicant;
import User.HDB_Manager;

import javax.swing.text.View;
import java.util.InputMismatchException;
import java.util.Objects;

public class ManagerMenu extends Menu
{
    private static HDB_Manager user;
    public static void start()
    {
        Display();
    }
    public static void SetUser(HDB_Manager u)
    {
        user = u;
    }
    private static void Display()
    {
        int choice = -1;
        while (choice != 10) {
            System.out.println("=====================================");
            System.out.println("|            Manager menu           |");
            System.out.println("| 1. Create project listing         |");
            System.out.println("| 2. Edit project listing           |");
            System.out.println("| 3. Delete project listing         |");
            System.out.println("| 4. View other projects            |");
            System.out.println("| 5. Handle officer registration    |");
            System.out.println("| 6. Handle applicant requests      |");
            System.out.println("| 7. Generate report                |");
            System.out.println("| 8. View enquiries                 |");
            System.out.println("| 9. Reply to an enquiry            |");
            System.out.println("| 10. Log out                        |");
            System.out.println("=====================================");
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
                case 1: // make new project
                    MakeProject();
//                    user.CreateBTOProject();
                    break;

                case 2: // edit project
                    EditProject();
                    break;

                case 3: // delete project
                    System.out.print("Enter name of project to delete: ");
                    user.DeleteBTOProject(sc.nextLine());
                    break;

                case 4: // view other project
                    ViewOtherProject();
                    break;

                case 5: // handle officer registration
                    HandleOfficerRegistration();
                    break;

                case 6: // handle applicant requests (apply and withdrawal)
                    HandleApplicantRequests();
                    break;

                case 7: // generate report
                    MakeReport();
                    break;

                case 8: // view enquiries
                    ViewEnquiries();
                    break;

                case 9: // reply to enquiry
                    ReplyEnquiry();
                    break;

                case 10: // log out
                    System.out.println("Logging out");
                    return;

                default:
                    System.out.println("Invalid number");
                    break;

            }
        }
    }

    private static int GetIntInput(String repeat)
    {
        int choice = -1;

        while (choice < 0) {
            try {
                choice = sc.nextInt();
            } catch (final InputMismatchException e) {
                System.out.println("Invalid character");
                sc.nextLine(); // ignore and move the cursor to next line
                System.out.println(repeat);
            }
        }

        return choice;
    }

    private static void MakeProject()
    {
        System.out.print("Enter project name: ");
        String name = sc.nextLine();

        System.out.print("Enter neighbourhood: ");
        String neighbourhood = sc.nextLine();

        System.out.print("Enter 2-room selling price: ");
        int selling2 = GetIntInput("Enter 2-room selling price: ");

        System.out.print("Enter number of available 2-room units: ");
        int avail2 = GetIntInput("Enter number of available 2-room units: ");

        System.out.print("Enter 3-room selling price: ");
        int selling3 = GetIntInput("Enter 3-room selling price: ");

        System.out.print("Enter number of available 3-room units: ");
        int avail3 = GetIntInput("Enter 3-room selling price: ");

        System.out.print("Enter opening date: ");
        String openDate = sc.nextLine();

        System.out.print("Enter closing date: ");
        String closeDate = sc.nextLine();

        System.out.print("Enter number of officer slots: ");
        int offrSlots = GetIntInput("Enter number of officer slots: ");

        System.out.print("Is this project visible right now? (1 for yes, 0 for no): ");
        int vis = GetIntInput("Is this project visible right now? (1 for yes, 0 for no): ");

        System.out.print("Enter number of project slots: ");
        int slots = GetIntInput("Enter number of officer slots: ");

        System.out.print("Enter group (single, married, all): ");
        String group = sc.nextLine();
        group = group.toUpperCase();

        user.CreateBTOProject(name, neighbourhood, selling2, selling3, avail2, avail3, openDate, closeDate, slots, (vis != 0), group);
    }

    private static void EditProject()
    {
        System.out.print("Enter project name: ");
        String name = sc.nextLine();

        System.out.print("Enter neighbourhood: ");
        String neighbourhood = sc.nextLine();

        System.out.print("Enter 2-room selling price: ");
        int selling2 = GetIntInput("Enter 2-room selling price: ");

        System.out.print("Enter number of available 2-room units: ");
        int avail2 = GetIntInput("Enter number of available 2-room units: ");

        System.out.print("Enter 3-room selling price: ");
        int selling3 = GetIntInput("Enter 3-room selling price: ");

        System.out.print("Enter number of available 3-room units: ");
        int avail3 = GetIntInput("Enter 3-room selling price: ");

        System.out.print("Enter opening date: ");
        String openDate = sc.nextLine();

        System.out.print("Enter closing date: ");
        String closeDate = sc.nextLine();

        System.out.print("Enter number of officer slots: ");
        int offrSlots = GetIntInput("Enter number of officer slots: ");

        System.out.print("Is this project visible right now? (1 for yes, 0 for no): ");
        int vis = GetIntInput("Is this project visible right now? (1 for yes, 0 for no): ");

        System.out.print("Enter number of project slots: ");
        int slots = GetIntInput("Enter number of officer slots: ");

        System.out.print("Enter group (single, married, all): ");
        String group = sc.nextLine();
        group = group.toUpperCase();

        user.EditBTOProject(name, neighbourhood, selling2, selling3, avail2, avail3, openDate, closeDate, slots, (vis != 0), group);
    }

    private static void ViewOtherProject()
    {
        // ask how to filter then show the filtered stuff
    }

    private static void HandleOfficerRegistration()
    {
        // for officer applying to be a part of this manager's projects
    }

    private static void HandleApplicantRequests()
    {

    }

    private static void MakeReport()
    {
        String projName = "";
        IFilter filterType = null;
        int filterChoice = -1;

        System.out.print("Enter name of project for report (leave blank to see all projects): ");
        projName = sc.nextLine();

        while (filterChoice < 0 || filterChoice > 7) {

            System.out.println("========================");
            System.out.println(" 0. No filter");
            System.out.println(" 1. Age");
            System.out.println(" 2. Alphabet");
            System.out.println(" 3. Flat type");
            System.out.println(" 4. Location");
            System.out.println(" 5. Marital status");
            System.out.println(" 6. Selling price");
            System.out.println(" 7. Visibility");
            System.out.println("========================");
            System.out.print("Enter choice of filter: ");
            try {
                filterChoice = sc.nextInt();
            } catch (final InputMismatchException e) {
                System.out.println("Invalid character");
                sc.nextLine(); // ignore and move the cursor to next line
            }
        }
        
        if (filterChoice == 0)
        {
            if (Objects.equals(projName, ""))
            {
                ReportGenerator.getInstance().GenerateReport();
            }
            else {
                ReportGenerator.getInstance().GenerateReport(projName);
            }
        }
        
        switch (filterChoice)
        {
            case 1:
                System.out.print("Order by (0 for ascending, 1 for descending): ");
                int temp1 = GetIntInput("Order by (0 for ascending, 1 for descending): ");
                while (temp1 < 0 || temp1 > 1)
                {
                    temp1 = GetIntInput("Order by (0 for ascending, 1 for descending): ");
                }

                if (temp1 == 0)
                {
                    filterType = new Filter_Age(-1, -1, IFilter.orderBy.ASCENDING);
                }
                else {
                    filterType = new Filter_Age(-1, -1, IFilter.orderBy.DESCENDING);
                }
                break;

            case 2:
                System.out.print("Order by (0 for ascending, 1 for descending): ");
                int temp2 = GetIntInput("Order by (0 for ascending, 1 for descending): ");
                while (temp2 < 0 || temp2 > 1)
                {
                    temp2 = GetIntInput("Order by (0 for ascending, 1 for descending): ");
                }

                if (temp2 == 0)
                {
                    filterType = new Filter_Alphabetic("a", IFilter.orderBy.ASCENDING);
                }
                else {
                    filterType = new Filter_Alphabetic("z", IFilter.orderBy.DESCENDING);
                }
                break;

            case 3:
                filterType = new Filter_FlatType(Request.FlatType.NULL, IFilter.orderBy.ASCENDING);
                break;

            case 4:
                filterType = new Filter_Location(ProjectDetails.Location.ANG_MO_KIO);
                break;

            case 5:
                filterType = new Filter_Marital(Applicant.MaritalStatus.ALL);
                break;

            case 6:
                filterType = new Filter_SellingPrice(-1, -1, IFilter.orderBy.ASCENDING, Request.FlatType.NULL);
                break;

            case 7:
                filterType = new Filter_Visibility();
                break;

        }

        if (Objects.equals(projName, "")) {
            ReportGenerator.getInstance().GenerateReport(filterType);
        }
        else {
            ReportGenerator.getInstance().GenerateReport(projName, filterType);
        }
    }

    private static void ViewEnquiries()
    {

    }

    private static void ReplyEnquiry()
    {

    }
}
