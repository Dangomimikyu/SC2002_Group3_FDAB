//Filter Project name filters objects with regards to their the given project. Doesn't work for Projects
// not for public use

package Filter;

import InteractableAttributePackage.*;
import User.Applicant;
import User.HDB_Manager;
import User.HDB_Officer;

public class Filter_ProjectName implements IFilter{
    
    public String project_name;
    public String type; //only used for users/requests (OFFICER and APPLICANT AND WITHDRAWAL AND BOOKING)
    
    public Filter_ProjectName(String pn, String ut) { project_name = pn; type = ut; }

    public boolean FilterBy(Object o) {
        if (o instanceof Enquiry) { return isRegardingProject((Enquiry)o); }
        else if (o instanceof Request) { return isRegardingProject((Request)o); }
        else if (o instanceof Applicant) { return isTiedToProject((Applicant)o); }
        else if (o instanceof HDB_Manager) { return false; }

        System.out.println("\nError: Filtering by project name is not supported for this object type!");
        return false;
    }

    private boolean isRegardingProject(Enquiry e) { return e.RegardingProject.equals(project_name); }

    private boolean isRegardingProject(Request r) { 
        if (type.equals("OFFICER")) { return (r.RegardingProject.equals(project_name) && r instanceof Officer_Application); }
        if (type.equals("APPLICANT")) { return (r.RegardingProject.equals(project_name) && r instanceof Applicant_Application); }
        if (type.equals("WITHDRAWAL")) { return (r.RegardingProject.equals(project_name) && r instanceof Withdrawal); }
        if (type.equals("BOOKING")) { return (r.RegardingProject.equals(project_name) && r instanceof Booking); }
        return false;
    }

    private boolean isTiedToProject(Applicant u) {
        //return hdb officers who are assigned to this project
        if ( u instanceof HDB_Officer && type.equals("OFFICER")) { 
            return ((HDB_Officer)u).project_name.equals(project_name); }
        // return applicants who are applied to this project
        return ((Applicant)u).AppliedProject.equals(project_name);
     }
}
