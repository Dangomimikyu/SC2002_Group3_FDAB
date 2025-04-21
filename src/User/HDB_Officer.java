package User;

import java.util.ArrayList;
import java.util.stream.Collectors;
import InteractableAttributePackage.*;
import InteractableAttributePackage.Request.ApplicationStatus;
import Database.*;
import Database.Database.DB_Action;

//HDB Officer possess all applicant’s capabilities.
public class HDB_Officer extends Applicant{

    //unique status as officer_application is exclusive from applicant_applications
    public enum Enum_OfficerStatus {
        PENDING, SUCCESSFUL, UNSUCCESSFUL

    }

    public Project projectAssigned;
    public String project_name; //Managed Project Name
    public Enum_OfficerStatus officerStatus;
    //private ArrayList<Applicant> applicantList;
    //private Map<String, Integer> flatAvailability;
    //private String officerID;
    //private ReceiptGenerator receiptGenerator = new ReceiptGenerator();

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

    //views all projects that are accepting officers
    public void viewProjectListForOfficers() {
        ArrayList<Project> projectList = ProjectListingDB.getInstance().getProjectDB();
        System.out.println("Projects open for officer positions:\n===================================");
        for (Project project : projectList) {
            //if project is active and has yet to reach max officer slots
            if (project.isOpenForApplication() && project.Details.visibility && !project.notAcceptingOfficers()) {
                System.out.println(project.getProjectDetails());
            }
        }
    }

