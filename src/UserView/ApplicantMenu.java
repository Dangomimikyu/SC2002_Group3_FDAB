package UserView;

import Filter.*;
import User.Applicant;
import User.Enum_FlatType;
import InteractableAttributePackage.ProjectDetails.Location;
import Filter.IFilter.orderBy;
import java.util.InputMismatchException;
import java.util.regex.Pattern;

public class ApplicantMenu extends Menu
{
    private static Applicant user;
    //default filter is in alphabetic ascending order
    private static IFilter filter = new Filter_Alphabetic(null, orderBy.ASCENDING);

    public static void start() { Display();}
    public static void SetUser(Applicant a) { user = a; }

    private static void Display()
    {
        System.out.println("\nWelcome " + user.name + " !\nWhat would you like to do today?");
        int choice = -1;
        while (choice != 12) {
            System.out.println("\n=====================================");
            System.out.println("|            Applicant menu           |");
            System.out.println("=======================================");
            System.out.println("|  1. View available projects         |");
            System.out.println("|  2. Apply for a project             |");
            System.out.println("|  3. Check Applied Project Details   |");
            System.out.println("|  4. Create an enquiry               |");
            System.out.println("|  5. View your enquiries             |");
            System.out.println("|  6. Edit your enquiries             |");
            System.out.println("|  7. Delete your enquiries           |");
            System.out.println("|  8. Book a flat                     |");
            System.out.println("|  9. Withdraw from project           |");
            System.out.println("|  10. Choose Filter                  |");
            System.out.println("|  11. Check Request Status           |");
            System.out.println("|  12. Log out                        |");
            System.out.println("=======================================");
            System.out.print("Enter choice: ");
            try
            {
                choice = sc.nextInt();
            }
            catch (final InputMismatchException e)
            {
                System.out.println("\nError: Invalid character");
                sc.nextLine();
                continue;
            }

            sc.nextLine();
            switch (choice)
            {
                case 1 -> user.viewProjectList(filter);
                case 2 -> {
                    System.out.print("Enter name of project to apply for: ");
                    user.ApplyProject(sc.nextLine());
                }
                case 3 -> user.viewAppliedProjectStatus();
                case 4 -> user.CreateEnquiry();
                case 5 -> user.ViewEnquiry();
                case 6 -> user.EditEnquiry();
                case 7 -> user.DeleteEnquiry();
                case 8 -> user.BookFlat();
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
                case 12 -> { System.out.println("Logging out"); return; }
                default -> System.out.println("\nError: Invalid number");

            }
        }
    }

    private static void SetFilterMenu() {
        int choice = -1;
        while (choice != 6) {
            System.out.println("\n=========================================");
            System.out.println("|               Filters                 |");
            System.out.println("=========================================");
            System.out.println("|1. Filter by Alphabetic Order (Default)|");
            System.out.println("|2. Filter by Age                       |");
            System.out.println("|3. Filter by Flat Type Availability    |");
            System.out.println("|4. Filter by Location                  |");
            System.out.println("|5. Filter by Selling Price             |");
            System.out.println("|6. Exit                                |");
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
                    if (order == 1) { filter = new Filter_Alphabetic(starting_char,orderBy.ASCENDING); }
                    else if (order == 2) { filter = new Filter_Alphabetic(starting_char,orderBy.DESCENDING); }
                    return;

                case 2: 
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
                    if (order == 1) { filter = new Filter_Age(minAge,maxAge,orderBy.ASCENDING); }
                    else if (order == 2) { filter = new Filter_Age(minAge,maxAge,orderBy.DESCENDING); }
                    return;

                case 3: 
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
                    if (order == 1) { filter = new Filter_FlatType(flatType,orderBy.ASCENDING); }
                    else if (order == 2) { filter = new Filter_FlatType(flatType,orderBy.DESCENDING); }
                    return;

                case 4: 
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
                    filter = new Filter_Location(Location.valueOf(location.toUpperCase().replace(" ","_")));
                    return;

                case 5: 
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
                    if (order == 1) { filter = new Filter_SellingPrice(minPrice,maxPrice,orderBy.ASCENDING,flatType); }
                    else if (order == 2) { filter = new Filter_SellingPrice(minPrice,maxPrice,orderBy.DESCENDING,flatType); }
                    return;

                case 6:
                    return;

                default:
                    System.out.println("\nError: Invalid number");
                    break;

            }
        }
    }
}
