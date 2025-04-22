//Filter_FlatType filters objects by flat type. It checks if the object is a Project, Enquiry, Booking, Withdrawal and filters accordingly.

package Filter;
import InteractableAttributePackage.*;
import User.Applicant;
import User.Enum_FlatType;
import User.HDB_Manager;

import java.util.List;

public class Filter_FlatType implements IFilter {
    
    public Enum_FlatType flatType = Enum_FlatType.DEFAULT; //DEFAULT signifies ignore if two or three-room
    public orderBy order = orderBy.ASCENDING; //this attribute will only be used when filtering projects

    public Filter_FlatType(Enum_FlatType type, orderBy ord) {
        this.flatType = type;
        this.order = ord;
    }

    public boolean FilterBy(Object o) {

        if (o instanceof Project) { return hasAvailableFlatTypes((Project)o); }
        else if (o instanceof Enquiry) { return hasFlatTypeKeyword((Enquiry)o); }
        else if (o instanceof Withdrawal) { return isRegardingFlatType((Withdrawal)o); }
        else if (o instanceof Booking) { return isRegardingFlatType((Booking)o); }
        //applicant and officer application request classes don't have flat type attributes hence will return false
        else if (o instanceof Applicant_Application || o instanceof Officer_Application) {return false; }
        else if (o instanceof Applicant) { return isBookedWith((Applicant)o); }
        else if (o instanceof HDB_Manager) { return false; }
        System.out.println("Error: Filtering by Flat Types is not supported for this object type!");
        return false;
    }

    //hasAvailableFlatTypes checks if project's specified flat types are still available for booking
    private boolean hasAvailableFlatTypes(Project p) {
        if (flatType == Enum_FlatType.DEFAULT) {return true; }
        else if (flatType == Enum_FlatType.TWO_ROOM) { return p.Details.NoOfUnitsLeft_2Room > 0; } 
        else if (flatType == Enum_FlatType.THREE_ROOM) { return p.Details.NoOfUnitsLeft_3Room > 0; } 
        return false; 
    }

    //hasFlatTypeKeyword checks if the enquiry's description, title or reply contains the specified flat type keyword
    private boolean hasFlatTypeKeyword(Enquiry e) {
        List<String> two_room_keywords = List.of("2 room","2-room","2room","two room","two-room","tworoom");
        List<String> three_room_keywords = List.of("3 room","3-room","3room","three room","three-room","threeroom");
        
        if (flatType == Enum_FlatType.DEFAULT) {
            return (two_room_keywords.stream().anyMatch((e.Description+e.Title+e.Reply).toLowerCase()::contains) ||
                    three_room_keywords.stream().anyMatch((e.Description+e.Title+e.Reply).toLowerCase()::contains));

        } else if (flatType == Enum_FlatType.TWO_ROOM) {
            return (two_room_keywords.stream().anyMatch((e.Description+e.Title+e.Reply).toLowerCase()::contains));

        } else if (flatType == Enum_FlatType.THREE_ROOM) {
            return (two_room_keywords.stream().anyMatch((e.Description+e.Title+e.Reply).toLowerCase()::contains));
        }
        return false;
    }

    //isRegardingFlatType checks if the withdrawal is withdrawing from specified booked flat type
    private boolean isRegardingFlatType(Withdrawal w) {
        if (flatType == Enum_FlatType.DEFAULT) {return true;}
        return w.BookedFlatType == flatType;
    }

    //isRegardingFlatType checks if the booking is trying to book the specified flat type
    private boolean isRegardingFlatType(Booking b) {
        if (flatType == Enum_FlatType.DEFAULT) {return true;}
        return b.flatTypeToBook == flatType;
    }

    //isBookedWith checks if the applicant is booked with specific flat type
    private boolean isBookedWith(Applicant u) {
        if (flatType == Enum_FlatType.DEFAULT) {return true;}
        return u.flatTypeBooked == flatType;
    }

}