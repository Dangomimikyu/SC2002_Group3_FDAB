// BTO Management System is a system for applicants and HDB staffs to view, apply and manage for BTO projects. 
// The system will act as a centralized hub for all applicants and HDB staffs
// A user list can be initiated through a file uploaded into the system at initialization.

import Controller.*;
import Entity.*;
import Users.*;
import Service.LocalData;
import java.util.Scanner;
import java.util.ArrayList;

public class HDB_Main {
    //System_User is the base class used to help initialise the user before the log-in process
    //one of the last 3 variables will be initialised when user successfully logs in as one of the respective types of user
    //note: ONLY ONE of the 3 variables will be initialised
    //needed to have the code initialised this way otherwise will encounter (variable may not be initialised before) errors.
    public static System_User user;
    public static Applicant user_applicant;
    public static HDBOfficer user_officer;
    public static HDBManager user_manager;

    public static void main(String[] args) {

        //initialisation of localdata
        String UserFilePath = "src/localdata/UserList.csv";
        String ProjectsFilePath = "src/localdata/ProjectList.csv";
        String EnquiriesFilePath = "src/localdata/Enquiries.csv";
        LocalData localdata = new LocalData(UserFilePath, ProjectsFilePath, EnquiriesFilePath);

        //initialisation of controllers (singleton classes - only initialised once)
        ArrayList<System_User> all_users = localdata.get_all_users();
        EnquiryManager Enquiry_Manager = new EnquiryManager(localdata);
        ProjectManager Project_Manager = new ProjectManager(localdata);
        System_User user = new System_User();
        Scanner input = new Scanner(System.in);

        //Log-in Menu
        login_loop:
        while (true) {
            String[] login_info = user.Login().split(",");
            for (System_User stored_user : all_users) {
                
                if (stored_user.getUserID().equals(login_info[0]) && stored_user.getPassword().equals(login_info[1])) {

                    if (stored_user.TypeofUser.equals("Applicant")) {
                        user_applicant = new Applicant(stored_user.name,stored_user.getUserID(),stored_user.age,stored_user.marital_status,stored_user.getPassword(),"");
                    }

                    else if (stored_user.TypeofUser.equals("Officer")) {
                        user_officer = new HDBOfficer(stored_user.name,stored_user.getUserID(),stored_user.age,stored_user.marital_status,stored_user.getPassword(),"");
                    }

                    else if (stored_user.TypeofUser.equals("Manager")) {
                        user_manager = new HDBManager(stored_user.name,stored_user.getUserID(),stored_user.age,stored_user.marital_status,stored_user.getPassword(),"");
                    }

                    break login_loop;
                }
            }
            System.out.println("Error: Cannot find such user in Database!");
        }


        //Applicant Menu
        if (user_applicant != null) {
            System.out.println("\nWelcome Applicant " +  user_applicant.name + "! What would you like to do?");
            
            int choice = 0;
            while (choice != 5) {
                System.out.println("\n-----------------------------\n" +
                "Please enter your choice:" +
                "\n1. Create Enquiry" +
                "\n2. View Created Enquiries" +
                "\n3. Edit or Reply to created Enquiries" +
                "\n4. Delete a created Enquiry" +
                "\n5. Logout\n-----------------------------");
                choice = input.nextInt();
                switch (choice) {
                    case 1:
                    input.nextLine();
                    System.out.println("Enter the title of your enquiry: ");
                    String title = input.nextLine();
                    System.out.println("Enter the the project name your enquiry is regarding: ");
                    String project_name = input.nextLine();
                    if (!Project_Manager.Exists(project_name)) {
                        System.out.println("Error: " + project_name + " does not exist in project database!");
                        break;
                    }
                    System.out.println("Enter the first message of your enquiry: ");
                    String message = input.nextLine();
                    Enquiry_Manager = user_applicant.CreateEnquiry(Enquiry_Manager,title,project_name,message);
                    break;
                    case 2:
                    user_applicant.ViewCreatedEnquiries(Enquiry_Manager);
                    break;
                    case 3:
                    Enquiry_Manager = user_applicant.EditEnquiry(Enquiry_Manager);
                    break;
                    case 4:
                    Enquiry_Manager = user_applicant.DeleteEnquiry(Enquiry_Manager);          
                    break;
                    case 5:
                    user_applicant.Logout();
                    break;
                    default:
                    break;
                }
            }
            input.close();
        }


        //Officer Menu
        else if (user_officer != null) {
            System.out.println("\nWelcome Officer " +  user_officer.name + "! What would you like to do?");
            
            int choice = 0;
            while (choice != 6) {
                System.out.println("\n-----------------------------\n" +
                "Please enter your choice:" + 
                "\n1. View or reply to Enquiries regarding projects you are handling" +
                "\n2. Create an enquiry" +
                "\n3. View Created Enquiries" +
                "\n4. Edit or Reply to created Enquiries" +
                "\n5. Delete a created Enquiry" + 
                "\n6. Logout" +
                "\n-----------------------------");
                choice = input.nextInt();
                input.nextLine();
                switch (choice) {
                    case 1:
                    ArrayList<Project> all_handled_projects = Project_Manager.get_all_handled_projects(user_officer);
                    Enquiry_Manager = user_officer.ViewAndReplytoProjectEnquiries(Enquiry_Manager, all_handled_projects);
                    break;
                    case 2:
                    System.out.println("Enter the title of your enquiry: ");
                    String title = input.nextLine();
                    System.out.println("Enter the the project name your enquiry is regarding: ");
                    String project_name = input.nextLine();
                    if (!Project_Manager.Exists(project_name)) {
                        System.out.println("Error: " + project_name + " does not exist in project database!");
                        break;
                    }
                    System.out.println("Enter the first message of your enquiry: ");
                    String message = input.nextLine();
                    Enquiry_Manager = user_officer.CreateEnquiry(Enquiry_Manager,title,project_name,message);
                    break;
                    case 3:
                    user_officer.ViewCreatedEnquiries(Enquiry_Manager);
                    break;
                    case 4:
                    Enquiry_Manager = user_officer.EditEnquiry(Enquiry_Manager);
                    break;
                    case 5:
                    Enquiry_Manager = user_officer.DeleteEnquiry(Enquiry_Manager);  
                    break;        
                    case 6:
                    user_officer.Logout();
                    break;
                    default:
                    break;
                }
            }
            input.close();
        }


        else if (user_manager != null) {
            System.out.println("\nWelcome Manager " +  user_manager.name + "! What would you like to do?\n--------------------------------------");
            
            int choice = 0;
            while (choice != 3) {
                System.out.println("\n-----------------------------\n" +
                "Please enter your choice:" + 
                "\n1. View or reply to Enquiries regarding projects you are handling" +
                "\n2. View enquiries from all known projects" +
                "\n3. Logout" +
                "\n-----------------------------");
                choice = input.nextInt();
                input.nextLine();
                switch (choice) {
                    case 1:
                    ArrayList<Project> all_handled_projects = Project_Manager.get_all_handled_projects(user_manager);
                    Enquiry_Manager = user_manager.ViewAndReplytoProjectEnquiries(Enquiry_Manager, all_handled_projects);
                    break;
                    case 2:
                    user_manager.ViewAllEnquiries(Enquiry_Manager);
                    break;
                    case 3:
                    user_manager.Logout();
                    break;
                    default:
                    break;
                }
            }
            input.close();
        }
    }
}
