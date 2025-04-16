//Filter_Martial filters objects by marital status. It checks if the object is a Project, Enquiry, Request or User and filters accordingly.

package Filter;

import InteractableAttributePackage.*;
import User.*;
import User.Applicant.MaritalStatus;

public class Filter_Marital implements IFilter {

    public MaritalStatus maritalStatus;

    public Filter_Marital(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public boolean FilterBy(Object o) {

        //If o is a Project, check if project is open to the specified marital status
        if (o instanceof Project) {
            Project p = (Project) o;
            return p.Details.OpentoUserGroup == maritalStatus;

        //if o is an enquiry, check if the enquiry's description, title or reply contains the specified marital status keyword
        } else if (o instanceof Enquiry) {
            Enquiry e = (Enquiry) o;
            if (maritalStatus == MaritalStatus.SINGLE) {
                return ((e.Description+e.Title+e.Reply).toLowerCase().contains("single"));
            } else if (maritalStatus == MaritalStatus.MARRIED) {
                return ((e.Description+e.Title+e.Reply).toLowerCase().contains("married"));
            } else {
                return false;
            }

        //if o is a request, check if the request initiator's marital status matches the specified marital status
        } else if (o instanceof Request) {
            Request r = (Request) o;
            return r.initiator.maritalStatus == maritalStatus;
        }

        //if o is an Applicant or HDBOfficer, check if the user's marital status matches the specified marital status
        //if o is an HDB_Manager, automatically return false as HDB_Manager is not a valid type to filter by marital status
        if (o instanceof Applicant) {
            Applicant a = (Applicant) o;
            return a.maritalStatus == maritalStatus;
        } else if (o instanceof HDB_Manager) {
            return false;
        }
        //else if o is not a Project, Enquiry, Request or User, throw error as it is not a valid type to filter by marital status
        throw new IllegalArgumentException("Object is not a Project, Enquiry, Request or User.");
        
    }
}
