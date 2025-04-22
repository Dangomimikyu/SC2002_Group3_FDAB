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

public class Applicant extends SystemUser{

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
        this.AppliedProject = appliedProj;
        this.maritalStatus = switch(m) {
            case "MARRIED" -> MaritalStatus.MARRIED;
            case "SINGLE" -> MaritalStatus.SINGLE;
            default -> MaritalStatus.ALL;
        };
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

    public String getUserDetails() {
        return super.getUserDetails() + 
               "\nAge: " + age +
               "\nMarital Status: " + maritalStatus.toString() +
               "\nProject Status: " +  AppliedProjectStatus.toString() +
               "\nFlat Type Booked: " + (flatTypeBooked == Enum_FlatType.DEFAULT ? "nil" : flatTypeBooked.toString());
    }

    ////////////////////////////////////////////////////////////////
    ////////////////// VIEW PROJECTS ///////////////////////////////
    ////////////////////////////////////////////////////////////////

    //applicants can only view projects that are open to their user group (S or M) and visibility turned on
    public void viewProjectList(ArrayList<IFilter> activeFilters) {

        ArrayList<IFilter> Filters = new ArrayList<>(activeFilters);
        Filters.add(new Filter_Marital(this.maritalStatus));
        Filters.add(new Filter_Visibility());
        ProjectListingDB.getInstance().ViewDB(Filters);

    }

    ////////////////////////////////////////////////////////////////
    ////////////////// APPLY FOR PROJECT ///////////////////////////
    ////////////////////////////////////////////////////////////////

    public void ApplyProject(String projectName) {

        try {
            Project pr = ProjectListingDB.getInstance().SearchDB(projectName);
                    
            if (pr == null) { throw new Exception("\nError: Project not found."); }

            //Cannot make a application if currently still have a request
            if (RequestsDB.getInstance().getRequestDB().stream().anyMatch(r -> this.userID.equals(r.initiator.userID))) {
                throw new Exception("\nError: You have unresolved/unread requests. Please resolve or read them before applying again."); }

            //Applicant must not be an existing applicant of another project
            if (this.AppliedProjectStatus != ApplicantStatus.UNSUCCESSFUL)   {
                throw new Exception("\nError: you have already applied for a project"); }

            //project must be active to apply.
            if (!pr.isActive()) { throw new Exception("\nError: This project is not open for application."); }

            //if is an officer: check if officer_status is PENDING. This is to ensure if they happen to apply as an officer of this project, 
            //they will not be an officer and applicant of the same project. Also obeys the rule of one request at a time.
            if (this instanceof HDB_Officer) {
                if (((HDB_Officer)this).officerStatus == Enum_OfficerStatus.PENDING) {
                    throw new Exception("\nError: Your current officer application has yet to be resolved. Wait until it is to apply again.");
                }
            }

            //Vice versa: IF the applicant is also an HDB_Officer and already an existing officer of the same project, cannot apply as well
            if (pr.Details.OfficerList.stream().anyMatch(officer -> officer.userID.equals(this.userID))) {
                throw new Exception("\nError: You cannot apply for a project where you are already an existing Officer.");
            }

            //Project must be open to applicant's marital status to apply
            String applicantGroup = this.maritalStatus.name();
            String projectGroup = pr.Details.OpentoUserGroup.name();
            if (!(projectGroup.equals("ALL") || projectGroup.equals(applicantGroup))) {
                throw new Exception("\nError: his project is not open to your marital status group."); }

            // If all checks passed, create a request (Applicant_Application) and add to the database
            // also modify user's applicant status to PENDING
            Applicant_Application application = new Applicant_Application(this, projectName);
            RequestsDB.getInstance().ModifyDB(application, Database.DB_Action.ADD);
            this.AppliedProjectStatus = ApplicantStatus.PENDING;
            UserInfoDB.getInstance().ModifyDB(this, Database.DB_Action.EDIT);
            System.out.println("\nApplication submitted successfully. Waiting for manager approval." +
            "\nIn the meantime, you cannot apply for another project until your current application is resolved.");
        }
        catch (Exception e) { System.out.println( e.getMessage()); }
    }

    ////////////////////////////////////////////////////////////////
    ////////////////// REQUEST WITHDRAWAL //////////////////////////
    ////////////////////////////////////////////////////////////////
    
