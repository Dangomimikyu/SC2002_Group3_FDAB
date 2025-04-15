//EnquiryDB is the database class that will be used to store all the enquiry information within the system
// it will be used to retrieve, modify, add and delete enquiries to/from the csv files

package Database;

import java.util.ArrayList;

import InteractableAttributePackage.Enquiry;
import Service.*;
import User.SystemUser;
import Filter.*;

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

    public void ViewDB(IFilter filter) {
        //if filtering by flat type, show all enquiries with description, title and replies containing keywords of that type
        if (filter instanceof Filter_FlatType) {
            System.out.println("Filter by enquiries containing flat type keyword: " + ((Filter_FlatType)filter).flatType);
        }
        else if (filter instanceof Filter_Marital) {
            System.out.println("Filter by enquiries containing marital status keyword: " + ((Filter_Marital)filter).maritalStatus);
        }
        else if (filter instanceof Filter_Alphabetic) {
            System.out.println("Filter by enquiries title starting with: " + (((Filter_Alphabetic)filter).first_char == null ? "any character" : ((Filter_Alphabetic)filter).first_char)
             + "\nin " + ((Filter_Alphabetic)filter).order + " order");
        }
        else  { 
            throw new UnsupportedOperationException("This filter type is not supported for enquiries");
        }
        
        //special case for Filter_Alphabetic, as it needs to sort the enquiries in ascending or descending order
        if (filter instanceof Filter_Alphabetic) {
            Filter_Alphabetic filter_alpha = (Filter_Alphabetic)filter;
            ArrayList<Enquiry> sortedEnquiries = new ArrayList<>();
            for (Enquiry e : enqList) {
                sortedEnquiries.add(e);
            };
            sortedEnquiries.removeIf(e -> !filter.FilterBy(e));
            if (filter_alpha.order == IFilter.orderBy.ASCENDING) {
                sortedEnquiries.sort((e1, e2) -> e1.Title.compareToIgnoreCase(e2.Title));
            }
            else if (filter_alpha.order == IFilter.orderBy.DESCENDING) {
                sortedEnquiries.sort((e1, e2) -> e2.Title.compareToIgnoreCase(e1.Title));
            }
            for (Enquiry e : sortedEnquiries) {
                for (int i = 0; i < enqList.size(); i++) {
                    if (enqList.get(i).Title.equals(e.Title) && enqList.get(i).Description.equals(e.Description) 
                    && enqList.get(i).Enquirer.userID.equals(e.Enquirer.userID)) {
                        System.out.println("================ " + i + " ================");
                        System.out.println(e.getEnquiryDetails());
                        break;
                    }
                }
            }
        }

        else {
            int index = 0;
            for (Enquiry e : enqList) {
                if (filter.FilterBy(e)) {
                    System.out.println("================ " + index + " ================");
                    System.out.println(e.getEnquiryDetails());
                }
                index++;
                }
            }
    }

    //modify enquiry by index. Works in conjunction with ViewDB().
    public void ModifyDB(int index, DB_Action action) {
        switch (action) {
            case ADD -> throw new UnsupportedOperationException("Adding Enquiries not supported with index method");
            case DELETE -> {
                writer.DeleteEnquiry(enqList.get(index));
                enqList.remove(index);
            }
            case EDIT -> throw new UnsupportedOperationException("Editing Enquiries not supported with index method");
        }
    }

    //modift enquiry by object. Meant for use in Manager classes. Not meant to use with ViewDB().
    public void ModifyDB(Enquiry enquiry, DB_Action action) {
        switch (action) {
            case ADD -> {
                writer.WriteNewEnquiry(enquiry);
                enqList.add(enquiry);
            }
            case DELETE -> {
                writer.DeleteEnquiry(enquiry);
                enqList.remove(enquiry);
            }
            case EDIT -> {
                writer.RewriteEnquiry(enquiry);
                enqList.set(enqList.indexOf(enquiry), enquiry);
            }
        }
    }


}