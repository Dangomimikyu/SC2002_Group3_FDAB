public class HDBManager extends User{

    HDBManager(String n, String nric, int a, String m, String p) {
        super(n, nric, a, m, p);
    }

    //create,edit,delete BTO project listings
    //Toggle project's visibility
    //View projects created by other creators, regardless of visibility (filterable)
    //View and reject/approve HDB Officer Registration
    //Approve/reject Applicant's BTO Application
    //Approve/reject Applicant's BTO Withdrawal
    //Generate Report (filterable)
    //View enquiries of ALL projects
}

// • Able to create, edit, and delete BTO project listings.
// • A BTO project information entered by the HDB Manager will include
// information like:
// o Project Name
// o Neighborhood (e.g. Yishun, Boon Lay, etc.)
// o Types of Flat – Assume there are only 2-Room and 3-Room
// o The number of units for the respective types of flat
// o Application opening date
// o Application closing date
// o HDB Manager in charge (automatically tied to the HDB Manager
// who created the listing)
// o Available HDB Officer Slots (max 10)
// • Can only be handling one project within an application period (from
// application opening date, inclusive, to application closing date,
// inclusive)
// • Able to toggle the visibility of the project to “on” or “off”. This will be
// reflected in the project list that will be visible to applicants.
// • Able to view all created projects, including projects created by other
// HDB Manager, regardless of visibility setting.
// • Able to filter and view the list of projects that they have created only.
// • Able to view pending and approved HDB Officer registration.
// • Able to approve or reject HDB Officer’s registration as the HDB
// Manager in-charge of the project – update project’s remaining HDB
// Officer slots
// • Able to approve or reject Applicant’s BTO application – approval is
// limited to the supply of the flats (number of units for the respective flat
// types)
// • Able to approve or reject Applicant's request to withdraw the application.
// • Able to generate a report of the list of applicants with their respective
// flat booking – flat type, project name, age, marital status
// o There should be filters to generate a list based on various categories
// (e.g. report of married applicants’ choice of flat type)
// • Cannot apply for any BTO project as an Applicant.
// • Able to view enquiries of ALL projects.
// • Able to view and reply to enquiries regarding the project he/she is
// handling. 

// • All users can use filters to view project (location, flat types, etc.) Assume
// that default is by alphabetical order. User filter settings are saved when
// they switch menu pages. 