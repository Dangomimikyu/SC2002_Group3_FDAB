//UserInfoDB is the database class that will be used to store all the user information within the system
// it will be used to retrieve, modify and add user information to/from the csv files

package Database;

import java.util.ArrayList;
import java.util.stream.Collectors;
import Service.*;
import User.*;
import Filter.*;

public class UserInfoDB extends Database {

    private static UserInfoDB instance;

    private ArrayList<SystemUser> userList = new ArrayList<SystemUser>();
    Reader reader = new Reader();
    Writer writer = new Writer();

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

    public void ViewDB() {
        System.out.println("\nAll users in order by index: ");
        int index = 0;
        for (SystemUser u : userList) {
            System.out.println("================ " + index + " ================");
            System.out.println(u.getUserDetails());
            index++;
        }
    }

    public void ViewDB(IFilter filter) {

        displayFilterInformation(filter);

        if (filter instanceof Filter_Alphabetic) { SortInOrder((Filter_Alphabetic)filter); }
        else if (filter instanceof Filter_Age) { SortInOrder((Filter_Age)filter); }

        //for filters with no orderBy attribute
        else {
            int index = 0;
            for (SystemUser u : userList) {
                if (filter.FilterBy(u)) {
                    System.out.println("================ " + index + " ================");
                    System.out.println(u.getUserDetails());
                }
                index++;
            }            
        }
    }

    //modifying user by index not supported as other people should not be able to indiscreetly modify user information

    //modify user by object. Meant for use in Manager classes. Not meant to use with ViewDB().
    public void ModifyDB(SystemUser user, DB_Action action) {
        switch (action) {
            case ADD -> {
                writer.WriteNewUser(user);
                userList.add(user);
            }
            case DELETE -> throw new UnsupportedOperationException("Deleting Users not supported");
            case EDIT -> {
                writer.RewriteUser(user);
                userList.set(userList.indexOf(user), user);
            }
        }
    }

    //prints the headers and information for which users will be filtered by
    private void displayFilterInformation(IFilter filter) {
        if (filter instanceof Filter_Marital) {
            System.out.println("Filter by users with marital status: " + ((Filter_Marital)filter).maritalStatus);
        }
        else if (filter instanceof Filter_Age) {
            System.out.println("Filter users if is in the age group between: " + 
            ((Filter_Age)filter).minAge + " and " + ((Filter_Age)filter).maxAge + "\nin " + ((Filter_Age)filter).order + " order");
        }
        else if (filter instanceof Filter_Alphabetic) {
            System.out.println("Filter by user's name starting with: " + (((Filter_Alphabetic)filter).first_char == null ? "any character" : ((Filter_Alphabetic)filter).first_char)
            + "\nin " + ((Filter_Alphabetic)filter).order + " order");
        }
        else {
            throw new UnsupportedOperationException("This filter method is not supported for users");
        }
    }

    //Sorts lexicographically in specified order
    private void SortInOrder(Filter_Alphabetic filter) {
        ArrayList<SystemUser> sortedUsers = new ArrayList<>(userList);
        sortedUsers.removeIf(u -> !filter.FilterBy(u));
        if (filter.order == IFilter.orderBy.ASCENDING) {
            sortedUsers.sort((u1, u2) -> u1.name.compareToIgnoreCase(u2.name));
        }
        else if (filter.order == IFilter.orderBy.DESCENDING) {
            sortedUsers.sort((u1, u2) -> u2.name.compareToIgnoreCase(u1.name));
        }
            displaySortedWithOriginalIndex(sortedUsers);
    }

    //Sorts age in specified order
    private void SortInOrder(Filter_Age filter) {
        ArrayList<Applicant> sortedUsers = userList.stream()
        .filter(obj -> obj instanceof Applicant)
        .map(obj -> (Applicant) obj)
        .collect(Collectors.toCollection(ArrayList::new));
        sortedUsers.removeIf(u -> !filter.FilterBy(u));
        if (filter.order == IFilter.orderBy.ASCENDING) {
            sortedUsers.sort((u1, u2) -> Double.compare(u1.age, u2.age));
        } else {
            sortedUsers.sort((u1, u2) -> Double.compare(u2.age, u1.age));
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

    //print the users in sorted order, as well as its original index in the original list
    //this is to show the user the original index of that users in the original list, so that they can modify it if needed
    private void displaySortedWithOriginalIndex(ArrayList<SystemUser> sortedUsers) {
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
}