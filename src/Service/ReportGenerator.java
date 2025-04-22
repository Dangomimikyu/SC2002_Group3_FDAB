package Service;

import Database.UserInfoDB;
import Filter.*;
import User.Applicant;
import User.HDB_Manager;
import User.SystemUser;
import java.util.ArrayList;

public class ReportGenerator
{
    private static ReportGenerator instance = null;

    private ReportGenerator() {}

    public static ReportGenerator getInstance()
    {
        if (instance == null) { instance = new ReportGenerator(); }
        return instance;
    }

    public void GenerateReport(String project_name, ArrayList<IFilter> activeFilters) {


        activeFilters.removeIf(f -> f instanceof Filter_Location || f instanceof Filter_ProjectName ||
        f instanceof Filter_SellingPrice || f instanceof Filter_Visibility);

        ArrayList<SystemUser> applicant_list =  UserInfoDB.getInstance().GetDB(activeFilters);
        applicant_list.removeIf(u -> u instanceof HDB_Manager);
        applicant_list.removeIf(u -> !((Applicant)u).AppliedProject.equals(project_name));
        //Filter_Age - only include applicants with age range (in A or D order)
        //Filter_Alphabetic - filter applicants starting with name (in A or D order)
        //Filter_FlatType - filter applicants who are booked with flat type
        //Filter_Marital - filter applicants with marital status
        //All other filters - ignore

        System.out.println("\nREPORT ON "+project_name.toUpperCase()+"\n===============================================");
        for (SystemUser u : applicant_list ) {
            System.out.println("\n"+(u.getUserDetails()));
        }
    }

    // private boolean ValidateLocation(String loc)
    // {
    //     try
    //     {
    //         ProjectDetails.Location.valueOf(loc.toUpperCase());
    //         return true;
    //     }
    //     catch (IllegalArgumentException e)
    //     {
    //         return false;
    //     }
    // }
    
    // private boolean ValidateMarital(String mar)
    // {
    //     try
    //     {
    //         Applicant.MaritalStatus.valueOf(mar.toUpperCase());
    //         return true;
    //     }
    //     catch (IllegalArgumentException e)
    //     {
    //         return false;
    //     }
    // }

    // // see all projects, no filter
    // public void GenerateReport() {
    //     for (SystemUser s : UserInfoDB.getInstance().getUserDB()) {
    //         if (!(s instanceof Applicant)) {
    //             continue;
    //         }
    //         System.out.print("Applicant name: ");
    //         System.out.println(s.name);
    //         System.out.print("Flat type: ");
    //         System.out.println(((Applicant) s).flatTypeBooked.toString());
    //         System.out.print("Project name: ");
    //         System.out.println(((Applicant) s).AppliedProject);
    //         System.out.print("Age: ");
    //         System.out.println(((Applicant) s).age);
    //         System.out.print("Marital status: ");
    //         System.out.println(((Applicant) s).maritalStatus);
    //     }
    // }

    // // see specific project, no filter
    // public void GenerateReport(String projectName) {
    //     for (SystemUser s : UserInfoDB.getInstance().getUserDB()) {
    //         if (!(s instanceof Applicant)) {
    //             continue;
    //         }
    //         if (Objects.equals(((Applicant) s).AppliedProject, projectName)) {
    //             System.out.print("Applicant name: ");
    //             System.out.println(s.name);
    //             System.out.print("Flat type: ");
    //             System.out.println(((Applicant) s).flatTypeBooked.toString());
    //             System.out.print("Age: ");
    //             System.out.println(((Applicant) s).age);
    //             System.out.print("Marital status: ");
    //             System.out.println(((Applicant) s).maritalStatus);
    //         }
    //     }
    // }

    // // see specific project and apply filter
    // public void GenerateReport(String projectName, IFilter filter)
    // {
    //     ArrayList<SystemUser> filteredDB = UserInfoDB.getInstance().getUserDB();
    //     Scanner sc = new Scanner(System.in);

