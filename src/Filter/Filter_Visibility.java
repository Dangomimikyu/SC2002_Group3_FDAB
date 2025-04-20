//Filter_Visibility filters objects by visibility. Exclusively for Projects only and filters accordingly.

package Filter;

import InteractableAttributePackage.*;

public class Filter_Visibility implements IFilter {

    public boolean isVisible = true;

    public Filter_Visibility() {}

    public boolean FilterBy(Object o) {

        if (o instanceof Project) { return isVisible((Project)o); }

        System.out.println("Error: Filtering by visibility is not supported for this object type!");
        return false;
    }

    //isVisible checks if the project's active status is on or off
    private boolean isVisible(Project p) { return p.Details.activeStatus == isVisible; }
        
    
}
