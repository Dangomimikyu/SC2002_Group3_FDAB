package Entity;

import User.Applicant;

// Withdrawal is the class that will be used to create the withdrawal request

public class Withdrawal extends Request {

    //by default, is null if the initiator has not booked a flat yet but has successfuly applied for a project
    public FlatType BookedFlatType = null;

    public Withdrawal(Applicant initiator, String regardingProject) {
        super(initiator, regardingProject);
    }

    //additionally, if the initiator has already booked a flat, will show the flat type that was booked
    //this is so that the officer can see which flat type the initiator is withdrawing from and manually update the system
    public String getRequestDetails() {
        return super.getRequestDetails() + 
               "Flat Type Booked: " + (BookedFlatType == null ? "HAVE NOT BOOKED YET" : BookedFlatType) + "\n";
    }
    
}
