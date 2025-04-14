//ProjectListingDB is the database class that will be used to store all the enquiry information within the system
// it will be used to retrieve, modify, add and delete projects to/from the csv files

package Database;

import java.util.ArrayList;

import InteractableAttributePackage.Project;
import Service.*;
import User.SystemUser;

public class ProjectListingDB extends Database {

    private static final ProjectListingDB instance = new ProjectListingDB();

    private ArrayList<Project> projList = new ArrayList<Project>();
    Reader reader = new Reader();
    Writer writer = new Writer();

    private ProjectListingDB() {}
    public static ProjectListingDB getInstance() {
        return instance;
    }

    public ProjectListingDB(String ProjectFilePath, ArrayList<SystemUser> userList) {
        projList = reader.readProjects(ProjectFilePath, userList);
    }

    public ArrayList<Project> getProjectDB() {
        return projList;
    }

    public void ViewDB() {
        System.out.println("\nAll projects in order by index: ");
        int index = 0;
        for (Project p : projList) {
            System.out.println("================ " + index + " ================");
            System.out.println(p.getProjectDetails());
            index++;
        }
    }

    //modify project by index
    public void ModifyDB(int index, DB_Action action) {
        switch (action) {
            case ADD -> writer.WriteNewProject(projList.get(index));
            case DELETE -> writer.DeleteProject(projList.get(index));
            case EDIT -> writer.RewriteProject(projList.get(index));
        }
    }
}