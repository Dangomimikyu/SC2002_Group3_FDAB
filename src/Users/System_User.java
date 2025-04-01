package Users;
//System_User is the superclass for Applicant, HDBManager and HDBOfficer
//will include basic necessary attributes and get/set methods

import java.util.Scanner;

public class System_User{

    public final String name;
    private final String NRIC;
    public final int age;
    public final String marital_status;
    private final String password;
    public final String TypeofUser;

    //for initialising pre login useer
    public System_User() {
        name = "";
        NRIC = "";
        age = -1;
        marital_status = "";
        password = "";
        TypeofUser = "";
    }

    //for database storing of user information
    public System_User(String n, String nric, int a, String m, String p, String TOP) {
        this.name = n;
        this.NRIC = nric;
        this.age = a;
        this.marital_status = m;
        this.password = p;
        this.TypeofUser = TOP;
    }

    public String getUserID() {
        return NRIC;
    }
    public String getPassword() {
        return password;
    }

    //all users has login and logout methods
    public String Login() {
        System.out.println("\nPlease enter your Log-in credentials: \n--------------------------------");
        Scanner sc = new Scanner(System.in);
        System.out.println("UserID: ");
        String user_id = sc.nextLine();
        System.out.println("Password: ");
        String pw = sc.nextLine();
        return user_id + "," + pw;
    }
    public void Logout() {
        System.out.println("\nLogging out...\n");
    }
    
}
