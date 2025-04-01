package Entity;

import java.util.ArrayList;
import Users.*;

public class Enquiry {
    public final String Title;
    public final System_User Enquirer; //creator of enquiry
    public final String RegardingProject; //in regards to (project name)
    public ArrayList<EnquiryMessage> thread = new ArrayList<>(); //Content of Enquiry

    //construction for when user creates enquiry in main menu
    public Enquiry(String title, System_User user, String project_name, String first_message) {
        this.Title = title;
        this.Enquirer = user;
        this.RegardingProject = project_name;
        this.thread.add(new EnquiryMessage(first_message,user));
    }

    //construction for reading from enquiries.csv (messages are added one by one to enquiry)
    public Enquiry(String title, System_User user, String project_name) {
        this.Title = title;
        this.Enquirer = user;
        this.RegardingProject = project_name;
    }

    //add message/reply to enquiry
    public void addMessage(String message, System_User user) {
        this.thread.add(new EnquiryMessage(message,user));
    }

    //edit latest message
    public void editLatestMessage(String editted_message) {
        thread.getLast().message = editted_message;
    }

    //display details of enquiry
    public void getDetails() {
        System.out.println("\n-----------\nTitle: " + Title +
        "\nCreator: " + Enquirer.name +
        "\nRegarding Project: " + RegardingProject + 
        "\n-----------");
        for (EnquiryMessage em : thread) {
            System.out.println("\nSender: " + em.sender.name + "\nMessage: " + em.message);
        }
    }

}
