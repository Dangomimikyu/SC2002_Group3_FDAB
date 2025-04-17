package UserView;

import Database.ProjectListingDB;
import Entity.Enquiry;
import Entity.Project;
import Managers.EnquiryManager;
import User.Applicant;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Optional;

public class ApplicantMenu extends Menu
{
    private static Applicant user;

    public static void start()
    {
        Display();
    }
    public static void SetUser(Applicant a)
    {
        user = a;
    }
    private static void Display()
    {
        int choice = -1;
        while (choice != 7) {
            System.out.println("============================");
            System.out.println("|      Applicant menu      |");
            System.out.println("| 1. View projects         |");
            System.out.println("| 2. Apply for a project   |");
            System.out.println("| 3. Create an enquiry     |");
            System.out.println("| 4. View your enquiries   |");
            System.out.println("| 5. Edit your enquiries   |");
            System.out.println("| 6. Delete an enquiry     |");
            System.out.println("| 7. Book a flat           |");
            System.out.println("| 8. Log out               |");
            System.out.println("============================");
            System.out.print("Enter choice: ");
            try
            {
                choice = sc.nextInt();
            }
            catch (final InputMismatchException e)
            {
                System.out.println("Invalid character");
                sc.nextLine(); // ignore and move the cursor to next line
                continue;
            }

            switch (choice)
            {
                case 1: // view available projects based on the applicant's stats
                    ViewProjects();
                    break;

                case 2: // apply for project
                    ApplyProject();
                    break;

                case 3: // make a new enquiry
                    MakeEnquiry();
                    break;

                case 4: // view applicant's own enquiries
                    ViewEnquiries();
                    break;

                case 5: // edit applicant's own enquiries
                    EditEnquiry();
                    break;

                case 6: // delete an enquiry
                    DeleteEnquiry();
                    break;

                case 7: // book a flat
                    BookFlat();
                    break;

                case 8: // log out
                    System.out.println("Logging out");
                    return;

                default:
                    System.out.println("Invalid number");
                    break;

            }
        }
    }

    private static void ViewProjects()
    {
        // filter by applicant's status and stuff
         ArrayList<Project> pList = ProjectListingDB.getInstance().ViewDB();
         PrintProjects(pList);
    }

    private static void ApplyProject()
    {
        // use ApplicationManager to make a request
        // request will be sent to RequestDB to be stored
    }

    private static void MakeEnquiry()
    {
        System.out.print("Enter name of project you're enquiring about: ");
        String pName = sc.nextLine();
        System.out.print("Enter title for the enquiry: ");
        String pTitle = sc.nextLine();
        System.out.print("Enter description");
        String pDesc = sc.nextLine();

        Enquiry e = new Enquiry(pName, pTitle, pDesc, user);
        EnquiryManager.getInstance().AddNewEnquiry(e);
    }

    private static void ViewEnquiries()
    {
//        ArrayList<Enquiry> enqList = user.GetEnquiries();
//        PrintEnquiries(enqList);
    }

    private static void EditEnquiry()
    {
//        ArrayList<Enquiry> enqList = user.GetEnquiries();
//        PrintEnquiries(enqList);

        System.out.print("Enter name of enquiry to edit: ");
        String eName = sc.nextLine();
//        Optional<Enquiry> enq = enqList.stream().filter(en -> eName.equals(en.Title)).findFirst();

        System.out.print("Enter new description: ");
        String desc = sc.nextLine();
//        enq.Description = desc;

//        EnquiryManager.getInstance().EditEnquiry(enq);
    }

    private static void DeleteEnquiry()
    {
        System.out.println("Your enquiries:");
        ViewEnquiries();

        System.out.print("Enter title of the enquiry to delete: ");
        String name = sc.nextLine();

        if (Objects.equals(name, ""))
        {
            System.out.println("Aborting delete");
            return;
        }

//        ArrayList<Enquiry> enqList = user.GetEnquiries();
//        Optional<Enquiry> enq = enqList.stream().filter(en -> enqName.equals(en.Title)).findFirst();
//        if (enq.isPresent())
//        {
//        user.RemoveEnquiry(enq);
//        }
//        else {
//            System.out.println("No enquiry with that name");
//        }
    }

    private static void BookFlat()
    {
        if (user.AppliedProjectStatus == Applicant.ApplicantStatus.SUCCESSFUL)
        {
            // able to book
            System.out.println("Choose a project to apply to:");
            ArrayList<Project> pList = ProjectListingDB.getInstance().ViewDB();
            PrintProjects(pList);

            System.out.print("Enter name of project to apply to: ");
            String projName = sc.nextLine();


            Optional<Project> p = pList.stream().filter(pp -> pList.equals(pp.Details.ProjectName)).findFirst();
            if (p.isPresent())
            {
//            ApplicationManager.CreateBookingRequest(p, user);
            }
            else {
                System.out.println("No project with that name");
            }
        }
        else
        {
            System.out.println("You are not able to book a flat yet");
        }
    }
}
