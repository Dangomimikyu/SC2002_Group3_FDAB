package Entity;

import User.*;

// Applicant_Application is the class that will be used to create the applicant application request

public class Applicant_Application extends Request {

    public Applicant_Application(Applicant initiator, String regardingProject) {
        super(initiator, regardingProject);
    }

}