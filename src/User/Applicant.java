package User;

import Entity.Project;
import Entity.Enquiry;
import Entity.EnquiryManager;
import Entity.Applicant_Application;
import java.util.List;
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
        SINGLE, MARRIED
    }

    public int age;
    public MaritalStatus maritalStatus;
    public String AppliedProject;
    public ApplicantStatus AppliedProjectStatus = ApplicantStatus.UNSUCCESSFUL;

    private EnquiryManager enquiryManager = new EnquiryManager();
    private String flatTypeBooked = null;

    public Applicant(String n, String nric, int a, String m, String p, String AP, String APS) {
        super(nric, p, n);
        this.age = a;
        this.maritalStatus = MaritalStatus.valueOf(m);
        this.ApplicantPerms = usertype.APPLICANT;
        this.AppliedProject = AP;
        this.AppliedProjectStatus = ApplicantStatus.valueOf(APS);
    }

    public void ApplyProject(Project pr) {
        if (this.AppliedProjectStatus == ApplicantStatus.UNSUCCESSFUL)   {
            System.out.println("Applicant has already applied for a project");
            return;
        }

        if (!pr.isOpenForApplication()) {
            System.out.println("This project is not open for application.");
            return;
        }

        if (!pr.Details.activeStatus) {
            System.out.println("The project is not visible to applicants.");
            return;
        }

        String applicantGroup = this.maritalStatus.toString();
        String projectGroup = pr.Details.OpentoUserGroup.name();

        if (!(projectGroup.equals("ALL") || projectGroup.equals(applicantGroup))) {
            System.out.println("This project is not open to your marital status group.");
            return;
        }

        // If passes all checks, apply
        this.AppliedProject = pr.Details.ProjectName;
        this.AppliedProjectStatus = ApplicantStatus.PENDING;
        System.out.println("Application submitted successfully!");
    }

    public void CreateEnquiry() {
        System.out.println("Enter the title of the enquiry:");
        String title = scanner.nextLine();
        System.out.println("Enter the name of the project you are referring to:");
        String projectName = scanner.nextLine();
        System.out.println("Enter the description of the enquiry:");
        String description = scanner.nextLine();
        Enquiry newEnquiry = new Enquiry(title, projectName, description, this);
        enquiryManager.addEnquiry(newEnquiry);
        System.out.println("Enquiry created: " + newEnquiry.getEnquiryDetails());
    }

    public void ViewEnquiry() {
        List<Enquiry> enquiries = enquiryManager.getEnquiriesByApplicant(this);
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found.");
        } else {
            for (Enquiry enquiry : enquiries) {
                System.out.println(enquiry.getEnquiryDetails());
            }
        }
    }

    public void EditEnquiry() {
        System.out.println("Enter the title of the enquiry you want to edit:");
        String title = scanner.nextLine();
        Enquiry enquiry = enquiryManager.getEnquiryByTitleAndApplicant(title, this);
        if (enquiry != null) {
            System.out.println("Enter the new description:");
            String newDescription = scanner.nextLine();
            enquiry.Description = newDescription;
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
            enquiryManager.removeEnquiry(enquiry);
            System.out.println("Enquiry deleted: " + title);
        } else {
            System.out.println("Enquiry not found or you are not the creator.");
        }
    }

    public boolean BookFlat(HDB_Officer offr, Applicant_Application app) {
        //to be completed
    }
}