package UserView;

import Filter.*;
import InteractableAttributePackage.ProjectDetails;
import InteractableAttributePackage.ProjectDetails.Location;
import Service.ReportGenerator;
import User.Applicant;
import User.HDB_Manager;
import User.Applicant.MaritalStatus;
import User.Enum_FlatType;
import Filter.IFilter.orderBy;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.regex.Pattern;
import java.util.Objects;

public class ManagerMenu extends Menu
{
    private static HDB_Manager user;
    private static ArrayList<IFilter> activeFilters = new ArrayList<>(); 
    //default filter is in alphabetic ascending order (executed only once)
    static { activeFilters.add(new Filter_Alphabetic(null, orderBy.ASCENDING)); }

    public static void start() { Display(); }
    public static void SetUser(HDB_Manager u) { user = u; }
    private static void Display()
    {
        System.out.println("\nWelcome " + user.name + " !\nWhat would you like to do today?");
        int choice = -1;
        while (choice != 14) {
            System.out.println("\n======================================");
            System.out.println("|            Manager menu            |");
            System.out.println("======================================");
            System.out.println("| 1.  Create Project Listing         |");
            System.out.println("| 2.  Edit Project Listing           |");
            System.out.println("| 3.  Delete Project Listing         |");
            System.out.println("| 4.  Toggle Project Visibility      |");
            System.out.println("| 5.  View Current Project Details   |");
            System.out.println("| 6.  View Projects                  |");

            System.out.println("| 7.  Handle Officer Applications    |");
            System.out.println("| 8.  Handle Applicant Applications  |");
            System.out.println("| 9.  Handle Withdrawals             |");

            System.out.println("| 10. Generate Report                |");

            System.out.println("| 11. View Enquiries                 |");
            System.out.println("| 12. Handle Enquiries               |");

            System.out.println("| 13. Set Filters                    |");

            System.out.println("| 14. Log out                        |");
            System.out.println("======================================");
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
                case 1 -> MakeProject();
                case 2 -> EditProject();
                case 3 -> { 
                    System.out.print("Enter name of project to delete: ");
                    user.DeleteBTOProject(sc.nextLine());
                }
                case 4 -> {
                    System.out.print("Enter name of created project to toggle visibility: ");                
                    user.ToggleProjectVisibility(sc.nextLine());
                }
                case 5 -> user.ViewActiveProject();
                case 6 -> user.ViewAllProjects(activeFilters);

                case 7 -> HandleApplicantRequest();
                case 8 -> MakeReport();
                case 9 -> ViewEnquiries();
                case 10 -> ReplyEnquiry();
                case 11 -> ViewHandledRequests();
                case 12 -> HandleWithdrawal();
                case 13 -> SetFilterMenu();
                case 14 -> { System.out.println("Logging out"); return; }
                default -> System.out.println("Invalid number");
            }
        }
    }

    private static void MakeProject()
    {
        String name = GetStringInput("Enter new Project's name: ");
        String neighbourhood = GetStringInput("Enter new Project's neighbourhood: ");
        int selling2 = GetIntInput("Enter 2-room selling price: ");
        int avail2 = GetIntInput("Enter new Project's number of available 2-room units: ");
        int selling3 = GetIntInput("Enter new Project's 3-room selling price: ");
        int avail3 = GetIntInput("Enter new Project's number of available 3-room units: ");
        sc.nextLine();
        String openDate = GetStringInput("Enter new Project's opening date (dd/mm/YYYY): ");
        String closeDate = GetStringInput("Enter new Project's closing date (dd/mm/YYYY): ");
        int offrSlots = GetIntInput("Enter new Project's number of officer slots: ");
        String vis = "nil";
        sc.nextLine();
        while (!vis.toUpperCase().equals("Y") && !vis.toUpperCase().equals("N")) {
        vis = GetStringInput("Make new Project visible? (Y/N): "); }
        int group = GetIntInput("Enter new Project's user group which is open to (1 for single, 2 for married, 3 for all): ");

        user.CreateBTOProject(name, neighbourhood, selling2, selling3, avail2, avail3, openDate, closeDate, offrSlots, 
        (vis.equals("Y")), (group == 1 ? "SINGLE" : group == 2 ? "MARRIED" : group == 3 ? "ALL" : null));
    }

    private static void EditProject()
    {
        String name = GetStringInput("Enter Project to edit's name: ");
        String neighbourhood = GetStringInput("Enter new Project's neighbourhood: ");
        int selling2 = GetIntInput("Enter new 2-room selling price: ");
        int avail2 = GetIntInput("Enter new Project's number of available 2-room units: ");
        int selling3 = GetIntInput("Enter new Project's 3-room selling price: ");
        int avail3 = GetIntInput("Enter new Project's number of available 3-room units: ");
        sc.nextLine();
        String openDate = GetStringInput("Enter new Project's opening date (dd/mm/YYYY): ");
        String closeDate = GetStringInput("Enter new Project's closing date (dd/mm/YYYY): ");
        int offrSlots = GetIntInput("Enter new Project's number of officer slots: ");
        int group = GetIntInput("Enter new Project's user group which is open to (1 for single, 2 for married, 3 for all): ");

        user.EditBTOProject(name, neighbourhood, selling2, selling3, avail2, avail3, openDate, closeDate, offrSlots, 
        (group == 1 ? "SINGLE" : group == 2 ? "MARRIED" : group == 3 ? "ALL" : null));
    }

    private static void SetFilterMenu() {
        int choice = -1;
        while (choice != 9) {
            System.out.println("\n=========================================");
            System.out.println("|               Filters                 |");
            System.out.println("=========================================");
            System.out.println("|1. Add Filter by Alphabetic Order      |");
            System.out.println("|2. Add Filter by Age                   |");
            System.out.println("|3. Add Filter by Flat Type Availability|");
            System.out.println("|4. Add Filter by Location              |");
            System.out.println("|5. Add Filter by Marital Status        |");
            System.out.println("|6. Add Filter by Selling Price         |");
            System.out.println("|7. Add Filter by Visibility            |");
            System.out.println("|8. Remove Active Filters               |");
            System.out.println("|9. Exit                                |");
            System.out.println("=========================================");
            System.out.print("Enter your choice of filter: ");
            try {
                choice = sc.nextInt();
            } catch (final InputMismatchException e) {
                System.out.println("\nError: Invalid character");
                sc.nextLine(); // ignore and move the cursor to next line
                continue;
            }

            sc.nextLine();
            switch (choice) {
                case 1: 
                    activeFilters.removeIf(f -> f instanceof Filter_Alphabetic);
                    String starting_char = "nil";
                    while (!Pattern.matches("[a-zA-Z]+",starting_char) || starting_char.length() != 1) {
                        System.out.println("\nEnter an alphabetic character for which all filtered project's name must start with(enter nil if null): ");
                        starting_char = sc.nextLine();
                        if (starting_char.equals("nil")) { break; }
                        if (!Pattern.matches("[a-zA-Z]+",starting_char) || starting_char.length() != 1) {
                            System.out.println("\nError: invalid input");
                        }
                    }
                    int order = -1;
                    while (order != 1 && order != 2) {
                        order = GetIntInput("\nWould you like it in ASCENDING(1) order or in DESCENDING(2) order? : ");
                    }
                    if (starting_char.equals("nil")) {starting_char = null;}
                    if (order == 1) { activeFilters.add(new Filter_Alphabetic(starting_char,orderBy.ASCENDING)); }
                    else if (order == 2) { activeFilters.add(new Filter_Alphabetic(starting_char,orderBy.DESCENDING)); }
                    break;

                case 2: 
                    activeFilters.removeIf(f -> f instanceof Filter_Age);
                    double minAge = -99;
                    double maxAge = -99;
                    while (true) {
                        minAge = GetDoubleInput("\nEnter min Age in years of filtered projects (enter -1 if you would like no min age): ");
                        if (minAge == -1 || minAge > 0) { break; }
                        if (minAge <= 0) { System.out.println("\nError: Invalid input"); }
                    }
                    while (true) {
                        maxAge = GetDoubleInput("\nEnter max Age in years of filtered projects (enter -1 if you would like no max age): ");
                        if (maxAge == -1 || (maxAge > 0 && maxAge >=  minAge)) { break; }
                        if (maxAge <= 0 || maxAge < minAge) { System.out.println("\nError: Invalid input"); }
                    }
                    order = -1;
                    while (order != 1 && order != 2) {
                        order = GetIntInput("\nWould you like it in ASCENDING(1) order or in DESCENDING(2) order? : ");
                    }
                    if (order == 1) { activeFilters.add(new Filter_Age(minAge,maxAge,orderBy.ASCENDING)); }
                    else if (order == 2) { activeFilters.add(new Filter_Age(minAge,maxAge,orderBy.DESCENDING)); }
                    break;

                case 3: 
                    activeFilters.removeIf(f -> f instanceof Filter_FlatType);
                    int flat_type_choice = -1;
                    while (flat_type_choice < 1 || flat_type_choice > 3) {
                        System.out.println("\nChoose which flat type to filter by (BOTH if you want to see availability of both flat types): ");
                        System.out.println("1. TWO_ROOM\n2. THREE_ROOM\n3. BOTH");
                        flat_type_choice = GetIntInput("Enter your choice: ");
                    }
                    order = -1;
                    while (order != 1 && order != 2) {
                        order = GetIntInput("\nWould you like it in ASCENDING(1) order or in DESCENDING(2) order? : ");
                    }
                    Enum_FlatType flatType = Enum_FlatType.DEFAULT;
                    if (flat_type_choice == 1) {flatType = Enum_FlatType.TWO_ROOM; } 
                    else if (flat_type_choice == 2) {flatType = Enum_FlatType.THREE_ROOM; } 
                    if (order == 1) { activeFilters.add( new Filter_FlatType(flatType,orderBy.ASCENDING)); }
                    else if (order == 2) { activeFilters.add(new Filter_FlatType(flatType,orderBy.DESCENDING)); }
                    break;

                case 4: 
                    activeFilters.removeIf(f -> f instanceof Filter_Location);
                    String location = null;
                    boolean valid_location = false;
                    while (!valid_location) {
                            System.out.print("\nEnter a Singaporean location/neighbourhood of project to filter from: ");
                            location = sc.nextLine();
                            for (Location l : Location.values()) {
                                if (l.toString().equals(location.toUpperCase().replace(" ","_"))) {
                                    valid_location = true;
                                }
                            }
                            if (!valid_location) { System.out.println("\nError: location is not valid"); }
                        }
                    activeFilters.add(new Filter_Location(Location.valueOf(location.toUpperCase().replace(" ","_"))));
                    break;

                case 5: 
                    activeFilters.removeIf(f -> f instanceof Filter_Marital);
                    int marital_choice = -1;
                    while (marital_choice < 1 || marital_choice > 2) {
                        System.out.println("\nChoose which marital status to filter projects that are open to that group: ");
                        System.out.println("1. SINGLE\n2. MARRIED");
                        marital_choice = GetIntInput("Enter your choice: ");
                    }
                    if (marital_choice == 1) { activeFilters.add(new Filter_Marital(MaritalStatus.SINGLE)); }
                    else if (marital_choice == 2) { activeFilters.add(new Filter_Marital(MaritalStatus.MARRIED)); }
                    break;

                case 6: 
                    activeFilters.removeIf(f -> f instanceof Filter_SellingPrice);
                    int minPrice = -99;
                    int maxPrice = -99;
                    while (true) {
                        minPrice = GetIntInput2("\nEnter min price of units in filtered projects (enter -1 if you would like no min price): ");
                        if (minPrice == -1 || minPrice > 0) { break; }
                        if (minPrice <= 0) { System.out.println("\nError: Invalid input"); }
                    }
                    while (true) {
                        maxPrice = GetIntInput2("\nEnter max price of units in filtered projects (enter -1 if you would like no max price): ");
                        if (maxPrice == -1 || (maxPrice > 0 && maxPrice >=  minPrice)) { break; }
                        if (maxPrice <= 0 || maxPrice < minPrice) { System.out.println("\nError: Invalid input"); }
                    }
                    flat_type_choice = -1;
                    while (flat_type_choice < 1 || flat_type_choice > 3) {
                        System.out.println("\nChoose which flat type to filter by (BOTH if you want to see combined prices of both flat types): ");
                        System.out.println("1. TWO_ROOM\n2. THREE_ROOM\n3. BOTH");
                        flat_type_choice = GetIntInput("Enter your choice: ");
                    }
                    order = -1;
                    while (order != 1 && order != 2) {
                        order = GetIntInput("\nWould you like it in ASCENDING(1) order or in DESCENDING(2) order? : ");
                    }
                    flatType = Enum_FlatType.DEFAULT;
                    if (flat_type_choice == 1) {flatType = Enum_FlatType.TWO_ROOM; } 
                    else if (flat_type_choice == 2) {flatType = Enum_FlatType.THREE_ROOM; } 
                    if (order == 1) {activeFilters.add(new Filter_SellingPrice(minPrice,maxPrice,orderBy.ASCENDING,flatType)); }
                    else if (order == 2) {activeFilters.add(new Filter_SellingPrice(minPrice,maxPrice,orderBy.DESCENDING,flatType)); }
                    break;

                case 7: 
                    activeFilters.removeIf(f -> f instanceof Filter_Visibility);
                    activeFilters.add(new Filter_Visibility());
                    break;

                case 8:
                    if (activeFilters.size() == 0) {System.out.println("\nError: No Filters currently active."); break; }
                    choice = -1;
                    int index = 0;
                    System.out.println("\nAll current filters active: ");
                    for (IFilter f : activeFilters) {
                        System.out.println(index +". "+f.getClass().getSimpleName());
                        index++;
                    }
                    while (choice < 0 || choice > activeFilters.size()-1) 
                    {
                        choice = GetIntInput("Enter the index of the filter you wish to remove: ");
                    }
                    activeFilters.remove(choice);
                    break;
                
                case 9:
                    return;

                default:
                    System.out.println("\nError: Invalid number");
                    break;

            }
        }
    }
    
    private static void ViewOtherProject()
    {}

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
                filterType = new Filter_FlatType(Enum_FlatType.DEFAULT, IFilter.orderBy.ASCENDING);
                break;

            case 4:
                filterType = new Filter_Location(ProjectDetails.Location.ANG_MO_KIO);
                break;

            case 5:
                filterType = new Filter_Marital(Applicant.MaritalStatus.ALL);
                break;

            case 6:
                filterType = new Filter_SellingPrice(-1, -1, IFilter.orderBy.ASCENDING, Enum_FlatType.DEFAULT);
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
