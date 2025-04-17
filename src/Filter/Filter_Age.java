//Filter_Age filters objects by age. It checks if the object is a Applicant or Project and filters accordingly.

package Filter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import InteractableAttributePackage.*;
import User.*;

public class Filter_Age implements IFilter {

    //only double/float supports negative and positive infinity, so we use double instead of int
    //*** in years, so 1 unit equals to 1 year
    public double minAge = Double.NEGATIVE_INFINITY;
    public double maxAge = Double.POSITIVE_INFINITY;
    public orderBy order = orderBy.ASCENDING;

    //by default, if min and max == -1(in this case, means not specified), there is no limit to that age range
    public Filter_Age(double min, double max, orderBy ord) {
        if (min != -1) { this.minAge = min; }
        if (max != -1) { this.maxAge = max; }
        this.order = ord;
    }

    public boolean FilterBy(Object o) {

        if (o instanceof Project) { return isWithinAgeRange((Project)o); }
        else if (o instanceof Applicant) { return isWithinAgeRange((Applicant)o); }

        throw new UnsupportedOperationException("Filtering by Age is not supported for this object type!");

    }


    //isWithinAgeRange checks if project's age is within specified age range 
    //Current Implementation: Project's age is calculated by finding the number of days 
    //between current date and project's open date and dividing by 365 to get the age in years
    private boolean isWithinAgeRange(Project p) {
        double projectAgeInYears = ChronoUnit.DAYS.between(p.Details.OpenDate, LocalDate.now()) / 365.0;
        return (projectAgeInYears >= minAge && projectAgeInYears <= maxAge);
    }

    //isWithinAgeRange checks if SystemUser's age is within specified age range 
    //Current Implementation: Directly uses the Applicant's age attribute
    private boolean isWithinAgeRange(Applicant u) {
        return ((double)u.age >= minAge && (double)u.age <= maxAge);
    }    
}