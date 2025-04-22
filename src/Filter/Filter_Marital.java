//Filter_Martial filters objects by marital status. It checks if the object is a Project, Enquiry, Request or User and filters accordingly.

package Filter;

import InteractableAttributePackage.*;
import User.*;
import User.Applicant.MaritalStatus;

public class Filter_Marital implements IFilter {

    public MaritalStatus maritalStatus;

    public Filter_Marital(MaritalStatus ms) {
        this.maritalStatus = ms;
    }

    public boolean FilterBy(Object o) {

        if (o instanceof Project) { return isOpenToUserGroup((Project)o); }
        else if (o instanceof Enquiry) { return hasMaritalStatusKeyword((Enquiry)o); }
        else if (o instanceof Request) { return hasMaritalStatusKeyword((Request)o); }
        else if (o instanceof Applicant) { return isOfMaritalStatus((Applicant)o); }
        //HDB_Managers don't have marital status as an attributes, hence will return false
        else if (o instanceof HDB_Manager) { return false; }

        System.out.println("\nError: Filtering by marital status is not supported for this object type!");
        return false;
        
    }

    //isOpenToUserGroup checks if project is open to the specified marital status
    private boolean isOpenToUserGroup(Project p) {
        return p.Details.OpentoUserGroup == maritalStatus;
    }

    //hasMaritalStatusKeyword checks if the enquiry's description, title or reply contains the specified marital status keyword
    private boolean hasMaritalStatusKeyword(Enquiry e) {
        if (maritalStatus == MaritalStatus.SINGLE) {
            return ((e.Description+e.Title+e.Reply).toLowerCase().contains("single"));
        } else if (maritalStatus == MaritalStatus.MARRIED) {
            return ((e.Description+e.Title+e.Reply).toLowerCase().contains("married"));
        } else {
            return false;
        } 
    }

    //hasMaritalStatusKeyword checks if the request initiator's marital status matches the specified marital status
    private boolean hasMaritalStatusKeyword(Request r) {
        return r.initiator.maritalStatus == maritalStatus;
    }    

    //isOfMaritalStatus checks if applicant is of specified marital status
    private boolean isOfMaritalStatus(Applicant u) {
        return u.maritalStatus == maritalStatus;
    }
}
