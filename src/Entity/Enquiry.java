package Entity;

import java.util.ArrayList;
import Users.*;

public class Enquiry {
    public String Title;
    public String RegardingProject; //in regards to (project name)
    public String Message;
    public System_User Enquirer; //creator of enquiry
    public String Reply = "";
    public System_User Sender = null; //latest sender of reply

    //construction for when user creates enquiry in main menu
    public Enquiry(String title, String project_name, String first_message, System_User enquirer) {
        this.Title = title;
        this.RegardingProject = project_name;
        this.Message = first_message;
        this.Enquirer = enquirer;
    }

    //construction for when reading from enquiries.csv
    public Enquiry(String title, String project_name, String first_message, System_User enquirer, String reply, System_User sender) {
        this.Title = title;
        this.RegardingProject = project_name;
        this.Message = first_message;
        this.Enquirer = enquirer;
        this.Reply = reply;
        this.Sender = sender;
    }

    //edit latest message
    public void editMessage(String editted_message) {
        this.Message = editted_message;
    }

    //get the userID of the creator of enquiry
    public String getCreatorID() {
        return Enquirer.getUserID();
    }

    //get the userID of the sender
    public String getLatestSenderID() {
        return Sender.getUserID();
    }

    //display details of enquiry
    public void getDetails() {
        System.out.println("\n-----------\nTitle: " + Title +
        "\nEnquirer: " + Enquirer.name +
        "\nRegarding Project: " + RegardingProject + 
        "\n-----------");
        if (!Reply.equals("")) {
            System.out.println("Reply: " + Reply +
            "\nSender: " + Sender.name +
            "\n-----------");
        }
        else {
            System.out.println("No reply yet!");
        }
    }

}