    //     switch (filter) {
    //         case Filter_Age filterAge -> {
    //             int minAge;
    //             int maxAge;

    //             System.out.print("Enter min age (inclusive): ");
    //             minAge = GetIntInput("Enter min age (inclusive): ", sc);

    //             System.out.print("Enter max age (inclusive): ");
    //             maxAge = GetIntInput("Enter max age (inclusive): ", sc);

    //             for (SystemUser s : UserInfoDB.getInstance().getUserDB()) {
    //                 if (!(s instanceof Applicant)) {
    //                     continue;
    //                 }
    //                 if (Objects.equals(((Applicant) s).AppliedProject, projectName) && (((Applicant) s).age > minAge) && (((Applicant) s).age < maxAge)) {
    //                     System.out.print("Applicant name: ");
    //                     System.out.println(s.name);
    //                     System.out.print("Age: ");
    //                     System.out.println(((Applicant) s).age);
    //                     System.out.print("Marital status: ");
    //                     System.out.println(((Applicant) s).maritalStatus);
    //                 }
    //             }
    //         }
    //         case Filter_Alphabetic filterAlphabetic -> {
    //             ArrayList<SystemUser> sortedUsers = UserInfoDB.getInstance().getUserDB();
    //             sortedUsers.removeIf(u -> !filter.FilterBy(u));
    //             if (filterAlphabetic.order == IFilter.orderBy.ASCENDING) {
    //                 sortedUsers.sort((u1, u2) -> u1.name.compareToIgnoreCase(u2.name));
    //             } else if (((Filter_Alphabetic) filter).order == IFilter.orderBy.DESCENDING) {
    //                 sortedUsers.sort((u1, u2) -> u2.name.compareToIgnoreCase(u1.name));
    //             }

    //             for (SystemUser s : sortedUsers) {
    //                 if (!(s instanceof Applicant)) {
    //                     continue;
    //                 }
    //                 if (Objects.equals(((Applicant) s).AppliedProject, projectName)) {
    //                     System.out.print("Applicant name: ");
    //                     System.out.println(s.name);
    //                     System.out.print("Flat type: ");
    //                     System.out.println(((Applicant) s).flatTypeBooked.toString());
    //                     System.out.print("Age: ");
    //                     System.out.println(((Applicant) s).age);
    //                     System.out.print("Marital status: ");
    //                     System.out.println(((Applicant) s).maritalStatus);
    //                 }
    //             }
    //         }
    //         case Filter_FlatType filterFlatType -> {
    //             System.out.print("Enter flat type to filter by (1 for all, 2 for 2-room, 3 for 3-room): ");
    //             int type = GetIntInput("Enter flat type to filter by (1 for all, 2 for 2-room, 3 for 3-room): ", sc);
    //             Enum_FlatType eType = null;

    //             // force the correct type of input
    //             while (type < 1 || type > 3) {
    //                 type = GetIntInput("Enter flat type to filter by (1 for all, 2 for 2-room, 3 for 3-room): ", sc);
    //             }

    //             switch (type) {
    //                 case 1 -> GenerateReport(projectName);
    //                 case 2 -> eType = Enum_FlatType.TWO_ROOM;
    //                 case 3 -> eType = Enum_FlatType.THREE_ROOM;
    //             }

    //             for (SystemUser s : UserInfoDB.getInstance().getUserDB()) {
    //                 if (!(s instanceof Applicant)) {
    //                     continue;
    //                 }
    //                 if (Objects.equals(((Applicant) s).AppliedProject, projectName) && ((Applicant) s).flatTypeBooked == eType) {
    //                     System.out.print("Applicant name: ");
    //                     System.out.println(s.name);
    //                     System.out.print("Flat type: ");
    //                     System.out.println(((Applicant) s).flatTypeBooked.toString());
    //                     System.out.print("Marital status: ");
    //                     System.out.println(((Applicant) s).maritalStatus);
    //                 }
    //             }
    //         }
    //         case Filter_Location filterLocation -> {
    //             System.out.print("Enter location you want: ");
    //             String loc = sc.nextLine();

