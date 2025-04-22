package UserView;

import Filter.*;
import InteractableAttributePackage.ProjectDetails.Location;
import User.HDB_Manager;
import User.Applicant.MaritalStatus;
import User.Enum_FlatType;
import Filter.IFilter.orderBy;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.regex.Pattern;

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
        while (choice != 15) {
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
            System.out.println("| 10. View Enquiries                 |");
            System.out.println("| 11. Handle Enquiries               |");
            System.out.println("| 12. Generate Report on Project     |");
            System.out.println("| 13. Set Filters                    |");
            System.out.println("| 14. Change Password                |");
            System.out.println("| 15. Log out                        |");
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
                case 3 -> DeleteProject();
                case 4 -> ToggleProject();
                case 5 -> user.ViewActiveProject();
                case 6 -> user.ViewAllProjects(activeFilters);
                case 7 -> ManageOfficerApplications();
                case 8 -> ManageApplicantApplications();
                case 9 -> ManageWithdrawals();
                case 10 -> user.ViewAllEnquiries();
                case 11 -> ManageEnquiries();
                case 12 -> MakeReport();
                case 13 -> SetFilterMenu();
                case 14 -> {
                    String new_password = GetStringInput("Please enter your new password: ");
                    if (authenticator.changePassword( user.userID, user.password, new_password)) { return; }
                }
                case 15 -> { System.out.println("Logging out"); return; }
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

    private static void DeleteProject()
    { 
        System.out.print("Enter name of project to delete: ");
        user.DeleteBTOProject(sc.nextLine());
    }

    private static void ToggleProject()
    { 
        System.out.print("Enter name of created project to toggle visibility: ");                
        user.ToggleProjectVisibility(sc.nextLine());
    }

    private static void ManageOfficerApplications()
    { 
        user.ViewOfficerApplications();
        int index = GetIntInput("\nPlease enter the index of the officer application you wish to handle: ");
        int approve = -1;
        while (approve != 0 && approve != 1) {
            approve = GetIntInput("Would you like to approve(1) or reject(0) it ?: ");
        }
        user.HandleOfficerApplications(index, (approve == 1));
    }

    private static void ManageApplicantApplications()
    { 
        user.ViewApplicantApplications();
        int index = GetIntInput("\nPlease enter the index of the applicant application you wish to handle: ");
        int approve = -1;
        while (approve != 0 && approve != 1) {
            approve = GetIntInput("Would you like to approve(1) or reject(0) it ?: ");
        }
        user.HandleApplicantApplications(index, (approve == 1));
    }

    private static void ManageWithdrawals()
    { 
        user.ViewWithdrawals();
        int index = GetIntInput("\nPlease enter the index of the withdrawal you wish to handle: ");
        int approve = -1;
        while (approve != 0 && approve != 1) {
            approve = GetIntInput("Would you like to approve(1) or reject(0) it ?: ");
        }
        user.HandleWithdrawals(index, (approve == 1));
    }

    private static void ManageEnquiries()
    {
        user.ViewHandledEnquiries();
        int index = GetIntInput("\nPlease enter the index of the withdrawal you wish to handle: ");
        sc.nextLine();
        String reply = GetStringInput("Please enter your reply: ");
        user.HandleEnquiries(index, reply);
    }

    private static void MakeReport()
    {
        String project_name = GetStringInput("\nEnter the name of your project you would like to make a report about: ");
        user.GenerateReport(project_name, activeFilters);
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
                        minPrice = GetIntInputAlt("\nEnter min price of units in filtered projects (enter -1 if you would like no min price): ");
                        if (minPrice == -1 || minPrice > 0) { break; }
                        if (minPrice <= 0) { System.out.println("\nError: Invalid input"); }
                    }
                    while (true) {
                        maxPrice = GetIntInputAlt("\nEnter max price of units in filtered projects (enter -1 if you would like no max price): ");
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
}
