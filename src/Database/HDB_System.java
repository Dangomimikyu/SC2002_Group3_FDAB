package Database;
//HDB_System is the database class that will be used to store all the data of the system
//it will the database of UserInfoDB, EnquiryDB, ProjectListingDB and RequestsDB respectively
//main.java will call the HDB_System class to access the database of the system

public class HDB_System {

    private static final HDB_System instance = new HDB_System();

    public UserInfoDB users = UserInfoDB.getInstance();
    public EnquiryDB enquiries = EnquiryDB.getInstance();
    public ProjectListingDB projects = ProjectListingDB.getInstance();
    public RequestsDB requests = RequestsDB.getInstance();

    private HDB_System() {}
    public static HDB_System getInstance() {
        return instance;
    }

    public HDB_System(String ApplicantFilePath, String OfficerFilePath, String ManagerFilePath, 
    String EnquiriesFilePath, String ProjectFilePath, String RequestsFilePath) {

        this.users = new UserInfoDB(ApplicantFilePath, OfficerFilePath, ManagerFilePath);
        this.enquiries = new EnquiryDB(EnquiriesFilePath, users.ViewDB());
        this.projects = new ProjectListingDB(ProjectFilePath, users.ViewDB());
        this.requests = new RequestsDB(RequestsFilePath, users.ViewDB());
    }
}