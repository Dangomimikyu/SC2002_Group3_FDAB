package Entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import Users.*;

public class Enquiry {
    private String PurposeOfEnquiry; //question or request for information?
    private String CreatorOfEnquiry; //creator of enquiry
    private String details; //Content of Enquiry
    private String date; //date and time of Enquiry made/last updated
    private String status; //open or closed
    private String reply; // content of reply

    public Enquiry(String COE) {
        this.PurposeOfEnquiry = "";
        this.CreatorOfEnquiry = COE;
        this.details = "";
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
        this.date = dateFormat.toString();
        this.status = "open";
        this.reply = "nil";
    }

    public String getPurposeOfEnquiry() {
        return PurposeOfEnquiry;
    }
    public void setPurposeOfEnquiry(String POE) {
        this.PurposeOfEnquiry = POE;
    }
    public String getCreatorOfEnquiry() {
        return CreatorOfEnquiry;
    }
    public void setCreatorOfEnquiry(String COE) {
        this.CreatorOfEnquiry = COE;
    }
    public String getDetails() {
        return details;
    }
    public void setDetails(String d) {
        this.details = d;
    }
    public String getDate() {
        return date;
    }
    public void updateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
        this.date = dateFormat.toString();
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String s) {
        this.status = s;
    }
    public String getReply() {
        return reply;
    }
    public void setReply(String r) {
        this.reply = r;
    }
}
