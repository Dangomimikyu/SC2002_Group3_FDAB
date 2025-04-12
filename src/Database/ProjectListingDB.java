//ProjectListingDB is the database class that will be used to store all the enquiry information within the system
// it will be used to retrieve, modify, add and delete projects to/from the csv files

package Database;

import java.util.ArrayList;
import Service.*;
import User.SystemUser;
import Entity.Project;

public class ProjectListingDB implements Database<Project> {

    private static final ProjectListingDB instance = new ProjectListingDB();

    private ArrayList<Project> projList = new ArrayList<Project>();
    Reader reader = new Reader();
    Writer writer = new Writer();

    // Singleton pattern to ensure only one instance of ProjectListingDB exists
    private ProjectListingDB() {
        // Private constructor to prevent instantiation
    }
    public static ProjectListingDB getInstance() {
        return instance;
    }

    public ProjectListingDB(String ProjectFilePath, ArrayList<SystemUser> userList) {
        projList = reader.readProjects(ProjectFilePath, userList);
    }

    public ArrayList<Project> ViewDB() {
        return projList;
    }

    public void ModifyDB(Project p, DB_Action action) {
        switch (action) {
            case ADD -> writer.WriteNewProject(p);
            case DELETE -> writer.DeleteProject(p);
            case EDIT -> writer.RewriteProject(p);
        }
    }
}