//EnquiryDB is the database class that will be used to store all the enquiry information within the system
// it will be used to retrieve, modify, add and delete enquiries to/from the csv files

package Database;

import java.util.ArrayList;

import InteractableAttributePackage.Enquiry;
import Service.*;
import User.SystemUser;

public class EnquiryDB extends Database {

    private static final EnquiryDB instance = new EnquiryDB();

    private ArrayList<Enquiry> enqList = new ArrayList<Enquiry>();
    Reader reader = new Reader();
    Writer writer = new Writer();

    private EnquiryDB() {}
    public static EnquiryDB getInstance() {
        return instance;
    }

    public EnquiryDB(String EnquiriesFilePath, ArrayList<SystemUser> userList) {
        enqList = reader.readEnquiries(EnquiriesFilePath, userList);
    }

    public ArrayList<Enquiry> getEnquiryDB() {
        return enqList;
    }

    public void ViewDB() {
        System.out.println("\nAll enquiries in order by index: ");
        int index = 0;
        for (Enquiry e : enqList) {
            System.out.println("================ " + index + " ================");
            System.out.println(e.getEnquiryDetails());
            index++;
        }
    }

    //modify enquiry by index
    public void ModifyDB(int index, DB_Action action) {
        switch (action) {
            case ADD -> writer.WriteNewEnquiry(enqList.get(index));
            case DELETE -> writer.DeleteEnquiry(enqList.get(index));
            case EDIT -> writer.RewriteEnquiry(enqList.get(index));
        }
    }
}