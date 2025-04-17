package Main;

import Database.*;
import User.Applicant;
import User.HDB_Manager;
import User.HDB_Officer;
import User.SystemUser;
import UserView.*;

public class MainApp {
    private static String ApplicantFilePath;
    private static String OfficerFilePath;
    private static String ManagerFilePath;
    private static String ProjectsFilePath;
    private static String EnquiriesFilePath;
    private static String RequestsFilePath;
    private static SystemUser user = null;

    public static void main(String[] args) {
        Init();
        HDB_System HDB_system = new HDB_System(ApplicantFilePath, OfficerFilePath, ManagerFilePath, EnquiriesFilePath, ProjectsFilePath, RequestsFilePath);

        Login();

        UseSystem();

        // save changes then log out the user
    }

    private static void Init()
    {
        ApplicantFilePath = "src/localdata/ApplicantList.csv";
        OfficerFilePath = "src/localdata/OfficerList.csv";
        ManagerFilePath = "src/localdata/ManagerList.csv";
        ProjectsFilePath = "src/localdata/ProjectList.csv";
        EnquiriesFilePath = "src/localdata/Enquiries.csv";
        RequestsFilePath = "src/localdata/Requests.csv";
    }

    private static void Login()
    {
        while (user == null)
        {
            // either they get locked out at Auth or they back out at the LoginMenu
            LoginMenu.start();
        }
    }

    private static void UseSystem()
    {
        if (user instanceof HDB_Officer)
        {
            HDB_Officer offUser = (HDB_Officer)user;
            OfficerMenu.SetUser(offUser);
            OfficerMenu.start();
        }
        else if (user instanceof Applicant)
        {
            Applicant appUser = (Applicant)user;
            ApplicantMenu.SetUser(appUser);
            ApplicantMenu.start();
        }
        else if (user instanceof HDB_Manager)
        {
            HDB_Manager mgrUser = (HDB_Manager)user;
            ManagerMenu.SetUser(mgrUser);
            ManagerMenu.start();
        }
    }

    public static void SetUser(SystemUser u)
    {
        user = u;
    }

}
