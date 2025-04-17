package Service;


import Entity.Applicant_Application;
import Entity.Project;
import Entity.ProjectDetails;
import Entity.Receipt;
import User.Enum_FlatType;
import User.HDB_Officer;
import Database.HDB_System;
import User.Applicant;

public class ReceiptGenerator {

    private HDB_System hdbSystem = HDB_System.getInstance();

    public Receipt generateReceipt(String applicantNRIC, String projectName, Enum_FlatType flatType, String officerID) {
        Applicant applicant = (Applicant) hdbSystem.users.ViewDB().stream()
                .filter(user -> user.userID.equals(applicantNRIC) && user instanceof Applicant)
                .findFirst()
                .orElse(null);

        Project project = hdbSystem.projects.ViewDB().stream()
                .filter(proj -> Service.Details.ProjectName.equals(projectName))
                .findFirst()
                .orElse(null);

        HDB_Officer officer = (HDB_Officer) hdbSystem.users.ViewDB().stream()
                .filter(user -> user.userID.equals(officerID) && user instanceof HDB_Officer)
                .findFirst()
                .orElse(null);

        if (applicant != null && project != null && officer != null) {
            
            Applicant_Application application = (Applicant_Application) hdbSystem.requests.ViewDB().stream()
                    .filter(request -> request instanceof Applicant_Application &&
                                      request.initiator.userID.equals(applicantNRIC) &&
                                      request.RegardingProject.equals(projectName) &&
                                      request.status == Entity.Request.ApplicationStatus.APPROVED) 
                    .findFirst()
                    .orElse(null);

            if (application != null) {
                return officer.GenerateReceipt(application, flatType);
            } else {
                System.out.println("Error: No approved application found for this applicant and project.");
                return null;
            }
        } else {
            System.out.println("Error: Applicant, Project, or Officer not found.");
            return null;
        }
    }
}