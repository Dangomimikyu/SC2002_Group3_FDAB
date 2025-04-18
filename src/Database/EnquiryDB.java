//EnquiryDB is the database class that will be used to store all the enquiry information within the system
// it will be used to retrieve, modify, add and delete enquiries to/from the csv files

package Database;

import java.util.ArrayList;
import InteractableAttributePackage.Enquiry;
import InteractableAttributePackage.Project;
import Service.*;
import User.SystemUser;
import Filter.*;

public class EnquiryDB extends Database {

    private static EnquiryDB instance;

    private ArrayList<Enquiry> enqList = new ArrayList<Enquiry>();
    Reader reader = new Reader();
    Writer writer = new Writer();

    private EnquiryDB() {}
    public static EnquiryDB getInstance() {
        if (instance == null) { instance = new EnquiryDB(); }
        return instance;
    }

    public void InitialiseDB(String EnquiriesFilePath, ArrayList<SystemUser> userList) {
        enqList = reader.readEnquiries(EnquiriesFilePath, userList);
    }

    public ArrayList<Enquiry> getEnquiryDB() {
        return enqList;
    }

    public ArrayList<Project> ViewDB() {
        System.out.println("\nAll enquiries in order by index: ");
        int index = 0;
        for (Enquiry e : enqList) {
            System.out.println("================ " + index + " ================");
            System.out.println(e.getEnquiryDetails());
            index++;
        }
        return null;
    }

    public void ViewDB(IFilter filter) {
        
        displayFilterInformation(filter);
        
        if (filter instanceof Filter_Alphabetic) { SortInOrder((Filter_Alphabetic)filter); }

        //for filters with no orderBy attribute
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

    //prints the headers and information for which enquiries will be filtered by
    private void displayFilterInformation(IFilter filter) {
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
    }

    //Sorts lexicographically in specified order
    private void SortInOrder(Filter_Alphabetic filter) {
        ArrayList<Enquiry> sortedEnquiries = new ArrayList<>(enqList);
            sortedEnquiries.removeIf(e -> !filter.FilterBy(e));
            if (filter.order == IFilter.orderBy.ASCENDING) {
                sortedEnquiries.sort((e1, e2) -> e1.Title.compareToIgnoreCase(e2.Title));
            }
            else if (filter.order == IFilter.orderBy.DESCENDING) {
                sortedEnquiries.sort((e1, e2) -> e2.Title.compareToIgnoreCase(e1.Title));
            }
            displaySortedWithOriginalIndex(sortedEnquiries);
    }

    //print the enquiries in sorted order, as well as its original index in the original list
    //this is to show the user the original index of that enquiry in the original list, so that they can modify it if needed
    private void displaySortedWithOriginalIndex(ArrayList<Enquiry> SortedEnquiries) {
        for (Enquiry e : SortedEnquiries) {
            for (int i = 0; i < enqList.size(); i++) {
                if (enqList.get(i).Title.equals(e.Title) && enqList.get(i).RegardingProject.equals(e.RegardingProject) 
                && enqList.get(i).Enquirer.userID.equals(e.Enquirer.userID)) {
                    System.out.println("================ " + i + " ================");
                    System.out.println(e.getEnquiryDetails());
                    break;
                }
            }
        }
    }

    public Enquiry SearchDB(String projectName)
    {
        return enqList.stream().filter(pp -> projectName.equals(pp.Title)).findFirst().orElse(null);
    }
}