package User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

import Database.*;
import InteractableAttributePackage.*;

public class HDB_Manager extends SystemUser{

    public HDB_Manager(String nric, String p, String n) {
        super(nric, p, n);
        this.UserPerms = usertype.MANAGER;
    }

    private boolean hasOverlappingProject(String projectName, LocalDate newOpenDate, LocalDate newCloseDate) {
        return ProjectListingDB.getInstance().getProjectDB().stream()
                .anyMatch(p -> p.Details.Manager.userID.equals(this.userID) &&
                        !p.Details.ProjectName.equals(projectName) && // 排除当前项目（编辑时）
                        !(newCloseDate.isBefore(p.Details.OpenDate) || newOpenDate.isAfter(p.Details.CloseDate)));
    }

    public void CreateBTOProject(String projectName, String neighborhood, int sell2room, int sell3room, int twoRoomUnits, int threeRoomUnits,
                                 String openDate, String closeDate, int officerSlots, boolean vis, String grp) {
        if (officerSlots > 10) {
            System.out.println("Officer slots cannot exceed 10.");
            return;
        }

        //Check if there is an overlapping project
        LocalDate newOpenDate = LocalDate.parse(openDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate newCloseDate = LocalDate.parse(closeDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));


        if (hasOverlappingProject("", newOpenDate, newCloseDate)) {
            System.out.println("You are already managing another project within the same application period.");
            return;
        }

        // Check if project name is valid
        boolean projectExists = ProjectListingDB.getInstance().getProjectDB().stream()
                .anyMatch(p -> p.Details.ProjectName.equals(projectName));
        if (projectExists) {
            System.out.println("A project with this name already exists.");
            return;
        }

        // Create new project
        Project newProject = new Project(projectName, neighborhood, sell2room, sell3room, twoRoomUnits, threeRoomUnits,
                openDate, closeDate, this, officerSlots, new ArrayList<>(), vis, grp);
        ProjectListingDB.getInstance().ModifyDB(newProject, Database.DB_Action.ADD);
        System.out.println("BTO project created successfully: " + projectName);
    }

