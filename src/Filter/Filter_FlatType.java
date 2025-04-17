//Filter_FlatType filters objects by flat type. It checks if the object is a Project, Enquiry, Booking, Withdrawal and filters accordingly.

package Filter;
import InteractableAttributePackage.*;
import InteractableAttributePackage.Request.FlatType;
import java.util.List;

public class Filter_FlatType implements IFilter {
    
    public FlatType flatType = FlatType.NULL; //null signifies ignore if two or three-room
    public orderBy order = orderBy.ASCENDING; //this attribute will only be used when filtering projects

    public Filter_FlatType(FlatType type, orderBy ord) {
        this.flatType = type;
        this.order = ord;
    }

    public boolean FilterBy(Object o) {

        if (o instanceof Project) { hasAvailableFlatTypes((Project)o); }
        else if (o instanceof Enquiry) { hasFlatTypeKeyword((Enquiry)o); }
        else if (o instanceof Withdrawal) { isRegardingFlatType((Withdrawal)o); }
        else if (o instanceof Booking) { isRegardingFlatType((Booking)o); }
        //applicant and officer application request classes don't have flat type attributes hence will return false
        else if (o instanceof Applicant_Application || o instanceof Officer_Application) {return false;}

        throw new UnsupportedOperationException("Filtering by Flat Types is not supported for this object type!");
    }

    //hasAvailableFlatTypes checks if project's specified flat types are still available for booking
    private boolean hasAvailableFlatTypes(Project p) {
        if (flatType == FlatType.NULL) {return true; }
        else if (flatType == FlatType.TWO_ROOM) { return p.Details.NoOfUnitsLeft_2Room > 0; } 
        else if (flatType == FlatType.THREE_ROOM) { return p.Details.NoOfUnitsLeft_3Room > 0; } 
        return false; 
    }

    //hasFlatTypeKeyword checks if the enquiry's description, title or reply contains the specified flat type keyword
    private boolean hasFlatTypeKeyword(Enquiry e) {
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
        return false;
    }

    //isRegardingFlatType checks if the withdrawal is withdrawing from specified booked flat type
    private boolean isRegardingFlatType(Withdrawal w) {
        if (flatType == FlatType.NULL) {return true;}
        return w.BookedFlatType == flatType;
    }

    //isRegardingFlatType checks if the booking is trying to book the specified flat type
    private boolean isRegardingFlatType(Booking b) {
        if (flatType == FlatType.NULL) {return true;}
        return b.flatTypeToBook == flatType;
    }

}