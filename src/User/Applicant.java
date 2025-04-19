package User;

import Database.*;
import InteractableAttributePackage.*;
import Managers.EnquiryManager;

import java.util.ArrayList;
import java.util.Scanner;

public class Applicant extends SystemUser{
    Scanner scanner = new Scanner(System.in);
    public enum ApplicantStatus {
        PENDING, SUCCESSFUL, UNSUCCESSFUL, BOOKED

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

    public Applicant(String nric, String p, String n, int a, String m) {
        super(nric, p, n);
        this.age = a;
        this.maritalStatus = MaritalStatus.valueOf(m);
    }

    public Applicant(String n, String nric, int a, String m, String p, String appliedProj, String projStatus) {
        super(nric, p, n);
        this.age = a;
        this.maritalStatus = MaritalStatus.valueOf(m);
        this.AppliedProject = appliedProj;
        this.AppliedProjectStatus = switch(projStatus) {
            case "SUCCESSFUL" -> ApplicantStatus.SUCCESSFUL;
            case "PENDING" -> ApplicantStatus.PENDING;
            case "BOOKED" -> ApplicantStatus.BOOKED;
            default -> ApplicantStatus.UNSUCCESSFUL;
        };
    }

    public void viewProjectList() {
        ArrayList<Project> projectList = ProjectListingDB.getInstance().getProjectDB();
        boolean hasVisibleProjects = false;

        System.out.println("Available Projects:");
        for (Project project : projectList) {
            // Check if the project is active and visible to the user's marital status group
            if (project.Details.activeStatus &&
                    (project.Details.OpentoUserGroup.toString().equals("ALL") ||
                            project.Details.OpentoUserGroup == this.maritalStatus)) {
                System.out.println(project.getProjectDetails());
                hasVisibleProjects = true;
            }
        }
        // If no projects are available for the user's marital status or visibility is off
        // Print a message indicating that no projects are available
        if (!hasVisibleProjects) {
            System.out.println("No projects available for your marital status or visibility is off.");
        }
    }

    public void ApplyProject(String projectName) {
        Project pr = ProjectListingDB.getInstance().getProjectDB().stream()
                .filter(p -> p.Details.ProjectName.equals(projectName))
                .findFirst()
                .orElse(null);
        if (pr == null) {
            System.out.println("Project not found.");
            return;
        }
        if (this.AppliedProjectStatus != ApplicantStatus.UNSUCCESSFUL)   {
            System.out.println("Applicant has already applied for a project");
            return;
        }

        if (!pr.isOpenForApplication()) {
            System.out.println("This project is not open for application.");
            return;
        }

        // Check if the applicant is already an officer in the project
        if (pr.Details.OfficerList.stream().anyMatch(officer -> officer.userID.equals(this.userID))) {
            System.out.println("You cannot apply for a project where you are already an Officer.");
            return;
        }

        String applicantGroup = this.maritalStatus.name();
        String projectGroup = pr.Details.OpentoUserGroup.name();

        if (!(projectGroup.equals("ALL") || projectGroup.equals(applicantGroup))) {
            System.out.println("This project is not open to your marital status group.");
            return;
        }

        // If passes all checks, apply
        Applicant_Application application = new Applicant_Application(this, projectName);
        RequestsDB.getInstance().ModifyDB(application, Database.DB_Action.ADD);
        this.AppliedProjectStatus = ApplicantStatus.PENDING;
        System.out.println("Application submitted successfully. Waiting for manager approval.");
    }


    public void RequestWithdrawal() {
        // Check if the applicant has applied for a project
        if (this.AppliedProjectStatus == ApplicantStatus.UNSUCCESSFUL) {
            System.out.println("You have not applied for any project. No withdrawal request can be made.");
            return;
        }

        // Create a withdrawal request
        Withdrawal withdrawalRequest = new Withdrawal(this, this.AppliedProject);
        if (this.flatTypeBooked.equals(Enum_FlatType.TWO_ROOM)) {
            withdrawalRequest.BookedFlatType = Enum_FlatType.TWO_ROOM;
        } else {
            withdrawalRequest.BookedFlatType = Enum_FlatType.THREE_ROOM;
        }

        // Add the request to the RequestsDB
        RequestsDB.getInstance().ModifyDB(withdrawalRequest, Database.DB_Action.ADD);
        System.out.println("Withdrawal request submitted successfully. Waiting for manager approval.");
    }


