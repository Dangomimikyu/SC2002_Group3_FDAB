public class HDBOfficer extends User{

    HDBOfficer(String n, String nric, int a, String m, String p) {
        super(n, nric, a, m, p);
    }

    //Register to join a project team and view its status (mutually exclusive for the same project)
    //Flat selection work: update applicant's profile with type of flat chosen under the project and no of flats for each flat type remaining
    //Generate flat selection receipt

}
// • HDB Officer possess all applicant’s capabilities.
// • Able to register to join a project if the following criteria are meant:
// o No intention to apply for the project as an Applicant (Cannot apply
// for the project as an Applicant before and after becoming an HDB
// Officer of the project)
// o Not a HDB Officer (registration not approved) for another project
// within an application period (from application opening date,
// inclusive, to application closing date, inclusive)
// • Able to see the status of their registration to be a HDB Officer for a
// project
// • Registration to be a HDB Officer of the project is subject to approval
// from the HDB Manager in charge of the project.
// Once approved, their profile will reflect the project he/she is a HDB
// Officer for.
// • Able to apply for other projects in which he/she is not handling – Once
// applied for a BTO project, he/she cannot register to handle the same
// project
// • Able to view the details of the project he/she is handling regardless of
// the visibility setting.
// • Not allowed to edit the project details.
// • Able to view and reply to enquiries regarding the project he/she is
// handling
// o With Applicant’s successful BTO application, HDB Officer’s flat
// selection responsibilities:
//  Update the number of flat for each flat type that are remaining
//  Retrieve applicant’s BTO application with applicant’s NRIC
//  Update applicant’s project status, from “successful” to
// “booked”.
//  Update applicant’s profile with the type of flat (2-Room or 3-
// Room) chosen under a project
// • Able to generate receipt of the applicants with their respective flat
// booking details – Applicant’s Name, NRIC, age, marital status, flat type
// booked and its project details.

// • All users can use filters to view project (location, flat types, etc.) Assume
// that default is by alphabetical order. User filter settings are saved when
// they switch menu pages. 