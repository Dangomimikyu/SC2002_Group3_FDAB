package Entity;

import User.*;

// Request is the superclass that will be used for all requests type entities in the system
// such as Applicant_Application, Officer_Application, Booking, Withdrawal
//
// users can only have one request in the system at any given point of time
// if they have a pending request, they cannot create another request until the first one is resolved
// after the request has been resolved and has been seen by the initiator, will be deleted from the system and cannot be seen again
// and the initiator will be able to create a new request 
// The Applicant's ApplicantStatus determine what type of request they can create
//
// if the applicant has not applied, they can only create an Applicant_Application request
// if the applicant has successfully applied, they can create a Booking or Withdrawal request
// if the applicant is a successful applicant and has booked a flat, they can only create a Withdrawal request 
//
// officers gain an additional choice to create an Officer_Application request
// officers CANNOT be both an applicant and an officer of the same project
// officers CANNOT withdraw from their officer position if Officer_Application is successful.

public class Request {

    public enum ApplicationStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    public enum FlatType {
        TWO_ROOM,
        THREE_ROOM,
    }

    public final Applicant initiator;
    public final String RegardingProject;
    public SystemUser handler = null;
    public ApplicationStatus status = ApplicationStatus.PENDING;

    public Request(Applicant initiator, String regardingProject) {
        this.initiator = initiator;
        this.RegardingProject = regardingProject;
    }

    //returns the class name of the request type
    public String getRequestType() {
        return this.getClass().getSimpleName();
    }

    public String getRequestDetails() {
        return "Request Type: " + getRequestType() + "\n" +
               "Initiator: " + initiator.name + "\n" +
               "Regarding Project: " + RegardingProject + "\n" +
               "Handler: " + (handler != null ? handler.name : "Not Assigned") + "\n" +
               "Status: " + status + "\n";
    }

    public void modifyRequestStatus(ApplicationStatus newStatus) {
        this.status = newStatus;
    } 
}
