package User;

import java.util.ArrayList;
import java.util.Map;

import InteractableAttributePackage.*;
import InteractableAttributePackage.Request.FlatType;

import Database.*;

//HDB Officer possess all applicant’s capabilities.
public class HDB_Officer extends Applicant{
    public enum Enum_OfficerStatus {
        PENDING, SUCCESSFUL, UNSUCCESSFUL

    }

    public Project projectAssigned;
    public Enum_OfficerStatus projectStatus;
    private ArrayList<Applicant> applicantList;
    private Map<String, Integer> flatAvailability;
    private String officerID;

    public HDB_Officer(String nric, String p, String n, int a, String m) {
        super(nric, p, n, a, m);
        this.projectAssigned = null;
        this.projectStatus = Enum_OfficerStatus.UNSUCCESSFUL;
    }

    public HDB_Officer(String name, String nric, int age, String marital, String pass, String proj, String projStatus)
    {
        super(nric, pass, name, age, marital);
        this.projectAssigned = ProjectListingDB.getInstance().SearchDB(proj);
        this.projectStatus = switch(projStatus)
        {
            case "PENDING" -> Enum_OfficerStatus.PENDING;
            case "SUCCESSFUL" -> Enum_OfficerStatus.SUCCESSFUL;
            default -> Enum_OfficerStatus.UNSUCCESSFUL;
        };
    }

    public void viewProjectList() {
        ArrayList<Project> projectList = ProjectListingDB.getInstance().getProjectDB();
        System.out.println("Projects:");
        for (Project project : projectList) {
            System.out.println(project.getProjectDetails());
        }
    }

    public boolean RegisterForProject(String projectName) {
        Project pr = ProjectListingDB.getInstance().getProjectDB().stream()
                .filter(p -> p.Details.ProjectName.equals(projectName))
                .findFirst()
                .orElse(null);
        if (pr == null) {
            System.out.println("Invalid project.");
            return false;
        }

        // Chek if already applied for the project as an applicant
        if (this.AppliedProject != null && this.AppliedProject.equals(projectName)) {
            System.out.println("You cannot register as an officer for a project you have applied for as an applicant.");
            return false;
        }

        // Check if already an officer for another project with overlapping application periods
        for (Project project : ProjectListingDB.getInstance().getProjectDB()) {
            if (project.Details.OfficerList.contains(this)) {
                boolean isOverlapping = !(pr.Details.CloseDate.isBefore(project.Details.OpenDate) ||
                        pr.Details.OpenDate.isAfter(project.Details.CloseDate));
                if (isOverlapping) {
                    System.out.println("You cannot register for this project as you are already an officer for another project with overlapping application periods.");
                    return false;
                }
            }
        }

        //Submit officer application to the project
        Officer_Application officerApplication = new Officer_Application(this, projectName);
        this.projectStatus = Enum_OfficerStatus.PENDING;
        RequestsDB.getInstance().ModifyDB(officerApplication, Database.DB_Action.ADD);
        System.out.println("Officer application submitted successfully. Waiting for manager approval.");
        return true;
    }

    public void ViewApplicationStatus() {
        System.out.println("Applied Project: " + this.AppliedProject);
        System.out.println("Application Status: " + this.projectStatus);
        return;
    }

    public void ViewProjectDetails() {
        if (this.projectAssigned == null) {
            System.out.println("No project assigned.");
            return;
        }
        System.out.println(this.projectAssigned.getProjectDetails());
        return;
    }

