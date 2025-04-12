package Entity;

import java.util.ArrayList;
import User.*;

public class Project {

    public ProjectDetails Details;

    public Project(String PN, String n, int SP2R, int SP3R, int Units2R, int Units3R, String AOD, String ACD, HDB_Manager M
    , int OS, ArrayList<HDB_Officer> OIC, boolean v, String group) {
        Details = new ProjectDetails(PN, n, SP2R, SP3R, Units2R, Units3R, AOD, ACD, M, OS, OIC, v, group); 
    }

    //checks if Project is still open for application
    public boolean isOpenForApplication() {
        if (Details.ApplicantOpeningDate.isBefore(java.time.LocalDate.now()) && Details.ApplicantClosingDate.isAfter(java.time.LocalDate.now())) {
            return true;
        } else {
            return false;
        }
    }

    //toggle visibility of project (only for manager)
    public void toggleVisibility() {
        if (Details.isVisible) {
            Details.isVisible = false;
        } else {
            Details.isVisible = true;
        }
    }

    //set officer slots (only for manager)
    public void setOfficerSlots(int OS) {
        if (OS > 10) {
            throw new IllegalArgumentException("Cannot set officer slots to a value greater than 10!");
        }
        if (OS < Details.OfficersInCharge.size()) {
            throw new IllegalArgumentException("Cannot set officer slots to a value less than the number of officers currently assigned!");
        }
        Details.MaxOfficerSlots = OS;
    }

    //for adding officers to the project (only for manager)
    public void addOfficer(HDB_Officer officer) {
        if (Details.OfficersInCharge.size() < Details.MaxOfficerSlots) {
            Details.OfficersInCharge.add(officer);
        } else {
            throw new IllegalArgumentException("Cannot add more officers, maximum slots reached!");
        }
    }

    //for removing officers from the project (only for manager)
    public void removeOfficer(HDB_Officer officer) {
        if (Details.OfficersInCharge.size() == 0) {
            throw new IllegalArgumentException("No officers to remove!");
        }
        Details.OfficersInCharge.stream().filter(o -> o.userID.equals(officer.userID)).findFirst().ifPresentOrElse(o -> {
            Details.OfficersInCharge.remove(o);
        }, () -> {
            throw new IllegalArgumentException("Officer not found in the project!");
        });
    }

}
