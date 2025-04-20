package Main;

import Database.*;
import UserView.*;

public class MainApp {
    private static String ApplicantFilePath;
    private static String OfficerFilePath;
    private static String ManagerFilePath;
    private static String ProjectsFilePath;
    private static String EnquiriesFilePath;
    private static String RequestsFilePath;

    public static void main(String[] args) {
        Init();
        HDB_System HDB_system = new HDB_System(ApplicantFilePath, OfficerFilePath, ManagerFilePath, EnquiriesFilePath, ProjectsFilePath, RequestsFilePath);
        Login();

    }

    //initialise file paths
    private static void Init()
    {
        ApplicantFilePath = "src/localdata/ApplicantList.csv";
        OfficerFilePath = "src/localdata/OfficerList.csv";
        ManagerFilePath = "src/localdata/ManagerList.csv";
        ProjectsFilePath = "src/localdata/ProjectList.csv";
        EnquiriesFilePath = "src/localdata/Enquiries.csv";
        RequestsFilePath = "src/localdata/Requests.csv";

    }

    //initate log-in menu
    private static void Login() { LoginMenu.start(); }

}
