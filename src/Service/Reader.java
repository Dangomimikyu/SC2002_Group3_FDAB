package Service;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import Entity.*;
import User.*;

//CSVReader is responsible for reading the data from the csv files and converting them into objects that can be used by the system

public class Reader 
{
    //reads applicantList.csv, officerList.csv and managerList.csv
    public ArrayList<SystemUser> readUsers(String ApplicantFilePath, String OfficerFilePath, String ManagerFilePath)
    {
        ArrayList<SystemUser> userList = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(ApplicantFilePath))) 
        {
            String line;
            br.readLine();
            
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",",-1);
                String name = data[0];
                String NRIC = data[1];
                int age = Integer.parseInt(data[2]);
                String marital_status = data[3];
                String password = data[4];
                String applied_project = data[5];
                String applied_project_status = data[6];
                
                userList.add(new Applicant(name, NRIC, age, marital_status, password, applied_project, applied_project_status));

            }
        } 
        catch (IOException | NumberFormatException e) {}

        try (BufferedReader br = new BufferedReader(new FileReader(OfficerFilePath))) 
        {
            String line;
            br.readLine();
            
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",",-1);
                String name = data[0];
                String NRIC = data[1];
                int age = Integer.parseInt(data[2]);
                String marital_status = data[3];
                String password = data[4];
                String applied_project = data[5];
                String applied_project_status = data[6];
                
                userList.add(new HDB_Officer(name, NRIC, age, marital_status, password, applied_project, applied_project_status));
            }
        } 
        catch (IOException | NumberFormatException e) {}

        try (BufferedReader br = new BufferedReader(new FileReader(ManagerFilePath))) 
        {
            String line;
            br.readLine();
            
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",",-1);
                String name = data[0];
                String NRIC = data[1];
                String password = data[2];
                
                userList.add(new HDB_Manager(NRIC, password, name));
            }
        } 
        catch (IOException | NumberFormatException e) {}
        
        return userList;
    }

    //reads projectList.csv. Needs userList to identify the manager in charge and officers in charge by their userID
    public ArrayList<Project> readProjects(String ProjectsFilePath, ArrayList<SystemUser> userList) {
        
        ArrayList<Project> projList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ProjectsFilePath)))
        {
            String line;
            br.readLine();
            
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",");
                String project_name = data[0];
                String neighborhood = data[1];
                int selling_price_2room = Integer.parseInt(data[2]);
                int selling_price_3room = Integer.parseInt(data[3]);
                int no_of_units_2room = Integer.parseInt(data[4]);
                int no_of_units_3room = Integer.parseInt(data[5]);
                String opening_date = data[6];
                String closing_date = data[7];
                String manager_id = data[8];
                int max_officer_slots = Integer.parseInt(data[9]);
                String[] officers_in_charge = data[10].split("-",-1);
                boolean activeStatus = Boolean.parseBoolean(data[11]);
                String open_to_user_group = data[12];

                ArrayList<HDB_Officer> officers = new ArrayList<>();
                if (!officers_in_charge[0].equals("")) {
                    for (String officerID : officers_in_charge) {
                        HDB_Officer officer = (HDB_Officer)userList.stream()
                            .filter(user -> user.userID.equals(officerID))
                            .findFirst()
                            .orElse(null);
                        officers.add(officer);
                    }
                }
                HDB_Manager manager = (HDB_Manager)userList.stream()
                    .filter(user -> user.userID.equals(manager_id))
                    .findFirst()
                    .orElse(null);

                projList.add(new Project(project_name, neighborhood, selling_price_2room, selling_price_3room, no_of_units_2room, 
                no_of_units_3room, opening_date, closing_date, manager, max_officer_slots, officers, 
                activeStatus, open_to_user_group));

            }
        }
        catch (IOException | NumberFormatException e) {}

        return projList;
    }

    //reads Enquiries.csv. Needs userList to identify the enquirer and replier by their userID
    public ArrayList<Enquiry> readEnquiries(String EnquiriesFilePath, ArrayList<SystemUser> userList) {
        
        ArrayList<Enquiry> enqList = new ArrayList<>();

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
                String description = data[2];
                String enquirerID  = data[3];
                String reply = data[4];
                String replierID = data[5];

                SystemUser enquirer = userList.stream()
                    .filter(user -> user.userID.equals(enquirerID))
                    .findFirst()
                    .orElse(null);

                //initialise enquiry object
                e = new Enquiry(title, project_name, description, (Applicant)enquirer);

                //if enquiry is replied, set the replier and reply
                if (!replierID.equals("")) {
                    SystemUser replier = userList.stream()
                        .filter(user -> user.userID.equals(replierID))
                        .findFirst()
                        .orElse(null);
                    e.Replier = replier;
                    e.Reply = reply;
                }
                enqList.add(e);
            }
        }
        catch (IOException | NumberFormatException e) {}

        return enqList;
    }

    //reads Requests.csv. Needs userList to identify the initiator and handler by their userID
    public ArrayList<Request> readRequests(String RequestsFilePath, ArrayList<SystemUser> userList) {
        
        ArrayList<Request> reqList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(RequestsFilePath)))
        {
            String line;
            br.readLine();
            
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",",-1);
                String request_type = data[0];
                String project_name = data[1];
                String initiatorID  = data[2];
                String handlerID = data[3];
                String status = data[4];
                String BookedFlatType = data[5];

                SystemUser initiator = userList.stream()
                    .filter(user -> user.userID.equals(initiatorID))
                    .findFirst()
                    .orElse(null);

                //initialise request object
                Request r = null;
                if (request_type.equals("Applicant_Application")) {
                    r = new Applicant_Application((Applicant)initiator, project_name);

                } else if (request_type.equals("Officer_Application")) {
                    r = new Officer_Application((HDB_Officer)initiator, project_name);

                } else if (request_type.equals("Booking")) {
                    r = new Booking((Applicant)initiator, project_name, Request.FlatType.valueOf(BookedFlatType));

                } else if (request_type.equals("Withdrawal")) {
                    r = new Withdrawal((Applicant)initiator, project_name);
                    if (!BookedFlatType.equals("")) {
                        ((Withdrawal)r).BookedFlatType = Request.FlatType.valueOf(BookedFlatType);
                    }
                }

                //if request has been handled, set the handler and status
                if (!handlerID.equals("")) {
                    SystemUser handler = userList.stream()
                        .filter(user -> user.userID.equals(handlerID))
                        .findFirst()
                        .orElse(null);
                    r.handler = handler;
                    r.status = Request.ApplicationStatus.valueOf(status);
                }

                reqList.add(r);
            }
        }
        catch (IOException | NumberFormatException e) {}

        return reqList;
    }
    
}