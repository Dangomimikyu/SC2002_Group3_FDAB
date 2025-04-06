// BTO Management System is a system for applicants and HDB staffs to view, apply and manage for BTO projects. 
// The system will act as a centralized hub for all applicants and HDB staffs
// A user list can be initiated through a file uploaded into the system at initialization.

import Users.*;
import Service.LocalData;
import Boundary.UserUI;

import java.util.ArrayList;

public class HDB_Main {
    //System_User is the base class used to help initialise the user before the log-in process
    //one of the last 3 variables will be initialised when user successfully logs in as one of the respective types of user
    //note: ONLY ONE of the 3 variables will be initialised
    //needed to have the code initialised this way otherwise will encounter (variable may not be initialised before) errors.

    public static void main(String[] args) {

        //initialisation of localdata
        String UserFilePath = "src/localdata/UserList.csv";
        String ProjectsFilePath = "src/localdata/ProjectList.csv";
        String EnquiriesFilePath = "src/localdata/Enquiries.csv";
        LocalData localdata = new LocalData(UserFilePath, ProjectsFilePath, EnquiriesFilePath);

        //initialisation of controllers (singleton classes - only initialised once)
        ArrayList<System_User> all_users = localdata.get_all_users();
        System_User user = new System_User();

        //Log-in Menu
        login_loop:
        while (true) {
            String[] login_info = user.Login().split(",");
            for (System_User stored_user : all_users) {
                
                if (stored_user.getUserID().equals(login_info[0]) && stored_user.getPassword().equals(login_info[1])) {
                    user = stored_user;
                    break login_loop;
                }
            }
            System.out.println("Error: Cannot find such user in Database!");
        }


        if (user.TypeofUser.equals("Applicant")) {
            UserUI userUI = new UserUI(new Applicant(user.name,user.getUserID(),user.age,user.marital_status,user.getPassword(),""), localdata);
        }

        else if (user.TypeofUser.equals("Officer")) {
            UserUI userUI = new UserUI(new HDBOfficer(user.name,user.getUserID(),user.age,user.marital_status,user.getPassword(),""), localdata);
        }

        else if (user.TypeofUser.equals("Manager")) {
            UserUI userUI = new UserUI(new HDBManager(user.name,user.getUserID(),user.age,user.marital_status,user.getPassword(),""), localdata);
        }
    }

}