    //register for project as an officer
    public void RegisterForProject(String projectName) {
        Project pr = ProjectListingDB.getInstance().getProjectDB().stream()
                .filter(p -> p.Details.ProjectName.equals(projectName))
                .findFirst()
                .orElse(null);
        if (pr == null) {
            System.out.println("\nError: Invalid project.");
            return;
        }

        //Cannot make a application if currently still have a request
        if (RequestsDB.getInstance().getRequestDB().stream().anyMatch(r -> this.userID.equals(r.initiator.userID))) {
            System.out.println("\nError: You have unresolved/unread requests. Please resolve or read them before applying again.");
            return;
        }

        // Check if already applied for the project as an applicant
        if (this.AppliedProject != null && this.AppliedProject.equals(projectName)) {
            System.out.println("\nError: You cannot register as an officer for a project you have applied for as an applicant.");
            return;
        }

        // Check if already an officer for another project with overlapping application periods
        for (Project project : ProjectListingDB.getInstance().getProjectDB()) {
            if (project.Details.OfficerList.contains(this)) {
                boolean isOverlapping = !(pr.Details.CloseDate.isBefore(project.Details.OpenDate) ||
                        pr.Details.OpenDate.isAfter(project.Details.CloseDate));
                if (isOverlapping) {
                    System.out.println("You cannot register for this project as you are already an officer for another project with overlapping application periods.");
                    return;
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

    public void viewAppliedProjectStatusOfficer() {
        if (this.projectAssigned == null) {
            System.out.println("\nError: No project assigned.");
            return;
        }
        System.out.println(this.projectAssigned.getProjectDetails());
        return;
    }

    public void viewAllHandledRequests() {
        if (projectAssigned == null) {
            System.out.println("\nError: You are not managing any project! ");
        }
        ArrayList<Request> handled_reqs = RequestsDB.getInstance().getRequestDB().stream()
        .filter(req -> (req instanceof Booking) &&
                       req.RegardingProject.equals(this.projectAssigned.Details.ProjectName) &&
                       req.status == Request.ApplicationStatus.PENDING).map(req -> req)
                       .collect(Collectors.toCollection(ArrayList::new));
        for (Request req : handled_reqs) {
            System.out.println(req.getRequestDetails());
        }
    }

    //resolve request. only approved or rejected allowed.
    public void ResolveHandledRequest(String userid, ApplicationStatus status, Enum_FlatType flatType) {

        //must exist, is a booking and can be handled by officer
        Request req = RequestsDB.getInstance().getRequestDB().stream()
        .filter(r -> r instanceof Booking 
        && r.initiator.userID.equals(userid) 
        && r.RegardingProject.equals(this.projectAssigned.Details.ProjectName)).findFirst().orElse(null);

        if (req == null) { 
            System.out.println("\nError: could not find handled request"); 
            return;
        }

        req.status = status;
        req.handler = this;
        RequestsDB.getInstance().ModifyDB(req, DB_Action.EDIT);
        if (status == ApplicationStatus.APPROVED) {
            req.initiator.AppliedProjectStatus = ApplicantStatus.BOOKED;
            req.initiator.flatTypeBooked = flatType;
            UserInfoDB.getInstance().ModifyDB(req.initiator, DB_Action.EDIT);
        }
        System.out.println("\nRequest successfully resolved");
    }

    public void UpdateFlatSelection(Enum_FlatType flatType) {

        // Check if the flat type is valid
        if (flatType == null) {
            System.out.println("Invalid flat type.");
            return;
        }
        // Update the flat selection based on availability
        try {
            if (flatType == Enum_FlatType.TWO_ROOM && this.projectAssigned.Details.NoOfUnitsLeft_2Room > 0) {
                this.projectAssigned.SellUnit(Enum_FlatType.TWO_ROOM);
                System.out.println("\nSuccessfully updated availablity of 2-Room flat");
                ProjectListingDB.getInstance().ModifyDB(projectAssigned, DB_Action.EDIT);
            } else if (flatType == Enum_FlatType.THREE_ROOM && this.projectAssigned.Details.NoOfUnitsLeft_3Room > 0) {
                this.projectAssigned.SellUnit(Enum_FlatType.THREE_ROOM);
                System.out.println("\nSuccessfully updated availablity of 3-Room flat");
                ProjectListingDB.getInstance().ModifyDB(projectAssigned, DB_Action.EDIT);
            } else {
                System.out.println("\nError: No units available for the selected flat type.");
                return;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\nError: cannot update flat selection: " + e.getMessage());
            return;
        }

    }

    public void ViewEnquiries() {
        if (this.projectAssigned == null) {
            System.out.println("No project assigned. Cannot view enquiries.");
            return;
        }

        ArrayList<Enquiry> enquiries = EnquiryDB.getInstance().getEnquiryDB();
        boolean hasEnquiries = false;

        System.out.println("\nEnquiries regarding project: " + this.projectAssigned.Details.ProjectName);
        for (Enquiry enquiry : enquiries) {
            if (enquiry.RegardingProject.equals(this.projectAssigned.Details.ProjectName)) {
                System.out.println(enquiry.getEnquiryDetails());
                hasEnquiries = true;
            }
        }

        if (!hasEnquiries) {
            System.out.println("\nError: No enquiries found for the assigned project.");
        }
    }

    public void ReplyToEnquiry(String enquiryTitle, String enquirierID, String reply) {
        if (this.projectAssigned == null) {
            System.out.println("\nError: No project assigned. Cannot reply to enquiries.");
            return;
        }

        ArrayList<Enquiry> enquiries = EnquiryDB.getInstance().getEnquiryDB();
        Enquiry targetEnquiry = enquiries.stream()
                .filter(enq -> enq.Title.equals(enquiryTitle) 
                && enq.RegardingProject.equals(this.projectAssigned.Details.ProjectName) 
                && enq.Enquirer.userID.equals(enquirierID))
                .findFirst()
                .orElse(null);

        if (targetEnquiry == null) {
            System.out.println("\nError: No enquiry with the given title found for the assigned project.");
            return;
        }

        if (!targetEnquiry.isUnreplied()) {
            System.out.println("\nError: Enquiry is already replied to");
            return;
        }

        targetEnquiry.Reply = reply;
        targetEnquiry.Replier = this;
        EnquiryDB.getInstance().ModifyDB(targetEnquiry, Database.DB_Action.EDIT);
        System.out.println("\nReply sent successfully for enquiry: " + enquiryTitle);
    }

    public void GenerateReceipt(String applicantNRIC) {
        if (this.projectAssigned == null) {
            System.out.println("\nError: No project assigned. Cannot generate receipt.");
            return;
        }

        Applicant applicant = (Applicant) UserInfoDB.getInstance().getUserDB().stream()
                .filter(user -> user instanceof Applicant && user.userID.equals(applicantNRIC))
                .findFirst()
                .orElse(null);

        if (applicant == null) {
            System.out.println("\nError: Applicant not found.");
            return;
        }

        if (!applicant.AppliedProject.equals(this.projectAssigned.Details.ProjectName) ||
                !applicant.AppliedProjectStatus.equals(Applicant.ApplicantStatus.BOOKED)) {
            System.out.println("\nError: The applicant has not booked a flat in the assigned project.");
            return;
        }

        Receipt receipt = new Receipt(
                new Applicant_Application(applicant, this.projectAssigned.Details.ProjectName),
                applicant.flatTypeBooked,
                this.projectAssigned.Details
        );

        System.out.println(receipt.getReceiptDetails());
    }
}