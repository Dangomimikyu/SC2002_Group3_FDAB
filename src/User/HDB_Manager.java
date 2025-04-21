package User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.stream.Collectors;

import Database.*;
import Database.Database.DB_Action;
import Filter.IFilter;
import InteractableAttributePackage.*;
import InteractableAttributePackage.ProjectDetails.Location;
import User.Applicant.ApplicantStatus;
import User.Applicant.MaritalStatus;
import User.HDB_Officer.Enum_OfficerStatus;

public class HDB_Manager extends SystemUser{

    public HDB_Manager(String nric, String p, String n) {
        super(nric, p, n);
        this.UserPerms = usertype.MANAGER;
    }

    //*** */ this is a very complex check, 
    // instead just make it so that editing projects can only be done if has NO ACTIVE PROJECTS, or if editted project is active
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // If manager will have an active project during a certain period and the current project's current visibility is ON
    // Cannot create/edit project such that new application dates overlaps the active one (otherwise will cause 2 active projects at the same time) 
    // private boolean hasOverlappingActiveProject(String projectName, LocalDate newOpenDate, LocalDate newCloseDate) {
    //     return ProjectListingDB.getInstance().getProjectDB().stream()
    //             .anyMatch(p -> p.Details.Manager.userID.equals(this.userID) &&
    //                     !p.Details.ProjectName.equals(projectName) &&
    //                     p.Details.visibility &&
    //                     !(newCloseDate.isBefore(p.Details.OpenDate) || newOpenDate.isAfter(p.Details.CloseDate)));
    // }

