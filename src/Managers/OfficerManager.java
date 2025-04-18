package Managers;

import Database.ProjectListingDB;
import InteractableAttributePackage.Project;
import User.HDB_Manager;
import User.HDB_Officer;
import User.SystemUser;

import java.time.LocalDate;
import java.util.*;

public class OfficerManager
{
    private static OfficerManager instance;
    private HashMap<HDB_Officer, ArrayList<String>> officerRequestList;

    public static OfficerManager getInstance()
    {
        if (instance == null)
        {
            instance = new OfficerManager();
        }
        return instance;
    }

    // Manager functions ================================================================
    public void ViewRequests(SystemUser s)
    {
        if (s instanceof HDB_Manager)
        {
            for (Map.Entry<HDB_Officer, ArrayList<String>> entry : officerRequestList.entrySet()) {
                System.out.println(entry.getKey().name + " | " + entry.getValue().toString());
            }
        }
        else {
            System.out.println("Not allowed to do this function");
        }
    }

    public void ApproveRequest(SystemUser s)
    {
        if (s instanceof HDB_Manager)
        {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter name of officer to approve: ");
            String name = sc.nextLine();
            Map.Entry<HDB_Officer, ArrayList<String>> entry = null;
            String projectName = "";

            for (Map.Entry<HDB_Officer, ArrayList<String>> e : officerRequestList.entrySet())
            {
                if (Objects.equals(e.getKey().name, name))
                {
                    entry = e;
                }
            }

            if (entry == null)
            {
                System.out.println("No officer by that name in list of requests");
                return;
            }

            // this officer had applied to multiple projects
            if (entry.getValue().size() > 1)
            {
                while (true) {
                    System.out.print("Enter name of the project to approve: ");
                    projectName = sc.nextLine();

                    if (entry.getValue().contains(projectName))
                    {
                        break;
                    }
                    else {
                        System.out.println("Invalid project name. Try again.");
                    }
                }
            }
            else
            {
                projectName = entry.getValue().getFirst();
            }

            Project p = ProjectListingDB.getInstance().SearchDB(projectName);
            p.AssignOfficer(entry.getKey());
            // TODO: also update the HDB_Officer class's Arraylist of projects
        }
        else {
            System.out.println("Not allowed to do this function");
        }
    }

    public void DenyRequest(SystemUser s)
    {
        if (s instanceof HDB_Manager)
        {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter name of officer to deny: ");
            String name = sc.nextLine();
            Map.Entry<HDB_Officer, ArrayList<String>> entry = null;
            String projectName = "";

            for (Map.Entry<HDB_Officer, ArrayList<String>> e : officerRequestList.entrySet())
            {
                if (Objects.equals(e.getKey().name, name))
                {
                    entry = e;
                }
            }

            if (entry == null)
            {
                System.out.println("No officer by that name in list of requests");
                return;
            }

            // this officer had applied to multiple projects
            if (entry.getValue().size() > 1)
            {
                while (true) {
                    System.out.print("Enter name of the project to deny: ");
                    projectName = sc.nextLine();

                    if (entry.getValue().contains(projectName))
                    {
                        break;
                    }
                    else {
                        System.out.println("Invalid project name. Try again.");
                    }
                }
            }
            else
            {
                projectName = entry.getValue().getFirst();
            }
            entry.getValue().remove(projectName);

            // if the officer got rejected and no more applications, remove from hashmap
            if (entry.getValue().isEmpty())
            {
                officerRequestList.remove(entry.getKey());
            }
        }
        else {
            System.out.println("Not allowed to do this function");
        }
    }

    // End manager functions ============================================================

    // Officer functions ================================================================

    // return true if there is clash
    private boolean CheckClashDate(LocalDate s1, LocalDate e1, LocalDate s2, LocalDate e2)
    {
        return !(e1.isBefore(s2) || e2.isBefore(s1));
    }

    public void ApplyJoinProject(SystemUser s)
    {
        // kick out of the function if the user isn't an officer
        if (!(s instanceof HDB_Officer))
        {
            System.out.println("You do not have access to this function.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        Map.Entry<HDB_Officer, ArrayList<String>> entry = null;
        String projectName = "";

        System.out.print("Enter name of project to apply for: ");
        projectName = sc.nextLine();

        // verify that this project name is valid
        if (ProjectListingDB.getInstance().SearchDB(projectName) == null)
        {
            System.out.println("No project with this name. Terminating application");
            return;
        }

        // check if they already applied to this project
        // check if their name is in local list (pending)

        for (Map.Entry<HDB_Officer, ArrayList<String>> e : officerRequestList.entrySet())
        {
            if (Objects.equals(e.getKey().name, s.name))
            {
                entry = e;
                if (e.getValue().contains(projectName))
                {
                    System.out.println("Already applied to this project. Current status: Pending");
                    return;
                }

            }
        }

        // check if that project alr has them listed as an officer (means approved alr)
        Project projectToApply = ProjectListingDB.getInstance().SearchDB(projectName);

        if (projectToApply.Details.OfficerList.contains(s))
        {
            System.out.println("Application already approved for this project. Current status: Approved");
            return;
        }

        // now need to actually add to the list
        // need to check that theres no date clash

        // this is the officer's first application
        // TODO: Date checking
        if (entry == null)
        {
            // for projects in s's project list, start1.isBefore(end2) || start1.isEqual(end2)) && (end1.isAfter(start1))
            // check for clash with approved projects
        }
        else {
            // check for clash with approved projects

            // check for clash with other pending projects
            for (String n : entry.getValue()) {
                Project curr = ProjectListingDB.getInstance().SearchDB(n);
                if (CheckClashDate( projectToApply.Details.OpenDate,
                                    projectToApply.Details.CloseDate,
                                    curr.Details.OpenDate,
                                    curr.Details.CloseDate))
                {
                    System.out.print("Dates clash with a project called ");
                    System.out.println(curr.Details.ProjectName);
                    System.out.println("Aborting application");
                    return;
                }
            }
        }
    }

    public void CheckProjectApplicationStatus()
    {
        // if their name is still in this class, pending
        for (Map.Entry<HDB_Officer, ArrayList<String>> e : officerRequestList.entrySet())
        {
            if (Objects.equals(e.getKey().name, s.name))
            {
//                entry = e;
                if (e.getValue().contains(projectName))
                {
                    System.out.println("Already applied to this project. Current status: Pending");
                    return;
                }

            }
        }

        // if they are an officer means approved
        // if neither means rejected / not applied
    }

    // End officer functions ============================================================
}
