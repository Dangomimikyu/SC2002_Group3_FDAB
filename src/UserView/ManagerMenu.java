package UserView;

import InteractableAttributePackage.ProjectDetails.Location;
import User.HDB_Manager;
import User.Applicant.MaritalStatus;
import User.Enum_FlatType;
import Filter.*;
import Filter.IFilter.orderBy;
import java.util.InputMismatchException;
import java.util.regex.Pattern;

public class ManagerMenu extends Menu
{
    private static HDB_Manager user;
    //default filter is in alphabetic ascending order
    private static IFilter filter = new Filter_Alphabetic(null, orderBy.ASCENDING);

    public static void start() { Display(); }
    public static void SetUser(HDB_Manager u) { user = u; }
    private static void Display()
    {
        System.out.println("\nWelcome " + user.name + " !\nWhat would you like to do today?");
        int choice = -1;
        while (choice != 12) {
            System.out.println("\n=====================================");
            System.out.println("|            Manager menu           |");
            System.out.println("\n=====================================");
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

    // private static void SetFilterMenu() {
    //     int choice = -1;
    //     while (choice != 8) {
    //         System.out.println("\n=========================================");
    //         System.out.println("|               Filters                 |");
    //         System.out.println("=========================================");
    //         System.out.println("|1. Filter by Alphabetic Order (Default)|");
    //         System.out.println("|2. Filter by Age                       |");
    //         System.out.println("|3. Filter by Flat Type Availability    |");
    //         System.out.println("|4. Filter by Location                  |");
    //         System.out.println("|5. Filter by Marital Status            |");
    //         System.out.println("|6. Filter by Selling Price             |");
    //         System.out.println("|7. Filter by Visibility                |");
    //         System.out.println("|8. Exit                                |");
    //         System.out.println("=========================================");
    //         System.out.print("Enter your choice of filter: ");
    //         try {
    //             choice = sc.nextInt();
    //         } catch (final InputMismatchException e) {
    //             System.out.println("\nError: Invalid character");
    //             sc.nextLine(); // ignore and move the cursor to next line
    //             continue;
    //         }

    //         sc.nextLine();
    //         switch (choice) {
    //             case 1: 
    //                 String starting_char = "nil";
    //                 while (!Pattern.matches("[a-zA-Z]+",starting_char) || starting_char.length() != 1) {
    //                     System.out.println("\nEnter an alphabetic character for which all filtered project's name must start with(enter nil if null): ");
    //                     starting_char = sc.nextLine();
    //                     if (starting_char.equals("nil")) { break; }
    //                     if (!Pattern.matches("[a-zA-Z]+",starting_char) || starting_char.length() != 1) {
    //                         System.out.println("\nError: invalid input");
    //                     }
    //                 }
    //                 int order = -1;
    //                 while (order != 1 && order != 2) {
    //                     order = GetIntInput("\nWould you like it in ASCENDING(1) order or in DESCENDING(2) order? : ");
    //                 }
    //                 if (starting_char.equals("nil")) {starting_char = null;}
    //                 if (order == 1) { filter = new Filter_Alphabetic(starting_char,orderBy.ASCENDING); }
    //                 else if (order == 2) { filter = new Filter_Alphabetic(starting_char,orderBy.DESCENDING); }
    //                 return;

    //             case 2: 
    //                 double minAge = -99;
    //                 double maxAge = -99;
    //                 while (true) {
    //                     minAge = GetDoubleInput("\nEnter min Age in years of filtered projects (enter -1 if you would like no min age): ");
    //                     if (minAge == -1 || minAge > 0) { break; }
    //                     if (minAge <= 0) { System.out.println("\nError: Invalid input"); }
    //                 }
    //                 while (true) {
    //                     maxAge = GetDoubleInput("\nEnter max Age in years of filtered projects (enter -1 if you would like no max age): ");
    //                     if (maxAge == -1 || (maxAge > 0 && maxAge >=  minAge)) { break; }
    //                     if (maxAge <= 0 || maxAge < minAge) { System.out.println("\nError: Invalid input"); }
    //                 }
    //                 order = -1;
    //                 while (order != 1 && order != 2) {
    //                     order = GetIntInput("\nWould you like it in ASCENDING(1) order or in DESCENDING(2) order? : ");
    //                 }
    //                 if (order == 1) { filter = new Filter_Age(minAge,maxAge,orderBy.ASCENDING); }
    //                 else if (order == 2) { filter = new Filter_Age(minAge,maxAge,orderBy.DESCENDING); }
    //                 return;

    //             case 3: 
    //                 int flat_type_choice = -1;
    //                 while (flat_type_choice < 1 || flat_type_choice > 3) {
    //                     System.out.println("\nChoose which flat type to filter by (BOTH if you want to see availability of both flat types): ");
    //                     System.out.println("1. TWO_ROOM\n2. THREE_ROOM\n3. BOTH");
    //                     flat_type_choice = GetIntInput("Enter your choice: ");
    //                 }
    //                 order = -1;
    //                 while (order != 1 && order != 2) {
    //                     order = GetIntInput("\nWould you like it in ASCENDING(1) order or in DESCENDING(2) order? : ");
    //                 }
    //                 Enum_FlatType flatType = Enum_FlatType.DEFAULT;
    //                 if (flat_type_choice == 1) {flatType = Enum_FlatType.TWO_ROOM; } 
    //                 else if (flat_type_choice == 2) {flatType = Enum_FlatType.THREE_ROOM; } 
    //                 if (order == 1) { filter = new Filter_FlatType(flatType,orderBy.ASCENDING); }
    //                 else if (order == 2) { filter = new Filter_FlatType(flatType,orderBy.DESCENDING); }
    //                 return;

    //             case 4: 
    //                 String location = null;
    //                 boolean valid_location = false;
    //                 while (!valid_location) {
    //                         System.out.print("\nEnter a Singaporean location/neighbourhood of project to filter from: ");
    //                         location = sc.nextLine();
    //                         for (Location l : Location.values()) {
    //                             if (l.toString().equals(location.toUpperCase().replace(" ","_"))) {
    //                                 valid_location = true;
    //                             }
    //                         }
    //                         if (!valid_location) { System.out.println("\nError: location is not valid"); }
    //                     }
    //                 filter = new Filter_Location(Location.valueOf(location.toUpperCase().replace(" ","_")));
    //                 return;

    //             case 5: 
    //                 int marital_choice = -1;
    //                 while (marital_choice < 1 || marital_choice > 2) {
    //                     System.out.println("\nChoose which marital status to filter projects that are open to that group: ");
    //                     System.out.println("1. SINGLE\n2. MARRIED");
    //                     marital_choice = GetIntInput("Enter your choice: ");
    //                 }
    //                 if (marital_choice == 1) { filter = new Filter_Marital(MaritalStatus.SINGLE); }
    //                 else if (marital_choice == 2) { filter = new Filter_Marital(MaritalStatus.MARRIED); }
    //                 return;

    //             case 6: 
    //                 int minPrice = -99;
    //                 int maxPrice = -99;
    //                 while (true) {
    //                     minPrice = GetIntInput2("\nEnter min price of units in filtered projects (enter -1 if you would like no min price): ");
    //                     if (minPrice == -1 || minPrice > 0) { break; }
    //                     if (minPrice <= 0) { System.out.println("\nError: Invalid input"); }
    //                 }
    //                 while (true) {
    //                     maxPrice = GetIntInput2("\nEnter max price of units in filtered projects (enter -1 if you would like no max price): ");
    //                     if (maxPrice == -1 || (maxPrice > 0 && maxPrice >=  minPrice)) { break; }
    //                     if (maxPrice <= 0 || maxPrice < minPrice) { System.out.println("\nError: Invalid input"); }
    //                 }
    //                 flat_type_choice = -1;
    //                 while (flat_type_choice < 1 || flat_type_choice > 3) {
    //                     System.out.println("\nChoose which flat type to filter by (BOTH if you want to see combined prices of both flat types): ");
    //                     System.out.println("1. TWO_ROOM\n2. THREE_ROOM\n3. BOTH");
    //                     flat_type_choice = GetIntInput("Enter your choice: ");
    //                 }
    //                 order = -1;
    //                 while (order != 1 && order != 2) {
    //                     order = GetIntInput("\nWould you like it in ASCENDING(1) order or in DESCENDING(2) order? : ");
    //                 }
    //                 flatType = Enum_FlatType.DEFAULT;
    //                 if (flat_type_choice == 1) {flatType = Enum_FlatType.TWO_ROOM; } 
    //                 else if (flat_type_choice == 2) {flatType = Enum_FlatType.THREE_ROOM; } 
    //                 if (order == 1) { filter = new Filter_SellingPrice(minPrice,maxPrice,orderBy.ASCENDING,flatType); }
    //                 else if (order == 2) { filter = new Filter_SellingPrice(minPrice,maxPrice,orderBy.DESCENDING,flatType); }
    //                 return;

    //             case 7: 
    //                     filter = new Filter_Visibility();
    //                     return;
                
    //             case 8:
    //                 return;

    //             default:
    //                 System.out.println("\nError: Invalid number");
    //                 break;

    //         }
    //     }
    // }

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
        //ReportGenerator.getInstance
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
