package Service;

import java.util.regex.Pattern;

import Database.UserInfoDB;
import User.SystemUser;

public class Authenticator {

    public Authenticator() {}
    
    public SystemUser login(String userID, String password) {
        return UserInfoDB.getInstance().AuthenticateUser(userID, password);
    }

    public boolean changePassword(String userID, String oldPassword, String newPassword) {
        
        SystemUser user = login(userID, oldPassword);
        if (newPassword.length() == 0) { System.out.println("\nError: Password cannot be empty!"); return false; }
        if (user != null) {
            user.password = newPassword;
            UserInfoDB.getInstance().ModifyDB(user, Database.Database.DB_Action.EDIT);
            System.out.println("\nPassword for user " + userID + " has been changed successfully.");
            return true;
        } else {
            System.out.println("\nError: Invalid User ID or Old Password.");
            return false;
        }
    }
}