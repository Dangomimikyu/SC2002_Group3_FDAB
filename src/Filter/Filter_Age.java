//Filter_Age filters objects by age. It checks if the object is a Applicant or Project and filters accordingly.

package Filter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import InteractableAttributePackage.*;
import User.*;

public class Filter_Age implements IFilter {

    //only double supports negative and positive infinity, so we use double instead of int
    //*** in years, so 1 unit equals to 1 year
    public double minAge = Double.NEGATIVE_INFINITY;
    public double maxAge = Double.POSITIVE_INFINITY;
    public orderBy order = orderBy.ASCENDING;

    //by default, if min and max == -1(in this case, means not specified), there is no limit to that age range
    public Filter_Age(double min, double max, orderBy order) {
        if (min != -1) {
            this.minAge = min;
        }
        if (max != -1) {
            this.maxAge = max;
        }
        this.order = order;
    }

    public boolean FilterBy(Object o) {

        //if o is a Project, check the project's age by finding the number of days between current date and project's open date
        //and dividing by 365 to get the age in years
        if (o instanceof Project) {
            Project p = (Project)o;
            double projectAgeInYears = ChronoUnit.DAYS.between(p.Details.OpenDate, LocalDate.now()) / 365.0;
            return (projectAgeInYears >= minAge && projectAgeInYears <= maxAge);
        }

        //if o is a Applicant/HDB_Officer, compares the user's age with the min and max age
        else if (o instanceof Applicant) {
            Applicant u = (Applicant)o;
            return ((double)u.age >= minAge && (double)u.age <= maxAge);
        }

        //any other object is not supported, hence will throw an exception
        throw new UnsupportedOperationException("Filtering by age is not supported for this object type");
    }
    
}