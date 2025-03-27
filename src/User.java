//User is the superclass for Applicant, HDBManager and HDBOfficer
//will include basic necessary attributes and get/set methods
import java.util.Scanner;

public class User {

    private String name;
    private String NRIC;
    private int age;
    private String marital_status;
    private String password;

    User(String n, String nric, int a, String m, String p) {
        this.name = n;
        this.NRIC = nric;
        this.age = a;
        this.marital_status = m;
        this.password = p;
    }

    public String getName() {
        return name;
    }
    public String getNRIC() {
        return NRIC;
    }
    public int getAge() {
        return age;
    }
    public String getMaritalStatus() {
        return marital_status;
    }
    public String getPassword() {
        return password;
    }    

    //All users able to view and reply to enquiries
    public void viewEnquiry(Enquiry e) {
        System.out.println("\nPurpose of Enquiry: " + e.getPurposeOfEnquiry() + "\nDetails: " + e.getDetails() + "\nDate: " + e.getDate() 
        + "\nStatus: " + e.getStatus() + "\nReply: " + e.getReply());
    }
    public Enquiry replytoEnquiry(Enquiry e) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your reply to this enquiry: ");
        String reply = sc.nextLine();
        sc.close();
        e.setReply(reply);
        return e;
    }
    //View project details,regardless of visibility settings
    
}
