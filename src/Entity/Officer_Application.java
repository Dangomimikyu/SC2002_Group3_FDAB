package Entity;

import User.*;

//Officer_Application is the class that will be used to create the officer application request
//Officer_Application is special because it has no bearing on the HDBOfficer's Applicant_Status
//Because of the way only one requests per user is allowed, this means that while officers can 
//create applicant applications and officer applications, they can only do so one at a time not simultaneously
//note that this is just for creating requests, officers can be both an applicant and an officer at the same time,
//so long as they are not regarding the same project
//NOTE: OFFICERS CANNOT WITHDRAW FROM OFFICER POSITIONS IN PROJECTS ONCE SUCCESSFUL

public class Officer_Application extends Request {
    
    public Officer_Application(Applicant initiator, String regardingProject) {
        super(initiator, regardingProject);
    }
    
}
