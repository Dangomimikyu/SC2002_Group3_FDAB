package Controller;

import Entity.*;
import Users.*;
import Service.*;
import java.util.ArrayList;

//EnquiryManager is the controller class responsible for acting as the boundary between how HDBMain interacts with functions regarding
//the creation, modification, reading and deletion of enquiries.  

public class EnquiryManager {

    private final LocalData localdata;
    private ArrayList<Enquiry> list_of_all_enquiries; 

    public EnquiryManager(LocalData ld) {
        this.localdata = ld;
        this.list_of_all_enquiries = localdata.get_initial_enquiries();
    }

    //gets all the current list of enquiries
    public ArrayList<Enquiry> get_all_current_enquiries() {
        return list_of_all_enquiries;
    }

    //gets all the current list of enquiries CREATED BY USER
    public ArrayList<Enquiry> get_all_created_enquiries(System_User user) {
        ArrayList<Enquiry> all_created_enquiries = new ArrayList<>();
        for (Enquiry enq : list_of_all_enquiries) {
            if (enq.Enquirer.getUserID().equals(user.getUserID())) {
                all_created_enquiries.add(enq);
            }
        }
        return all_created_enquiries;
    }

    //checks if enquiry has a unique combination of title,regarding what project and creator of enquiry
    public boolean isUniqueEnquiry(Enquiry e) {
        for (Enquiry enq : list_of_all_enquiries) {
            if (e.Title.equals(enq.Title) &&
            e.RegardingProject.equals(enq.RegardingProject) &&
            e.Enquirer.getUserID().equals(enq.Enquirer.getUserID())) {
                return false;
            }
        }
        return true;
    }

    //relates to the creation of a new enquiry
    public void addEnquiry(System_User user, String title, String project_name, String message) {
        Enquiry e = new Enquiry(title,user,project_name,message);
        if (isUniqueEnquiry(e)) {
            list_of_all_enquiries.add(e);
            localdata.addEnquiry(e);
        }
        else {
            System.out.println("Error: Cannot create Enquiries with the same Title, Enquirer and Regarding Project!");
        }
    }

    //edits an existing enquiry
    public void editEnquiry(Enquiry edited_enquiry) {
        int index = 0;
        for (Enquiry enq : list_of_all_enquiries) {
            if (enq.Title.equals(edited_enquiry.Title) &&
             enq.Enquirer.getUserID().equals(edited_enquiry.Enquirer.getUserID()) &&
             enq.RegardingProject.equals(edited_enquiry.RegardingProject)) {
                list_of_all_enquiries.remove(index);
                list_of_all_enquiries.add(edited_enquiry);
                break;
            }
            index++;
        }
        localdata.editEnquiry(edited_enquiry);
    }

    //deletes an existing enquiry
    public void deleteEnquiry(Enquiry enquiry_to_delete) {
        int index = 0;
        for (Enquiry enq : list_of_all_enquiries) {
            if (enq.Title.equals(enquiry_to_delete.Title) &&
             enq.Enquirer.getUserID().equals(enquiry_to_delete.Enquirer.getUserID()) &&
             enq.RegardingProject.equals(enquiry_to_delete.RegardingProject)) {
                list_of_all_enquiries.remove(index);
                break;
            }
            index++;
        }
        localdata.deleteEnquiry(enquiry_to_delete); 
    }
    
}
