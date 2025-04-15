//Filter_FlatType filters objects by flat type. It checks if the object is a Project, Enquiry, Booking, Withdrawal and filters accordingly.

package Filter;
import InteractableAttributePackage.*;
import InteractableAttributePackage.Request.FlatType;

public class Filter_FlatType implements IFilter {
    
    public FlatType flatType;

    public Filter_FlatType(FlatType flatType) {
        this.flatType = flatType;
    }

    public boolean FilterBy(Object o) {

        //If o is a Project, check if the project has available units of the specified flat type
        if (o instanceof Project) {
            Project p = (Project) o;
            if (flatType == FlatType.TWO_ROOM) {
                return p.Details.NoOfUnitsLeft_2Room > 0;
            } else if (flatType == FlatType.THREE_ROOM) {
                return p.Details.NoOfUnitsLeft_3Room > 0;
            }
            else {
                return false;
            }

        //if o is an enquiry, check if the enquiry's description, title or reply contains the specified flat type keyword
        } else if (o instanceof Enquiry) {
            Enquiry e = (Enquiry) o;
            String e_description = e.Description.toLowerCase();
            String e_title = e.Title.toLowerCase();
            String e_reply = e.Reply.toLowerCase();
            if (flatType == FlatType.TWO_ROOM) {
                return (e_description.contains("2 room") || e_description.contains("2-room") || e_description.contains("2room") || e_description.contains("two room") || e_description.contains("tworoom") || e_description.contains("two-room") ||
                        e_title.contains("2 room") || e_title.contains("2-room") || e_title.contains("2room") || e_title.contains("two room") || e_title.contains("tworoom") || e_title.contains("two-room") ||
                        e_reply.contains("2 room") || e_reply.contains("2-room") || e_reply.contains("2room") || e_reply.contains("two room") || e_reply.contains("tworoom") || e_reply.contains("two-room"));
            } else if (flatType == FlatType.THREE_ROOM) {
                return (e_description.contains("3 room") || e_description.contains("3-room") || e_description.contains("3room") || e_description.contains("three room") || e_description.contains("threeroom") || e_description.contains("three-room") ||
                        e_title.contains("3 room") || e_title.contains("3-room") || e_title.contains("3room") || e_title.contains("three room") || e_title.contains("threeroom") || e_title.contains("three-room") ||
                        e_reply.contains("3 room") || e_reply.contains("3-room") || e_reply.contains("3room") || e_reply.contains("three room") || e_reply.contains("threeroom") || e_reply.contains("three-room"));
            }
            else {
                return false;
            }

        //if o is a booking or withdrawal request, check if the request's flat type matches the specified flat type
        //if o is an application, return false automatically as applications cannot be filtered by flat type
        } else if (o instanceof Withdrawal) {
            Withdrawal w = (Withdrawal) o;
            return w.BookedFlatType == flatType;

        } else if (o instanceof Booking) {
            Booking b = (Booking) o;
            return b.flatTypeToBook == flatType;
            
        } else if (o instanceof Applicant_Application || o instanceof Officer_Application) {
            return false;
        }

        //if o is not a Project, Enquiry or Request, throw error as it is not a valid object to filter by flat type
        throw new UnsupportedOperationException("Cannot filter by flat type for this object type.");
    }

}