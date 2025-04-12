package User;

public class Applicant extends SystemUser{

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

    public int age;
    public String maritalStatus;
    public String AppliedProject; //applied project name
    public ApplicantStatus AppliedProjectStatus;

    public Applicant(String n, String nric, int a, String m, String p, String AP, String APS) {
        super(nric, p, n);
        this.age = a;
        this.maritalStatus = m;
        this.ApplicantPerms = usertype.APPLICANT;
        this.AppliedProject = AP;
        this.AppliedProjectStatus = ApplicantStatus.valueOf(APS);
    }
}