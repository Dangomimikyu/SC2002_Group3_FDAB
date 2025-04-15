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
import User.*;

//CSVWriter controls how the data from the csv files are written

public class Writer 
{
    //////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// Writing Users //////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////
    
    public void WriteNewUser(SystemUser user) 
    {
        String filePath = "";

        if (user instanceof HDB_Officer) 
        {
            filePath = "src/localdata/OfficerList.csv";
        } 
        else if (user instanceof HDB_Manager) 
        {
            filePath = "src/localdata/ManagerList.csv";
        } 
        else if (user instanceof Applicant)
        {
            filePath = "src/localdata/ApplicantList.csv";
        } 

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) 
        {
            bw.newLine();
            String user_info = "";
            if (user instanceof Applicant) 
            {
                Applicant app = (Applicant)user;
                user_info = String.join(",",app.name,app.userID,String.valueOf(app.age)
                ,String.valueOf(app.maritalStatus),app.password,app.AppliedProject,String.valueOf(app.AppliedProjectStatus));
            } 
            else if (user instanceof HDB_Manager) 
            {
                HDB_Manager manager = (HDB_Manager)user;
                user_info = String.join(",",manager.name,manager.userID,manager.password);
            }
            bw.write(user_info);
        } 
        catch (IOException | NumberFormatException e) {}
    }

    public void RewriteUser(SystemUser user) 
    {
        String filePath = "";

        if (user instanceof HDB_Officer) 
        {
            filePath = "src/localdata/OfficerList.csv";
        } 
        else if (user instanceof HDB_Manager) 
        {
            filePath = "src/localdata/ManagerList.csv";
        } 
        else if (user instanceof Applicant)
        {
            filePath = "src/localdata/ApplicantList.csv";
        } 

        List<String> lines = new ArrayList<>();     
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            br.readLine();
        
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",");
                if (!data[1].equals(user.userID)) 
                {
                    lines.add(line);
                }
            }
        } 
        catch (IOException | NumberFormatException e) {}

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) 
        {
            String user_info ="";
            if (user instanceof Applicant) 
            {
                bw.write("Name,NRIC,Age,MaritalStatus,Password,AppliedProject,AppliedProjectStatus");
                Applicant app = (Applicant)user;
                user_info = String.join(",",app.name,app.userID,String.valueOf(app.age)
                ,String.valueOf(app.maritalStatus),app.password,app.AppliedProject,String.valueOf(app.AppliedProjectStatus));
            } 
            else if (user instanceof HDB_Manager) 
            {
                bw.write("Name,NRIC,Password");
                HDB_Manager manager = (HDB_Manager)user;
                user_info = String.join(",",manager.name,manager.userID,manager.password);
            }

            bw.newLine();
            for (String line : lines) 
            {
                bw.write(line);
                bw.newLine();
            }
            bw.write(user_info);
        } 
        catch (IOException | NumberFormatException e) {}
    }


    ////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// Writing Enquiries ////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////

    public void WriteNewEnquiry(Enquiry enq)
    {
        String filePath = "src/localdata/Enquiries.csv";
        File file = new File(filePath);
        boolean fileExists = file.exists();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) 
        {
            if (!fileExists) 
            {
                bw.write("Title,RegardingProject,Description,EnquirerID,Reply,ReplySenderID");
            }
            bw.newLine();
            bw.write(String.join(",",enq.Title,enq.RegardingProject,enq.Description,enq.Enquirer.userID,"",""));
        } 
        catch (IOException | NumberFormatException e) {}
    }

    public void RewriteEnquiry(Enquiry enq) {
        String filePath = "src/localdata/Enquiries.csv";
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            br.readLine();
        
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",");
                if (!data[0].equals(enq.Title) || 
                !data[1].equals(enq.RegardingProject) ||
                !data[3].equals(enq.Enquirer.userID))
                {
                    lines.add(line);
                }
            }
        } 
        catch (IOException | NumberFormatException e) {}

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) 
        {
            bw.write("Title,RegardingProject,Description,EnquirerID,Reply,ReplySenderID");
            bw.newLine();
            for (String line : lines) 
            {
                bw.write(line);
                bw.newLine();
            }
            if (enq.isUnreplied()) {
                bw.write(String.join(",",enq.Title,enq.RegardingProject,enq.Description,enq.Enquirer.userID,"",""));
            }
            else {
                bw.write(String.join(",",enq.Title,enq.RegardingProject,enq.Description,enq.Enquirer.userID,enq.Reply,enq.Replier.userID));
            }

        } 
        catch (IOException | NumberFormatException e) {}
    }

    public void DeleteEnquiry(Enquiry enq) {
        String filePath = "src/localdata/Enquiries.csv";
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            br.readLine();
        
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",");
                if (!data[0].equals(enq.Title) || 
                !data[1].equals(enq.RegardingProject) ||
                !data[3].equals(enq.Enquirer.userID))
                {
                    lines.add(line);
                }
            }
        } 
        catch (IOException | NumberFormatException e) {}

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) 
        {
            bw.write("Title,RegardingProject,Description,EnquirerID,Reply,ReplySenderID");
            for (String line : lines) 
            {
                bw.newLine();
                bw.write(line);
            }
        } 
        catch (IOException | NumberFormatException e) {}
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// Writing Projects ////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    public void WriteNewProject(Project p)
    {
        String filePath = "src/localdata/ProjectList.csv";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) 
        {
            bw.newLine();
            String project_info = String.join(",",p.Details.ProjectName,p.Details.Neighborhood,
            String.valueOf(p.Details.SellingPrice_2Room), String.valueOf(p.Details.SellingPrice_3Room),
            String.valueOf(p.Details.NoOfUnitsLeft_2Room), String.valueOf(p.Details.NoOfUnitsLeft_3Room),
            p.Details.OpenDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), 
            p.Details.CloseDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            p.Details.Manager.userID, String.valueOf(p.Details.OfficerSlots),
            String.join("-",p.Details.OfficerList.stream().map(o -> o.userID).toArray(String[]::new)),
            String.valueOf(p.Details.activeStatus), String.valueOf(p.Details.OpentoUserGroup));

            bw.write(project_info);
        } 
        catch (IOException | NumberFormatException e) {}
    }

    public void DeleteProject(Project p) {
        String filePath = "src/localdata/ProjectList.csv";
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            br.readLine();
        
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",");
                if (!data[0].equals(p.Details.ProjectName)) 
                {
                    lines.add(line);
                }
            }
        } 
        catch (IOException | NumberFormatException e) {}

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) 
        {
            bw.write("Project Name,Neighborhood,SellingPriceFor2Room,SellingPrice3Room,NoOfUnitsLeft2Room,NoOfUnitsLeft3Room,Application opening date,Application closing date,Manager,Officer Slot,Officers,Visibility,OpentoUserGroup");
            for (String line : lines) 
            {
                bw.newLine();
                bw.write(line);
            }
        } 
        catch (IOException | NumberFormatException e) {}
    }

    public void RewriteProject(Project p) {
        String filePath = "src/localdata/ProjectList.csv";
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            br.readLine();
        
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",");
                if (!data[0].equals(p.Details.ProjectName)) 
                {
                    lines.add(line);
                }
            }
        } 
        catch (IOException | NumberFormatException e) {}

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) 
        {
            bw.write("Project Name,Neighborhood,SellingPriceFor2Room,SellingPrice3Room,NoOfUnitsLeft2Room,NoOfUnitsLeft3Room,Application opening date,Application closing date,Manager,Officer Slot,Officers,Visibility,OpentoUserGroup");
            for (String line : lines) 
            {
                bw.newLine();
                bw.write(line);
            }
            bw.newLine();
            String project_info = String.join(",",p.Details.ProjectName,p.Details.Neighborhood,
            String.valueOf(p.Details.SellingPrice_2Room), String.valueOf(p.Details.SellingPrice_3Room),
            String.valueOf(p.Details.NoOfUnitsLeft_2Room), String.valueOf(p.Details.NoOfUnitsLeft_3Room),
            p.Details.OpenDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), 
            p.Details.CloseDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            p.Details.Manager.userID, String.valueOf(p.Details.OfficerSlots),
            String.join("-",p.Details.OfficerList.stream().map(o -> o.userID).toArray(String[]::new)),
            String.valueOf(p.Details.activeStatus), String.valueOf(p.Details.OpentoUserGroup));

            bw.write(project_info);
        } 
        catch (IOException | NumberFormatException e) {}
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// Writing Requests ////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    
    public void WriteNewRequest(Request r) 
    {
        String filePath = "src/localdata/Requests.csv";
        File file = new File(filePath);
        boolean fileExists = file.exists();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) 
        {
            if (!fileExists) 
            {
                bw.write("RequestType,RegardingProject,InitiatorID,HandlerID,Status,BookedFlatType");
            }
            bw.newLine();
            String handlerID = r.handler != null ? r.handler.userID : "";
            String status = r.status != Request.ApplicationStatus.PENDING ? r.status.toString() : "";
            String bookedFlatType = "";
            if (r instanceof Withdrawal) 
            {
                bookedFlatType = ((Withdrawal)r).BookedFlatType != null ? ((Withdrawal)r).BookedFlatType.toString() : "";
            } 
            else if (r instanceof Booking) 
            {
                bookedFlatType = ((Booking)r).flatTypeToBook.toString();
            } 

            bw.write(String.join(",",r.getRequestType(),r.RegardingProject,r.initiator.userID,handlerID,status,bookedFlatType));
        } 
        catch (IOException | NumberFormatException e) {}
    }

    public void DeleteRequest(Request r) {
        String filePath = "src/localdata/Requests.csv";
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            br.readLine();
        
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",");
                //one user can only have one request at any given time. Hence only need to check the initiatorID
                if (!data[2].equals(r.initiator.userID))
                {
                    lines.add(line);
                }
            }
        } 
        catch (IOException | NumberFormatException e) {}

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) 
        {
            bw.write("RequestType,RegardingProject,InitiatorID,HandlerID,Status,BookedFlatType");
            for (String line : lines) 
            {
                bw.newLine();
                bw.write(line);
            }
        } 
        catch (IOException | NumberFormatException e) {}
    }

    public void RewriteRequest(Request r) {
        String filePath = "src/localdata/Requests.csv";
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            br.readLine();
        
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",");
                //one user can only have one request at any given time. Hence only need to check the initiatorID
                if (!data[2].equals(r.initiator.userID))
                {
                    lines.add(line);
                }
            }
        } 
        catch (IOException | NumberFormatException e) {}

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) 
        {
            bw.write("RequestType,RegardingProject,InitiatorID,HandlerID,Status,BookedFlatType");
            for (String line : lines) 
            {
                bw.newLine();
                bw.write(line);
            }
            bw.newLine();
            String handlerID = r.handler != null ? r.handler.userID : "";
            String status = r.status != Request.ApplicationStatus.PENDING ? r.status.toString() : "";
            String bookedFlatType = "";
            if (r instanceof Withdrawal) 
            {
                bookedFlatType = ((Withdrawal)r).BookedFlatType != null ? ((Withdrawal)r).BookedFlatType.toString() : "";
            } 
            else if (r instanceof Booking) 
            {
                bookedFlatType = ((Booking)r).flatTypeToBook.toString();
            } 

            bw.write(String.join(",",r.getRequestType(),r.RegardingProject,r.initiator.userID,handlerID,status,bookedFlatType));
        } 
        catch (IOException | NumberFormatException e) {}
    }
}