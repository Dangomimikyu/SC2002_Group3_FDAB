package Service;
import java.util.*;
import Entity.*;
import Users.System_User;

//LocalData is responsible for the initialisation of data from csv files on start-up
//Mainly functions as a boundary between how Controller Classes interact with the CSVReader and CSVWriter classes

public class LocalData 
{
    //holds the initial data read on start_up
    private final ArrayList <System_User> database_all_users;
    private final ArrayList <Project> initialisation_projects;
    private final ArrayList <Enquiry> initialisation_enquiries;
    private final CSVReader reader;
    private final CSVWriter writer;

    //constructor
    public LocalData(String UserFilePath, String ProjectsFilePath, String EnquiriesFilePath)
    {
        reader= new CSVReader();
        writer= new CSVWriter();

        //reads data from csvs to initialise system first
        database_all_users = reader.readUsers(UserFilePath);
        initialisation_projects = reader.readProjects(ProjectsFilePath);
        initialisation_enquiries = reader.readEnquiries(EnquiriesFilePath);
    }

    //operations to WRITE data

    public void addEnquiry(Enquiry e) {
        writer.writeNewEnquiry(e);
    }

    public void editEnquiry(Enquiry e) {
        writer.rewriteEnquiry(e);
    }

    public void deleteEnquiry(Enquiry e) {
        writer.deleteEnquiry(e);
    }


    //operations to RETRIEVE initial data

    public ArrayList <System_User> get_all_users()
    {
        return database_all_users;
    }

    public ArrayList <Project> get_initial_projects()
    {
        return initialisation_projects;
    }

    public ArrayList <Enquiry> get_initial_enquiries()
    {
        return initialisation_enquiries;
    }

}