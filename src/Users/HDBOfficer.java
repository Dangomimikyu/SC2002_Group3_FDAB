package Users;

import java.util.ArrayList;
import java.util.Scanner;

import Controller.EnquiryManager;
import Entity.*;

//HDB Officer possess all applicant’s capabilities.

public class HDBOfficer extends Applicant{

    public HDBOfficer(String n, String nric, int a, String m, String p, String TOP) {
        super(n, nric, a, m, p, "Officer");
    }

    //View and reply to enquiries regarding handled projects
    public EnquiryManager ViewAndReplytoProjectEnquiries(EnquiryManager Enquiry_Manager, ArrayList<Project> all_handled_projects) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Enquiry> all_handled_enquiries = new ArrayList<>();
        for (Enquiry enq : Enquiry_Manager.get_all_current_enquiries()) {
            for (Project p : all_handled_projects) {
                if (enq.RegardingProject.equals(p.ProjectName) && enq.Reply.equals("")) {
                    enq.getDetails();
                    all_handled_enquiries.add(enq);
                    break;
                } 
            }
        }

        if (all_handled_enquiries.size() == 0) {
            System.out.println("Error: There are no open enquiries regarding any projects your handling!");
            return Enquiry_Manager;
        }

        String choice_to_edit_or_not;
        edit_enquiry_y_n_loop:
        while (true) {
            System.out.println("Would you like to reply to any enquiries? (Y/N): ");
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
            System.out.println("\nPlease choose which enquiry to reply to: ");
            for (Enquiry enq : all_handled_enquiries ) {
                System.out.println(count + ". " + enq.Title);
                count++;
            }
            choice = sc.nextInt();
        }
        Enquiry enquiry_to_edit = all_handled_enquiries.get(choice-1); 

        sc.nextLine(); // Consume newline left-over
        System.out.println("Please enter your reply: ");
        String reply = sc.nextLine();
        enquiry_to_edit.Reply = reply;
        enquiry_to_edit.Sender = this;
        Enquiry_Manager.RecordEnquiryEdits(enquiry_to_edit);
        return Enquiry_Manager;
    }

    //1. first, list all enquiries regarding project
    //2. prompt officer if they would like to reply to one of the enquiries
    //3. if the latest message from enquiry was not from user, ask if they want to reply
    //4. if the latest message is from user, ask if they want to edit

}