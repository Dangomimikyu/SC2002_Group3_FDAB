package Service;

import Database.UserInfoDB;
import Filter.*;
import User.Applicant;
import User.HDB_Manager;
import User.SystemUser;
import java.util.ArrayList;

public class ReportGenerator
{
    private static ReportGenerator instance = null;

    private ReportGenerator() {}

    public static ReportGenerator getInstance()
    {
        if (instance == null) { instance = new ReportGenerator(); }
        return instance;
    }

    public void GenerateReport(String project_name, ArrayList<IFilter> activeFilters) {


        activeFilters.removeIf(f -> f instanceof Filter_Location || f instanceof Filter_ProjectName ||
        f instanceof Filter_SellingPrice || f instanceof Filter_Visibility);

        ArrayList<SystemUser> applicant_list =  UserInfoDB.getInstance().GetDB(activeFilters);
        applicant_list.removeIf(u -> u instanceof HDB_Manager);
        applicant_list.removeIf(u -> !((Applicant)u).AppliedProject.equals(project_name));
        //Filter_Age - only include applicants with age range (in A or D order)
        //Filter_Alphabetic - filter applicants starting with name (in A or D order)
        //Filter_FlatType - filter applicants who are booked with flat type
        //Filter_Marital - filter applicants with marital status
        //All other filters - ignore

        System.out.println("\nREPORT ON "+project_name.toUpperCase()+"\n===============================================");
        for (SystemUser u : applicant_list ) {
            System.out.println("\n"+(u.getUserDetails()));
        }
    }
}
