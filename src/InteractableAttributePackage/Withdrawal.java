package InteractableAttributePackage;

import User.Applicant;
import User.Enum_FlatType;

// Withdrawal is the class that will be used to create the withdrawal request
// according to FAQ, the withdrawal request is assumed to always be successful

public class Withdrawal extends Request {

    public Enum_FlatType BookedFlatType = Enum_FlatType.DEFAULT;

    public Withdrawal(Applicant initiator, String regardingProject) {
        super(initiator, regardingProject);
    }

    //additionally, if the initiator has already booked a flat, will show the flat type that was booked
    //this is so that the officer can see which flat type the initiator is withdrawing from and manually update the system
    public String getRequestDetails() {
        return super.getRequestDetails() + 
               "Flat Type Booked: " + (BookedFlatType == Enum_FlatType.DEFAULT ? "HAVE NOT BOOKED YET" : BookedFlatType) + "\n";
    }
    
}