    //             while (!ValidateLocation(loc)) {
    //                 System.out.print("Enter location you want: ");
    //                 loc = sc.nextLine();
    //             }
    //             loc = loc.toUpperCase();

    //             for (SystemUser s : UserInfoDB.getInstance().getUserDB()) {
    //                 if (!(s instanceof Applicant)) {
    //                     continue;
    //                 }
    //                 Project p = ProjectListingDB.getInstance().SearchDB(projectName);
    //                 if (Objects.equals(((Applicant) s).AppliedProject, projectName) && (Objects.equals(p.Details.Neighborhood.toString(), loc))) {
    //                     System.out.print("Applicant name: ");
    //                     System.out.println(s.name);
    //                     System.out.print("Flat type: ");
    //                     System.out.println(((Applicant) s).flatTypeBooked.toString());
    //                     System.out.print("Age: ");
    //                     System.out.println(((Applicant) s).age);
    //                     System.out.print("Marital status: ");
    //                     System.out.println(((Applicant) s).maritalStatus);
    //                 }
    //             }
    //         }
    //         case Filter_Marital filterMarital -> {
    //             System.out.print("Enter type of marital status: ");
    //             String mar = sc.nextLine();

    //             while (!ValidateLocation(mar)) {
    //                 System.out.print("Enter location you want: ");
    //                 mar = sc.nextLine();
    //             }
    //             mar = mar.toUpperCase();

    //             for (SystemUser s : UserInfoDB.getInstance().getUserDB()) {
    //                 if (!(s instanceof Applicant)) {
    //                     continue;
    //                 }
    //                 if (Objects.equals(((Applicant) s).AppliedProject, projectName) && (Objects.equals(((Applicant) s).maritalStatus.toString().toUpperCase(), mar))) {
    //                     System.out.print("Applicant name: ");
    //                     System.out.println(s.name);
    //                     System.out.print("Flat type: ");
    //                     System.out.println(((Applicant) s).flatTypeBooked.toString());
    //                     System.out.print("Age: ");
    //                     System.out.println(((Applicant) s).age);
    //                     System.out.print("Marital status: ");
    //                     System.out.println(((Applicant) s).maritalStatus);
    //                 }

    //             }
    //         }
    //         case Filter_SellingPrice filterSellingPrice -> {
    //             // this function is a huge pain
    //             int minPrice;
    //             int maxPrice;

    //             System.out.print("Enter min price (inclusive): ");
    //             minPrice = GetIntInput("Enter min price (inclusive): ", sc);
    //             System.out.print("Enter max price (inclusive): ");
    //             maxPrice = GetIntInput("Enter max price (inclusive): ", sc);

    //             for (SystemUser s : UserInfoDB.getInstance().getUserDB()) {
    //                 if (!(s instanceof Applicant)) {
    //                     continue;
    //                 }
    //                 Project p = ProjectListingDB.getInstance().SearchDB(projectName);

    //                 switch (((Applicant) s).flatTypeBooked) {
    //                     case Enum_FlatType.TWO_ROOM:
    //                         if (minPrice < p.Details.SellingPrice_2Room && p.Details.SellingPrice_2Room < maxPrice) {
    //                             System.out.print("Applicant name: ");
    //                             System.out.println(s.name);
    //                             System.out.print("Flat type: ");
    //                             System.out.println(((Applicant) s).flatTypeBooked.toString());
    //                             System.out.print("Age: ");
    //                             System.out.println(((Applicant) s).age);
    //                             System.out.print("Marital status: ");
    //                             System.out.println(((Applicant) s).maritalStatus);
    //                         }
    //                         break;

