//Filter_Location filters objects by location. Exclusively only for projects and filters accordingly.
package Filter;

import InteractableAttributePackage.*;
import InteractableAttributePackage.ProjectDetails.Location;

public class Filter_Location implements IFilter {

    public Location location;

    public Filter_Location(Location location) {
        this.location = location;
    }

    public boolean FilterBy(Object o) {

        if (o instanceof Project) { isInLocation((Project)o);}

        throw new UnsupportedOperationException("Filtering by location is not supported for this object type!");
    }

    //isInLocation checks if the project is in the specified location
    private boolean isInLocation(Project p) {
        return p.Details.Neighborhood == location;
    }
}