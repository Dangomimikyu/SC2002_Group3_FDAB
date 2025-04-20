package Service;

import Database.UserInfoDB;
import User.SystemUser;

public class Authenticator {

    public Authenticator() {}
    
    public SystemUser login(String userID, String password) {
        return UserInfoDB.getInstance().AuthenticateUser(userID, password);
    }

    public boolean changePassword(String userID, String oldPassword, String newPassword) {
        SystemUser user = login(userID, oldPassword);
        if (user != null) {

            user.password = newPassword;
            UserInfoDB.getInstance().ModifyDB(user, Database.Database.DB_Action.EDIT);
            System.out.println("Password for user " + userID + " has been changed successfully.");
            return true;
        } else {
            System.out.println("Error: Invalid User ID or Old Password.");
            return false;
        }
    }
}