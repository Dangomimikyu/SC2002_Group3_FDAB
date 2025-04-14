import Database.*;

public class MainApp {
    public static void main(String[] args) {

        //initialisation of HDB_System
        //specify file paths here
        String ApplicantFilePath = "src/localdata/ApplicantList.csv";
        String OfficerFilePath = "src/localdata/OfficerList.csv";
        String ManagerFilePath = "src/localdata/ManagerList.csv";
        String ProjectsFilePath = "src/localdata/ProjectList.csv";
        String EnquiriesFilePath = "src/localdata/Enquiries.csv";
        String RequestsFilePath = "src/localdata/Requests.csv";
        HDB_System HDB_system = HDB_System.getInstance();
        HDB_system = new HDB_System(ApplicantFilePath, OfficerFilePath, ManagerFilePath, EnquiriesFilePath, ProjectsFilePath, RequestsFilePath);

        //test
        HDB_system.users.ViewDB();
        HDB_system.enquiries.ViewDB();
        HDB_system.projects.ViewDB();
        HDB_system.requests.ViewDB();
    }

}
