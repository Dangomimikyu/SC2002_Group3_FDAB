package InteractableAttributePackage;

import User.Applicant;
import User.Enum_FlatType;

// Booking is the class that will be used to create the booking request

public class Booking extends Request {

    public Enum_FlatType flatTypeToBook;

    public Booking(Applicant initiator, String regardingProject, Enum_FlatType flatTypeToBook) {
        super(initiator, regardingProject);
        this.flatTypeToBook = flatTypeToBook;
    }

    public String getRequestDetails() {
        return super.getRequestDetails() + 
               "Flat Type to Book: " + flatTypeToBook + "\n";
    }

}