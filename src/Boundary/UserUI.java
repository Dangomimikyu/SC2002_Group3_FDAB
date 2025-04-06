package Boundary;

import Users.*;
import Controller.*;
import Entity.*;

import java.util.Scanner;
import java.util.ArrayList;
import Service.LocalData;

public class UserUI {
    // This class is responsible for displaying the user interface for the system user.
    // It will include methods to display menus and handle user input.
    Scanner input = new Scanner(System.in);
    
    public UserUI(Applicant user, LocalData localdata) {
        // Constructor for Applicant user interface
        // Initialize the UI for the applicant user
        EnquiryManager Enquiry_Manager = new EnquiryManager(localdata);
        ProjectManager Project_Manager = new ProjectManager(localdata);
        System.out.println("\nWelcome Applicant " +  user.name + "! What would you like to do?");
            
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
                    Enquiry_Manager = user.CreateEnquiry(Enquiry_Manager,title,project_name,message);
                    break;
                    case 2:
                    user.ViewCreatedEnquiries(Enquiry_Manager);
                    break;
                    case 3:
                    Enquiry_Manager = user.EditEnquiry(Enquiry_Manager);
                    break;
                    case 4:
                    Enquiry_Manager = user.DeleteEnquiry(Enquiry_Manager);          
                    break;
                    case 5:
                    user.Logout();
                    break;
                    default:
                    break;
                }
            }
            input.close();
        }

        public UserUI(HDBOfficer user, LocalData localdata) {
        // Constructor for HDB Officer user interface
        // Initialize the UI for the HDB officer user
        EnquiryManager Enquiry_Manager = new EnquiryManager(localdata);
        ProjectManager Project_Manager = new ProjectManager(localdata);
        System.out.println("\nWelcome Officer " +  user.name + "! What would you like to do?");
            
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
                    ArrayList<Project> all_handled_projects = Project_Manager.get_all_handled_projects(user);
                    Enquiry_Manager = user.ViewAndReplytoProjectEnquiries(Enquiry_Manager, all_handled_projects);
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
                    Enquiry_Manager = user.CreateEnquiry(Enquiry_Manager,title,project_name,message);
                    break;
                    case 3:
                    user.ViewCreatedEnquiries(Enquiry_Manager);
                    break;
                    case 4:
                    Enquiry_Manager = user.EditEnquiry(Enquiry_Manager);
                    break;
                    case 5:
                    Enquiry_Manager = user.DeleteEnquiry(Enquiry_Manager);  
                    break;        
                    case 6:
                    user.Logout();
                    break;
                    default:
                    break;
                }
            }
            input.close();
        }

    public UserUI(HDBManager user, LocalData localdata) {
        // Constructor for HDB Manager user interface
        // Initialize the UI for the HDB manager user
        EnquiryManager Enquiry_Manager = new EnquiryManager(localdata);
        ProjectManager Project_Manager = new ProjectManager(localdata);
        System.out.println("\nWelcome Manager " +  user.name + "! What would you like to do?\n--------------------------------------");
            
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
                ArrayList<Project> all_handled_projects = Project_Manager.get_all_handled_projects(user);
                Enquiry_Manager = user.ViewAndReplytoProjectEnquiries(Enquiry_Manager, all_handled_projects);
                break;
                case 2:
                user.ViewAllEnquiries(Enquiry_Manager);
                break;
                case 3:
                user.Logout();
                break;
                default:
                break;
            }
        }
        input.close();
    }

    public void displayMenu() {
        System.out.println("Welcome to the HDB System!");
        System.out.println("Please select an option:");
        System.out.println("1. Create Enquiry");
        System.out.println("2. View Created Enquiries");
        System.out.println("3. Update Enquiry Status");
        System.out.println("4. Logout");
    }
    
}
