package Entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import Users.*;

public class Project {
    private boolean isVisible = true; //on or off
    private String OpentoUserGroup = "all"; //all, single or married

    public String ProjectName;
    public String Neighborhood;
    public String Type1; //2-room flat
    public int NoOfUnitsForType1;
    public int SellingPriceForType1;
    public String Type2; //3-room flat
    public int NoOfUnitsForType2;
    public int SellingPriceForType2;
    public LocalDate ApplicantOpeningDate;
    public LocalDate ApplicantClosingDate;
    public System_User ManagerInCharge;
    public int OfficerSlots;
    public ArrayList<System_User> OfficersInCharge;

    public Project(String PN, String n, String T1, String T1no, String SPT1, String T2, String T2no, String SPT2, String AOD, String ACD, String MIC, String OS, String OIC, String v, String group) {
        this.ProjectName = PN;
        this.Neighborhood = n;
        this.Type1 = T1;
        this.NoOfUnitsForType1 = Integer.parseInt(T1no);
        this.SellingPriceForType1 = Integer.parseInt(SPT1);
        this.Type2 = T2;
        this.NoOfUnitsForType2 = Integer.parseInt(T1no);
        this.SellingPriceForType2 = Integer.parseInt(SPT2);
        this.ApplicantOpeningDate = LocalDate.parse(AOD, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.ApplicantClosingDate = LocalDate.parse(ACD, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String[] manager_info = MIC.split("-");
        this.ManagerInCharge = new System_User(manager_info[0],manager_info[1],Integer.parseInt(manager_info[2]),manager_info[3],"","Manager");
        this.OfficerSlots = Integer.parseInt(OS);
        this.OfficersInCharge = new ArrayList<>();
        for (String officer : OIC.split("\\|"))  {
            String[] officer_info = officer.split("-");
            this.OfficersInCharge.add(new System_User(officer_info[0],officer_info[1],Integer.parseInt(officer_info[2]),officer_info[3],"","Officer"));
        }  
        this.isVisible = Boolean.valueOf(v);
        this.OpentoUserGroup = group;
    }


    //functions for Managers
    public boolean isVisible() {
        return isVisible;
    }
    public void setVisibility(boolean val) {
        this.isVisible = val;
    }
    public String getUserGroupOpento() {
        return OpentoUserGroup;
    }
    public void setGroupProjOpento(String group) {
        this.OpentoUserGroup = group;
    }


    //Functions related to modification of officers in charge
    
    public int getNoOfOfficersCurrentlyAssigned() {
        return OfficersInCharge.size();
    }

    public void setOfficerSlots(int OS) {
        if (OS < getNoOfOfficersCurrentlyAssigned()) {
            System.out.println("There are more officers assigned than the slots you are trying to assign! Consider removing existing officers to reduce number of slots.");
            return;
        }
        this.OfficerSlots = OS;
    }

    public void addOfficersInCharge(System_User officer) {
        if (getNoOfOfficersCurrentlyAssigned() == OfficerSlots) {
            System.out.println("There are no more slots to assign anymore new officers!");
            return;
        }
        OfficersInCharge.add(new System_User(officer.name,officer.getUserID(),officer.age,officer.marital_status,"","Officer"));
    }
    public void deleteOfficerInCharge(String NRIC) {
        int index = 0;
        for (System_User officer : OfficersInCharge) {
            if (officer.getUserID().equals(NRIC)) {
                OfficersInCharge.remove(index);
                return;
            }           
            index++;
        }
        System.out.println("Cannot find such an officer in project!");
    }

    public void viewProjectDetails() {
        System.out.println("\nProject Name: " + ProjectName + 
                           "\nNeighbourhood: " + Neighborhood +
                           "\nNumber of Units for " + Type1 + "flats: " + NoOfUnitsForType1 +
                           "\nSelling Price for " + Type1 + "flats: " + SellingPriceForType1 +
                           "\nNumber of Units for " + Type2 + "flats: " + NoOfUnitsForType2 +
                           "\nSelling Price for" + Type2 + "flats: " + SellingPriceForType2 +
                           "\nApplication Opening Date: " + ApplicantOpeningDate +
                           "\nApplication Closing Date: " + ApplicantClosingDate +
                           "\nManagers in Charge: " + ManagerInCharge +
                           "\nOfficer Slots: " + OfficerSlots +
                           "\nOfficers in Charge: ");
        
        for (System_User officer : OfficersInCharge) {
            System.out.print(officer.name+", ");
        }
    }

}
