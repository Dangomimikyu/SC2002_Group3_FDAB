//EnquiryDB is the database class that will be used to store all the enquiry information within the system
// it will be used to retrieve, modify, add and delete enquiries to/from the csv files

package Database;

import java.util.ArrayList;
import Service.*;
import Entity.Enquiry;
import User.SystemUser;

public class EnquiryDB implements Database<Enquiry> {

    private static final EnquiryDB instance = new EnquiryDB();

    private ArrayList<Enquiry> enqList = new ArrayList<Enquiry>();
    Reader reader = new Reader();
    Writer writer = new Writer();

    // Singleton pattern to ensure only one instance of EnquiryDB exists
    private EnquiryDB() {
        // Private constructor to prevent instantiation
    }
    public static EnquiryDB getInstance() {
        return instance;
    }

    public EnquiryDB(String EnquiriesFilePath, ArrayList<SystemUser> userList) {
        enqList = reader.readEnquiries(EnquiriesFilePath, userList);
    }

    public ArrayList<Enquiry> ViewDB() {
        return enqList;
    }

    public void ModifyDB(Enquiry e, DB_Action action) {
        switch (action) {
            case ADD -> writer.WriteNewEnquiry(e);
            case DELETE -> writer.DeleteEnquiry(e);
            case EDIT -> writer.RewriteEnquiry(e);
        }
    }
}