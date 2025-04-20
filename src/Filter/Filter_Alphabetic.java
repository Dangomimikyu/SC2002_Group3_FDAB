//Filter_Alphabetic is the default filter class that filters objects by alphabetical order (based on their attributes)
//It checks if the object is a User, Project, Enquiry or Requests and filters accordingly.
package Filter;

import InteractableAttributePackage.*;
import User.*;

public class Filter_Alphabetic implements IFilter {

    public String first_char = null; //default character is null, which means won't filter based on the first character
    public orderBy order = orderBy.ASCENDING; //default order is ascending

    public Filter_Alphabetic(String fc, orderBy ord) {
        this.first_char = fc;
        this.order = ord;
    }

    public boolean FilterBy(Object o) {

        //if first_char is null, return true (no filtering of first_char)
        if (first_char == null) { return true; }

        if (o instanceof Project) { return StartsWithChar((Project)o); } 
        else if (o instanceof Enquiry) { return StartsWithChar((Enquiry)o); } 
        else if (o instanceof SystemUser) { return StartsWithChar((SystemUser)o); }
        else if (o instanceof Request) { return StartsWithChar((Request)o); }

        System.out.println("Error: Filtering by Alphabetic is not supported for this object type!");
        return false;
    }

    //StartsWithChar checks if project's name start with the specificed character
    private boolean StartsWithChar(Project p) {
        return p.Details.ProjectName.toLowerCase().startsWith(first_char.toLowerCase());
    }

    //StartsWithChar checks if enquiries's title start with the specificed character
    private boolean StartsWithChar(Enquiry e) {
        return e.Title.toLowerCase().startsWith(first_char.toLowerCase());
    }

    //StartsWithChar checks if user's name start with the specificed character
    private boolean StartsWithChar(SystemUser u) {
        return u.name.toLowerCase().startsWith(first_char.toLowerCase());
    }
    
    //StartsWithChar checks if request's initiator's name start with the specificed character
    private boolean StartsWithChar(Request r) {
        return r.initiator.name.toLowerCase().startsWith(first_char.toLowerCase());
    }

}
