//Filter_FlatType filters objects by flat type. It checks if the object is a Project, Enquiry, Booking, Withdrawal and filters accordingly.

package Filter;
import InteractableAttributePackage.*;
import InteractableAttributePackage.Request.FlatType;
import java.util.List;

public class Filter_FlatType implements IFilter {
    
    public FlatType flatType = FlatType.NULL; //null signifies ignore if two or three-room
    public orderBy order = orderBy.ASCENDING; //this attribute will only be used when filtering projects

    public Filter_FlatType(FlatType flatType, orderBy order) {
        this.flatType = flatType;
        this.order = order;
    }

    public boolean FilterBy(Object o) {

        //If o is a Project, check if the project has available units of the specified flat type
        if (o instanceof Project) {
            Project p = (Project) o;
            if (flatType == FlatType.NULL) {
                return true;
            } else if (flatType == FlatType.TWO_ROOM) {
                return p.Details.NoOfUnitsLeft_2Room > 0;
            } else if (flatType == FlatType.THREE_ROOM) {
                return p.Details.NoOfUnitsLeft_3Room > 0;
            } else { return false; }

        //if o is an enquiry, check if the enquiry's description, title or reply contains the specified flat type keyword
        } else if (o instanceof Enquiry) {
            Enquiry e = (Enquiry) o;
            List<String> two_room_keywords = List.of("2 room","2-room","2room","two room","two-room","tworoom");
            List<String> three_room_keywords = List.of("3 room","3-room","3room","three room","three-room","threeroom");
            if (flatType == FlatType.NULL) {
                return (two_room_keywords.stream().anyMatch((e.Description+e.Title+e.Reply).toLowerCase()::contains) ||
                        three_room_keywords.stream().anyMatch((e.Description+e.Title+e.Reply).toLowerCase()::contains));

            } else if (flatType == FlatType.TWO_ROOM) {
                return (two_room_keywords.stream().anyMatch((e.Description+e.Title+e.Reply).toLowerCase()::contains));

            } else if (flatType == FlatType.THREE_ROOM) {
                return (two_room_keywords.stream().anyMatch((e.Description+e.Title+e.Reply).toLowerCase()::contains));
            }
            else { return false; }

        //if o is a booking or withdrawal request, check if the request's flat type matches the specified flat type
        //if o is an application, return false automatically as applications cannot be filtered by flat type
        } else if (o instanceof Withdrawal) {
            Withdrawal w = (Withdrawal) o;
            if (flatType == FlatType.NULL) {return true;}
            return w.BookedFlatType == flatType;

        } else if (o instanceof Booking) {
            Booking b = (Booking) o;
            if (flatType == FlatType.NULL) {return true;}
            return b.flatTypeToBook == flatType;
            
        } else if (o instanceof Applicant_Application || o instanceof Officer_Application) {
            return false;
        }

        //if o is not a Project, Enquiry or Request, throw error as it is not a valid object to filter by flat type
        throw new UnsupportedOperationException("Cannot filter by flat type for this object type.");
    }

}