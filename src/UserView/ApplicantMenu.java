package UserView;

import Filter.*;
import User.Applicant;
import User.Applicant.MaritalStatus;
import User.Enum_FlatType;
import User.HDB_Officer;
import InteractableAttributePackage.ProjectDetails.Location;
import Filter.IFilter.orderBy;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.regex.Pattern;

public class ApplicantMenu extends Menu
{
    private static Applicant user;
    private static ArrayList<IFilter> activeFilters = new ArrayList<>(); 
    //default filter is in alphabetic ascending order (executed only once)
    static { activeFilters.add(new Filter_Alphabetic(null, orderBy.ASCENDING)); }

    public static void start() { Display();}
    public static void SetUser(Applicant a) { user = a; }

    private static void Display()
    {
        System.out.println("\nWelcome " + user.name + " !\nWhat would you like to do today?");
        int choice = -1;
        while (choice != 13) {
            System.out.println("\n=======================================");
            System.out.println("|            Applicant menu           |");
            System.out.println("=======================================");
            System.out.println("|  1.  View available projects        |");
            System.out.println("|  2.  Apply for a project            |");
            System.out.println("|  3.  Check Applied Project Details  |");
            System.out.println("|  4.  Create an enquiry              |");
            System.out.println("|  5.  View your enquiries            |");
            System.out.println("|  6.  Edit your enquiries            |");
            System.out.println("|  7.  Delete your enquiries          |");
            System.out.println("|  8.  Book a flat                    |");
            System.out.println("|  9.  Withdraw from project          |");
            System.out.println("|  10. Choose Filter                  |");
            System.out.println("|  11. Check Request Status           |");
            System.out.println("|  12. Change Password                |");
            System.out.println(user instanceof HDB_Officer ? 
                                 "|  13. Return to Officer Menu         |" : 
                                 "|  13. Log out                        |");
            System.out.println("=======================================");
            System.out.print("Enter choice: ");
            try
            {
                choice = sc.nextInt();
            }
            catch (final InputMismatchException e)
            {
                System.out.println("\nError: Invalid character");
                continue;
            }
            finally { sc.nextLine(); }

            switch (choice)
            {
                case 1 -> user.viewProjectList(activeFilters);
                case 2 -> {
                    System.out.print("Enter name of project to apply for: ");
                    user.ApplyProject(sc.nextLine());
                }
                case 3 -> user.viewAppliedProjectStatus();
                case 4 -> {
                    String title = GetStringInput("Enter the title of the enquiry: ");
                    String projectName = GetStringInput("Enter the name of the project you are referring to: ");
                    String description = GetStringInput("Enter the description of the enquiry: ");;
                    user.CreateEnquiry(title,projectName,description);
                }
                case 5 -> user.ViewEnquiry();
                case 6 -> {
                    String title = GetStringInput("Enter the title of the enquiry you want to edit: ");
                    String projectName = GetStringInput("Enter the project name your enquiry to delete is regarding: ");
                    String description = GetStringInput("Enter the new description: ");
                    user.EditEnquiry(title,projectName,description);
                }
                case 7 -> {
                    String title = GetStringInput("Enter the title of the enquiry you want to delete: ");
                    String projectName = GetStringInput("Enter the project name your enquiry to delete is regarding: ");
                    user.DeleteEnquiry(title,projectName);
                }
                case 8 -> {
                    // Single, 35 y/o and above, can ONLY apply for 2-Room
                    // Married, 21 y/0 and above, can apply for both
                    if (user.maritalStatus == MaritalStatus.SINGLE && user.age >= 35) {
                        System.out.println("\nNote: you are eligible for only two-room flats");
                    }
                    else if (user.maritalStatus == MaritalStatus.MARRIED && user.age >= 21) {
                        System.out.println("\nNote: you are eligible for both two-room and three-room flats");
                    }
                    int flat_choice = -1;
                    while (flat_choice != 2 && choice != 1 && choice != 0) {
                        flat_choice = GetIntInput("Enter your flat choice to book (0 to decline for now, 1 for 2-room, 2 for 3-room): ");
                    }
                    if (flat_choice == 1) { user.BookFlat(Enum_FlatType.TWO_ROOM); }
                    else if (flat_choice == 2) { user.BookFlat(Enum_FlatType.THREE_ROOM); }
                }
                case 9 -> {
                    int yes_or_no = -1;
                    while (yes_or_no != 1 && yes_or_no != 2)  {
                        yes_or_no = GetIntInput("Are you sure you want to withdraw from your current project? (1-Yes) (2-No): ");
                    }
                    if (yes_or_no == 1) {
                        user.RequestWithdrawal();
                    }
                }
                case 10 -> SetFilterMenu();
                case 11 -> user.checkRequestStatus();
                case 12 -> {
                    if (!(user instanceof HDB_Officer)) {
                        String new_password = GetStringInput("Please enter your new password: ");
                        if (authenticator.changePassword( user.userID, user.password, new_password)) { return; }
                    }
                    else {System.out.println("\nTo change password, change it in officer menu (HOME MENU)"); }
                }
                case 13 -> { System.out.println("Logging out"); return; }
                default -> System.out.println("\nError: Invalid number");

            }
        }
    }

    private static void SetFilterMenu() {
        int choice = -1;
        while (choice != 7) {
            System.out.println("\n=========================================");
            System.out.println("|               Filters                 |");
            System.out.println("=========================================");
            System.out.println("|1. Add Filter by Alphabetic Order      |");
            System.out.println("|2. Add Filter by Age                   |");
            System.out.println("|3. Add Filter by Flat Type Availability|");
            System.out.println("|4. Add Filter by Location              |");
            System.out.println("|5. Add Filter by Selling Price         |");
            System.out.println("|6. Remove active filters               |");
            System.out.println("|7. Exit                                |");
            System.out.println("=========================================");
            System.out.print("Enter your choice of filter: ");
            try {
                choice = sc.nextInt();
            } catch (final InputMismatchException e) {
                System.out.println("\nError: Invalid character");
                continue;
            }
            finally { sc.nextLine(); }

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

                case 6:
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

                case 7:
                    return;

                default:
                    System.out.println("\nError: Invalid number");
                    break;

            }
        }
    }
}
