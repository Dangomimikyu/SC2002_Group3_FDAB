package User;

import Database.*;
import Database.Database.DB_Action;
import Filter.Filter_Marital;
import Filter.Filter_Visibility;
import Filter.IFilter;
import InteractableAttributePackage.*;
import InteractableAttributePackage.Request.ApplicationStatus;
import Managers.EnquiryManager;
import User.HDB_Officer.Enum_OfficerStatus;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Applicant extends SystemUser{
    Scanner scanner = new Scanner(System.in);

    public enum ApplicantStatus {
        UNSUCCESSFUL, PENDING, SUCCESSFUL, BOOKED

        // PENDING means the applicant has is waiting for approval from the officer
        // SUCCESSFUL means the applicant has successfully applied for a project
        // UNSUCCESSFUL means the applicant has gotten their application rejected, withdrawal approved or have yet to sent an application(can re-apply)
        // BOOKED means the applicant has successfully booked a flat
        // general progression of the application status is UNSUCCESSFUL -> PENDING -> SUCCESSFUL -> BOOKED
        // note that officers that have applied as an officer of a project has no bearing on their ApplicantStatus as will be handed by the OfficerManager class
        // this means that an officer can be both an applicant and an officer, as long as they are not the same project
    }

    public enum MaritalStatus {
        SINGLE, MARRIED, ALL
    }

    public int age;
    public MaritalStatus maritalStatus;
    public String AppliedProject;
    public ApplicantStatus AppliedProjectStatus = ApplicantStatus.UNSUCCESSFUL;
    private EnquiryManager enquiryManager = EnquiryManager.getInstance();
    public Enum_FlatType flatTypeBooked;

    public Applicant(String n, String nric, int a, String m, String p, String appliedProj, String projStatus, String bookedflattype) {
        super(nric, p, n);
        this.UserPerms = usertype.APPLICANT;
        this.age = a;
        this.maritalStatus = MaritalStatus.valueOf(m);
        this.AppliedProject = appliedProj;
        this.AppliedProjectStatus = switch(projStatus) {
            case "SUCCESSFUL" -> ApplicantStatus.SUCCESSFUL;
            case "PENDING" -> ApplicantStatus.PENDING;
            case "BOOKED" -> ApplicantStatus.BOOKED;
            default -> ApplicantStatus.UNSUCCESSFUL;
        };
        this.flatTypeBooked = switch(bookedflattype) {
            case "TWO_ROOM" -> Enum_FlatType.TWO_ROOM;
            case "THREE_ROOM" -> Enum_FlatType.THREE_ROOM;
            default -> Enum_FlatType.DEFAULT;
        };
    }

    //applicants can only view projects that are open to their user group (S or M) and visibility turned on
    public void viewProjectList(ArrayList<IFilter> activeFilters) {

        ArrayList<IFilter> Filters = new ArrayList<>(activeFilters);
        Filters.add(new Filter_Marital(this.maritalStatus));
        Filters.add(new Filter_Visibility());
        ProjectListingDB.getInstance().ViewDB(Filters);

    }

    //apply for a project.
    public void ApplyProject(String projectName) {
        Project pr = ProjectListingDB.getInstance().getProjectDB().stream()
                .filter(p -> p.Details.ProjectName.equals(projectName))
                .findFirst()
                .orElse(null);
        if (pr == null) {
            System.out.println("\nError: Project not found.");
            return;
        }

        //Cannot make a application if currently still have a request
        if (RequestsDB.getInstance().getRequestDB().stream().anyMatch(r -> this.userID.equals(r.initiator.userID))) {
            System.out.println("\nError: You have unresolved/unread requests. Please resolve or read them before applying again.");
            return;
        }

        //Applicant must not be an existing applicant of another project
        if (this.AppliedProjectStatus != ApplicantStatus.UNSUCCESSFUL)   {
            System.out.println("\nError: you have already applied for a project");
            return;
        }

        //project must be open for application.
        if (!pr.isOpenForApplication()) {
            System.out.println("\nError: This project is not open for application.");
            return;
        }

        //if is an officer: check if officer_status is PENDING. This is to ensure if they happen to apply as an officer of this project, 
        //they will not be an officer and applicant of the same project. Also obeys the rule of one request at a time.
        if (this instanceof HDB_Officer) {
            if (((HDB_Officer)this).officerStatus == Enum_OfficerStatus.PENDING) {
                System.out.println("\nError: Your current officer application has yet to be resolved. Wait until it is to apply again.");
            }
        }

        //Vice versa: IF the applicant is also an HDB_Officer and already an existing officer of the same project, cannot apply as well
        if (pr.Details.OfficerList.stream().anyMatch(officer -> officer.userID.equals(this.userID))) {
            System.out.println("\nError: You cannot apply for a project where you are already an existing Officer.");
            return;
        }

        //Project must be open to applicant's marital status to apply
        String applicantGroup = this.maritalStatus.name();
        String projectGroup = pr.Details.OpentoUserGroup.name();
        if (!(projectGroup.equals("ALL") || projectGroup.equals(applicantGroup))) {
            System.out.println("\nError: his project is not open to your marital status group.");
            return;
        }

        // If all checks passed, create a request (Applicant_Application) and add to the database
        // also modify user's applicant status to PENDING
        Applicant_Application application = new Applicant_Application(this, projectName);
        RequestsDB.getInstance().ModifyDB(application, Database.DB_Action.ADD);
        this.AppliedProjectStatus = ApplicantStatus.PENDING;
        UserInfoDB.getInstance().ModifyDB(this, Database.DB_Action.EDIT);
        System.out.println("\nApplication submitted successfully. Waiting for manager approval." +
        "\nIn the meantime, you cannot apply for another project until your current application is resolved.");
    }


    //request withdrawal from applied project
    public void RequestWithdrawal() {

        // Check if the applicant has applied for a project
        if (this.AppliedProjectStatus == ApplicantStatus.UNSUCCESSFUL || this.AppliedProjectStatus == ApplicantStatus.PENDING) {
            System.out.println("\nError: You are currently not fully applied for any project. No withdrawal request can be made.");
            return;
        }

        //Cannot make withdrawal if currently still have a request
        if (RequestsDB.getInstance().getRequestDB().stream().anyMatch(r -> this.userID.equals(r.initiator.userID))) {
            System.out.println("\nError: You have unresolved/unread requests. Please resolve or read them before withdrawing again.");
            return;
        }

        //if is an officer and is currently applying for an officer positon for any project, cannot withdraw from their applied as an applicant project
        //NOTE: not the same as withdrawing from officer positon, which they cannot in the first place.
        if (this instanceof HDB_Officer) {
            if (((HDB_Officer)this).officerStatus == Enum_OfficerStatus.PENDING) {
                System.out.println("\nError: Your current officer application has yet to be resolved. Wait until it is to withdraw from your applied project.");
            }
        }        

        // Create a withdrawal request
        Withdrawal withdrawalRequest = new Withdrawal(this, this.AppliedProject);
        //if already booked, add the flat type book so that manager know which unit to edit in project
        //else, default means haven't booekd yet
        if (this.flatTypeBooked.equals(Enum_FlatType.TWO_ROOM)) {
            withdrawalRequest.BookedFlatType = Enum_FlatType.TWO_ROOM;
        } else if (this.flatTypeBooked.equals(Enum_FlatType.THREE_ROOM)) {
            withdrawalRequest.BookedFlatType = Enum_FlatType.THREE_ROOM;
        }

        // Add the request to the RequestsDB
        RequestsDB.getInstance().ModifyDB(withdrawalRequest, Database.DB_Action.ADD);
        System.out.println("\nWithdrawal request submitted successfully. Waiting for manager approval." +
        "\nIn the meantime, you cannot apply for another project until your current withdrawal is resolved.");
    }


    //Request for a booking
    public void BookFlat() {

        // Check if the applicant has applied for the project successfully
        if (this.AppliedProjectStatus != ApplicantStatus.SUCCESSFUL) {
            System.out.println("\nError: You have yet to apply for a project or have already booked a flat, or is waiting for a request to be solved.");
            return;
        }

        //Cannot make booking if currently still have a request
        if (RequestsDB.getInstance().getRequestDB().stream().anyMatch(r -> this.userID.equals(r.initiator.userID))) {
            System.out.println("\nError: You have unresolved/unread requests. Please resolve or read them before booking again.");
            return;
        }

        // Get the project details
        Project project = ProjectListingDB.getInstance().getProjectDB().stream()
                .filter(p -> p.Details.ProjectName.equals(this.AppliedProject))
                .findFirst()
                .orElse(null);

        if (project == null) {
            System.out.println("\nError: Project not found.");
            return;
        }

        System.out.println("Available flat types for project: " + project.Details.ProjectName 
        + "\n==========================================================");

        // Show available flat types based on marital status
        // Single, 35 y/o and above, can ONLY apply for 2-Room
        // Married, 21 y/0 and above, can apply for both
        int choice = -1;
        if (this.maritalStatus == MaritalStatus.SINGLE && this.age >= 35) {
            System.out.println("You are eligible for only a 2-room flat in " + project.Details.ProjectName);
            System.out.println("2-Room units left: " + project.Details.NoOfUnitsLeft_2Room +
                               "\n2-Room unit selling price: " + project.Details.SellingPrice_2Room);
            System.out.println("Please enter 1 if you want to book a 2-Room flat or 0 to decline booking for now: ");
        }
        else if (this.maritalStatus == MaritalStatus.MARRIED && this.age >= 21) {
            System.out.println("You are eligible for both a 2-room and 3-room flat in " + project.Details.ProjectName);
            System.out.println("2-Room units left: " + project.Details.NoOfUnitsLeft_2Room +
                               "\n2-Room unit selling price: " + project.Details.SellingPrice_2Room);
            System.out.println("3-Room units left: " + project.Details.NoOfUnitsLeft_3Room +
                               "\n3-Room unit selling price: " + project.Details.SellingPrice_3Room);
            System.out.println("Please enter 1 if you want to book a 2-Room flat or 2 to book a 3-room flat, or 0 to decline for now: ");
        }
        else {
            System.out.println("You are not eligible to book for any flats." + 
            "\nSingles and 35 y/o and above are only eligible for 2-room flats" +
            "\nMarried and 21 y/o and above are eligible for both 2-room flats and 3-room flats");
            return;
        }

        while (choice != 2 && choice != 1 && choice != 0) {
            try {
                choice = scanner.nextInt();
            }
            catch (InputMismatchException e) {
                System.out.println("\nError: Invalid character");
                scanner.nextLine(); // ignore and move the cursor to next line
                continue; 
            }
        }
        if (choice == 1) {
            if (project.Details.NoOfUnitsLeft_2Room == 0) {
                System.out.println("\nError: There is no more available units of this type. Returning to main menu...");
                return;
            }
            Booking booking = new Booking(this, this.AppliedProject, Enum_FlatType.TWO_ROOM);
            RequestsDB.getInstance().ModifyDB(booking, DB_Action.ADD);
            System.out.println("\nYou have successfully booked a 2-Room flat in " + project.Details.ProjectName + 
            "\nAwaiting approval from an officer of your applied project.");
        }
        else if (choice == 2) {
            if (project.Details.NoOfUnitsLeft_3Room == 0) {
                System.out.println("\nError: There is no more available units of this type. Returning to main menu...");
                return;
            }
            else if (this.maritalStatus == MaritalStatus.SINGLE && this.age >= 35) {
                System.out.println("\nError: 2 indicates 3-room flats which you can't book. Returning to main menu...");
                return;
            }           
            Booking booking = new Booking(this, this.AppliedProject, Enum_FlatType.THREE_ROOM);
            RequestsDB.getInstance().ModifyDB(booking, DB_Action.ADD);
            System.out.println("\nYou have successfully booked a 3-Room flat in " + project.Details.ProjectName + 
            "\nAwaiting approval from an officer of your applied project.");
        }
        
    }

    //create an enquiry. Cannot have the same title and regarding same project
    public void CreateEnquiry() {
        System.out.println("Enter the title of the enquiry:");
        String title = scanner.nextLine();
        System.out.println("Enter the name of the project you are referring to:");
        String projectName = scanner.nextLine();
        if (enquiryManager.isNotUniqueEnquiry(title, this, projectName)) {
            System.out.println("\nError: Cannot make an enquiry about a project with a similar title!");
            return;
        }
        if (!ProjectListingDB.getInstance().getProjectDB().stream().anyMatch(p -> p.Details.ProjectName.equals(projectName))) {
            System.out.println("\nError: Project cannot be found!");
            return;
        }
        System.out.println("Enter the description of the enquiry:");
        String description = scanner.nextLine();
        Enquiry newEnquiry = new Enquiry(title, projectName, description, this);
        enquiryManager.AddNewEnquiry(newEnquiry);
        System.out.println("\nEnquiry has been created. Waiting to be replied.");
    }

    //only view all created enquiries
    public void ViewEnquiry() {
        enquiryManager.ViewEnquiries(this);
    }

    //cannot edit enquiries if replied to
    public void EditEnquiry() {
        System.out.println("Enter the title of the enquiry you want to edit:");
        String title = scanner.nextLine();
        System.out.println("Enter the project name your enquiry to delete is regarding: ");
        String projname = scanner.nextLine();
        Enquiry enquiry = enquiryManager.getEnquiryWithDetails(title, this, projname);
        if (enquiry != null) {
            if (!enquiry.isUnreplied()) {
                System.out.println("\nError: Can't edit an enquiry if it's already replied to");
                return;
            }
            System.out.println("Enter the new description:");
            String newDescription = scanner.nextLine();
            enquiry.Description = newDescription;
            enquiryManager.EditEnquiry(enquiry);
            System.out.println("Enquiry updated: " + enquiry.getEnquiryDetails());
        } else {
            System.out.println("Error: Enquiry not found or you are not the creator.");
        }
    }

    //cannot delete enquiries if replied to
    public void DeleteEnquiry() {
        System.out.println("Enter the title of the enquiry you want to delete: ");
        String title = scanner.nextLine();
        System.out.println("Enter the project name your enquiry to delete is regarding: ");
        String projname = scanner.nextLine();
        Enquiry enquiry = enquiryManager.getEnquiryWithDetails(title, this, projname);
        if (enquiry != null) {
            if (!enquiry.isUnreplied()) {
                System.out.println("\nError: Can't delete an enquiry if it's already replied to");
                return;
            }
            enquiryManager.DeleteEnquiry(enquiry);
            System.out.println("\nEnquiry successfully deleted: " + title);
        } else {
            System.out.println("\"Error: Enquiry not found or you are not the creator.");
        }
    }

    //Applicant's can check their applied project's details
    public void viewAppliedProjectStatus() {
        Project proj = ProjectListingDB.getInstance().getProjectDB().stream()
        .filter(p -> p.Details.ProjectName.equals(AppliedProject)).findFirst().orElse(null);
        if (proj == null) {
            System.out.println("\nError: You are not applied to any project!");
            return;
        }
        System.out.println(proj.getProjectDetails());
    }

    //Applicant can check any unread requests that have been resolved upon log-in. Auto-deleted after reading if resolved.
    public void checkRequestStatus() {
        Request req = RequestsDB.getInstance().getRequestDB().stream()
        .filter(r -> r.initiator.userID.equals(userID)).findFirst().orElse(null);
        if (req == null) {
            System.out.println("\nError: you have no requests!");
            return;
        }
        System.out.println(req.getRequestDetails());
        if (req.status == ApplicationStatus.APPROVED) {
            System.out.println("\nNote: will auto-delete after reading details");
            RequestsDB.getInstance().ModifyDB(req, DB_Action.DELETE);

        }
    }

}