    public void BookFlat() {
        // Check if the applicant has applied for the project successfully
        if (this.AppliedProjectStatus != ApplicantStatus.SUCCESSFUL) {
            System.out.println("You are not eligible to book a flat. Please ensure your application is approved.");
            return;
        }

        // Get the project details
        Project project = ProjectListingDB.getInstance().getProjectDB().stream()
                .filter(p -> p.Details.ProjectName.equals(this.AppliedProject))
                .findFirst()
                .orElse(null);

        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        System.out.println("Available flat types for project: " + project.Details.ProjectName);

        // Show available flat types based on marital status
        if (this.maritalStatus == MaritalStatus.SINGLE) {
            System.out.println("1. 2-Room (" + project.Details.NoOfUnitsLeft_2Room + " units available)");
        }
        else if (this.maritalStatus == MaritalStatus.MARRIED) {
            if (project.Details.NoOfUnitsLeft_2Room > 0) {
                System.out.println("1. 2-Room (" + project.Details.NoOfUnitsLeft_2Room + " units available)");
            }
            if (project.Details.NoOfUnitsLeft_3Room > 0) {
                System.out.println("2. 3-Room (" + project.Details.NoOfUnitsLeft_3Room + " units available)");
            }
        }


        System.out.println("Enter the number corresponding to the flat type you want to book:");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1 && project.Details.NoOfUnitsLeft_2Room > 0) {
            project.SellUnit(Enum_FlatType.TWO_ROOM);
            this.flatTypeBooked = Enum_FlatType.TWO_ROOM;
            this.AppliedProjectStatus = ApplicantStatus.BOOKED;
            System.out.println("You have successfully booked a 2-Room flat in project: " + project.Details.ProjectName);
        } else if (choice == 2 && project.Details.NoOfUnitsLeft_3Room > 0 && this.maritalStatus == MaritalStatus.MARRIED) {
            project.SellUnit(Enum_FlatType.THREE_ROOM);
            this.flatTypeBooked = Enum_FlatType.THREE_ROOM;
            this.AppliedProjectStatus = ApplicantStatus.BOOKED;
            System.out.println("You have successfully booked a 3-Room flat in project: " + project.Details.ProjectName);
        }
    }

    public void CreateEnquiry() {
        System.out.println("Enter the title of the enquiry:");
        String title = scanner.nextLine();
        System.out.println("Enter the name of the project you are referring to:");
        String projectName = scanner.nextLine();
        System.out.println("Enter the description of the enquiry:");
        String description = scanner.nextLine();
        Enquiry newEnquiry = new Enquiry(title, projectName, description, this);
        enquiryManager.AddNewEnquiry(newEnquiry);
        System.out.println("Enquiry created: " + newEnquiry.getEnquiryDetails());
    }

    public void ViewEnquiry() {
        enquiryManager.ViewEnquiries(this);
    }

    public void EditEnquiry() {
        System.out.println("Enter the title of the enquiry you want to edit:");
        String title = scanner.nextLine();
        Enquiry enquiry = enquiryManager.getEnquiryByTitleAndApplicant(title, this);
        if (enquiry != null) {
            System.out.println("Enter the new description:");
            String newDescription = scanner.nextLine();
            enquiry.Description = newDescription;
            enquiryManager.EditEnquiry(enquiry);
            System.out.println("Enquiry updated: " + enquiry.getEnquiryDetails());
        } else {
            System.out.println("Enquiry not found or you are not the creator.");
        }
    }

    public void DeleteEnquiry() {
        System.out.println("Enter the title of the enquiry you want to delete:");
        String title = scanner.nextLine();
        Enquiry enquiry = enquiryManager.getEnquiryByTitleAndApplicant(title, this);
        if (enquiry != null) {
            enquiryManager.DeleteEnquiry(enquiry);
            System.out.println("Enquiry deleted: " + title);
        } else {
            System.out.println("Enquiry not found or you are not the creator.");
        }
    }

}