    public boolean UpdateFlatSelection(String applicantNRIC, Enum_FlatType flatType) {
        // Search for the project assigned to the officer
        Applicant_Application app = (Applicant_Application) RequestsDB.getInstance().getRequestDB().stream()
                .filter(req -> req instanceof Applicant_Application &&
                        req.initiator.userID.equals(applicantNRIC) &&
                        req.RegardingProject.equals(this.projectAssigned.Details.ProjectName) &&
                        req.status == Request.ApplicationStatus.APPROVED)
                .findFirst()
                .orElse(null);

        if (app == null) {
            System.out.println("No approved application found for the applicant in the assigned project.");
            return false;
        }

        // Check if the flat type is valid
        if (flatType == null) {
            System.out.println("Invalid flat type.");
            return false;
        }

        // Update the flat selection based on availability
        try {
            if (flatType == Enum_FlatType.TWO_ROOM && this.projectAssigned.Details.NoOfUnitsLeft_2Room > 0) {
                this.projectAssigned.SellUnit(FlatType.TWO_ROOM);
                System.out.println("Updated flat selection to Two-room flat for applicant: " + app.initiator.name);
            } else if (flatType == Enum_FlatType.THREE_ROOM && this.projectAssigned.Details.NoOfUnitsLeft_3Room > 0) {
                this.projectAssigned.SellUnit(FlatType.THREE_ROOM);
                System.out.println("Updated flat selection to Three-room flat for applicant: " + app.initiator.name);
            } else {
                System.out.println("No units available for the selected flat type.");
                return false;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error updating flat selection: " + e.getMessage());
            return false;
        }

        // Update the applicant's status to BOOKED
        app.initiator.AppliedProjectStatus = Applicant.ApplicantStatus.BOOKED;

        // Update the flat type booked by the applicant
        if (flatType == Enum_FlatType.TWO_ROOM) {
            app.initiator.flatTypeBooked = Enum_FlatType.TWO_ROOM;
        } else if (flatType == Enum_FlatType.THREE_ROOM) {
            app.initiator.flatTypeBooked = Enum_FlatType.THREE_ROOM;
        }

        System.out.println("Applicant's status updated to BOOKED and flat type recorded.");
        return true;
    }

    public void ViewEnquiries() {
        if (this.projectAssigned == null) {
            System.out.println("No project assigned. Cannot view enquiries.");
            return;
        }

        ArrayList<Enquiry> enquiries = EnquiryDB.getInstance().getEnquiryDB();
        boolean hasEnquiries = false;

        System.out.println("Enquiries regarding project: " + this.projectAssigned.Details.ProjectName);
        for (Enquiry enquiry : enquiries) {
            if (enquiry.RegardingProject.equals(this.projectAssigned.Details.ProjectName)) {
                System.out.println(enquiry.getEnquiryDetails());
                hasEnquiries = true;
            }
        }

        if (!hasEnquiries) {
            System.out.println("No enquiries found for the assigned project.");
        }
    }

    public void ReplyToEnquiry(String enquiryTitle, String reply) {
        if (this.projectAssigned == null) {
            System.out.println("No project assigned. Cannot reply to enquiries.");
            return;
        }

        ArrayList<Enquiry> enquiries = EnquiryDB.getInstance().getEnquiryDB();
        Enquiry targetEnquiry = enquiries.stream()
                .filter(enq -> enq.Title.equals(enquiryTitle) && enq.RegardingProject.equals(this.projectAssigned.Details.ProjectName))
                .findFirst()
                .orElse(null);

        if (targetEnquiry == null) {
            System.out.println("No enquiry with the given title found for the assigned project.");
            return;
        }

        targetEnquiry.Reply = reply;
        targetEnquiry.Replier = this;
        EnquiryDB.getInstance().ModifyDB(targetEnquiry, Database.DB_Action.EDIT);
        System.out.println("Reply sent successfully for enquiry: " + enquiryTitle);
    }

    public void GenerateReceipt(String applicantNRIC) {
        if (this.projectAssigned == null) {
            System.out.println("No project assigned. Cannot generate receipt.");
            return;
        }

        Applicant applicant = (Applicant) UserInfoDB.getInstance().getUserDB().stream()
                .filter(user -> user instanceof Applicant && user.userID.equals(applicantNRIC))
                .findFirst()
                .orElse(null);

        if (applicant == null) {
            System.out.println("Applicant not found.");
            return;
        }

        if (!applicant.AppliedProject.equals(this.projectAssigned.Details.ProjectName) ||
                !applicant.AppliedProjectStatus.equals(Applicant.ApplicantStatus.BOOKED)) {
            System.out.println("The applicant has not booked a flat in the assigned project.");
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