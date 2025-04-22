package Managers;

import Database.Database;
import Database.EnquiryDB;
import InteractableAttributePackage.Enquiry;
import User.Applicant;
import User.HDB_Officer;
import User.SystemUser;
import java.util.Objects;

public class EnquiryManager
{
    private static EnquiryManager instance;

    private EnquiryManager() {}

    public static EnquiryManager getInstance()
    {
        if (instance == null)
        {
            instance = new EnquiryManager();
        }
        return instance;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    /////////////// ENQUIRY MANAGER FETCH ENQUIRIES DEPENDING ON APPLICANT OR OFFICER  ///////
    //////////////////////////////////////////////////////////////////////////////////////////

    public void ViewEnquiries(SystemUser s)
    {
        try {
            boolean hasEnquiries = false;
            //HDBOfficers can view all enquiries regarding their assigned project + created enquiries
            if (s instanceof HDB_Officer)
            {
                System.out.println("\nShowing handled enquiries:" + "\n====================================");
                for (Enquiry e : EnquiryDB.getInstance().getEnquiryDB())
                {
                    if (e.RegardingProject.equals(((HDB_Officer)s).project_name)) {
                        System.out.println(e.getEnquiryDetails() + "\n====================================");
                        hasEnquiries = true;
                    }
                }
            }
            //Applicants can only view all enquiries they have created
            else if (s instanceof Applicant)
            {
                System.out.println("\nShowing your enquiries:" + "\n====================================");
                for (Enquiry e : EnquiryDB.getInstance().getEnquiryDB())
                {
                    if (Objects.equals(e.Enquirer.userID, s.userID)) {
                        System.out.println(e.getEnquiryDetails() + "\n====================================");
                        hasEnquiries = true;
                    }
                }
            }
            if (!hasEnquiries) { throw new Exception("\nNo enquiries can be found!"); }
        }
        catch (Exception e) { System.out.println(e.getMessage()); }
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    /////////////// ENQUIRY MANAGER REPLIES TO ENQUIRIES FOR OFFICER /////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    public void ReplyEnquiry(Enquiry enq, SystemUser s, String reply)
    {
        try {
            enq.Replier = s;
            enq.Reply = reply;
            EditEnquiry(enq);
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }

    public Enquiry getEnquiryWithDetails(String title, SystemUser s, String projname)
    {
        return EnquiryDB.getInstance().getEnquiryDB().stream()
                .filter(enq -> Objects.equals(enq.Title, title) 
                && Objects.equals(enq.Enquirer.userID, s.userID) 
                && Objects.equals(enq.RegardingProject, projname))
                .findFirst()
                .orElse(null);
    }

    public boolean isNotUniqueEnquiry(String title, SystemUser s, String projname)
    {
        return EnquiryDB.getInstance().getEnquiryDB().stream()
                .anyMatch(enq -> Objects.equals(enq.Title, title) 
                && Objects.equals(enq.Enquirer.userID, s.userID) 
                && Objects.equals(enq.RegardingProject, projname));
    }

    public void AddNewEnquiry(Enquiry e)
    {
        EnquiryDB.getInstance().ModifyDB(e, Database.DB_Action.ADD);
    }

    public void EditEnquiry(Enquiry e)
    {
        EnquiryDB.getInstance().ModifyDB(e, Database.DB_Action.EDIT);
    }

    public void DeleteEnquiry(Enquiry e)
    {
        EnquiryDB.getInstance().ModifyDB(e, Database.DB_Action.DELETE);
    }
}
