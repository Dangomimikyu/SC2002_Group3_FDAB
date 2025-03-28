package Users;
import java.util.Scanner;

import Entity.*;

public class HDBManager extends User{

    public HDBManager(String n, String nric, int a, String m, String p) {
        super(n, nric, a, m, p);
    }

    // • Able to view enquiries of ALL projects.
    // • Able to view and reply to enquiries regarding the project he/she is
    // handling. 
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

    //create,edit,delete BTO project listings
    //Toggle project's visibility
    //View projects created by other creators, regardless of visibility (filterable)
    //View and reject/approve HDB Officer Registration
    //Approve/reject Applicant's BTO Application
    //Approve/reject Applicant's BTO Withdrawal
    //Generate Report (filterable)
}

// • Able to create, edit, and delete BTO project listings.
// • A BTO project information entered by the HDB Manager will include
// information like:
// o Project Name
// o Neighborhood (e.g. Yishun, Boon Lay, etc.)
// o Types of Flat – Assume there are only 2-Room and 3-Room
// o The number of units for the respective types of flat
// o Application opening date
// o Application closing date
// o HDB Manager in charge (automatically tied to the HDB Manager
// who created the listing)
// o Available HDB Officer Slots (max 10)
// • Can only be handling one project within an application period (from
// application opening date, inclusive, to application closing date,
// inclusive)
// • Able to toggle the visibility of the project to “on” or “off”. This will be
// reflected in the project list that will be visible to applicants.
// • Able to view all created projects, including projects created by other
// HDB Manager, regardless of visibility setting.
// • Able to filter and view the list of projects that they have created only.
// • Able to view pending and approved HDB Officer registration.
// • Able to approve or reject HDB Officer’s registration as the HDB
// Manager in-charge of the project – update project’s remaining HDB
// Officer slots
// • Able to approve or reject Applicant’s BTO application – approval is
// limited to the supply of the flats (number of units for the respective flat
// types)
// • Able to approve or reject Applicant's request to withdraw the application.
// • Able to generate a report of the list of applicants with their respective
// flat booking – flat type, project name, age, marital status
// o There should be filters to generate a list based on various categories
// (e.g. report of married applicants’ choice of flat type)
// • Cannot apply for any BTO project as an Applicant.