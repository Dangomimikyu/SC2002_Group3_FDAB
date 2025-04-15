package User;

import java.util.ArrayList;
import java.util.Map;

import Entity.Project;
import Entity.ProjectDetails;

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
        //to be completed
    }

    public ProjectDetails ViewProjectDetails() {
        //to be completed
    }

    public boolean UpdateFlatSelection(Application app, Enum_FlatType flat) {
        //to be completed
    }

    public Receipt GenerateReceipt(Application app, Enum_FlatType flat) {
        //to be completed
    }
    
}