    //////////////////////////////////////////////////////////////////////////////
    ////////////////// MANAGER CREATES PROJECT ///////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    public void CreateBTOProject(String projectName, String neighborhood, int sell2room, int sell3room, int twoRoomUnits, int threeRoomUnits,
                                 String openDate, String closeDate, int officerSlots, boolean vis, String grp) {
       
        try {

            if (officerSlots > 10) { throw new Exception("\nError: Officer slots cannot exceed 10."); }

            //Check if dates are valid
            try {
                LocalDate newOpenDate = LocalDate.parse(openDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                LocalDate newCloseDate = LocalDate.parse(closeDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
            catch (Exception e) {System.out.println("\nError: Open Date or Closing Date inputted is invalid"); return; }


            //Cannot create a project if currently has an active one
            if (ProjectListingDB.getInstance().getProjectDB().stream().anyMatch(p -> p.isActive() && p.Details.Manager.userID.equals(userID))) {
                throw new Exception("\nError: You are currently managing an active project"); }

            // Check if project name is valid
            boolean projectExists = ProjectListingDB.getInstance().getProjectDB().stream()
                    .anyMatch(p -> p.Details.ProjectName.equals(projectName));
            if (projectExists) {
                throw new Exception("\nError: A project with this name already exists."); }
            
            // Check if location is valid
            boolean valid_location = false;
            for (Location l : Location.values()) {
                if (l.toString().equals(neighborhood.toUpperCase().replace(" ","_"))) {
                    valid_location = true;
                }
            }
            if (!valid_location) { throw new Exception("\nError: Invalid Location inputted"); }

            //check if group inputted is valid
            if (grp == null) {throw new Exception("\nError: user group input is invalid"); }

            // Create new project
            Project newProject = new Project(projectName, neighborhood, sell2room, sell3room, twoRoomUnits, threeRoomUnits,
                    openDate, closeDate, this, officerSlots, new ArrayList<>(), vis, grp);
            ProjectListingDB.getInstance().ModifyDB(newProject, Database.DB_Action.ADD);
            System.out.println("\nBTO project created successfully: " + projectName);
        }
        catch (Exception e) { System.out.println( e.getMessage()); }
    }

    //////////////////////////////////////////////////////////////////////////////
    ////////////////// MANAGER EDITS PROJECT /////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    public void EditBTOProject(String projectName, String newNeighborhood, int sell2room, int sell3room, int newTwoRoomUnits, int newThreeRoomUnits,
                               String newOpenDate, String newCloseDate, int newOfficerSlots, String grp) {

        
        try {

            Project project = ProjectListingDB.getInstance().SearchDB(projectName);

            if (project == null) { throw new Exception("\nError: Project not found."); }

            // Check if the manager is the creator of the project
            if (!project.Details.Manager.userID.equals(this.userID)) { 
                throw new Exception("\nError: You can only edit projects you have created."); }

            if (newOfficerSlots > 10) { throw new Exception("\nError: Officer slots cannot exceed 10."); }
            if (project.Details.OfficerList.size() > newOfficerSlots) { throw new Exception("\nError: New Officer Slots cannot be less than existing number of officers in Project."); }

            LocalDate newOpenDateParsed;
            LocalDate newCloseDateParsed;
            //Check if dates are valid
            try {
                newOpenDateParsed = LocalDate.parse(newOpenDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                newCloseDateParsed = LocalDate.parse(newCloseDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
            catch (Exception e) {System.out.println("\nError: Open Date or Closing Date inputted is invalid"); return; }

            //Cannot edit a project if currently has an active one
            // but can edit if the edited project is the active one
            if (ProjectListingDB.getInstance().getProjectDB().stream()
            .anyMatch(p -> p.isActive() && p.Details.Manager.userID.equals(userID) && !p.Details.ProjectName.equals(projectName))) {
                throw new Exception("\nError: You are currently managing another active project.");
            }

            // Check if location is valid
            boolean valid_location = false;
            for (Location l : Location.values()) {
                if (l.toString().equals(newNeighborhood.toUpperCase().replace(" ","_"))) {
                    valid_location = true;
                }
            }
            if (!valid_location) { throw new Exception("\nError: Invalid Location inputted"); }

            //check if group inputted is valid
            if (grp == null) {throw new Exception("\nError: user group input is invalid"); }

            // Update project details
            project.Details.Neighborhood = Location.valueOf(newNeighborhood.toUpperCase().replace(" ","_"));
            project.Details.SellingPrice_2Room = sell2room;
            project.Details.SellingPrice_3Room = sell3room;
            project.Details.NoOfUnitsLeft_2Room = newTwoRoomUnits;
            project.Details.NoOfUnitsLeft_3Room = newThreeRoomUnits;
            project.Details.OpenDate = newOpenDateParsed;
            project.Details.CloseDate = newCloseDateParsed;
            project.Details.OfficerSlots = newOfficerSlots;
            project.Details.OpentoUserGroup = MaritalStatus.valueOf(grp);
            ProjectListingDB.getInstance().ModifyDB(project, Database.DB_Action.EDIT);
            System.out.println("\nBTO project updated successfully: " + projectName);
        }
        catch (Exception e) { System.out.println( e.getMessage()); }
    }

    //////////////////////////////////////////////////////////////////////////////
    ////////////////// MANAGER DELETES PROJECT ///////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    public void DeleteBTOProject(String projectName) {

        try {
            Project project = ProjectListingDB.getInstance().SearchDB(projectName);

            //check if project is found
            if (project == null) { throw new Exception("\nError: Project not found."); }

            // Check if the manager is the creator of the project
            if (!project.Details.Manager.userID.equals(this.userID)) { 
                throw new Exception("\nError: You can only delete projects you have created."); }

            // Delete project
            ProjectListingDB.getInstance().ModifyDB(project, Database.DB_Action.DELETE);
            System.out.println("\nBTO project deleted successfully: " + projectName);

            // Delete ALL EXISTING RECORDS OF PROJECTS from database
            for (SystemUser u : UserInfoDB.getInstance().getUserDB())
            {
                if (u instanceof Applicant && ((Applicant)u).AppliedProject.equals(projectName))
                { 
                    ((Applicant)u).AppliedProject = "";
                    ((Applicant)u).AppliedProjectStatus = ApplicantStatus.UNSUCCESSFUL;
                    ((Applicant)u).flatTypeBooked = Enum_FlatType.DEFAULT;
                    UserInfoDB.getInstance().ModifyDB(u, DB_Action.EDIT);
                }

                if (u instanceof HDB_Officer && ((HDB_Officer)u).project_name.equals(projectName))
                {
                    ((HDB_Officer)u).project_name = "";
                    ((HDB_Officer)u).officerStatus = Enum_OfficerStatus.UNSUCCESSFUL;
                    UserInfoDB.getInstance().ModifyDB(u, DB_Action.EDIT);
                }
            }

            //Remove all enquiries regarding project
            for (Enquiry e : EnquiryDB.getInstance().getEnquiryDB())
            {
                if (e.RegardingProject.equals(projectName))
                {
                    EnquiryDB.getInstance().ModifyDB(e,DB_Action.DELETE);
                }
            }

            //Remove all requests associated with project
            for (Request r : RequestsDB.getInstance().getRequestDB())
            {
                if (r.RegardingProject.equals(projectName))
                {
                    SystemUser initiator = UserInfoDB.getInstance().SearchDB(r.initiator.userID);
                    //in case Applicant_Application is PENDING, need to revert initator status to UNSUCCESSFUL
                    if (r instanceof Applicant_Application) {
                        ((Applicant)initiator).AppliedProjectStatus = ApplicantStatus.UNSUCCESSFUL; 
                        UserInfoDB.getInstance().ModifyDB(initiator, DB_Action.EDIT);
                    }
                    //in case Officer_Application is PENDING, need to revert initator status to UNSUCCESSFUL
                    if (r instanceof Officer_Application) {
                        ((HDB_Officer)initiator).officerStatus = Enum_OfficerStatus.UNSUCCESSFUL; 
                        UserInfoDB.getInstance().ModifyDB(initiator, DB_Action.EDIT);
                    }

                    RequestsDB.getInstance().ModifyDB(r,DB_Action.DELETE);
                }
            }

            }
            catch (ConcurrentModificationException e) {}
            catch (Exception e) { System.out.println( e.getMessage()); }
    }
    

    //////////////////////////////////////////////////////////////////////////////
    ////////////////// MANAGER TOGGLES PROJECT VISIBILITY ////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    public void ToggleProjectVisibility(String projectName) {

        try {
            Project project = ProjectListingDB.getInstance().SearchDB(projectName);

            //check if project is found
            if (project == null) { throw new Exception("\nError: Project not found."); }

            // Check if the manager is the creator of the project
            if (!project.Details.Manager.userID.equals(this.userID)) { 
            throw new Exception("\nError: You can only toggle visibility for projects you have created."); }

            //Cannot toggle visiblity of any project ON if there is currently an active project (cannot have more than one active projects at a time)
            if (!project.Details.visibility && 
            ProjectListingDB.getInstance().getProjectDB().stream().anyMatch(p -> p.isActive() && p.Details.Manager.userID.equals(userID)))
            { throw new Exception("\nError: You currently already have an active project! Cannot turn on chosen project's visibility. "); }

            // Toggle project visibility
            project.ToggleVisibility();
            ProjectListingDB.getInstance().ModifyDB(project, DB_Action.EDIT);
            System.out.println("\nProject visibility toggled to: " + (project.Details.visibility ? "ON" : "OFF"));
        
        } catch (Exception e) { System.out.println( e.getMessage()); }
    }

    //////////////////////////////////////////////////////////////////////////////
    ////////////////// MANAGER VIEWS CURRENT ACTIVE PROJECT //////////////////////
    //////////////////////////////////////////////////////////////////////////////
    
    public void ViewActiveProject() {

        try { 
        Project current_proj = ProjectListingDB.getInstance().getProjectDB().stream()
        .filter(p -> p.isActive() && p.Details.Manager.userID.equals(userID)).findFirst().orElse(null);

        System.out.println("\n"+current_proj.getProjectDetails());

        } catch (NullPointerException e) { System.out.println("\nError: You currently have no active Projects!"); }

    }

    //////////////////////////////////////////////////////////////////////////////
    ////////////////// MANAGER VIEWS ALL PROJECTS WITH FILTERS ///////////////////
    //////////////////////////////////////////////////////////////////////////////
    
    public void ViewAllProjects(ArrayList<IFilter> activeFilters) {
        ProjectListingDB.getInstance().ViewDB(activeFilters);
    }

    //////////////////////////////////////////////////////////////////////////////
    ////////////////// - //////////////////////
    //////////////////////////////////////////////////////////////////////////////



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

        // if (hasOverlappingActiveProject(projectName, project.Details.OpenDate, project.Details.CloseDate)) {
        //     System.out.println("You are already managing another project within the same application period.");
        //     return;
        // }

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

        if (!project.Details.Manager.userID.equals(userID)) {
            System.out.println("Project is not managed by you");
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

    public void ViewApplicantApplications(String projectName) {
        Project project = ProjectListingDB.getInstance().getProjectDB().stream()
                .filter(p -> p.Details.ProjectName.equals(projectName))
                .findFirst()
                .orElse(null);

        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        if (!project.Details.Manager.userID.equals(userID)) {
            System.out.println("Project is not managed by you");
            return;
        }

        // Get all applicant applications and withdrawals of a project
        ArrayList<Request> requests = RequestsDB.getInstance().getRequestDB().stream()
                .filter(req -> req instanceof Applicant_Application || req instanceof Withdrawal && req.RegardingProject.equals(projectName))
                .map(req -> req)
                .collect(Collectors.toCollection(ArrayList::new));

        if (requests.isEmpty()) {
            System.out.println("No officer applications found for the project: " + projectName);
            return;
        }

        // Display officer applications
        System.out.println("Officer Applications for project: " + projectName);
        for (Request request : requests) {
            System.out.println(request.getRequestDetails());
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
            ((HDB_Officer) application.initiator).officerStatus = HDB_Officer.Enum_OfficerStatus.SUCCESSFUL;


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
            if (Enum_FlatType.TWO_ROOM.equals(applicant.flatTypeBooked) && project.Details.NoOfUnitsLeft_2Room > 0) {
                project.SellUnit(Enum_FlatType.TWO_ROOM);
            }
            else if (Enum_FlatType.THREE_ROOM.equals(applicant.flatTypeBooked) && project.Details.NoOfUnitsLeft_3Room > 0) {
                project.SellUnit(Enum_FlatType.THREE_ROOM);
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
            if (withdrawalRequest.BookedFlatType != Enum_FlatType.DEFAULT) {
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