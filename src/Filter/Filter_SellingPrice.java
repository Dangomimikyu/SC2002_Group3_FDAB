//Filter_sellingPrice filters objects by price according to specified unit. Exclusively only for projects and filters accordingly.

package Filter;

import InteractableAttributePackage.Project;
import InteractableAttributePackage.Request.FlatType;

public class Filter_SellingPrice implements IFilter{

    public float minPrice = Float.NEGATIVE_INFINITY;
    public float maxPrice = Float.POSITIVE_INFINITY;
    public orderBy order;
    public FlatType flatType = FlatType.NULL;

    //by default, if min and max == -1(in this case, means not specified), there is no limit to that price range
    public Filter_SellingPrice(int min, int max, orderBy order, FlatType flatType) {
        if (min != -1) {
            this.minPrice = min;
        }
        if (max != -1) {
            this.maxPrice = max;
        }
        this.order = order;
        this.flatType = flatType;
    }

    public boolean FilterBy(Object o) {

        //if o is a Project, compare the project's price with the min and max price on the specified flat type
        //if flat type is not specified (NULL), will compare both 2-room and 3-room prices with the min and max price 
        if (o instanceof Project) {
            Project p = (Project)o;
            if (flatType == FlatType.TWO_ROOM) {
                return (p.Details.SellingPrice_2Room >= minPrice && p.Details.SellingPrice_2Room <= maxPrice);
            }
            else if (flatType == FlatType.THREE_ROOM) {
                return (p.Details.SellingPrice_3Room >= minPrice && p.Details.SellingPrice_3Room <= maxPrice);
            }
            else if (flatType == FlatType.NULL) {
                return (p.Details.SellingPrice_2Room >= minPrice && p.Details.SellingPrice_2Room <= maxPrice &&
                        p.Details.SellingPrice_3Room >= minPrice && p.Details.SellingPrice_3Room <= maxPrice);
            }
        }

        //any other object is not supported, hence will throw an exception
        throw new UnsupportedOperationException("Filtering by price is not supported for this object type");
    }

    
}
