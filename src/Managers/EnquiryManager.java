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

        ArrayList<Enquiry> enqList = EnquiryDB.getInstance().ViewDB();

        // maybe can replace when the DB has its own print function?
        for (Enquiry e : enqList)
        {
            System.out.println(e.getEnquiryDetails());
        }
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
        ViewEnquiries(s);

        ArrayList<Enquiry> enqList = EnquiryDB.getInstance().ViewDB();

        System.out.print("Enter name of enquiry to reply to: ");
        String enqName = sc.nextLine();

        Optional<Enquiry> enq = enqList.stream().filter(en -> enqName.equals(en.Title)).findFirst();
        if (enq.isPresent())
        {
            System.out.print("Enter reply:");
            String reply = sc.nextLine();
            enq.get().Reply = reply; // must replace this with the function
        }
        else
        {
            System.out.println("No enquiry by that name.");
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
