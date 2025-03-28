import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import Entity.*;
import Users.*;

public class Main {
    public static void main(String[] args) {

        try (BufferedReader br = new BufferedReader(new FileReader("src/localdata/ProjectList.csv")))
        {
            LinkedList <Project> all_projects =new LinkedList<>();
            String line;
            br.readLine();
            
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",");
                String PN = data[0].trim();
                String n = data[1].trim();
                String T1 = data[2].trim();
                int T1no = Integer.parseInt(data[3].trim());
                int SPT1 = Integer.parseInt(data[4].trim());
                String T2 = data[5].trim();
                int T2no = Integer.parseInt(data[6].trim());
                int SPT2 = Integer.parseInt(data[7].trim());
                String AOD = data[8].trim();
                String ACD = data[9].trim();
                String MIC = data[10].trim();
                int OS = Integer.parseInt(data[11].trim());
                String OIC = data[12].trim();
                
                Project proj = new Project(PN,n,T1,T1no,SPT1,T2,T2no,SPT2,AOD,ACD,MIC,OS,OIC);
                all_projects.add(proj);
            }
        }
        catch (IOException | NumberFormatException e) 
        {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader("src/localdata/ApplicantList.csv")))
        {
            LinkedList <Applicant> all_applicants =new LinkedList<>();
            String line;
            br.readLine();
            
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",");
                String name = data[0].trim();
                String NRIC = data[1].trim();
                int age = Integer.parseInt(data[2].trim());
                String marital_status = data[3].trim();
                String password = data[4].trim();
                
                Applicant applicant = new Applicant(name,NRIC,age,marital_status,password);
                all_applicants.add(applicant);
            }
        }
        catch (IOException | NumberFormatException e) 
        {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader("src/localdata/ManagerList.csv")))
        {
            LinkedList <HDBManager> all_managers =new LinkedList<>();
            String line;
            br.readLine();
            
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",");
                String name = data[0].trim();
                String NRIC = data[1].trim();
                int age = Integer.parseInt(data[2].trim());
                String marital_status = data[3].trim();
                String password = data[4].trim();
                
                HDBManager manager = new HDBManager(name,NRIC,age,marital_status,password);
                all_managers.add(manager);
            }
        }
        catch (IOException | NumberFormatException e) 
        {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader("src/localdata/OfficerList.csv")))
        {
            LinkedList <HDBOfficer> all_officers =new LinkedList<>();
            String line;
            br.readLine();
            
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",");
                String name = data[0].trim();
                String NRIC = data[1].trim();
                int age = Integer.parseInt(data[2].trim());
                String marital_status = data[3].trim();
                String password = data[4].trim();
                
                HDBOfficer officer = new HDBOfficer(name,NRIC,age,marital_status,password);
                all_officers.add(officer);
            }
        }
        catch (IOException | NumberFormatException e) 
        {
            e.printStackTrace();
        }


    }
}

// BTO Management System is a system for applicants and HDB staffs to view, apply and manage for BTO projects. 
// The system will act as a centralized hub for all applicants and HDB staffs
// A user list can be initiated through a file uploaded into the system at initialization.
