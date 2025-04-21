package InteractableAttributePackage;

import java.util.ArrayList;
import User.*;

public class Project {

    public ProjectDetails Details;

    public Project(String PN, String n, int SP2R, int SP3R, int Units2R, int Units3R, String AOD, String ACD, HDB_Manager M
    , int OS, ArrayList<HDB_Officer> OIC, boolean v, String group) {
        Details = new ProjectDetails(PN, n, SP2R, SP3R, Units2R, Units3R, AOD, ACD, M, OS, OIC, v, group); 
    }

    //checks if Project is currently still open for application
    public boolean isOpenForApplication() {
        if (Details.OpenDate.isBefore(java.time.LocalDate.now()) && Details.CloseDate.isAfter(java.time.LocalDate.now())) {
            return true;
        } else {
            return false;
        }
    }

    //checks if Project is Active (Visibility ON + within Application Period)
    public boolean isActive() {
        return isOpenForApplication() && Details.visibility;
    }
    
    //toggle visibility of project (only for manager)
    public void ToggleVisibility() {
        if (Details.visibility) {
            Details.visibility = false;
        } else {
            Details.visibility = true;
        }
    }

    public boolean notAcceptingOfficers() {
        return Details.OfficerSlots == Details.OfficerList.size();
    }

    //set officer slots (only for manager)
    public void setOfficerSlots(int OS) {
        if (OS > 10) {
            throw new IllegalArgumentException("Cannot set officer slots to a value greater than 10!");
        }
        if (OS < Details.OfficerList.size()) {
            throw new IllegalArgumentException("Cannot set officer slots to a value less than the number of officers currently assigned!");
        }
        Details.OfficerSlots = OS;
    }

    //for adding officers to the project (only for manager)
    public void AssignOfficer(HDB_Officer officer) {
        if (Details.OfficerList.size() < Details.OfficerSlots) {
            if (Details.OfficerList.stream().anyMatch(o -> o.userID.equals(officer.userID))) {
                throw new IllegalArgumentException("Officer already assigned to this project!");
            }
            Details.OfficerList.add(officer);
        } else {
            throw new IllegalArgumentException("Cannot add more officers, maximum slots reached!");
        }
    }

    //for removing officers from the project (only for manager)
    //TODO: NOTE: According to FAQ, officers cannot withdraw from officer positions in projects once successful. Still need this function?
    public void UnassignOfficer(HDB_Officer officer) {
        if (Details.OfficerList.size() == 0) {
            throw new IllegalArgumentException("No officers to remove!");
        }
        Details.OfficerList.stream().filter(o -> o.userID.equals(officer.userID)).findFirst().ifPresentOrElse(o -> {
            Details.OfficerList.remove(o);
        }, () -> {
            throw new IllegalArgumentException("Officer not found in the project!");
        });
    }

    //for selling units to applicants (for officers and managers)
    public void SellUnit(Enum_FlatType type) {
        if (type == Enum_FlatType.TWO_ROOM) {
            if (Details.NoOfUnitsLeft_2Room > 0) {
                Details.NoOfUnitsLeft_2Room--;
            } else {
                throw new IllegalArgumentException("No more 2-room units left to sell!");
            }
        } else if (type == Enum_FlatType.THREE_ROOM) {
            if (Details.NoOfUnitsLeft_3Room > 0) {
                Details.NoOfUnitsLeft_3Room--;
            } else {
                throw new IllegalArgumentException("No more 3-room units left to sell!");
            }
        } else {
            throw new IllegalArgumentException("Invalid flat type!");
        }
    }

    //for unselling units (for officers and managers)
    public void UnsellUnit(Enum_FlatType type) {
        if (type == Enum_FlatType.TWO_ROOM) {
            Details.NoOfUnitsLeft_2Room++;
        } else if (type == Enum_FlatType.THREE_ROOM) {
            Details.NoOfUnitsLeft_3Room++;
        } else {
            throw new IllegalArgumentException("Invalid flat type!");
        }
    }

    public String getProjectDetails () {
        return "Project Name: " + Details.ProjectName + "\n" +
               "Open to User Group: " + Details.OpentoUserGroup + "\n" +
               "Neighborhood: " + Details.Neighborhood + "\n" +
               "Selling Price (2-Room): " + Details.SellingPrice_2Room + "\n" +
               "Selling Price (3-Room): " + Details.SellingPrice_3Room + "\n" +
               "No of Units Left (2-Room): " + Details.NoOfUnitsLeft_2Room + "\n" +
               "No of Units Left (3-Room): " + Details.NoOfUnitsLeft_3Room + "\n" +
               "Applicant Opening Date: " + Details.OpenDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
               "Applicant Closing Date: " + Details.CloseDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
               "Manager: " + Details.Manager.name + "\n" +
               "Officer Slots: " + Details.OfficerSlots + "\n" +
               "Officers in Charge: " + (Details.OfficerList.size() > 0 ? Details.OfficerList.stream().map(o -> o.name).reduce("", (a, b) -> a + "," + b).substring(1) : "None") + "\n" +
               "Visible: " + (Details.visibility ? "True" : "False") + "\n";
    }

}
