package Entity;

import User.Applicant;
import User.Enum_FlatType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Receipt {
    private String applicantName;
    private String applicantNRIC;
    private int applicantAge;
    private String applicantMaritalStatus;
    private Enum_FlatType flatTypeBooked;
    private String projectName;
    private String projectNeighborhood;
    private LocalDateTime bookingDate;

    public Receipt(Applicant_Application application, Enum_FlatType flatType, ProjectDetails projectDetails) {
        this.applicantName = application.initiator.name;
        this.applicantNRIC = application.initiator.userID;
        this.applicantAge = application.initiator.age;
        this.applicantMaritalStatus = application.initiator.maritalStatus.toString();
        this.flatTypeBooked = flatType;
        this.projectName = projectDetails.ProjectName;
        this.projectNeighborhood = projectDetails.Neighborhood;
        this.bookingDate = LocalDateTime.now();
    }



    public String getReceiptDetails() {
        return "--- Booking Receipt ---" +
               "\nApplicant Name: " + applicantName +
               "\nNRIC: " + applicantNRIC +
               "\nAge: " + applicantAge +
               "\nMarital Status: " + applicantMaritalStatus +
               "\nFlat Type Booked: " + flatTypeBooked +
               "\nProject: " + projectName + " (" + projectNeighborhood + ")" +
               "\nBooking Date: " + bookingDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

}