    //                     case Enum_FlatType.THREE_ROOM:
    //                         if (minPrice < p.Details.SellingPrice_3Room && p.Details.SellingPrice_3Room < maxPrice) {
    //                             System.out.print("Applicant name: ");
    //                             System.out.println(s.name);
    //                             System.out.print("Flat type: ");
    //                             System.out.println(((Applicant) s).flatTypeBooked.toString());
    //                             System.out.print("Age: ");
    //                             System.out.println(((Applicant) s).age);
    //                             System.out.print("Marital status: ");
    //                             System.out.println(((Applicant) s).maritalStatus);
    //                         }
    //                         break;
    //                 }
    //             }
    //         }
    //         case Filter_Visibility filterVisibility -> {
    //             ArrayList<Applicant> visible = new ArrayList<>();
    //             ArrayList<Applicant> notVisible = new ArrayList<>();

    //             for (SystemUser s : UserInfoDB.getInstance().getUserDB()) {
    //                 if (!(s instanceof Applicant)) {
    //                     continue;
    //                 }
    //                 Project p = ProjectListingDB.getInstance().SearchDB(projectName);
    //                 if (Objects.equals(((Applicant) s).AppliedProject, projectName) && p.Details.visibility) {
    //                     visible.add((Applicant) s);
    //                 } else {
    //                     notVisible.add((Applicant) s);
    //                 }
    //             }

    //             System.out.println("=====================");
    //             for (Applicant a : visible) {
    //                 System.out.print("Applicant name: ");
    //                 System.out.println(a.name);
    //                 System.out.print("Flat type: ");
    //                 System.out.println(a.flatTypeBooked.toString());
    //                 System.out.print("Age: ");
    //                 System.out.println(a.age);
    //                 System.out.print("Marital status: ");
    //                 System.out.println(a.maritalStatus);
    //             }

    //             System.out.println("=====================");

    //             for (Applicant a : notVisible) {
    //                 System.out.print("Applicant name: ");
    //                 System.out.println(a.name);
    //                 System.out.print("Flat type: ");
    //                 System.out.println(a.flatTypeBooked.toString());
    //                 System.out.print("Age: ");
    //                 System.out.println(a.age);
    //                 System.out.print("Marital status: ");
    //                 System.out.println(a.maritalStatus);
    //             }
    //             System.out.println("=====================");
    //         }
    //         case null, default -> GenerateReport(projectName);
    //     }
    // }

    // // see all projects but apply filter
    // public void GenerateReport(IFilter filter)
    // {
    //     ArrayList<SystemUser> filteredDB = UserInfoDB.getInstance().getUserDB();
    //     Scanner sc = new Scanner(System.in);

    //     switch (filter) {
    //         case Filter_Age filterAge -> {
    //             int minAge;
    //             int maxAge;

    //             System.out.print("Enter min age (inclusive): ");
    //             minAge = GetIntInput("Enter min age (inclusive): ", sc);

    //             System.out.print("Enter max age (inclusive): ");
    //             maxAge = GetIntInput("Enter max age (inclusive): ", sc);

    //             for (SystemUser s : UserInfoDB.getInstance().getUserDB()) {
    //                 if (!(s instanceof Applicant)) {
    //                     continue;
    //                 }
    //                 if ((((Applicant) s).age > minAge) && (((Applicant) s).age < maxAge)) {
    //                     System.out.print("Applicant name: ");
    //                     System.out.println(s.name);
    //                     System.out.print("Age: ");
    //                     System.out.println(((Applicant) s).age);
    //                     System.out.print("Marital status: ");
    //                     System.out.println(((Applicant) s).maritalStatus);
    //                 }
    //             }
    //         }
    //         case Filter_Alphabetic filterAlphabetic -> {
    //             ArrayList<SystemUser> sortedUsers = UserInfoDB.getInstance().getUserDB();
    //             sortedUsers.removeIf(u -> !filter.FilterBy(u));
    //             if (filterAlphabetic.order == IFilter.orderBy.ASCENDING) {
    //                 sortedUsers.sort((u1, u2) -> u1.name.compareToIgnoreCase(u2.name));
    //             } else if (((Filter_Alphabetic) filter).order == IFilter.orderBy.DESCENDING) {
    //                 sortedUsers.sort((u1, u2) -> u2.name.compareToIgnoreCase(u1.name));
    //             }

