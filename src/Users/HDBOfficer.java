package Users;

import java.util.LinkedList;
import java.util.Scanner;

import Entity.*;

// • HDB Officer possess all applicant’s capabilities.
public class HDBOfficer extends Applicant{

    public HDBOfficer(String n, String nric, int a, String m, String p) {
        super(n, nric, a, m, p);
    }

    //Register to join a project team and view its status (mutually exclusive for the same project)
    // • Able to register to join a project if the following criteria are meant:
    // o No intention to apply for the project as an Applicant (Cannot apply
    // for the project as an Applicant before and after becoming an HDB
    // Officer of the project)
    // o Not a HDB Officer (registration not approved) for another project
    // within an application period (from application opening date,
    // inclusive, to application closing date, inclusive)

    // • Registration to be a HDB Officer of the project is subject to approval
    // from the HDB Manager in charge of the project.
    // Once approved, their profile will reflect the project he/she is a HDB
    // Officer for.

    // • Able to apply for other projects in which he/she is not handling – Once
    // applied for a BTO project, he/she cannot register to handle the same
    // project
    public void RegisterforProj(Project proj) {}

    //Flat selection work: update applicant's profile with type of flat chosen under the project and no of flats for each flat type remaining
    //Generate flat selection receipt

    // • Able to see the status of their registration to be a HDB Officer for a
    // project
    public void viewProjectStatus(Project proj) {}

    // • Able to view and reply to enquiries regarding the project he/she is handling
    public void viewEnquiry(Enquiry e) {
        System.out.println("\nPurpose of Enquiry: " + e.getPurposeOfEnquiry() + "\nDetails: " + e.getDetails() + "\nDate: " + e.getDate() 
        + "\nStatus: " + e.getStatus() + "\nReply: " + e.getReply());
    }
    public Enquiry replytoEnquiry(Enquiry e) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your reply to this enquiry: ");
        String reply = sc.nextLine();
        sc.close();
        e.setReply(reply);
        return e;
    }

    //View project details,regardless of visibility settings
    public void viewProjectDetails(Project proj) {
        System.out.println("\nProject Name: " + proj.getProjectName() + 
                           "\nNeighbourhood: " + proj.getNeighbourhood() +
                           "\nType 1: " + proj.getType1() +
                           "\nNumber of Units for Type 1: " + proj.getNoOfUnitsForType1() +
                           "\nSelling Price for Type 1: " + proj.getSellingPriceForType1() +
                           "\nType 2: " + proj.getType2() +
                           "\nNumber of Units for Type 2: " + proj.getNoOfUnitsForType2() +
                           "\nSelling Price for Type 2: " + proj.getSellingPriceForType2() +
                           "\nApplication Opening Date: " + proj.getApplicantOpeningDate() +
                           "\nApplication Closing Date: " + proj.getApplicantClosingDate() +
                           "\nManagers in Charge: " + proj.getManagerInCharge() +
                           "\nOfficer Slots: " + proj.getOfficerSlots() +
                           "\nOfficers assigned: " + proj.getOfficersInCharge());
    }

    // • Able to view the details of the project he/she is handling regardless of
    // the visibility setting.

    // • All users can use filters to view project (location, flat types, etc.) Assume
    // that default is by alphabetical order. User filter settings are saved when
    // they switch menu pages. 

    // • Not allowed to edit the project details.
    public void ViewProjectsOpentoUser(LinkedList <Project> all_projects, String filter) {
        for (Project proj : all_projects) {
            if (proj.getGroupProjOpento().equals(getMaritalStatus())) {
                viewProjectDetails(proj);
            }
        }
    }

}


// o With Applicant’s successful BTO application, HDB Officer’s flat
// selection responsibilities:
//  Update the number of flat for each flat type that are remaining
//  Retrieve applicant’s BTO application with applicant’s NRIC
//  Update applicant’s project status, from “successful” to
// “booked”.
//  Update applicant’s profile with the type of flat (2-Room or 3-
// Room) chosen under a project
// • Able to generate receipt of the applicants with their respective flat
// booking details – Applicant’s Name, NRIC, age, marital status, flat type
// booked and its project details.