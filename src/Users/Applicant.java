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
        Enquiry_Manager.RecordNewEnquiry(this, title, project_name, message);
        return Enquiry_Manager;
    }

    //View all enquiries ONLY CREATED by the user
    public void ViewCreatedEnquiries(EnquiryManager Enquiry_Manager) {
        if (Enquiry_Manager.get_all_created_enquiries(this).size() == 0) {
            System.out.println("Error: You do not have any existing created enquiries!");
            return;
        }
        for (Enquiry enq : Enquiry_Manager.get_all_current_enquiries()) {
            if (enq.getCreatorID().equals(getUserID())) {
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

        if (!enquiry_to_edit.Reply.equals("")) {
            System.out.println("Error: Someone has replied to your enquiry! You can't edit this enquiry anymore.");
            return Enquiry_Manager;
        }

        sc.nextLine(); // Consume newline left-over
        System.out.println("Please enter your newly editted message: ");
        String editted_message = sc.nextLine();
        enquiry_to_edit.editMessage(editted_message);
        Enquiry_Manager.RecordEnquiryEdits(enquiry_to_edit);
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
        Enquiry enquiry_to_delete = user_enquiries.get(choice-1);

        if (!enquiry_to_delete.Reply.equals("")) {
            System.out.println("Error: Someone has replied to your enquiry! You can't delete this enquiry anymore.");
            return Enquiry_Manager;
        }

        Enquiry_Manager.RecordEnquiryDeletion(enquiry_to_delete);
        return Enquiry_Manager;
    }

}