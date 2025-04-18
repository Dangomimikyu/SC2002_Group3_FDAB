package Service;


import Database.ProjectListingDB;
import Database.RequestsDB;
import Database.UserInfoDB;
import InteractableAttributePackage.Applicant_Application;
import InteractableAttributePackage.Project;
import InteractableAttributePackage.Receipt;
import InteractableAttributePackage.Request;
import User.Enum_FlatType;
import User.HDB_Officer;
import User.Applicant;

public class ReceiptGenerator {

    public Receipt generateReceipt(String applicantNRIC, String projectName, Enum_FlatType flatType, String officerID) {
        Applicant applicant = (Applicant) UserInfoDB.getInstance().getUserDB().stream()
                .filter(user -> user.userID.equals(applicantNRIC) && user instanceof Applicant)
                .findFirst()
                .orElse(null);

        Project project = ProjectListingDB.getInstance().getProjectDB().stream()
                .filter(proj -> proj.Details.ProjectName.equals(projectName))
                .findFirst()
                .orElse(null);

        HDB_Officer officer = (HDB_Officer) UserInfoDB.getInstance().getUserDB().stream()
                .filter(user -> user.userID.equals(officerID) && user instanceof HDB_Officer)
                .findFirst()
                .orElse(null);

        if (applicant != null && project != null && officer != null) {
            
            Applicant_Application application = (Applicant_Application) RequestsDB.getInstance().getDB().stream()
                    .filter(request -> request instanceof Applicant_Application &&
                                      ((Applicant_Application) request).initiator.userID.equals(applicantNRIC) &&
                                      ((Applicant_Application) request).RegardingProject.equals(projectName) &&
                                      ((Applicant_Application) request).status == Request.ApplicationStatus.APPROVED)
                    .findFirst()
                    .orElse(null);

            if (application != null) {
//                return officer.GenerateReceipt(application, flatType);
                officer.GenerateReceipt(applicantNRIC);
                return null;
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