    //             for (SystemUser s : sortedUsers) {
    //                 if (!(s instanceof Applicant)) {
    //                     continue;
    //                 }
    //                 System.out.print("Applicant name: ");
    //                 System.out.println(s.name);
    //                 System.out.print("Flat type: ");
    //                 System.out.println(((Applicant) s).flatTypeBooked.toString());
    //                 System.out.print("Age: ");
    //                 System.out.println(((Applicant) s).age);
    //                 System.out.print("Marital status: ");
    //                 System.out.println(((Applicant) s).maritalStatus);
    //             }
    //         }
    //         case Filter_FlatType filterFlatType -> {
    //             System.out.print("Enter flat type to filter by (1 for all, 2 for 2-room, 3 for 3-room): ");
    //             int type = GetIntInput("Enter flat type to filter by (1 for all, 2 for 2-room, 3 for 3-room): ", sc);
    //             Enum_FlatType eType = null;

    //             // force the correct type of input
    //             while (type < 1 || type > 3) {
    //                 type = GetIntInput("Enter flat type to filter by (1 for all, 2 for 2-room, 3 for 3-room): ", sc);
    //             }

    //             switch (type) {
    //                 case 1 -> GenerateReport();
    //                 case 2 -> eType = Enum_FlatType.TWO_ROOM;
    //                 case 3 -> eType = Enum_FlatType.THREE_ROOM;
    //             }

    //             for (SystemUser s : UserInfoDB.getInstance().getUserDB()) {
    //                 if (!(s instanceof Applicant)) {
    //                     continue;
    //                 }
    //                 if (((Applicant) s).flatTypeBooked == eType) {
    //                     System.out.print("Applicant name: ");
    //                     System.out.println(s.name);
    //                     System.out.print("Flat type: ");
    //                     System.out.println(((Applicant) s).flatTypeBooked.toString());
    //                     System.out.print("Marital status: ");
    //                     System.out.println(((Applicant) s).maritalStatus);
    //                 }
    //             }
    //         }
    //         case Filter_Location filterLocation -> {
    //             System.out.print("Enter location you want: ");
    //             String loc = sc.nextLine();

    //             while (!ValidateLocation(loc)) {
    //                 System.out.print("Enter location you want: ");
    //                 loc = sc.nextLine();
    //             }
    //             loc = loc.toUpperCase();

    //             for (SystemUser s : UserInfoDB.getInstance().getUserDB()) {
    //                 if (!(s instanceof Applicant)) {
    //                     continue;
    //                 }
    //                 Project p = ProjectListingDB.getInstance().SearchDB(((Applicant) s).AppliedProject);
    //                 if (loc.equals(p.Details.Neighborhood.toString())) {
    //                     System.out.print("Applicant name: ");
    //                     System.out.println(s.name);
    //                     System.out.print("Flat type: ");
    //                     System.out.println(((Applicant) s).flatTypeBooked.toString());
    //                     System.out.print("Age: ");
    //                     System.out.println(((Applicant) s).age);
    //                     System.out.print("Marital status: ");
    //                     System.out.println(((Applicant) s).maritalStatus);
    //                 }
    //             }
    //         }
    //         case Filter_Marital filterMarital -> {
    //             System.out.print("Enter type of marital status: ");
    //             String mar = sc.nextLine();

    //             while (!ValidateLocation(mar)) {
    //                 System.out.print("Enter location you want: ");
    //                 mar = sc.nextLine();
    //             }
    //             mar = mar.toUpperCase();

    //             for (SystemUser s : UserInfoDB.getInstance().getUserDB()) {
    //                 if (!(s instanceof Applicant)) {
    //                     continue;
    //                 }
    //                 if ((Objects.equals(((Applicant) s).maritalStatus.toString().toUpperCase(), mar))) {
    //                     System.out.print("Applicant name: ");
    //                     System.out.println(s.name);
    //                     System.out.print("Flat type: ");
    //                     System.out.println(((Applicant) s).flatTypeBooked.toString());
    //                     System.out.print("Age: ");
    //                     System.out.println(((Applicant) s).age);
    //                     System.out.print("Marital status: ");
    //                     System.out.println(((Applicant) s).maritalStatus);
    //                 }

