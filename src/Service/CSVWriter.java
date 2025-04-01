package Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Entity.*;
import Users.System_User;

//CSVWriter controls how the data from the csv files are written

public class CSVWriter 
{
    //creates a new enquiry (NOT enquiry message) to Enquiries.CSV
    public void writeNewEnquiry(Enquiry enq)
    {
        String filePath = "src/localdata/Enquiries.csv";
        File file = new File(filePath);
        boolean fileExists = file.exists();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) 
        {
            if (!fileExists) 
            {
                bw.write("Title,RegardingProject,Enquirer,Thread");
            }
            bw.newLine();
            String enquirer_info = String.join("-",enq.Enquirer.name,enq.Enquirer.getUserID(),String.valueOf(enq.Enquirer.age),enq.Enquirer.marital_status,enq.Enquirer.TypeofUser);
            String thread_info = "";
            for (EnquiryMessage em : enq.thread) {
                thread_info += String.join("-",em.message,em.sender.name,em.sender.getUserID(),String.valueOf(em.sender.age),em.sender.marital_status,em.sender.TypeofUser) + "|";
            }
            thread_info = thread_info.substring(0,thread_info.length()-1);
            bw.write(String.join(",",enq.Title,enq.RegardingProject,enquirer_info,thread_info));
        } 
        catch (IOException | NumberFormatException e) {}
    }

    //re-edits existing Enquiry to Enquiries.CSV
    public void rewriteEnquiry(Enquiry enq) {
        String filePath = "src/localdata/Enquiries.csv";
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            br.readLine();
        
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",");
                String[] enquirer_info = data[2].split("-");
                if (!data[0].equals(enq.Title) || 
                !enquirer_info[1].equals(enq.Enquirer.getUserID()) ||
                !data[1].equals(enq.RegardingProject))
                {
                    lines.add(line);
                }
            }
        } 
        catch (IOException | NumberFormatException e) {}

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) 
        {
            bw.write("Title,RegardingProject,Enquirer,Thread");
            bw.newLine();
            for (String line : lines) 
            {
                bw.write(line);
                bw.newLine();
            }
            String enquirer_info = String.join("-",enq.Enquirer.name,enq.Enquirer.getUserID(),String.valueOf(enq.Enquirer.age),enq.Enquirer.marital_status,enq.Enquirer.TypeofUser);
            String thread_info = "";
            for (EnquiryMessage em : enq.thread) {
                thread_info += String.join("-",em.message,em.sender.name,em.sender.getUserID(),String.valueOf(em.sender.age),em.sender.marital_status,em.sender.TypeofUser) + "|";
            }
            thread_info = thread_info.substring(0,thread_info.length()-1);
            bw.write(String.join(",",enq.Title,enq.RegardingProject,enquirer_info,thread_info));

        } 
        catch (IOException | NumberFormatException e) {}
    }

    //deletes existing Enquiry to Enquiries.CSV
    public void deleteEnquiry(Enquiry enq) {
        String filePath = "src/localdata/Enquiries.csv";
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            br.readLine();
        
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",");
                String[] enquirer_info = data[2].split("-");
                if (!data[0].equals(enq.Title) || 
                !enquirer_info[1].equals(enq.Enquirer.getUserID()) ||
                !data[1].equals(enq.RegardingProject))
                {
                    lines.add(line);
                }
            }
        } 
        catch (IOException | NumberFormatException e) {}

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) 
        {
            bw.write("Title,RegardingProject,Enquirer,Thread");
            for (String line : lines) 
            {
                bw.newLine();
                bw.write(line);
            }
        } 
        catch (IOException | NumberFormatException e) {}
    }

    //add new project
    public void addnewProject(Project p)
    {
        String filePath = "src/localdata/ProjectList.csv";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) 
        {
            bw.newLine();
            String officers = "";
            for (System_User off : p.OfficersInCharge) {
                officers += String.join("-",off.name,off.getUserID(),String.valueOf(off.age),off.marital_status) + "|";
            }
            String project_info = String.join(",",
            p.ProjectName,p.Neighborhood,
            String.valueOf(p.Type1), String.valueOf(p.NoOfUnitsForType1), String.valueOf(p.SellingPriceForType1),
            String.valueOf(p.Type2), String.valueOf(p.NoOfUnitsForType2), String.valueOf(p.SellingPriceForType2),
            String.valueOf(p.ApplicantOpeningDate),String.valueOf(p.ApplicantClosingDate),
            String.join("-",p.ManagerInCharge.name + p.ManagerInCharge.getUserID() + p.ManagerInCharge.age + p.ManagerInCharge.marital_status),
            String.valueOf(p.OfficerSlots),officers.substring(0,-1)
            ,String.valueOf(p.isVisible()),p.getUserGroupOpento());

            bw.write(project_info);
        } 
        catch (IOException | NumberFormatException e) {}
    }

    //delete existing project
    public void deleteProject(Project p) {
        String filePath = "src/localdata/ProjectList.csv";
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            br.readLine();
        
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",");
                if (!data[0].equals(p.ProjectName)) 
                {
                    lines.add(line);
                }
            }
        } 
        catch (IOException | NumberFormatException e) {}

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) 
        {
            bw.write("Project Name,Neighborhood,Type 1,Number of units for Type 1,Selling price for Type 1,Type 2,Number of units for Type 2,Selling price for Type 2,Application opening date,Application closing date,Manager,Officer Slot,Officers,Visibility,OpentoUserGroup");
            for (String line : lines) 
            {
                bw.newLine();
                bw.write(line);
            }
        } 
        catch (IOException | NumberFormatException e) {}
    }

    //edit existing project
    public void rewriteProject(Project p) {
        String filePath = "src/localdata/ProjectList.csv";
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            br.readLine();
        
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",");
                if (!data[0].equals(p.ProjectName)) 
                {
                    lines.add(line);
                }
            }
        } 
        catch (IOException | NumberFormatException e) {}

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) 
        {
            bw.write("Project Name,Neighborhood,Type 1,Number of units for Type 1,Selling price for Type 1,Type 2,Number of units for Type 2,Selling price for Type 2,Application opening date,Application closing date,Manager,Officer Slot,Officers,Visibility,OpentoUserGroup");
            for (String line : lines) 
            {
                bw.newLine();
                bw.write(line);
            }
            bw.newLine();
            String officers = "";
            for (System_User off : p.OfficersInCharge) {
                officers += String.join("-",off.name,off.getUserID(),String.valueOf(off.age),off.marital_status) + "|";
            }
            String project_info = String.join(",",
            p.ProjectName,p.Neighborhood,
            String.valueOf(p.Type1), String.valueOf(p.NoOfUnitsForType1), String.valueOf(p.SellingPriceForType1),
            String.valueOf(p.Type2), String.valueOf(p.NoOfUnitsForType2), String.valueOf(p.SellingPriceForType2),
            String.valueOf(p.ApplicantOpeningDate),String.valueOf(p.ApplicantClosingDate),
            String.join("-",p.ManagerInCharge.name + p.ManagerInCharge.getUserID() + p.ManagerInCharge.age + p.ManagerInCharge.marital_status),
            String.valueOf(p.OfficerSlots),officers.substring(0,-1)
            ,String.valueOf(p.isVisible()),p.getUserGroupOpento());

            bw.write(project_info);
        } 
        catch (IOException | NumberFormatException e) {}
    }
}