package Service;

import Database.HDB_System;
import User.SystemUser;

public class Authenticator {

    private HDB_System hdbSystem = HDB_System.getInstance();

    public SystemUser login(String userID, String password) {
        return hdbSystem.users.ViewDB().stream()
                .filter(user -> user.userID.equals(userID) && user.password.equals(password))
                .findFirst()
                .orElse(null);
    }

    
    public boolean changePassword(String userID, String oldPassword, String newPassword) {
        SystemUser user = login(userID, oldPassword);
        if (user != null) {
           
            ((SystemUser) user).password = newPassword;
            hdbSystem.users.ModifyDB(user, Database.Database.DB_Action.EDIT);
            System.out.println("Password for user " + userID + " has been changed successfully.");
            return true;
        } else {
            System.out.println("Error: Invalid User ID or Old Password.");
            return false;
        }
    }
}