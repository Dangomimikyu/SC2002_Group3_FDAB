package User;

import java.util.ArrayList;
import InteractableAttributePackage.*;
import InteractableAttributePackage.Request.ApplicationStatus;
import Managers.EnquiryManager;
import Database.*;
import Database.Database.DB_Action;
import Filter.Filter_ProjectName;
import Filter.IFilter;

//HDB Officer possess all applicant’s capabilities.
public class HDB_Officer extends Applicant{

    //unique status as officer_application is exclusive from applicant_applications
    public enum Enum_OfficerStatus {
        PENDING, SUCCESSFUL, UNSUCCESSFUL

    }

    public Project projectAssigned;
    public String project_name; //Managed Project Name
    public Enum_OfficerStatus officerStatus;
    private EnquiryManager enquiryManager = EnquiryManager.getInstance();

    public HDB_Officer(String name, String nric, int age, String marital, String pass, 
    String applied_proj, String appStatus, String bookedflattype, String managed_proj, String offStatus)
    {
        super(name, nric, age, marital, pass, applied_proj, appStatus, bookedflattype);
        this.UserPerms = usertype.OFFICER;
        this.project_name = managed_proj;
        this.officerStatus = switch(offStatus)
        {
            case "PENDING" -> Enum_OfficerStatus.PENDING;
            case "SUCCESSFUL" -> Enum_OfficerStatus.SUCCESSFUL;
            default -> Enum_OfficerStatus.UNSUCCESSFUL;
        };
    }

    //assign project to officer (initalisation), because users are initialised first, project won't be found
    //hence need to assign after projects are read
    public void InitialiseAssignedProject(String managed_proj) {
        this.projectAssigned = ProjectListingDB.getInstance().SearchDB(managed_proj);
    }


    /////////////////////////////////////////////////////////////////////////////
    ////////////////////// VIEW PROJECTS OPEN FOR OFFICER APPLICATIONS //////////
    /////////////////////////////////////////////////////////////////////////////

    public void viewProjectListForOfficers() {
        ArrayList<Project> projectList = ProjectListingDB.getInstance().getProjectDB();
        System.out.println("Projects open for officer positions:\n===================================");
        for (Project project : projectList) {
            //if project is active and has yet to reach max officer slots
            if (project.isActive() && !project.notAcceptingOfficers()) {
                System.out.println(project.getProjectDetails());
            }
        }
        if (projectList.size() == 0) {System.out.println("\nNo Active Projects with open positions available!"); }
    }

    /////////////////////////////////////////////////////////////////////////////
    ////////////////////// REGISTER FOR PROJECT /////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////

    //register for project as an officer
    public void RegisterForProject(String projectName) {
        try {
            Project pr = ProjectListingDB.getInstance().SearchDB(projectName);

            if (pr == null) { throw new Exception("\nError: Invalid project."); }

            //Cannot make a application if currently still have a request
            if (RequestsDB.getInstance().getRequestDB().stream().anyMatch(r -> this.userID.equals(r.initiator.userID))) {
                throw new Exception("\nError: You have unresolved/unread requests. Please resolve or read them before applying again.");
            }

            // Check if already applied for the project as an applicant
            if (this.AppliedProject != null && this.AppliedProject.equals(projectName)) {
                throw new Exception("\nError: You cannot register as an officer for a project you have applied for as an applicant.");
            }

            // Check if already an officer for another project with overlapping application periods
            for (Project project : ProjectListingDB.getInstance().getProjectDB()) {
                if (project.Details.OfficerList.contains(this)) {
                    boolean isOverlapping = !(pr.Details.CloseDate.isBefore(project.Details.OpenDate) ||
                            pr.Details.OpenDate.isAfter(project.Details.CloseDate));
                    if (isOverlapping) {
                        throw new Exception("You cannot register for this project as you are already an officer for another project with overlapping application periods.");
                    }
                }
            }

            //Submit officer application to DB
            Officer_Application officerApplication = new Officer_Application(this, projectName);
            this.officerStatus = Enum_OfficerStatus.PENDING;
            RequestsDB.getInstance().ModifyDB(officerApplication, DB_Action.ADD);
            UserInfoDB.getInstance().ModifyDB(this, DB_Action.EDIT);
            System.out.println("\nOfficer application submitted successfully. Waiting for manager approval." +
            "\nIn the meantime, you cannot apply for another project until your current application is resolved.");
        }
        catch (Exception e) { System.out.println( e.getMessage() ); }
    }

    /////////////////////////////////////////////////////////////////////////////
    ////////////////////// VIEW ASSIGNED PROJECT STATUS /////////////////////////
    /////////////////////////////////////////////////////////////////////////////

    public void viewAppliedProjectStatusOfficer() {
        try {
            if (this.projectAssigned == null) { throw new Exception("\nError: No project assigned."); }
            System.out.println(this.projectAssigned.getProjectDetails());
        }
        catch (Exception e) { System.out.println( e.getMessage() ); }
    }

    /////////////////////////////////////////////////////////////////////////////
    ////////////////////// VIEW HANDLED REQUESTS ////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////

    public void viewAllHandledRequests() {
        if (projectAssigned == null) {
            System.out.println("\nError: You are not managing any project! ");
        }
        ArrayList<IFilter> filters = new ArrayList<>();
        filters.add(new Filter_ProjectName(projectAssigned.Details.ProjectName, "BOOKING"));
        RequestsDB.getInstance().ViewDB(filters);
    }

