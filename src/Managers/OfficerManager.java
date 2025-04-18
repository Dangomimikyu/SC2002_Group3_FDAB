package Managers;

import Database.ProjectListingDB;
import InteractableAttributePackage.Project;
import User.HDB_Manager;
import User.HDB_Officer;
import User.SystemUser;

import java.time.LocalDate;
import java.util.*;

public class OfficerManager {
    private static OfficerManager instance;
    private HashMap<HDB_Officer, String> officerRequestList;

    private OfficerManager() {
        officerRequestList = new HashMap<>();
    }

    public static OfficerManager getInstance() {
        if (instance == null) {
            instance = new OfficerManager();
        }
        return instance;
    }

    // Manager functions ================================================================
    public void ViewRequests(SystemUser s) {
        if (s instanceof HDB_Manager) {
            for (Map.Entry<HDB_Officer, String> entry : officerRequestList.entrySet()) {
                System.out.println(entry.getKey().name + " | " + entry.getValue());
            }
        } else {
            System.out.println("Not allowed to do this function");
        }
    }

    public void ApproveRequest(SystemUser s) {
        if (s instanceof HDB_Manager) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter name of officer to approve: ");
            String name = sc.nextLine();
            Map.Entry<HDB_Officer, String> entry = null;
            String projectName = "";

            for (Map.Entry<HDB_Officer, String> e : officerRequestList.entrySet()) {
                if (Objects.equals(e.getKey().name, name)) {
                    entry = e;
                }
            }

            if (entry == null) {
                System.out.println("No officer by that name in list of requests");
                return;
            }

            projectName = entry.getValue();

            Project p = ProjectListingDB.getInstance().SearchDB(projectName);
            p.AssignOfficer(entry.getKey());
            entry.getKey().projectAssigned = p;

        } else {
            System.out.println("Not allowed to do this function");
        }
    }

    public void DenyRequest(SystemUser s) {
        if (s instanceof HDB_Manager) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter name of officer to deny: ");
            String name = sc.nextLine();
            Map.Entry<HDB_Officer, String> entry = null;
            String projectName = "";

            for (Map.Entry<HDB_Officer, String> e : officerRequestList.entrySet()) {
                if (Objects.equals(e.getKey().name, name)) {
                    entry = e;
                }
            }

            if (entry == null) {
                System.out.println("No officer by that name in list of requests");
                return;
            }

            officerRequestList.remove(entry.getKey());
        } else {
            System.out.println("Not allowed to do this function");
        }
    }

    // End manager functions ============================================================

    // Officer functions ================================================================

    // return true if there is clash
    private boolean CheckClashDate(LocalDate s1, LocalDate e1, LocalDate s2, LocalDate e2) {
        return !(e1.isBefore(s2) || e2.isBefore(s1));
    }

    public void ApplyJoinProject(SystemUser s) {
        // kick out of the function if the user isn't an officer
        if (!(s instanceof HDB_Officer)) {
            System.out.println("You do not have access to this function.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        Map.Entry<HDB_Officer, String> entry = null;
        String projectName = "";
        Project projectToApply = null;

        System.out.print("Enter name of project to apply for: ");
        projectName = sc.nextLine();

        // verify that this project name is valid
        if (ProjectListingDB.getInstance().SearchDB(projectName) == null) {
            System.out.println("No project with this name. Terminating application");
            return;
        }

        // check if they already applied to this project
        // check if their name is in local list (pending)

        for (Map.Entry<HDB_Officer, String> e : officerRequestList.entrySet()) {
            if (Objects.equals(e.getKey().name, s.name)) {
                entry = e;
                if (Objects.equals(e.getValue(), projectName)) {
                    System.out.println("Already applied to this project. Current status: Pending");
                    return;
                }

            }
        }

        // check if that project alr has them listed as an officer (means approved alr)
        projectToApply = ProjectListingDB.getInstance().SearchDB(projectName);

        if (projectToApply.Details.OfficerList.contains(s)) {
            System.out.println("Application already approved for this project. Current status: Approved");
            return;
        }

        // now need to actually add to the list
        // need to check that theres no date clash

        // Date checking
        Project temp = ProjectListingDB.getInstance().SearchDB(((HDB_Officer) s).AppliedProject);
        if (CheckClashDate(projectToApply.Details.OpenDate,
                projectToApply.Details.CloseDate,
                temp.Details.OpenDate,
                temp.Details.CloseDate)) {
            System.out.print("Dates clash with a project called ");
            System.out.println(temp.Details.ProjectName);
            System.out.println("Aborting application");
            return;
        }

        // if reach here means all okay and can put the request in
        if (entry != null) {
            entry.setValue(projectName);
        }
        else {
            officerRequestList.put(((HDB_Officer) s), projectName);
        }
    }

    public void CheckProjectApplicationStatus(SystemUser s) {
        // if their name is still in this class, pending
        for (Map.Entry<HDB_Officer, String> e : officerRequestList.entrySet()) {
            {
                if (Objects.equals(e.getKey().name, s.name)) {
                    System.out.print("Applied to project ");
                    System.out.print(e.getValue());
                    System.out.println(" | Status: Pending");
                    return;
                }
            }
            // TODO: if not here means it's either been approved or rejected, either way theres no status
            // if they are an officer means approved
            // if neither means rejected / not applied
        }

        // End officer functions ============================================================
    }

}
