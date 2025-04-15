//UserInfoDB is the database class that will be used to store all the user information within the system
// it will be used to retrieve, modify and add user information to/from the csv files

package Database;

import java.util.ArrayList;
import Service.*;
import User.*;
import Filter.*;

public class UserInfoDB extends Database {

    private static final UserInfoDB instance = new UserInfoDB();

    private ArrayList<SystemUser> userList = new ArrayList<SystemUser>();
    Reader reader = new Reader();
    Writer writer = new Writer();

    private UserInfoDB() {}
    public static UserInfoDB getInstance() {
        return instance;
    }

    public UserInfoDB(String ApplicantFilePath, String OfficerFilePath, String ManagerFilePath) {
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

        //special case for Filter_Alphabetic, as it needs to sort the users in ascending or descending order
        if (filter instanceof Filter_Alphabetic) {
            Filter_Alphabetic filter_alpha = (Filter_Alphabetic)filter;
            ArrayList<SystemUser> sortedUsers = new ArrayList<>();
            for (SystemUser u : userList) {
                sortedUsers.add(u);
            }
            sortedUsers.removeIf(u -> !filter.FilterBy(u));
            if (filter_alpha.order == IFilter.orderBy.ASCENDING) {
                sortedUsers.sort((u1, u2) -> u1.name.compareToIgnoreCase(u2.name));
            }
            else if (filter_alpha.order == IFilter.orderBy.DESCENDING) {
                sortedUsers.sort((u1, u2) -> u2.name.compareToIgnoreCase(u1.name));
            }
            for (SystemUser u : sortedUsers) {
                for (int i = 0; i < userList.size(); i++) {
                    //System.out.println(userList.get(i).userID.equals(u.userID));
                    if (userList.get(i).userID.equals(u.userID)) {
                        System.out.println("================ " + i + " ================");
                        System.out.println(u.getUserDetails());
                        break;
                    }
                }
            }
        }

        //special case for filtering by age, as it also needs to sort the users in ascending or descending order
        else if (filter instanceof Filter_Age) {
            ArrayList<Applicant> sortedUsers = new ArrayList<Applicant>();
            for (SystemUser u : userList) {
                if (u instanceof Applicant) {
                    sortedUsers.add((Applicant)u);
                }
            }
            sortedUsers.removeIf(u -> !filter.FilterBy(u));
            if (((Filter_Age)filter).order == IFilter.orderBy.ASCENDING) {
                sortedUsers.sort((u1, u2) -> Double.compare(u1.age, u2.age));
            } else {
                sortedUsers.sort((u1, u2) -> Double.compare(u2.age, u1.age));
            }
            for (Applicant u : sortedUsers) {
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).userID.equals(u.userID)) {
                        System.out.println("================ " + i + " ================");
                        System.out.println(u.getUserDetails());
                        break;
                    }
                }
            }
        }

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
}