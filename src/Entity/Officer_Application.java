package Entity;

import User.*;

//Officer_Application is the class that will be used to create the officer application request

public class Officer_Application extends Request {
    
    public Officer_Application(Applicant initiator, String regardingProject) {
        super(initiator, regardingProject);
    }
    
}