    public void EditBTOProject(String projectName, String newNeighborhood, int sell2room, int sell3room, int newTwoRoomUnits, int newThreeRoomUnits,
                               String newOpenDate, String newCloseDate, int newOfficerSlots, boolean vis, String grp) {
        // Get project
        Project project = ProjectListingDB.getInstance().getProjectDB().stream()
                .filter(p -> p.Details.ProjectName.equals(projectName))
                .findFirst()
                .orElse(null);

        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        if (newOfficerSlots > 10) {
            System.out.println("Officer slots cannot exceed 10.");
            return;
        }

        LocalDate newOpenDateParsed = LocalDate.parse(newOpenDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate newCloseDateParsed = LocalDate.parse(newCloseDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        if (hasOverlappingProject(projectName, newOpenDateParsed, newCloseDateParsed)) {
            System.out.println("You are already managing another project within the same application period.");
            return;
        }

        // Update project details
        project.Details.Neighborhood = ProjectDetails.Location.valueOf(newNeighborhood.toUpperCase());
        project.Details.SellingPrice_2Room = sell2room;
        project.Details.SellingPrice_3Room = sell3room;
        project.Details.NoOfUnitsLeft_2Room = newTwoRoomUnits;
        project.Details.NoOfUnitsLeft_3Room = newThreeRoomUnits;
        project.Details.OpenDate = LocalDate.parse(newOpenDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        project.Details.CloseDate = LocalDate.parse(newCloseDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        project.Details.OfficerSlots = newOfficerSlots;
        project.Details.activeStatus = vis;
        project.Details.OpentoUserGroup = switch(grp) {
            case "SINGLE" -> Applicant.MaritalStatus.SINGLE;
            case "MARRIED" -> Applicant.MaritalStatus.MARRIED;
            default -> Applicant.MaritalStatus.ALL;
        };
        ProjectListingDB.getInstance().ModifyDB(project, Database.DB_Action.EDIT);
        System.out.println("BTO project updated successfully: " + projectName);
    }


    public void DeleteBTOProject(String projectName) {
        // Get project
        Project project = ProjectListingDB.getInstance().getProjectDB().stream()
                .filter(p -> p.Details.ProjectName.equals(projectName))
                .findFirst()
                .orElse(null);

        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        // Delete project
        ProjectListingDB.getInstance().ModifyDB(project, Database.DB_Action.DELETE);
        System.out.println("BTO project deleted successfully: " + projectName);
    }


    public void ApproveOfficerApplication(String officerID, String projectName) {
        // Get pending officer application
        Officer_Application application = (Officer_Application) RequestsDB.getInstance().getRequestDB().stream()
                .filter(req -> req instanceof Officer_Application &&
                        req.initiator.userID.equals(officerID) &&
                        req.RegardingProject.equals(projectName) &&
                        req.status == Request.ApplicationStatus.PENDING)
                .findFirst()
                .orElse(null);

        if (application == null) {
            System.out.println("No pending officer application found for the given officer and project.");
            return;
        }

        // Get project
        Project project = ProjectListingDB.getInstance().getProjectDB().stream()
                .filter(p -> p.Details.ProjectName.equals(projectName))
                .findFirst()
                .orElse(null);

        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        if (hasOverlappingProject(projectName, project.Details.OpenDate, project.Details.CloseDate)) {
            System.out.println("You are already managing another project within the same application period.");
            return;
        }

        // Check if there are available officer slots
        if (project.Details.OfficerList.size() >= project.Details.OfficerSlots) {
            System.out.println("No available officer slots for this project.");
            return;
        }

        // Approve application and update status
        application.status = Request.ApplicationStatus.APPROVED;
        project.Details.OfficerList.add((HDB_Officer) application.initiator);
        ((HDB_Officer) application.initiator).projectAssigned = project;
        RequestsDB.getInstance().ModifyDB(application, Database.DB_Action.EDIT);
        System.out.println("Officer application approved. Officer added to the project.");
    }



    public void ViewAllProjects() {
        ArrayList<Project> projectList = ProjectListingDB.getInstance().getProjectDB();
        System.out.println("All Projects:");
        for (Project project : projectList) {
            System.out.println(project.getProjectDetails());
        }
    }


    public void ToggleProjectVisibility(String projectName) {
        Project project = ProjectListingDB.getInstance().getProjectDB().stream()
                .filter(p -> p.Details.ProjectName.equals(projectName))
                .findFirst()
                .orElse(null);

        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        // Check if the manager is the creator of the project
        if (!project.Details.Manager.userID.equals(this.userID)) {
            System.out.println("You can only toggle visibility for projects you have created.");
            return;
        }

        // Toggle project visibility
        project.ToggleActiveStatus();
        String visibilityStatus = project.Details.activeStatus ? "ON" : "OFF";
        System.out.println("Project visibility toggled to: " + visibilityStatus);
    }

    public void ViewMyProjects() {
        ArrayList<Project> projectList = ProjectListingDB.getInstance().getProjectDB();
        System.out.println("Projects created by you:");
        boolean hasProjects = false;
        for (Project project : projectList) {
            if (project.Details.Manager.userID.equals(this.userID)) {
                System.out.println(project.getProjectDetails());
                hasProjects = true;
            }
        }
        if (!hasProjects) {
            System.out.println("You have not created any projects.");
        }
    }


    public void ViewOfficerApplications(String projectName) {
        Project project = ProjectListingDB.getInstance().getProjectDB().stream()
                .filter(p -> p.Details.ProjectName.equals(projectName))
                .findFirst()
                .orElse(null);

        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        // Get all officer applications for the project
        ArrayList<Officer_Application> officerApplications = RequestsDB.getInstance().getRequestDB().stream()
                .filter(req -> req instanceof Officer_Application && req.RegardingProject.equals(projectName))
                .map(req -> (Officer_Application) req)
                .collect(Collectors.toCollection(ArrayList::new));

        if (officerApplications.isEmpty()) {
            System.out.println("No officer applications found for the project: " + projectName);
            return;
        }

        // Display officer applications
        System.out.println("Officer Applications for project: " + projectName);
        for (Officer_Application application : officerApplications) {
            System.out.println("Officer Name: " + application.initiator.name);
            System.out.println("Officer NRIC: " + application.initiator.userID);
            System.out.println("Status: " + application.status);
            System.out.println("-----------------------------");
        }
    }


    public void ApproveOfficerApplication(String officerID, String projectName, boolean approve) {
        Officer_Application application = (Officer_Application) RequestsDB.getInstance().getRequestDB().stream()
                .filter(req -> req instanceof Officer_Application &&
                        req.initiator.userID.equals(officerID) &&
                        req.RegardingProject.equals(projectName) &&
                        req.status == Request.ApplicationStatus.PENDING)
                .findFirst()
                .orElse(null);

        if (application == null) {
            System.out.println("No pending officer application found for the given officer and project.");
            return;
        }

        Project project = ProjectListingDB.getInstance().getProjectDB().stream()
                .filter(p -> p.Details.ProjectName.equals(projectName))
                .findFirst()
                .orElse(null);

        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        if (approve) {

            if (project.Details.OfficerList.size() >= project.Details.OfficerSlots) {
                System.out.println("No available officer slots for this project.");
                return;
            }

            application.status = Request.ApplicationStatus.APPROVED;
            project.Details.OfficerList.add((HDB_Officer) application.initiator);
            ((HDB_Officer) application.initiator).projectAssigned = project;
            ((HDB_Officer) application.initiator).projectStatus = HDB_Officer.Enum_OfficerStatus.SUCCESSFUL;


            RequestsDB.getInstance().ModifyDB(application, Database.DB_Action.EDIT);
            ProjectListingDB.getInstance().ModifyDB(project, Database.DB_Action.EDIT);

            System.out.println("Officer application approved. Officer added to the project.");
        } else {

            application.status = Request.ApplicationStatus.REJECTED;
            RequestsDB.getInstance().ModifyDB(application, Database.DB_Action.EDIT);

            System.out.println("Officer application rejected for officer: " + officerID);
        }
    }


    public void ApproveOrRejectApplication(String applicantNRIC, String projectName, boolean approve) {
        // Get pending application
        Applicant_Application application = (Applicant_Application) RequestsDB.getInstance().getRequestDB().stream()
                .filter(req -> req instanceof Applicant_Application &&
                        req.initiator.userID.equals(applicantNRIC) &&
                        req.RegardingProject.equals(projectName) &&
                        req.status == Request.ApplicationStatus.PENDING)
                .findFirst()
                .orElse(null);

        if (application == null) {
            System.out.println("No pending application found for the given applicant and project.");
            return;
        }

        Applicant applicant = application.initiator;

        // Get project
        Project project = ProjectListingDB.getInstance().getProjectDB().stream()
                .filter(p -> p.Details.ProjectName.equals(projectName))
                .findFirst()
                .orElse(null);

        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        if (approve) {
            // Check if there are available units for the selected flat type
            if ("2-Room".equals(applicant.flatTypeBooked) && project.Details.NoOfUnitsLeft_2Room > 0) {
                project.SellUnit(Request.FlatType.TWO_ROOM);
            }
            else if ("3-Room".equals(applicant.flatTypeBooked) && project.Details.NoOfUnitsLeft_3Room > 0) {
                project.SellUnit(Request.FlatType.THREE_ROOM);
            }
            else {
                System.out.println("Not enough units available for the selected flat type. Application rejected.");
                application.status = Request.ApplicationStatus.REJECTED;
                RequestsDB.getInstance().ModifyDB(application, Database.DB_Action.EDIT);
                return;
            }

            // Approve application and update status
            application.status = Request.ApplicationStatus.APPROVED;
            application.initiator.AppliedProjectStatus = Applicant.ApplicantStatus.SUCCESSFUL;
            RequestsDB.getInstance().ModifyDB(application, Database.DB_Action.EDIT);
            System.out.println("Application approved for applicant: " + applicantNRIC);
        }
        else {
            // Reject application and update status
            application.status = Request.ApplicationStatus.REJECTED;
            RequestsDB.getInstance().ModifyDB(application, Database.DB_Action.EDIT);
            System.out.println("Application rejected for applicant: " + applicantNRIC);
        }
    }

    public void ApproveOrRejectWithdrawal(String applicantNRIC, String projectName, boolean approve) {
        // Get pending withdrawal request
        Withdrawal withdrawalRequest = (Withdrawal) RequestsDB.getInstance().getRequestDB().stream()
                .filter(req -> req instanceof Withdrawal &&
                        req.initiator.userID.equals(applicantNRIC) &&
                        req.RegardingProject.equals(projectName) &&
                        req.status == Request.ApplicationStatus.PENDING)
                .findFirst()
                .orElse(null);

        if (withdrawalRequest == null) {
            System.out.println("No pending withdrawal request found for the given applicant and project.");
            return;
        }

        // Get project
        Project project = ProjectListingDB.getInstance().getProjectDB().stream()
                .filter(p -> p.Details.ProjectName.equals(projectName))
                .findFirst()
                .orElse(null);

        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        if (approve) {
            // If applicant has already booked a flat, release the flat
            if (withdrawalRequest.BookedFlatType != Request.FlatType.NULL) {
                project.UnsellUnit(withdrawalRequest.BookedFlatType);
            }

            // Update applicant status
            withdrawalRequest.initiator.AppliedProjectStatus = Applicant.ApplicantStatus.UNSUCCESSFUL;
            withdrawalRequest.initiator.AppliedProject = "";
            withdrawalRequest.initiator.flatTypeBooked = Enum_FlatType.DEFAULT;

            // Update withdrawal request status
            withdrawalRequest.status = Request.ApplicationStatus.APPROVED;
            RequestsDB.getInstance().ModifyDB(withdrawalRequest, Database.DB_Action.EDIT);
            System.out.println("Withdrawal request approved for applicant: " + applicantNRIC);
        } else {
            // Reject withdrawal request and update status
            withdrawalRequest.status = Request.ApplicationStatus.REJECTED;
            RequestsDB.getInstance().ModifyDB(withdrawalRequest, Database.DB_Action.EDIT);
            System.out.println("Withdrawal request rejected for applicant: " + applicantNRIC);
        }
    }


    public void ViewProjectEnquiries(String projectName) {
        Project project = ProjectListingDB.getInstance().getProjectDB().stream()
                .filter(p -> p.Details.ProjectName.equals(projectName))
                .findFirst()
                .orElse(null);

        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        ArrayList<Enquiry> enquiries = EnquiryDB.getInstance().getEnquiryDB().stream()
                .filter(enq -> enq.RegardingProject.equals(projectName))
                .collect(Collectors.toCollection(ArrayList::new));

        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found for the project: " + projectName);
            return;
        }

        System.out.println("Enquiries for project: " + projectName);
        for (Enquiry enquiry : enquiries) {
            System.out.println(enquiry.getEnquiryDetails());
            System.out.println("-----------------------------");
        }
    }

    public void ReplyToProjectEnquiry(String projectName, String enquiryTitle, String reply) {
        Project project = ProjectListingDB.getInstance().getProjectDB().stream()
                .filter(p -> p.Details.ProjectName.equals(projectName))
                .findFirst()
                .orElse(null);

        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        Enquiry enquiry = EnquiryDB.getInstance().getEnquiryDB().stream()
                .filter(enq -> enq.RegardingProject.equals(projectName) && enq.Title.equals(enquiryTitle))
                .findFirst()
                .orElse(null);

        if (enquiry == null) {
            System.out.println("No enquiry with the given title found for the project: " + projectName);
            return;
        }

        enquiry.Reply = reply;
        enquiry.Replier = this;
        EnquiryDB.getInstance().ModifyDB(enquiry, Database.DB_Action.EDIT);
        System.out.println("Reply sent successfully for enquiry: " + enquiryTitle);
    }

}