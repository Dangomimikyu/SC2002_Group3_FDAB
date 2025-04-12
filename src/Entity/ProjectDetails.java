package Entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import User.*;

// ProjectDetails is a class that contains all the details of the project
public class ProjectDetails {

    enum UserGroup {
        SINGLE, MARRIED, ALL
    }

    public boolean isVisible = true;
    public UserGroup OpentoUserGroup;
    //regarding editing project name: since it's a unique identifier, should it be allowed to be changed? might mess up how enquires/requests are identified
    public String ProjectName;
    //maybe type should be an enum? for now, just let it be a string
    public String Neighborhood;
    //all projects are assumed to have 2 room and 3 room flats, so no need to specify type of flats in constructor
    public int SellingPrice_2Room; 
    public int SellingPrice_3Room;
    public int NoOfUnitsLeft_2Room;
    public int NoOfUnitsLeft_3Room;
    public LocalDate ApplicantOpeningDate;
    public LocalDate ApplicantClosingDate;
    public HDB_Manager ManagerInCharge;
    //hardcoded max is 10, but managers can decide what is the max slots as long as it is less than 10
    public int MaxOfficerSlots;
    public ArrayList<HDB_Officer> OfficersInCharge;

    public ProjectDetails(String PN, String n, int SP2R, int SP3R, int Units2R, int Units3R, String AOD, String ACD, HDB_Manager M
    , int OS, ArrayList<HDB_Officer> OIC, boolean v, String group) {
        this.ProjectName = PN;
        this.Neighborhood = n;
        this.SellingPrice_2Room = SP2R;
        this.SellingPrice_3Room = SP3R;
        this.NoOfUnitsLeft_2Room = Units2R;
        this.NoOfUnitsLeft_3Room = Units3R;
        this.ApplicantOpeningDate = LocalDate.parse(AOD, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.ApplicantClosingDate = LocalDate.parse(ACD, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.ManagerInCharge = M;
        this.MaxOfficerSlots = OS;
        this.OfficersInCharge = OIC;
        this.isVisible = v;
        if (group.equals("SINGLE")) {
            this.OpentoUserGroup = UserGroup.SINGLE;
        } else if (group.equals("MARRIED")) {
            this.OpentoUserGroup = UserGroup.MARRIED;
        } else if (group.equals("ALL")) {
            this.OpentoUserGroup = UserGroup.ALL;
        }

    }

}