    //             }
    //         }
    //         case Filter_SellingPrice filterSellingPrice -> {
    //             // this function is a huge pain
    //             int minPrice;
    //             int maxPrice;

    //             System.out.print("Enter min price (inclusive): ");
    //             minPrice = GetIntInput("Enter min price (inclusive): ", sc);
    //             System.out.print("Enter max price (inclusive): ");
    //             maxPrice = GetIntInput("Enter max price (inclusive): ", sc);

    //             for (SystemUser s : UserInfoDB.getInstance().getUserDB()) {
    //                 if (!(s instanceof Applicant)) {
    //                     continue;
    //                 }
    //                 Project p = ProjectListingDB.getInstance().SearchDB(((Applicant) s).AppliedProject);

    //                 switch (((Applicant) s).flatTypeBooked) {
    //                     case Enum_FlatType.TWO_ROOM:
    //                         if (minPrice < p.Details.SellingPrice_2Room && p.Details.SellingPrice_2Room < maxPrice) {
    //                             System.out.print("Applicant name: ");
    //                             System.out.println(s.name);
    //                             System.out.print("Flat type: ");
    //                             System.out.println(((Applicant) s).flatTypeBooked.toString());
    //                             System.out.print("Age: ");
    //                             System.out.println(((Applicant) s).age);
    //                             System.out.print("Marital status: ");
    //                             System.out.println(((Applicant) s).maritalStatus);
    //                         }
    //                         break;

    //                     case Enum_FlatType.THREE_ROOM:
    //                         if (minPrice < p.Details.SellingPrice_3Room && p.Details.SellingPrice_3Room < maxPrice) {
    //                             System.out.print("Applicant name: ");
    //                             System.out.println(s.name);
    //                             System.out.print("Flat type: ");
    //                             System.out.println(((Applicant) s).flatTypeBooked.toString());
    //                             System.out.print("Age: ");
    //                             System.out.println(((Applicant) s).age);
    //                             System.out.print("Marital status: ");
    //                             System.out.println(((Applicant) s).maritalStatus);
    //                         }
    //                         break;
    //                 }
    //             }
    //         }
    //         case Filter_Visibility filterVisibility -> {
    //             ArrayList<Applicant> visible = new ArrayList<>();
    //             ArrayList<Applicant> notVisible = new ArrayList<>();

    //             for (SystemUser s : UserInfoDB.getInstance().getUserDB()) {
    //                 if (!(s instanceof Applicant)) {
    //                     continue;
    //                 }
    //                 Project p = ProjectListingDB.getInstance().SearchDB(((Applicant) s).AppliedProject);
    //                 if (p.Details.visibility) {
    //                     visible.add((Applicant) s);
    //                 } else {
    //                     notVisible.add((Applicant) s);
    //                 }
    //             }

    //             System.out.println("=====================");
    //             for (Applicant a : visible) {
    //                 System.out.print("Applicant name: ");
    //                 System.out.println(a.name);
    //                 System.out.print("Flat type: ");
    //                 System.out.println(a.flatTypeBooked.toString());
    //                 System.out.print("Age: ");
    //                 System.out.println(a.age);
    //                 System.out.print("Marital status: ");
    //                 System.out.println(a.maritalStatus);
    //             }

    //             System.out.println("=====================");

    //             for (Applicant a : notVisible) {
    //                 System.out.print("Applicant name: ");
    //                 System.out.println(a.name);
    //                 System.out.print("Flat type: ");
    //                 System.out.println(a.flatTypeBooked.toString());
    //                 System.out.print("Age: ");
    //                 System.out.println(a.age);
    //                 System.out.print("Marital status: ");
    //                 System.out.println(a.maritalStatus);
    //             }
    //             System.out.println("=====================");
    //         }
    //         case null, default -> {


    //         }
    //     }
    // }
}