    public void ResolveHandledRequest(int index, boolean approve)
    {
        try {

            //must exist, is a booking and can be handled by officer
            Request req = RequestsDB.getInstance().getRequestDB().get(index);

            if (req == null) { throw new Exception("\nError: could not find handled request"); }
            
            if (!req.RegardingProject.equals(this.project_name) || !(req instanceof Booking) || (req.status != ApplicationStatus.PENDING)) 
            { throw new Exception("\nError: Cannot Handle given Request!"); }
            

            req.handler = this;
            if (approve) { 
                req.initiator.AppliedProjectStatus = ApplicantStatus.BOOKED;
                req.initiator.flatTypeBooked = ((Booking)req).flatTypeToBook;
                UserInfoDB.getInstance().ModifyDB(req.initiator, DB_Action.EDIT);
                req.status = ApplicationStatus.APPROVED;
            }
            else { req.status = ApplicationStatus.REJECTED; }
            RequestsDB.getInstance().ModifyDB(req, DB_Action.EDIT);
            System.out.println("\nRequest successfully resolved");

            //auto generate receipt
            if (approve) { GenerateReceipt(req.initiator.userID); 
                System.out.println("\nRemember to update flat availability for your assigned project!");}
        }
        catch (Exception e) { System.out.println( e.getMessage()); }

    }

    /////////////////////////////////////////////////////////////////////////////
    ////////////////////// UPDATE FLAT SELECTION ////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////

    public void UpdateFlatSelection(Enum_FlatType flatType) {

        try {
            // Check if the flat type is valid
            if (flatType == null) { throw new Exception("Invalid flat type."); }

            // Update the flat selection based on availability
                if (flatType == Enum_FlatType.TWO_ROOM && this.projectAssigned.Details.NoOfUnitsLeft_2Room > 0) {
                    this.projectAssigned.SellUnit(Enum_FlatType.TWO_ROOM);
                    System.out.println("\nSuccessfully updated availablity of 2-Room flat");
                    ProjectListingDB.getInstance().ModifyDB(projectAssigned, DB_Action.EDIT);
                } else if (flatType == Enum_FlatType.THREE_ROOM && this.projectAssigned.Details.NoOfUnitsLeft_3Room > 0) {
                    this.projectAssigned.SellUnit(Enum_FlatType.THREE_ROOM);
                    System.out.println("\nSuccessfully updated availablity of 3-Room flat");
                    ProjectListingDB.getInstance().ModifyDB(projectAssigned, DB_Action.EDIT);
                } else { throw new Exception("\nError: No units available for the selected flat type."); }
        }
        catch (Exception e) { System.out.println( e.getMessage()); }
    }

    /////////////////////////////////////////////////////////////////////////////
    ////////////////////// VIEW HANDLED ENQUIRIES ///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////

    public void ViewEnquiries() {
        try {
            if (this.projectAssigned == null) { throw new Exception("No project assigned. Cannot view enquiries."); }
            enquiryManager.ViewEnquiries(this);
        }
        catch (Exception e) { System.out.println( e.getMessage()); }
    }

    /////////////////////////////////////////////////////////////////////////////
    ////////////////////// REPLY TO ENQUIRIES ///////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////
    
    public void ReplyToEnquiry(int index, String reply) {

        try {
            if (this.projectAssigned == null) { throw new Exception("\nError: No project assigned. Cannot reply to enquiries."); }

            Enquiry e = EnquiryDB.getInstance().getEnquiryDB().get(index);
            if ( !e.isUnreplied() || !projectAssigned.Details.ProjectName.equals(e.RegardingProject))
            { throw new Exception("\nError: Not allowed to handle given enquiry!"); }

            if (e.Enquirer.userID.equals(userID)) { throw new Exception("\nError: you cannot reply to your own created enquiries!");}

            enquiryManager.ReplyEnquiry(e, this, reply);
            
            System.out.println("\nSuccessfully replied to enquiry");
        }
        catch (Exception e) { System.out.println( e.getMessage()); }
    }

    /////////////////////////////////////////////////////////////////////////////
    ////////////////////// GENERATE RECEIPT /////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////

    public void GenerateReceipt(String applicantNRIC) {
        try {
            if (this.projectAssigned == null) { throw new Exception("\nError: No project assigned. Cannot generate receipt."); }

            Applicant applicant = (Applicant) UserInfoDB.getInstance().getUserDB().stream()
                    .filter(user -> user instanceof Applicant && user.userID.equals(applicantNRIC))
                    .findFirst()
                    .orElse(null);

            if (applicant == null) { throw new Exception("\nError: Applicant not found."); }

            if (!applicant.AppliedProject.equals(this.projectAssigned.Details.ProjectName) ||
                    !applicant.AppliedProjectStatus.equals(Applicant.ApplicantStatus.BOOKED)) {
                throw new Exception("\nError: The applicant has not booked a flat in the assigned project.");
            }

            Receipt receipt = new Receipt(
                    new Applicant_Application(applicant, this.projectAssigned.Details.ProjectName),
                    applicant.flatTypeBooked,
                    this.projectAssigned.Details
            );

            System.out.println(receipt.getReceiptDetails());
        }
        catch (Exception e) { System.out.println( e.getMessage()); }
    }
    
}