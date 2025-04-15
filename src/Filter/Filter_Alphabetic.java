//Filter_Alphabetic is the default filter class that filters objects by alphabetical order (based on their attributes)
//It checks if the object is a User, Project, Enquiry or Requests and filters accordingly.
package Filter;

import InteractableAttributePackage.*;
import User.*;

public class Filter_Alphabetic implements IFilter {

    public String first_char = null; //default character is null, which means won't filter based on the first character
    public orderBy order = orderBy.ASCENDING; //default order is ascending

    public Filter_Alphabetic(String first_char, orderBy order) {
        this.first_char = first_char;
        this.order = order;
    }

    public boolean FilterBy(Object o) {

        //if first_char is null, return true (no filtering of first_char)
        if (first_char == null) {
            return true;
        }

        //if o is a Project, check if the project name starts with the specified character
        if (o instanceof Project) {
            Project p = (Project)o;
            return p.Details.ProjectName.toLowerCase().startsWith(first_char.toLowerCase());
        } 

        //if o is an enquiry, check if the enquiry's title starts with the specified character
        else if (o instanceof Enquiry) {
            Enquiry e = (Enquiry)o;
            return e.Title.toLowerCase().startsWith(first_char.toLowerCase());
        } 

        //if o is a User, check if the user's name starts with the specified character
        else if (o instanceof SystemUser) {
            SystemUser u = (SystemUser)o;
            return u.name.toLowerCase().startsWith(first_char.toLowerCase());
        } 

        //if o is a Request, check if the request's initiator name start with the specified character
        else if (o instanceof Request) {
            Request r = (Request)o;
            return r.initiator.name.toLowerCase().startsWith(first_char.toLowerCase());
        }

        //if o is a not a Project, Enquiry, Request or User, throw an exception
        throw new UnsupportedOperationException("Cannot filter by alphabetic for this object type");
    }

    
}
