package Managers;

import Database.Database;
import Database.EnquiryDB;
import Database.ProjectListingDB;
import InteractableAttributePackage.Enquiry;
import User.Applicant;
import User.SystemUser;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class EnquiryManager
{
    private static EnquiryManager instance;

    private EnquiryManager()
    {

    }

    public static EnquiryManager getInstance()
    {
        if (instance == null)
        {
            instance = new EnquiryManager();
        }
        return instance;
    }

    public void ViewEnquiries(SystemUser s)
    {
        if (s instanceof Applicant)
        {
            // this should never happen but just in case
            System.out.println("This user doesn't have the ability to see all enquiries");
            return;
        }

        EnquiryDB.getInstance().ViewDB();
    }

    public void ReplyEnquiry(SystemUser s)
    {
        if (s instanceof Applicant)
        {
            // this should never happen but just in case
            System.out.println("This user doesn't have the ability to see all enquiries");
            return;
        }
        Scanner sc = new Scanner(System.in);

        System.out.println("Choose an enquiry to reply to:");
        EnquiryDB.getInstance().ViewDB();

        System.out.print("Enter name of enquiry to reply to: ");
        String enqName = sc.nextLine();

        Enquiry enq = EnquiryDB.getInstance().SearchDB(enqName);
        if (enq != null)
        {
            System.out.print("Enter reply:");
            String reply = sc.nextLine();
            enq.Reply = reply; // must replace this with the function
        }
        else
        {
            System.out.println("No enquiry with a project by that name.");
        }
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
