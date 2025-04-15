package User;


public class HDB_Manager extends SystemUser{
    private EnquiryManager enquiryManager;
    private OfficerManager officerManager;
    private ListingManager listingManager;
    
    public HDB_Manager(String nric, String p, String n) {
        super(nric, p, n);
        this.ApplicantPerms = usertype.MANAGER;
    }

}