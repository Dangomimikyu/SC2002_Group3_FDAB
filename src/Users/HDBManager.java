package Users;
import java.util.ArrayList;
import java.util.Scanner;

import Controller.EnquiryManager;
import Entity.*;

public class HDBManager extends System_User{

    public HDBManager(String n, String nric, int a, String m, String p, String TOP) {
        super(n, nric, a, m, p, "Manager");
    }

    //View ALL enquiries
    public void ViewAllEnquiries(EnquiryManager Enquiry_Manager) {
        boolean no_enquiries = true;
        for (Enquiry enq : Enquiry_Manager.get_all_current_enquiries()) {
            enq.getDetails();
            no_enquiries = false;
        }
        if (no_enquiries) {
            System.out.println("Error: There are no enquiries throughout all projects!");
        }
    }

    //View and reply to enquiries regarding handled projects
    public EnquiryManager ViewAndReplytoProjectEnquiries(EnquiryManager Enquiry_Manager, ArrayList<Project> all_handled_projects) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Enquiry> all_handled_enquiries = new ArrayList<>();
        for (Enquiry enq : Enquiry_Manager.get_all_current_enquiries()) {
            for (Project p : all_handled_projects) {
                if (enq.RegardingProject.equals(p.ProjectName)) {
                    enq.getDetails();
                    all_handled_enquiries.add(enq);
                    break;
                } 
            }
        }

        if (all_handled_enquiries.size() == 0) {
            System.out.println("Error: There are no enquiries regarding any projects your handling!");
            return Enquiry_Manager;
        }

        String choice_to_edit_or_not;
        edit_enquiry_y_n_loop:
        while (true) {
            System.out.println("Would you like to edit any enquiries? (Y/N): ");
            choice_to_edit_or_not = sc.nextLine();
            switch (choice_to_edit_or_not) {
            case "Y": case "y":
            break edit_enquiry_y_n_loop;
            case "N": case "n":
            return Enquiry_Manager;
            default:
            break;
            }
        }

        int choice = 0;
        int count = 0;
        while (choice <= 0 || choice > count) {
            count = 1;
            System.out.println("\nPlease choose which enquiry to edit: ");
            for (Enquiry enq : all_handled_enquiries ) {
                System.out.println(count + ". " + enq.Title);
                count++;
            }
            choice = sc.nextInt();
        }
        Enquiry enquiry_to_edit = all_handled_enquiries.get(choice-1); 

        //two choices: edit latest message (if its user created) or reply to latest message (cannot add consecutively from same user)
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
                    System.out.println("\nPlease enter your reply: ");
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
    
}