package User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.time.format.DateTimeParseException;
import Database.*;
import Database.Database.DB_Action;
import Filter.Filter_ProjectName;
import Filter.IFilter;
import InteractableAttributePackage.*;
import InteractableAttributePackage.ProjectDetails.Location;
import InteractableAttributePackage.Request.ApplicationStatus;
import Service.ReportGenerator;
import User.Applicant.ApplicantStatus;
import User.Applicant.MaritalStatus;
import User.HDB_Officer.Enum_OfficerStatus;

public class HDB_Manager extends SystemUser{

    public HDB_Manager(String nric, String p, String n) {
        super(nric, p, n);
        this.UserPerms = usertype.MANAGER;
    }

    //////////////////////////////////////////////////////////////////////////////
    ////////////////// MANAGER CREATES PROJECT ///////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    public void CreateBTOProject(String projectName, String neighborhood, int sell2room, int sell3room, int twoRoomUnits, int threeRoomUnits,
                                 String openDate, String closeDate, int officerSlots, boolean vis, String grp) {
       
        try {

            if (officerSlots > 10) { throw new Exception("\nError: Officer slots cannot exceed 10."); }

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
        catch ( DateTimeParseException e ) { System.out.println( "\nError: input open or close dates are in wrong format!"); }
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
            // Delete all records of project and reset statuses for affected users
            ArrayList<SystemUser> userlist_copy = new ArrayList<>(UserInfoDB.getInstance().getUserDB());
            for (SystemUser u : userlist_copy)
            {
                if (u instanceof Applicant && ((Applicant)u).AppliedProject.equals(projectName))
                { 
                    SystemUser original_u = UserInfoDB.getInstance().SearchDB(u.userID);
                    ((Applicant)original_u).AppliedProject = "";
                    ((Applicant)original_u).AppliedProjectStatus = ApplicantStatus.UNSUCCESSFUL;
                    ((Applicant)original_u).flatTypeBooked = Enum_FlatType.DEFAULT;
                    UserInfoDB.getInstance().ModifyDB(original_u, DB_Action.EDIT);
                }

                if (u instanceof HDB_Officer && ((HDB_Officer)u).project_name.equals(projectName))
                {
                    SystemUser original_u = UserInfoDB.getInstance().SearchDB(u.userID);
                    ((HDB_Officer)original_u).project_name = "";
                    ((HDB_Officer)original_u).officerStatus = Enum_OfficerStatus.UNSUCCESSFUL;
                    UserInfoDB.getInstance().ModifyDB(original_u, DB_Action.EDIT);
                }
            }

            //Remove all enquiries regarding project
            ArrayList<Enquiry> enqlist_copy = new ArrayList<>(EnquiryDB.getInstance().getEnquiryDB());
            for (Enquiry e : enqlist_copy)
            {
                if (e.RegardingProject.equals(projectName))
                {
                    Enquiry original_e = EnquiryDB.getInstance().SearchDB(e.Title,e.Enquirer.userID,e.RegardingProject);
                    EnquiryDB.getInstance().ModifyDB(original_e,DB_Action.DELETE);
                }
            }

            //Remove all requests associated with project
            ArrayList<Request> reqlist_copy = new ArrayList<>(RequestsDB.getInstance().getRequestDB());
            for (Request r : reqlist_copy)
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

                    Request original_r = RequestsDB.getInstance().SearchDB(r.initiator.userID);
                    RequestsDB.getInstance().ModifyDB(original_r,DB_Action.DELETE);
                }
            }

        }
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

    ///////////////////////////////////////////////////////////////////////////////////////
    ////////////////// VIEW AND HANDLE OFFICER APPLICATIONS ///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    
    public void ViewOfficerApplications()
    {
        try {
            ArrayList<Project> managed_projects = ProjectListingDB.getInstance().getProjectDB().stream()
            .filter(p -> p.Details.Manager.userID.equals(userID)).map(p -> p).collect(Collectors.toCollection(ArrayList::new));

            if (managed_projects.size() == 0) { throw new Exception("\nError: you are not managing any projects!"); }

            for (Project p : managed_projects)
            {
                ArrayList<IFilter> filters = new ArrayList<>();
                filters.add(new Filter_ProjectName(p.Details.ProjectName, "OFFICER"));
                RequestsDB.getInstance().ViewDB(filters);
            }
        }
        catch (Exception e) { System.out.println( e.getMessage()); }

    }

    public void HandleOfficerApplications(int index, boolean approve)
    {
        try {
            ArrayList<Project> managed_projects = ProjectListingDB.getInstance().getProjectDB().stream()
            .filter(p -> p.Details.Manager.userID.equals(userID)).map(p -> p).collect(Collectors.toCollection(ArrayList::new));

            Request r = RequestsDB.getInstance().getRequestDB().get(index);
            if ( !(r instanceof Officer_Application) || r.status != ApplicationStatus.PENDING ||
            !managed_projects.stream().anyMatch(p -> p.Details.ProjectName.equals(r.RegardingProject)))
            { throw new Exception("\nError: Not allowed to handle given request!"); }

            Project p = ProjectListingDB.getInstance().SearchDB(r.RegardingProject);
            SystemUser u = UserInfoDB.getInstance().SearchDB(r.initiator.userID);

            //automatically reject if project's slots are full 
            if (p.Details.OfficerList.size() >= p.Details.OfficerSlots)
            { 
                System.out.println("\nYour project's slots are full. Automatically rejecting application...");
                approve = false; 
            } 

            r.handler = this;
            if (approve) { 
                ((HDB_Officer)u).project_name = r.RegardingProject;
                ((HDB_Officer)u).officerStatus = Enum_OfficerStatus.SUCCESSFUL;
                r.status = ApplicationStatus.APPROVED;
                p.AssignOfficer((HDB_Officer)u);

            } else { 
                ((HDB_Officer)u).officerStatus = Enum_OfficerStatus.UNSUCCESSFUL;
                r.status = ApplicationStatus.REJECTED;
             }
            RequestsDB.getInstance().ModifyDB(r, DB_Action.EDIT);
            UserInfoDB.getInstance().ModifyDB(u,  DB_Action.EDIT);
            ProjectListingDB.getInstance().ModifyDB(p, DB_Action.EDIT);
            System.out.println("\nSuccessfully " + (approve ? "approved" : "rejected") + " officer application");
            
        }
        catch (Exception e) { System.out.println( e.getMessage()); }

    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ////////////////// VIEW AND HANDLE APPLICANT APPLICATIONS /////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    
    public void ViewApplicantApplications()
    {
        try {
            ArrayList<Project> managed_projects = ProjectListingDB.getInstance().getProjectDB().stream()
            .filter(p -> p.Details.Manager.userID.equals(userID)).map(p -> p).collect(Collectors.toCollection(ArrayList::new));

            if (managed_projects.size() == 0) { throw new Exception("\nError: you are not managing any projects!"); }

            for (Project p : managed_projects)
            {
                ArrayList<IFilter> filters = new ArrayList<>();
                filters.add(new Filter_ProjectName(p.Details.ProjectName, "APPLICANT"));
                RequestsDB.getInstance().ViewDB(filters);
            }
        }
        catch (Exception e) { System.out.println( e.getMessage()); }

    }

    public void HandleApplicantApplications(int index, boolean approve)
    {
        try {
            ArrayList<Project> managed_projects = ProjectListingDB.getInstance().getProjectDB().stream()
            .filter(p -> p.Details.Manager.userID.equals(userID)).map(p -> p).collect(Collectors.toCollection(ArrayList::new));

            Request r = RequestsDB.getInstance().getRequestDB().get(index);
            if ( !(r instanceof Applicant_Application) || r.status != ApplicationStatus.PENDING ||
            !managed_projects.stream().anyMatch(p -> p.Details.ProjectName.equals(r.RegardingProject)))
            { throw new Exception("\nError: Not allowed to handle given request!"); }

            SystemUser u = UserInfoDB.getInstance().SearchDB(r.initiator.userID);

            r.handler = this;
            if (approve) { 
                ((Applicant)u).AppliedProject = r.RegardingProject;
                ((Applicant)u).AppliedProjectStatus = ApplicantStatus.SUCCESSFUL;
                r.status = ApplicationStatus.APPROVED;

            } else { 
                ((Applicant)u).AppliedProjectStatus = ApplicantStatus.UNSUCCESSFUL;
                r.status = ApplicationStatus.REJECTED;
             }
            RequestsDB.getInstance().ModifyDB(r, DB_Action.EDIT);
            UserInfoDB.getInstance().ModifyDB(u,  DB_Action.EDIT);
            System.out.println("\nSuccessfully " + (approve ? "approved" : "rejected") + " applicant application");
            
        }
        catch (Exception e) { System.out.println( e.getMessage()); }

    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ////////////////// VIEW AND HANDLE WITHDRAWALS ////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    
    public void ViewWithdrawals()
    {
        try {
            ArrayList<Project> managed_projects = ProjectListingDB.getInstance().getProjectDB().stream()
            .filter(p -> p.Details.Manager.userID.equals(userID)).map(p -> p).collect(Collectors.toCollection(ArrayList::new));

            if (managed_projects.size() == 0) { throw new Exception("\nError: you are not managing any projects!"); }

            for (Project p : managed_projects)
            {
                ArrayList<IFilter> filters = new ArrayList<>();
                filters.add(new Filter_ProjectName(p.Details.ProjectName, "WITHDRAWAL"));
                RequestsDB.getInstance().ViewDB(filters);
            }
        }
        catch (Exception e) { System.out.println( e.getMessage()); }

    }

    public void HandleWithdrawals(int index, boolean approve)
    {
        try {
            ArrayList<Project> managed_projects = ProjectListingDB.getInstance().getProjectDB().stream()
            .filter(p -> p.Details.Manager.userID.equals(userID)).map(p -> p).collect(Collectors.toCollection(ArrayList::new));

            Request r = RequestsDB.getInstance().getRequestDB().get(index);
            if ( !(r instanceof Withdrawal) || r.status != ApplicationStatus.PENDING ||
            !managed_projects.stream().anyMatch(p -> p.Details.ProjectName.equals(r.RegardingProject)))
            { throw new Exception("\nError: Not allowed to handle given request!"); }

            SystemUser u = UserInfoDB.getInstance().SearchDB(r.initiator.userID);
            Project p = ProjectListingDB.getInstance().SearchDB(r.RegardingProject);

            r.handler = this;
            if (approve) { 
                ((Applicant)u).AppliedProject = "";
                ((Applicant)u).AppliedProjectStatus = ApplicantStatus.UNSUCCESSFUL;
                r.status = ApplicationStatus.APPROVED;
                //if booked and withdrawing
                if (((Withdrawal)r).BookedFlatType != Enum_FlatType.DEFAULT) {
                    ((Applicant)u).flatTypeBooked = Enum_FlatType.DEFAULT;
                    p.UnsellUnit(((Withdrawal)r).BookedFlatType);
                    ProjectListingDB.getInstance().ModifyDB(p, DB_Action.EDIT);
                 }

            } else { 
                r.status = ApplicationStatus.REJECTED;
             }
            RequestsDB.getInstance().ModifyDB(r, DB_Action.EDIT);
            UserInfoDB.getInstance().ModifyDB(u,  DB_Action.EDIT);
            System.out.println("\nSuccessfully " + (approve ? "approved" : "rejected") + " withdrawal");
            
        }
        catch (Exception e) { System.out.println( e.getMessage()); }

    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ////////////////// VIEW AND HANDLE ENQUIRIES //////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    public void ViewAllEnquiries() {
        EnquiryDB.getInstance().ViewDB();
    }

    public void ViewHandledEnquiries() {
        try {
            ArrayList<Project> managed_projects = ProjectListingDB.getInstance().getProjectDB().stream()
            .filter(p -> p.Details.Manager.userID.equals(userID)).map(p -> p).collect(Collectors.toCollection(ArrayList::new));

            if (managed_projects.size() == 0) { throw new Exception("\nError: you are not managing any projects!"); }

            for (Project p : managed_projects)
            {
                ArrayList<IFilter> filters = new ArrayList<>();
                filters.add(new Filter_ProjectName(p.Details.ProjectName, ""));
                EnquiryDB.getInstance().ViewDB(filters);
            }
        }
        catch (Exception e) { System.out.println( e.getMessage()); }
    }

    public void HandleEnquiries(int index, String reply)
    {
        try {
            ArrayList<Project> managed_projects = ProjectListingDB.getInstance().getProjectDB().stream()
            .filter(p -> p.Details.Manager.userID.equals(userID)).map(p -> p).collect(Collectors.toCollection(ArrayList::new));

            Enquiry e = EnquiryDB.getInstance().getEnquiryDB().get(index);
            if ( !e.isUnreplied() || !managed_projects.stream().anyMatch(p -> p.Details.ProjectName.equals(e.RegardingProject)))
            { throw new Exception("\nError: Not allowed to handle given enquiry!"); }

            e.Replier = this;
            e.Reply = reply;
            
            EnquiryDB.getInstance().ModifyDB(e, DB_Action.EDIT);
            System.out.println("\nSuccessfully replied to enquiry");
            
        }
        catch (Exception e) { System.out.println( e.getMessage()); }
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////  GENERATE REPORT  /////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    
    // Able to generate a report of the list of applicants with their respective
    //flat booking – flat type, project name, age, marital status
    //There should be filters to generate a list based on various categories
    //(e.g. report of married applicants’ choice of flat type) 

    public void GenerateReport(String project_name, ArrayList<IFilter> activeFilters) {
        try {
            Project p = ProjectListingDB.getInstance().SearchDB(project_name);
            if (p == null) { throw new Exception("\nError: could not find project!"); }
            if (!p.Details.Manager.userID.equals(userID)) { throw new Exception("\nError: project is not managed by you!"); }
            ReportGenerator.getInstance().GenerateReport(project_name, activeFilters);
        }
        catch (Exception e) { System.out.println( e.getMessage()); }
    }

}