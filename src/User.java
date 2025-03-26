//User is the superclass for Applicant, HDBManager and HDBOfficer
//will include basic necessary attributes and get/set methods

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

    //View and reply to enquiries
    //View project details,regardless of visibility settings
    
}
