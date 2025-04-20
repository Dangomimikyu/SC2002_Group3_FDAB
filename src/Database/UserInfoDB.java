//UserInfoDB is the database class that will be used to store all the user information within the system
// it will be used to retrieve, modify and add user information to/from the csv files

package Database;

import java.util.ArrayList;
import InteractableAttributePackage.Project;
import User.*;
import Filter.*;

public class UserInfoDB extends Database {

    private static UserInfoDB instance;

    private ArrayList<SystemUser> userList = new ArrayList<SystemUser>();

    private UserInfoDB() {}
    public static UserInfoDB getInstance() {
        if (instance == null) { instance = new UserInfoDB(); }
        return instance;
    }

    public void InitialiseDB(String ApplicantFilePath, String OfficerFilePath, String ManagerFilePath) {
        userList = reader.readUsers(ApplicantFilePath, OfficerFilePath, ManagerFilePath);
    }

    public ArrayList<SystemUser> getUserDB() {
        return userList;
    }

    public ArrayList<Project> ViewDB() {
        System.out.println("\nAll users in order by index: ");
        int index = 0;
        for (SystemUser u : userList) {
            System.out.println("================ " + index + " ================");
            System.out.println(u.getUserDetails());
            index++;
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////
    /////////////////// VIEW DB WITH FILTERS //////////////////////////////
    ///////////////////////////////////////////////////////////////////////

    public void ViewDB(ArrayList<IFilter> filters) {

        for ( IFilter filter : filters) { displayFilterInformation(filter); }

        ArrayList<SystemUser> sortedUsers = new ArrayList<>(userList);

        for (IFilter filter : filters) {
            if (filter instanceof Filter_Alphabetic) { SortInOrder(sortedUsers, (Filter_Alphabetic)filter); }
            else if (filter instanceof Filter_Age) { SortInOrder(sortedUsers, (Filter_Age)filter); }
            else { sortedUsers.removeIf(u -> !filter.FilterBy(u)); }
        }

        displaySortedWithOriginalIndex(sortedUsers);
    }

    /////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////// MODIFYING DB METHODS ////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    //modifying user by index not supported as other people should not be able to indiscreetly modify user information

    //modify user by object. Meant for use in Manager classes. Not meant to use with ViewDB().
    public void ModifyDB(SystemUser user, DB_Action action) {
        switch (action) {
            case ADD -> {
                writer.WriteNewUser(user);
                userList.add(user);
            }
            case DELETE -> System.out.println("Error: Deleting Users not supported");
            case EDIT -> {
                writer.RewriteUser(user);
                userList.set(userList.indexOf(user), user);
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////// SORTING ALGORTIHMS + HELPER FUNCTIONS////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    //prints the headers and information for which users will be filtered by
    private void displayFilterInformation(IFilter filter) {
        if (filter instanceof Filter_Marital) {
            System.out.println("Filter by users with marital status: " + ((Filter_Marital)filter).maritalStatus);
            System.out.println("============================================================================");
        }
        else if (filter instanceof Filter_Age) {
            System.out.println("Filter users if is in the age group between: " + 
            ((Filter_Age)filter).minAge + " and " + ((Filter_Age)filter).maxAge + " in " + ((Filter_Age)filter).order + " order");
            System.out.println("============================================================================");
        }
        else if (filter instanceof Filter_Alphabetic) {
            System.out.println("Filter by user's name starting with: " + (((Filter_Alphabetic)filter).first_char == null ? "any character" : ((Filter_Alphabetic)filter).first_char)
            + " in " + ((Filter_Alphabetic)filter).order + " order");
            System.out.println("============================================================================");
        }
        else {
            System.out.println("Error: This filter method is not supported for users");
            return;
        }
    }

    //Sorts lexicographically in specified order
    private ArrayList<SystemUser> SortInOrder(ArrayList<SystemUser> sortedUsers, Filter_Alphabetic filter) {
        sortedUsers.removeIf(u -> !filter.FilterBy(u));
        if (filter.order == IFilter.orderBy.ASCENDING) {
            sortedUsers.sort((u1, u2) -> u1.name.compareToIgnoreCase(u2.name));
        }
        else if (filter.order == IFilter.orderBy.DESCENDING) {
            sortedUsers.sort((u1, u2) -> u2.name.compareToIgnoreCase(u1.name));
        }
        return sortedUsers;
    }

    //Sorts age in specified order
    private ArrayList<SystemUser> SortInOrder(ArrayList<SystemUser> sortedUsers, Filter_Age filter) {
        // ArrayList<Applicant> sortedUsers = userList.stream()
        // .filter(obj -> obj instanceof Applicant)
        // .map(obj -> (Applicant) obj)
        // .collect(Collectors.toCollection(ArrayList::new));
        sortedUsers.removeIf(u -> !filter.FilterBy(u));
        if (filter.order == IFilter.orderBy.ASCENDING) {
            sortedUsers.sort((u1, u2) -> Double.compare(((Applicant)u1).age, ((Applicant)u2).age));
        } else {
            sortedUsers.sort((u1, u2) -> Double.compare(((Applicant)u2).age, ((Applicant)u1).age));
        }
        return sortedUsers;
    }

    //print the users in sorted order, as well as its original index in the original list
    //this is to show the user the original index of that users in the original list, so that they can modify it if needed
    private void displaySortedWithOriginalIndex(ArrayList<SystemUser> sortedUsers) {

        if (sortedUsers.size() == 0) {
            System.out.println("\nNo Users found!");
        }

        for (SystemUser u : sortedUsers) {
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).userID.equals(u.userID)) {
                    System.out.println("================ " + i + " ================");
                    System.out.println(u.getUserDetails());
                    break;
                }
            }
        }
    }

    public SystemUser SearchDB(String userName)
    {
        return userList.stream().filter(pp -> userName.equals(pp.name)).findFirst().orElse(null);
    }

    public SystemUser AuthenticateUser(String id, String password)
    {
        return userList.stream()
                .filter(user -> user.userID.equals(id) && user.password.equals(password))
                .findFirst()
                .orElse(null);
    }
}