package Service;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import Entity.*;
import Users.*;

//CSVReader controls how the data from the csv files are read

public class CSVReader 
{
    //read applicant,manager and officer.csv
    public ArrayList<System_User> readUsers(String UserFilePath)
    {
        ArrayList<System_User> database_of_users = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(UserFilePath))) 
        {
            String line;
            br.readLine();
            
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",");
                String name = data[0];
                String NRIC = data[1];
                int age = Integer.parseInt(data[2]);
                String marital_status = data[3];
                String password = data[4];
                String type_user = data[5];
                
                System_User user = new System_User(name, NRIC, age, marital_status, password, type_user);
                database_of_users.add(user);
            }
        } 
        catch (IOException | NumberFormatException e) {}
        return database_of_users;
    }

    //reads projectList.csv
    public ArrayList<Project> readProjects(String ProjectsFilePath) {
        
        ArrayList<Project> list_of_all_projects = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ProjectsFilePath)))
        {
            String line;
            br.readLine();
            
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",");
                Project proj = new Project(data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],
                data[8],data[9],data[10],data[11],data[12],data[13],data[14]);
                list_of_all_projects.add(proj);
            }
        }
        catch (IOException | NumberFormatException e) {}

        return list_of_all_projects;
    }

    //reads Enquiries.csv
    public ArrayList<Enquiry> readEnquiries(String EnquiriesFilePath) {
        
        ArrayList<Enquiry> list_of_all_enquiries = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(EnquiriesFilePath)))
        {
            String line;
            Enquiry e;
            br.readLine();
            
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",",-1);
                String title = data[0];
                String project_name = data[1];
                String message = data[2];
                String[] enquirer_info  = data[3].split("-");
                String reply = data[4];
                String[] sender_info = data[5].split("-");

                System_User creator = new System_User(enquirer_info[0],enquirer_info[1],Integer.parseInt(enquirer_info[2]),enquirer_info[3],"",enquirer_info[4]);
                if (reply.equals("")) {
                    e = new Enquiry(title,project_name,message,creator);
                }
                else {
                    System_User sender = new System_User(sender_info[0],sender_info[1],Integer.parseInt(sender_info[2]),sender_info[3],"",sender_info[4]);
                    e = new Enquiry(title,project_name,message,creator,reply,sender);
                }
                
                list_of_all_enquiries.add(e);
            }
        }
        catch (IOException | NumberFormatException e) {}

        return list_of_all_enquiries;
    }
}