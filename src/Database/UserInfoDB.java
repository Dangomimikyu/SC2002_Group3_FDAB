//UserInfoDB is the database class that will be used to store all the user information within the system
// it will be used to retrieve, modify and add user information to/from the csv files

package Database;

import java.util.ArrayList;
import Service.*;
import User.SystemUser;

public class UserInfoDB implements Database<SystemUser> {

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

    public ArrayList<SystemUser> ViewDB() {
        return userList;
    }

    public void ModifyDB(SystemUser user, DB_Action action) {
        switch (action) {
            case ADD -> writer.WriteNewUser(user);
            case DELETE -> throw new UnsupportedOperationException("Error: unable to delete users"); // TODO: handle deletion of users
            case EDIT -> writer.RewriteUser(user);
        }
    }
}