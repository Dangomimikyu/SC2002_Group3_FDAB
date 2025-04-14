//UserInfoDB is the database class that will be used to store all the user information within the system
// it will be used to retrieve, modify and add user information to/from the csv files

package Database;

import java.util.ArrayList;
import Service.*;
import User.SystemUser;

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
            System.out.println("UserID: " + u.userID
                    + "\nName: " + u.name
                    + "\nUser Perms: " + u.getClass().getSimpleName());
            index++;
        }
    }

    //modify user by index
    public void ModifyDB(int index, DB_Action action) {
        switch (action) {
            case ADD -> writer.WriteNewUser(userList.get(index));
            case DELETE -> throw new UnsupportedOperationException("Error: unable to delete users"); // TODO: handle deletion of users
            case EDIT -> writer.RewriteUser(userList.get(index));
        }
    }
}