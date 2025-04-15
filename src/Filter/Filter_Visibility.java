//Filter_Visibility filters objects by visibility. Exclusively for Projects only and filters accordingly.

package Filter;

import InteractableAttributePackage.*;

public class Filter_Visibility implements IFilter {

    public boolean isVisible = true;

    public Filter_Visibility() {}

    public boolean FilterBy(Object o) {

        //If o is a Project, check if the project is visible or not
        if (o instanceof Project) {
            Project p = (Project) o;
            return p.Details.activeStatus == isVisible;
        }

        //any other object is not supported, hence will throw an exception
        throw new UnsupportedOperationException("Filtering by visibility is only supported for Projects");
    }
        
    
}
