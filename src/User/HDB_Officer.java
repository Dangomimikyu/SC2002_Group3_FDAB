package User;

import java.util.ArrayList;
import java.util.Map;

import InteractableAttributePackage.*;
import javax.swing.text.html.parser.Entity;

//HDB Officer possess all applicant’s capabilities.
public class HDB_Officer extends Applicant{

    private Project projectAssigned;
    private Enum_OfficerStatus projectStatus;
    private ArrayList<Applicant> applicantList;
    private Map<String, Integer> flatAvailability;
    private String officerID;

    public HDB_Officer(String n, String nric, int a, String m, String p, String AP, String APS) {
        super(n, nric, a, m, p, AP, APS);
        this.ApplicantPerms = usertype.OFFICER;
    }

    public boolean RegisterForProject(Project pr) {
        if (pr == null) {
            System.out.println("Invalid project.");
            return false;
        }
        try {
            pr.Details.OfficerList.add(this);
            this.projectAssigned = pr;
            System.out.println("Successfully registered for project: " + pr.Details.ProjectName);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to register for project: " + e.getMessage());
            return false;
        }
    }

    public ProjectDetails ViewProjectDetails() {
        if (this.projectAssigned == null) {
            System.out.println("No project assigned.");
            return null;
        }
        System.out.println(this.projectAssigned.getProjectDetails());
        return this.projectAssigned.Details;
    }

    public boolean UpdateFlatSelection(Applicant_Application app, Enum_FlatType flat) {
        if (app == null || flat == null) {
            System.out.println("Invalid applicant or flat type.");
            return false;
        }
        if (app.AppliedProject != this.projectAssigned.Details.ProjectName) {
            System.out.println("Applicant is not part of the assigned project.");
            return false;
        }
        try {
            if (flat == Enum_FlatType.Two_room && this.projectAssigned.Details.NoOfUnitsLeft_2Room > 0) {
                this.projectAssigned.SellUnit(FlatType.TWO_ROOM);
                System.out.println("Updated flat selection to Two-room flat for applicant: " + app.name);
            } else if (flat == Enum_FlatType.Three_room && this.projectAssigned.Details.NoOfUnitsLeft_3Room > 0) {
                this.projectAssigned.SellUnit(FlatType.THREE_ROOM);
                System.out.println("Updated flat selection to Three-room flat for applicant: " + app.name);
            } else {
                System.out.println("No units available for the selected flat type.");
                return false;
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Error updating flat selection: " + e.getMessage());
            return false;
        }
    }

    public Receipt GenerateReceipt(Applicant_Application app, Enum_FlatType flat) {
        if (app == null || flat == null) {
            System.out.println("Invalid applicant or flat type.");
            return null;
        }
        if (!app.AppliedProject.equals(this.projectAssigned.Details.ProjectName)) {
            System.out.println("Applicant is not part of the assigned project.");
            return null;
        }
        Receipt receipt = new Receipt(app, flat, this.projectAssigned.Details);
        System.out.println("Receipt generated successfully:");
        System.out.println(receipt.getReceiptDetails());
        return receipt;
    }
}

}