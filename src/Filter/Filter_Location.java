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

        //If o is a Project, check if the project is in the specified location
        if (o instanceof Project) {
            Project p = (Project) o;
            return p.Details.Neighborhood == location;
        }

        //any other object is not supported, hence will throw an exception
        throw new UnsupportedOperationException("Filtering by location is only supported for Projects");
    }
}