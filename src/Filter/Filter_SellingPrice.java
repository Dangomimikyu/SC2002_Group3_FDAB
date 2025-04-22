//Filter_sellingPrice filters objects by price according to specified unit. Exclusively only for projects and filters accordingly.

package Filter;

import InteractableAttributePackage.Project;
import User.Enum_FlatType;

public class Filter_SellingPrice implements IFilter{

    public float minPrice = Float.NEGATIVE_INFINITY;
    public float maxPrice = Float.POSITIVE_INFINITY;
    public orderBy order;
    public Enum_FlatType flatType = Enum_FlatType.DEFAULT;

    //by default, if min and max == -1(in this case, means not specified), there is no limit to that price range
    public Filter_SellingPrice(int min, int max, orderBy order, Enum_FlatType flatType) {
        if (min != -1) { this.minPrice = min; }
        if (max != -1) { this.maxPrice = max; }
        this.order = order;
        this.flatType = flatType;
    }

    public boolean FilterBy(Object o) {

        if (o instanceof Project) { return isWithinPriceRange((Project)o); }

        System.out.println("\nError: Filtering by price is not supported for this object type!");
        return false;
    }

    //isWithinPriceRange checks if project's specified flat types are within the price ranges
    private boolean isWithinPriceRange(Project p) {
        if (flatType == Enum_FlatType.TWO_ROOM) {
            return (p.Details.SellingPrice_2Room >= minPrice && p.Details.SellingPrice_2Room <= maxPrice);
        }
        else if (flatType == Enum_FlatType.THREE_ROOM) {
            return (p.Details.SellingPrice_3Room >= minPrice && p.Details.SellingPrice_3Room <= maxPrice);
        }
        return (p.Details.SellingPrice_2Room >= minPrice && p.Details.SellingPrice_2Room <= maxPrice &&
                p.Details.SellingPrice_3Room >= minPrice && p.Details.SellingPrice_3Room <= maxPrice);
    }

    
}
