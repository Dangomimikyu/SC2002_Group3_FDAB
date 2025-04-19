package UserView;

import Filter.*;
import InteractableAttributePackage.Project;
import InteractableAttributePackage.ProjectDetails;
import InteractableAttributePackage.Request;
import InteractableAttributePackage.ProjectDetails.Location;
import Service.ReportGenerator;
import User.Applicant;
import User.HDB_Manager;
import User.Applicant.MaritalStatus;
import User.Enum_FlatType;
import Database.ProjectListingDB;
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
        while (choice != 12) {
            System.out.println("=====================================");
            System.out.println("|            Manager menu           |");
            System.out.println("| 1. Create project listing         |");
            System.out.println("| 2. Edit project listing           |");
            System.out.println("| 3. Delete project listing         |");
            System.out.println("| 4. View other projects            |");
            System.out.println("| 5. Handle officer registration    |");
            System.out.println("| 6. Handle applicant applications  |");
            System.out.println("| 7. Generate report                |");
            System.out.println("| 8. View enquiries                 |");
            System.out.println("| 9. Reply to an enquiry            |");
            System.out.println("| 10. View unresolved requests      |");
            System.out.println("| 11. Handle withdrawals            |");
            System.out.println("| 12. Log out                       |");
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
                    HandleApplicantRequest();
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

                case 10:
                    ViewHandledRequests();
                    return;

                case 11:
                    HandleWithdrawal();
                    return;

                case 12: // log out
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

        System.out.print("Enter group (single, married, all): ");
        String group = sc.nextLine();
        group = group.toUpperCase();

        user.CreateBTOProject(name, neighbourhood, selling2, selling3, avail2, avail3, openDate, closeDate, offrSlots, (vis != 0), group);
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

        System.out.print("Enter group (single, married, all): ");
        String group = sc.nextLine();
        group = group.toUpperCase();

        user.EditBTOProject(name, neighbourhood, selling2, selling3, avail2, avail3, openDate, closeDate, offrSlots, (vis != 0), group);
    }

    private static void ViewOtherProject()
    {
        int choice = -1;
        while (choice < 1 || choice > 7) {
            // ask how to filter then show the filtered stuff
            System.out.println("Choose which filter to use: ");
            System.out.println("1. Filter by Alphabetic Order");
            System.out.println("2. Filter by Age");
            System.out.println("3. Filter by Flat Type Availability");
            System.out.println("4. Filter by Location");
            System.out.println("5. Filter by Marital Status");
            System.out.println("6. Filter by Selling Price");
            System.out.println("7. Filter by Visibility");
            System.out.print("\nEnter your choice of filter: ");
            try {
                choice = sc.nextInt();
            } catch (final InputMismatchException e) {
                System.out.println("Invalid character");
                sc.nextLine(); // ignore and move the cursor to next line
                continue;
            }

            int order = -1;
            switch (choice) {
                case 1: 
                    String starting_char = "";
                    while (!starting_char.matches("[a-zA-Z]+") && starting_char.length() != 1 || starting_char.length() != 0) {
                        System.out.print("Enter a alphabetic character to filter projects with such starting character (enter nothing for no filtering based on starting char): ");
                        starting_char = sc.nextLine();
                    }
                    order = -1;
                    while (order != 1 || order != 2) {
                        System.out.print("Enter 1 if you want it in ASCENDING order and 2 if you want it in DESCENDING order: ");
                        order = GetIntInput("Enter 1 if you want it in ASCENDING order and 2 if you want it in DESCENDING order: ");
                    }
                    if (starting_char.equals("")) {starting_char = null;}
                    if (order == 1) { ProjectListingDB.getInstance().ViewDB(new Filter_Alphabetic(starting_char,orderBy.ASCENDING)); }
                    else if (order == 2) { ProjectListingDB.getInstance().ViewDB(new Filter_Alphabetic(starting_char,orderBy.DESCENDING)); }

                case 2: 
                    int minAge = -99;
                    while (minAge != -1 || minAge < 0) {
                        System.out.print("Enter min Age in years of filtered projects (enter -1 if you would like no min age): ");
                        minAge = sc.nextInt();
                    }
                    int maxAge = -99;
                    while (maxAge != -1 || maxAge < 0) {
                        System.out.print("Enter min Age in years of filtered projects (enter -1 if you would like no min age): ");
                        maxAge = sc.nextInt();
                    }
                    order = -1;
                    while (order != 1 || order != 2) {
                        System.out.print("Enter 1 if you want it in ASCENDING order and 2 if you want it in DESCENDING order: ");
                        order = GetIntInput("Enter 1 if you want it in ASCENDING order and 2 if you want it in DESCENDING order: ");
                    }
                    if (order == 1) { ProjectListingDB.getInstance().ViewDB(new Filter_Age(minAge,maxAge,orderBy.ASCENDING)); }
                    else if (order == 2) { ProjectListingDB.getInstance().ViewDB(new Filter_Age(minAge,maxAge,orderBy.DESCENDING)); }

                    case 3: 
                        int flat_type_choice = -1;
                        while (flat_type_choice < 1 || flat_type_choice > 3) {
                            System.out.println("Choose which flat type to filter by (BOTH if you want to see availability of both flat types): ");
                            System.out.println("1. TWO_ROOM\n2. THREE_ROOM\n3. BOTH");
                            flat_type_choice = GetIntInput("Enter your choice: ");
                        }
                        order = -1;
                        while (order != 1 || order != 2) {
                            System.out.print("Enter 1 if you want it in ASCENDING order and 2 if you want it in DESCENDING order: ");
                            order = GetIntInput("Enter 1 if you want it in ASCENDING order and 2 if you want it in DESCENDING order: ");
                        }
                        Enum_FlatType flatType = Enum_FlatType.DEFAULT;
                        if (flat_type_choice == 1) {flatType = Enum_FlatType.TWO_ROOM; } 
                        else if (flat_type_choice == 2) {flatType = Enum_FlatType.THREE_ROOM; } 
                        if (order == 1) { ProjectListingDB.getInstance().ViewDB(new Filter_FlatType(flatType,orderBy.ASCENDING)); }
                        else if (order == 2) { ProjectListingDB.getInstance().ViewDB(new Filter_FlatType(flatType,orderBy.DESCENDING)); }

                    case 4: 
                        System.out.print("Enter location/neighbourhood of project to filter from: ");
                        String location = sc.nextLine();
                        ProjectListingDB.getInstance().ViewDB(new Filter_Location(Location.valueOf(location)));

                    case 5: 
                        int marital_choice = -1;
                        while (marital_choice < 1 || marital_choice > 2) {
                            System.out.println("Choose which marital status to filter projects that are open to that group: ");
                            System.out.println("1. SINGLE\n2. MARRIED");
                            marital_choice = GetIntInput("Enter your choice: ");
                        }
                        if (marital_choice == 1) { ProjectListingDB.getInstance().ViewDB(new Filter_Marital(MaritalStatus.SINGLE)); }
                        else if (marital_choice == 2) { ProjectListingDB.getInstance().ViewDB(new Filter_Marital(MaritalStatus.MARRIED)); }

                    case 6: 
                        int minPrice = -99;
                        while (minPrice != -1 || minPrice < 0) {
                            System.out.print("Enter min selling price of units in filtered projects (enter -1 if you would like no min price): ");
                            minPrice = sc.nextInt();
                        }
                        int maxPrice = -99;
                        while (maxPrice != -1 || maxPrice < 0) {
                            System.out.print("Enter max selling price of units in filtered projects (enter -1 if you would like no max price): ");
                            maxPrice = sc.nextInt();
                        }
                        flat_type_choice = -1;
                        while (flat_type_choice < 1 || flat_type_choice > 3) {
                            System.out.println("Choose which flat type to filter by (BOTH if you want to see availability of both flat types): ");
                            System.out.println("1. TWO_ROOM\n2. THREE_ROOM\n3. BOTH");
                            flat_type_choice = GetIntInput("Enter your choice: ");
                        }
                        order = -1;
                        while (order != 1 || order != 2) {
                            System.out.print("Enter 1 if you want it in ASCENDING order and 2 if you want it in DESCENDING order: ");
                            order = GetIntInput("Enter 1 if you want it in ASCENDING order and 2 if you want it in DESCENDING order: ");
                        }
                        flatType = Enum_FlatType.DEFAULT;
                        if (flat_type_choice == 1) {flatType = Enum_FlatType.TWO_ROOM; } 
                        else if (flat_type_choice == 2) {flatType = Enum_FlatType.THREE_ROOM; } 
                        if (order == 1) { ProjectListingDB.getInstance().ViewDB(new Filter_SellingPrice(minPrice,maxPrice,orderBy.ASCENDING,flatType)); }
                        else if (order == 2) { ProjectListingDB.getInstance().ViewDB(new Filter_SellingPrice(minPrice,maxPrice,orderBy.DESCENDING,flatType)); }

                    case 7: 
                        ProjectListingDB.getInstance().ViewDB(new Filter_Visibility());

                    default:
                        System.out.println("Invalid number");
                        break;
            }
        }
    }

    private static void ViewHandledRequests()
    {
        // for officer applying to be a part of this manager's projects
        System.out.print("Enter one of your projects name to see all applicant and officer applications: ");
        String projname = sc.nextLine();
        user.ViewApplicantApplications(projname);
        user.ViewOfficerApplications(projname);
    }

    private static void HandleApplicantRequest()
    {
        System.out.print("Enter userID of the applicant's application you wish to resolve: ");
        String appID = sc.nextLine();
        System.out.print("Enter the project name to which applicant is trying to apply to: ");
        String projname = sc.nextLine();
        int status_choice = -1;
        while (status_choice != 1 || status_choice != 2) {
            status_choice = GetIntInput("Would you like to approve or reject it? (1 for yes and 2 for no): ");
        }
        boolean status = false;
        if (status_choice == 1) {status = true;}
        user.ApproveOrRejectApplication(appID, projname, status);
    }

    private static void HandleOfficerRegistration()
    {
        System.out.print("Enter userID of the officer's application you wish to resolve: ");
        String officerID = sc.nextLine();
        System.out.print("Enter the project name to which officer is trying to apply to: ");
        String projname = sc.nextLine();
        int status_choice = -1;
        while (status_choice != 1 || status_choice != 2) {
            status_choice = GetIntInput("Would you like to approve or reject it? (1 for yes and 2 for no): ");
        }
        boolean status = false;
        if (status_choice == 1) {status = true;}
        user.ApproveOfficerApplication(officerID, projname, status);
    }

    private static void HandleWithdrawal()
    {
        System.out.print("Enter userID of the applicant's withdrawal you wish to resolve: ");
        String appID = sc.nextLine();
        System.out.print("Enter the project name to which applicant is trying to apply to: ");
        String projname = sc.nextLine();
        int status_choice = -1;
        while (status_choice != 1 || status_choice != 2) {
            status_choice = GetIntInput("Would you like to approve or reject it? (1 for yes and 2 for no): ");
        }
        boolean status = false;
        if (status_choice == 1) {status = true;}
        user.ApproveOrRejectWithdrawal(appID, projname, status);
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
        System.out.print("Enter the project name you wish to view all enquiries for: ");
        user.ViewProjectEnquiries(sc.nextLine());
    }

    private static void ReplyEnquiry()
    {
        System.out.print("Enter the project name of the enquiry you wish to reply to: ");
        String projname = sc.nextLine();
        System.out.print("Enter the title of the enquiry you wish to reply to: ");
        String enqTitle = sc.nextLine();
        System.out.print("Enter your reply: ");
        String reply = sc.nextLine();
        user.ReplyToProjectEnquiry(projname,enqTitle,reply);
    }
}
