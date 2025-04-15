package Entity;

import User.*;

public class Enquiry {
    public String Title;
    public String RegardingProject;
    public String Description;
    // The applicant who made the enquiry cannot be changed
    public final Applicant Enquirer;
    public String Reply = "";
    public SystemUser Replier = null;

    public Enquiry(String title, String project_name, String description, Applicant enquirer) {
        this.Title = title;
        this.RegardingProject = project_name;
        this.Description = description;
        this.Enquirer = enquirer;
    }

    //check if the enquiry has been replied to
    public boolean isUnreplied() {
        return Replier == null;
    }

    public String getEnquiryDetails() {
        return "Title: " + Title + "\n" +
               "Regarding Project: " + RegardingProject + "\n" +
               "Description: " + Description + "\n" +
               "Enquirer: " + Enquirer.name + "\n" +
               (isUnreplied() ? "YET TO BE REPLIED" : "Reply: " + Reply + "\nReplier: " + Replier.name + "\n");
    }

}
