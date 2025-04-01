package Controller;

import java.util.ArrayList;
import Entity.Project;
import Service.LocalData;
import Users.*;

//ProjectManager is the controller class responsible for acting as the boundary between how HDBMain interacts with functions regarding
//the creation, modification, reading and deletion of projects. (WIP)

public class ProjectManager {

    private final LocalData localdata;
    private ArrayList<Project> list_of_all_projects; 

    public ProjectManager(LocalData ld) {
        this.localdata = ld;
        this.list_of_all_projects = localdata.get_initial_projects();
    }

    public ArrayList<Project> get_current_projects() {
        return list_of_all_projects;
    }

    //obtaining filtered list of projects for officers
    public ArrayList<Project> get_all_handled_projects(HDBOfficer user) {
        ArrayList<Project> list_of_handled_projects = new ArrayList<>();
        for (Project proj : list_of_all_projects) {
            for (System_User officer : proj.OfficersInCharge) {
                if (officer.getUserID().equals(user.getUserID())) {
                    list_of_handled_projects.add(proj);
                    break;
                }
            }
        }
        return list_of_handled_projects;
    }

    //obtaining filtered list of projects for managers
    public ArrayList<Project> get_all_handled_projects(HDBManager user) {
        ArrayList<Project> list_of_handled_projects = new ArrayList<>();
        for (Project proj : list_of_all_projects) {
            if (proj.ManagerInCharge.getUserID().equals(user.getUserID())) {
                list_of_handled_projects.add(proj);
            }
        }
        return list_of_handled_projects;
    }

    //checks if the project name has already been taken by another project OR checks if project exists or not
    public boolean Exists(String project_name) {
        for (Project proj : list_of_all_projects) {
            if (proj.ProjectName.equals(project_name)) {
                return true;
            }
        }
        return false;
    }

}