    //request withdrawal from applied project
    public void RequestWithdrawal() {

        try {
            // Check if the applicant has applied for a project
            if (this.AppliedProjectStatus == ApplicantStatus.UNSUCCESSFUL || this.AppliedProjectStatus == ApplicantStatus.PENDING) {
                throw new Exception("\nError: You are currently not fully applied for any project. No withdrawal request can be made.");
            }

            //Cannot make withdrawal if currently still have a request
            if (RequestsDB.getInstance().getRequestDB().stream().anyMatch(r -> this.userID.equals(r.initiator.userID))) {
                throw new Exception("\nError: You have unresolved/unread requests. Please resolve or read them before withdrawing again.");
            }

            //if is an officer and is currently applying for an officer positon for any project, cannot withdraw from their applied as an applicant project
            //NOTE: not the same as withdrawing from officer positon, which they cannot in the first place.
            if (this instanceof HDB_Officer) {
                if (((HDB_Officer)this).officerStatus == Enum_OfficerStatus.PENDING) {
                    throw new Exception("\nError: Your current officer application has yet to be resolved. Wait until it is to withdraw from your applied project.");
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
        catch (Exception e) { System.out.println( e.getMessage()); }
    }

    ////////////////////////////////////////////////////////////////
    ////////////////// BOOK FLAT ///////////////////////////////////
    ////////////////////////////////////////////////////////////////

    //Request for a booking
    public void BookFlat(Enum_FlatType flat_to_book) {

        try {
            // CheckProjStatus: Check if the applicant has applied for the project successfully
            if (this.AppliedProjectStatus != ApplicantStatus.SUCCESSFUL) {
                throw new Exception("\nError: You have yet to apply for a project or have already booked a flat, or is waiting for a request to be solved.");
            }

            // CheckCurrentRequest: Cannot make booking if currently still have a request
            if (RequestsDB.getInstance().getRequestDB().stream().anyMatch(r -> this.userID.equals(r.initiator.userID))) {
                throw new Exception("\nError: You have unresolved/unread requests. Please resolve or read them before booking again.");
            }

            Project project = ProjectListingDB.getInstance().SearchDB(this.AppliedProject);

            if (project == null) { throw new Exception("\nError: Project not found."); }

            // CheckEligibility and if have available units:
            // Show available flat types based on marital status
            // Single, 35 y/o and above, can ONLY apply for 2-Room
            // Married, 21 y/0 and above, can apply for both

            if (flat_to_book == Enum_FlatType.TWO_ROOM) {
                if (!(this.maritalStatus == MaritalStatus.SINGLE && this.age >= 35) ||
                 !(this.maritalStatus == MaritalStatus.MARRIED && this.age >= 21) )
                 { throw new Exception("\nError: You are not eligible for this flat!"); }
                if (project.Details.NoOfUnitsLeft_2Room == 0) {
                    throw new Exception("\nError: There is no more available units of this type");
                }
                Booking booking = new Booking(this, this.AppliedProject, Enum_FlatType.TWO_ROOM);
                RequestsDB.getInstance().ModifyDB(booking, DB_Action.ADD);
                System.out.println("\nYou have successfully booked a 2-Room flat in " + project.Details.ProjectName + 
                "\nAwaiting approval from an officer of your applied project.");
            }
            else if (flat_to_book == Enum_FlatType.THREE_ROOM) {
                if (!(this.maritalStatus == MaritalStatus.MARRIED && this.age >= 21))
                { throw new Exception("\nError: You are not eligible for this flat!"); }
                if (project.Details.NoOfUnitsLeft_3Room == 0) {
                    throw new Exception("\nError: There is no more available units of this type");
                }
                Booking booking = new Booking(this, this.AppliedProject, Enum_FlatType.THREE_ROOM);
                RequestsDB.getInstance().ModifyDB(booking, DB_Action.ADD);
                System.out.println("\nYou have successfully booked a 3-Room flat in " + project.Details.ProjectName + 
                "\nAwaiting approval from an officer of your applied project.");
            }
        }
        catch (Exception e) { System.out.println( e.getMessage()); }
    }

    ////////////////////////////////////////////////////////////////
    ////////////////// CREATE ENQUIRY //////////////////////////////
    ////////////////////////////////////////////////////////////////

    //create an enquiry. Cannot have the same title and regarding same project
    public void CreateEnquiry(String title, String projectName, String description) {
        try {
            if (enquiryManager.isNotUniqueEnquiry(title, this, projectName)) {
                throw new Exception("\nError: Cannot make an enquiry about a project with a similar title!");
            }
            if (!ProjectListingDB.getInstance().getProjectDB().stream().anyMatch(p -> p.Details.ProjectName.equals(projectName))) {
                throw new Exception("\nError: Project cannot be found!");
            }
            Enquiry newEnquiry = new Enquiry(title, projectName, description, this);
            enquiryManager.AddNewEnquiry(newEnquiry);
            System.out.println("\nEnquiry has been created. Waiting to be replied.");
        }
        catch (Exception e) { System.out.println( e.getMessage()); }
    }

    ////////////////////////////////////////////////////////////////
    ////////////////// VIEW ENQUIRY   //////////////////////////////
    ////////////////////////////////////////////////////////////////

    //only view all created enquiries
    public void ViewEnquiry() {
        if (this instanceof HDB_Officer) { 
            //placeholder applicant variant of hdbofficer. this is so that officers can view created enquiries in applicant menu
            enquiryManager.ViewEnquiries(new Applicant("", this.userID, -1,"", "","","", "")); 
        }
        else { enquiryManager.ViewEnquiries(this); }
    }

    ////////////////////////////////////////////////////////////////
    ////////////////// EDIT ENQUIRY   //////////////////////////////
    ////////////////////////////////////////////////////////////////

    //cannot edit enquiries if replied to
    public void EditEnquiry(String title, String projectName, String description) {
        try {
            Enquiry enquiry = enquiryManager.getEnquiryWithDetails(title, this, projectName);
            if (enquiry != null) {
                if (!enquiry.isUnreplied()) {
                    throw new Exception("\nError: Can't edit an enquiry if it's already replied to");
                }
                enquiry.Description = description;
                enquiryManager.EditEnquiry(enquiry);
                System.out.println("Enquiry updated: " + enquiry.getEnquiryDetails());
            } else { throw new Exception("Error: Enquiry not found or you are not the creator."); }
            }
            catch (Exception e) { System.out.println( e.getMessage()); }
    }

    ////////////////////////////////////////////////////////////////
    ////////////////// DELETE ENQUIRY  /////////////////////////////
    ////////////////////////////////////////////////////////////////

    //cannot delete enquiries if replied to
    public void DeleteEnquiry(String title, String projectName) {
        try {
            Enquiry enquiry = enquiryManager.getEnquiryWithDetails(title, this, projectName);
            if (enquiry == null) { throw new Exception("\"Error: Enquiry not found or you are not the creator."); }
            if (!enquiry.isUnreplied()) {
                throw new Exception("\nError: Can't delete an enquiry if it's already replied to");
            }
            enquiryManager.DeleteEnquiry(enquiry);
            System.out.println("\nEnquiry successfully deleted: " + title);
        }
        catch (Exception e) { System.out.println( e.getMessage()); }
    }

    ////////////////////////////////////////////////////////////////
    ////////////////// VIEW APPLIED PROJECT STATUS  ////////////////
    ////////////////////////////////////////////////////////////////
    
    //Applicant's can check their applied project's details
    public void viewAppliedProjectStatus() {
        try {
            Project proj = ProjectListingDB.getInstance().SearchDB(AppliedProject);
            if (proj == null) { throw new Exception("\nError: You are not applied to any project!"); }
            System.out.println(proj.getProjectDetails());
        }
        catch (Exception e) { System.out.println( e.getMessage()); }
    }

    ////////////////////////////////////////////////////////////////
    ////////////////// CHECK REQUEST STATUS  ///////////////////////
    ////////////////////////////////////////////////////////////////
    
    //Applicant can check any unread requests that have been resolved upon log-in. Auto-deleted after reading if resolved.
    public void checkRequestStatus() {
        try {
            Request req = RequestsDB.getInstance().SearchDB(userID);
            if (req == null) { throw new Exception("\nError: you have no requests!"); }
            System.out.println(req.getRequestDetails());
            if (req.status == ApplicationStatus.APPROVED) {
                System.out.println("\nNote: will auto-delete after reading details");
                RequestsDB.getInstance().ModifyDB(req, DB_Action.DELETE);

            }
        }
        catch (Exception e) { System.out.println( e.getMessage()); }
    }

}