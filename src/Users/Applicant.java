package Users;

import Entity.*;
import Controller.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Applicant extends System_User{

    public Applicant(String n, String nric, int a, String m, String p, String TOP) {
        super(n, nric, a, m, p, "Applicant");
    }

    //Applicants can create new Enquiries regarding any project
    public EnquiryManager CreateEnquiry(EnquiryManager Enquiry_Manager, String title, String project_name, String message) {
        Enquiry_Manager.addEnquiry(this, title, project_name, message);
        return Enquiry_Manager;
    }

    //View all enquiries ONLY CREATED by the user
    public void ViewCreatedEnquiries(EnquiryManager Enquiry_Manager) {
        if (Enquiry_Manager.get_all_created_enquiries(this).size() == 0) {
            System.out.println("Error: You do not have any existing created enquiries!");
            return;
        }
        for (Enquiry enq : Enquiry_Manager.get_all_current_enquiries()) {
            if (enq.Enquirer.getUserID().equals(getUserID())) {
                enq.getDetails();
            }
        }
    }
    
    // EditEnquiry refers to ability modify latest message(provided it is from the creator) or to create new message
    public EnquiryManager EditEnquiry(EnquiryManager Enquiry_Manager) {
        //Prompt which enquiry created by user to edit
        Scanner sc = new Scanner(System.in);
        ArrayList<Enquiry> user_enquiries = Enquiry_Manager.get_all_created_enquiries(this);

        if (user_enquiries.size() == 0) {
            System.out.println("Error: You do not have any existing created enquiries!");
            return Enquiry_Manager;
        }

        int choice = 0;
        int count = 0;
        while (choice <= 0 || choice > count) {
            count = 1;
            System.out.println("\nPlease choose which enquiry to edit: ");
            for (Enquiry enq : user_enquiries) {
                System.out.println(count + ". " + enq.Title);
                count++;
            }
            choice = sc.nextInt();
        }
        Enquiry enquiry_to_edit = user_enquiries.get(choice-1); 

        //two choices: edit latest message or add latest message to a existing enquiry (cannot add consecutively from same user)
        sc.nextLine();
        String choice_2 = "nil";
        if (enquiry_to_edit.thread.getLast().sender.getUserID().equals(getUserID())) {
            y_n_loop:
            while (true) {
                System.out.println("No one has replied to your latest message yet. Would you like to edit your latest message? (Y/N): ");
                choice_2 = sc.nextLine();
                switch (choice_2) {
                    case "Y": case "y":
                    System.out.println("your latest message: " + enquiry_to_edit.thread.getLast().message);
                    System.out.println("\nPlease enter your re-edited message: ");
                    enquiry_to_edit.editLatestMessage(sc.nextLine());
                    break y_n_loop;
                    case "N": case "n":
                    break y_n_loop;
                    default:
                    break;
                }
            }
        }
        else {
            y_n_loop_2:
            while (true) {
                System.out.println("Someone has replied to this thread! Would you like to see and reply to the latest message? (Y/N): ");
                choice_2 = sc.nextLine();
                switch (choice_2) {
                    case "Y": case "y":
                    System.out.println(enquiry_to_edit.thread.getLast().sender.TypeofUser + " " + enquiry_to_edit.thread.getLast().sender.name + "'s message: " + enquiry_to_edit.thread.getLast().message);
                    System.out.println("Please enter your reply: ");
                    enquiry_to_edit.addMessage(sc.nextLine(),this);
                    break y_n_loop_2;
                    case "N": case "n":
                    break y_n_loop_2;
                    default:
                    break;
                }
            }
        }

        Enquiry_Manager.editEnquiry(enquiry_to_edit);
        return Enquiry_Manager;
    }

    //delete any created enquiry
    public EnquiryManager DeleteEnquiry(EnquiryManager Enquiry_Manager) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Enquiry> user_enquiries = Enquiry_Manager.get_all_created_enquiries(this);

        if (user_enquiries.size() == 0) {
            System.out.println("Error: You do not have any existing created enquiries!");
            return Enquiry_Manager;
        }

        int choice = 0;
        int count = 0;
        while (choice <= 0 || choice > count) {
            count = 1;
            System.out.println("\nPlease choose which enquiry to delete: ");
            for (Enquiry enq : user_enquiries) {
                System.out.println(count + ". " + enq.Title);
                count++;
            }
            choice = sc.nextInt();
        }
        Enquiry_Manager.deleteEnquiry(user_enquiries.get(choice-1));
        return Enquiry_Manager;
    }

}