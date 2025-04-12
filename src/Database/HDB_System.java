package Database;
//HDB_System is the database class that will be used to store all the data of the system
//it will the database of UserInfoDB, EnquiryDB, ProjectListingDB and RequestsDB respectively
//main.java will call the HDB_System class to access the database of the system

public class HDB_System {
    private UserInfoDB userInfoDB;
    private EnquiryDB enquiryDB;
    private ProjectListingDB projectListingDB;
    private RequestsDB requestsDB;

    public HDB_System(String ApplicantFilePath, String OfficerFilePath, String ManagerFilePath, 
    String EnquiriesFilePath, String ProjectFilePath, String RequestsFilePath) {

        this.userInfoDB = new UserInfoDB(ApplicantFilePath, OfficerFilePath, ManagerFilePath);
        this.enquiryDB = new EnquiryDB(EnquiriesFilePath, userInfoDB.ViewDB());
        this.projectListingDB = new ProjectListingDB(ProjectFilePath, userInfoDB.ViewDB());
        this.requestsDB = new RequestsDB(RequestsFilePath, userInfoDB.ViewDB());
    }
}