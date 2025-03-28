package Users;

import Entity.*;

import java.util.LinkedList;
//User is the superclass for Applicant, HDBManager and HDBOfficer
//will include basic necessary attributes and get/set methods
import java.util.Scanner;

public class User {

    private String name;
    private String NRIC;
    private int age;
    private String marital_status;
    private String password;

    public User(String n, String nric, int a, String m, String p) {
        this.name = n;
        this.NRIC = nric;
        this.age = a;
        this.marital_status = m;
        this.password = p;
    }

    public String getName() {
        return name;
    }
    public String getNRIC() {
        return NRIC;
    }
    public int getAge() {
        return age;
    }
    public String getMaritalStatus() {
        return marital_status;
    }
    public String getPassword() {
        return password;
    }    

    //helper function to view project details (independent function from filtering projects for types of users)
    public void getProjectDetails(Project proj) {
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

    //helper function to get enquiry details (independent function from filtering enquires for types of users)
    public void getEnquiryDetails(Enquiry e) {
        System.out.println("\nPurpose of Enquiry: " + e.getPurposeOfEnquiry() +
        "\nCreator of Enquiry: " + e.getCreatorOfEnquiry() 
        + "\nDetails: " + e.getDetails() + "\nDate: " + e.getDate() 
        + "\nStatus: " + e.getStatus() + "\nReply: " + e.getReply());
    }

    public void Login() {}
    public void Logout() {}
    
}
