package InteractableAttributePackage;

import User.Applicant;

// Booking is the class that will be used to create the booking request

public class Booking extends Request {

    public FlatType flatTypeToBook;

    public Booking(Applicant initiator, String regardingProject, FlatType flatTypeToBook) {
        super(initiator, regardingProject);
        this.flatTypeToBook = flatTypeToBook;
    }

    public String getRequestDetails() {
        return super.getRequestDetails() + 
               "Flat Type to Book: " + flatTypeToBook + "\n";
    }

}