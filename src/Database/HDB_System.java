package Database;
//HDB_System is the database class that will be used to store all the data of the system
//it will the database of UserInfoDB, EnquiryDB, ProjectListingDB and RequestsDB respectively
//main.java will call the HDB_System class to access the database of the system

public class HDB_System {

    public UserInfoDB users = UserInfoDB.getInstance();
    public EnquiryDB enquiries = EnquiryDB.getInstance();
    public ProjectListingDB projects = ProjectListingDB.getInstance();
    public RequestsDB requests = RequestsDB.getInstance();

    public HDB_System(String ApplicantFilePath, String OfficerFilePath, String ManagerFilePath, 
    String EnquiriesFilePath, String ProjectFilePath, String RequestsFilePath) {

        users.InitialiseDB(ApplicantFilePath, OfficerFilePath, ManagerFilePath);
        enquiries.InitialiseDB(EnquiriesFilePath, users.getUserDB());
        projects.InitialiseDB(ProjectFilePath, users.getUserDB());
        requests.InitialiseDB(RequestsFilePath, users.getUserDB());
    }
}