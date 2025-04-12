import Database.*;

public class MainApp {
    public static void main(String[] args) {

        //initialisation of localdata
        String ApplicantFilePath = "src/localdata/ApplicantList.csv";
        String OfficerFilePath = "src/localdata/OfficerList.csv";
        String ManagerFilePath = "src/localdata/ManagerList.csv";
        String ProjectsFilePath = "src/localdata/ProjectList.csv";
        String EnquiriesFilePath = "src/localdata/Enquiries.csv";
        String RequestsFilePath = "src/localdata/Requests.csv";
        HDB_System HDB_system = new HDB_System(ApplicantFilePath, OfficerFilePath, ManagerFilePath, ProjectsFilePath, EnquiriesFilePath, RequestsFilePath);

        //initialisation of controllers (singleton classes - only initialised once)
        // ArrayList<SystemUser> all_users = localdata.get_all_users();
        // SystemUser pre_user = new SystemUser();

        // //Log-in Menu
        // login_loop:
        // while (true) {
        //     String[] login_info = pre_user.Login().split(",");
        //     for (SystemUser stored_user : all_users) {
                
        //         if (stored_user.getUserID().equals(login_info[0]) && stored_user.getPassword().equals(login_info[1])) {
        //             pre_user = stored_user;
        //             break login_loop;
        //         }
        //     }
        //     System.out.println("Error: Cannot find such user in Database!");
        // }


        // if (pre_user instanceof Applicant) {
        //     Applicant user = (Applicant)pre_user;
        // }

        // else if (pre_user instanceof HDBOfficer) {
        //     HDBOfficer user = (HDBOfficer)pre_user;
        // }

        // else if (pre_user instanceof HDBManager) {
        //     HDBManager user = (HDBManager)pre_user;
        // }

    }

}
