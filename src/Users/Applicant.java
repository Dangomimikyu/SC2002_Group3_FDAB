package Users;

import Entity.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Applicant extends User{

    public Applicant(String n, String nric, int a, String m, String p) {
        super(n, nric, a, m, p);
    }

    // • Can only view the list of projects that are open to their user group (Single
    // or Married) and if their visibility has been toggled “on”.

    // • Able to apply for a project – cannot apply for multiple projects.
    // o Singles, 35 years old and above, can ONLY apply for 2-Room
    // o Married, 21 years old and above, can apply for any flat types (2-Room or 3-Room)
    public void ApplyforProject(Project proj) {}

    // • Able to view the project he/she applied for and the application status, even after visibility is turned off
    // o Pending: Entry status upon application – No conclusive decision made about the outcome of the application
    // o Successful: Outcome of the application is successful, hence invited to make a flat booking with the HDB Officer
    // o Unsuccessful: Outcome of the application is unsuccessful, hence cannot make a flat booking for this application. 
    // Applicant may apply for another project.
    // o Booked: Secured a unit after a successful application and completed a flat booking with the HDB Officer.
    // • If Application status is “Successful”, Applicant can book one flat through the HDB Officer 
    // (Only HDB Officer can help to book a flat)
    // cannot book more than one flat, within a project or across different project
    public void viewAppliedProjectStatus(Project proj) {}

    // • Allowed to request withdrawal for their BTO application before/after flat booking
    // public WithdrawalRequest requestWithdrawal() {}

    // • Can only view the list of projects that are open to their user group (Single
    // or Married) and if their visibility has been toggled “on”.
    // • All users can use filters to view project (location, flat types, etc.) Assume
    // that default is by alphabetical order. User filter settings are saved when
    // they switch menu pages. 
    public void ViewProjectsOpentoUser(LinkedList <Project> all_projects, String filter) {
        for (Project proj : all_projects) {
            if (proj.getGroupProjOpento().equals(getMaritalStatus()) && proj.isVisible()) {
                getProjectDetails(proj);
            }
        }
    }

    // • Able to submit enquiries, a string, regarding the projects.
    // • Able to view, edit, and delete their enquiries.
    public void viewEnquirysOnlyByUser(LinkedList<Enquiry> all_enquires) {
        for (Enquiry enquiry : all_enquires) {
            if (enquiry.getCreatorOfEnquiry().equals(getName())) {
                getEnquiryDetails(enquiry);
            }
        }
    }

    public Enquiry CreateEnquiry() {
        Enquiry e = new Enquiry(getName());
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the purpose of your enquiry: ");
        e.setPurposeOfEnquiry(sc.nextLine());
        System.out.println("Enter the details of your enquiry: ");
        e.setDetails(sc.nextLine());
        sc.close();
        return e;
    }
    public Enquiry EditEnquiry(Enquiry e) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the purpose of your enquiry(don't type anything and just submit if you would not like to edit this part): ");
        String user_input = sc.nextLine();
        if (user_input != "") {
            e.setPurposeOfEnquiry(user_input);
            e.updateTime();
        }
        System.out.println("Enter the details of your enquiry(don't type anything and just submit if you would not like to edit this part): ");
        user_input = sc.nextLine();
        if (user_input != "") {
            e.setDetails(user_input);
            e.updateTime();
        }
        sc.close();
        return e;
    }
    public Enquiry DeleteEnquiry(Enquiry e) {
        e.setStatus("Deleted by Creator");
        return e;
    }

}