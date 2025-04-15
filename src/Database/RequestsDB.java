//RequestDB is the database class that will be used to store all the enquiry information within the system
// it will be used to retrieve, modify, add and delete requests to/from the csv files

package Database;

import java.util.ArrayList;

import InteractableAttributePackage.Request;
import Service.*;
import User.SystemUser;
import Filter.*;

public class RequestsDB extends Database {
    private static final RequestsDB instance = new RequestsDB();

    private ArrayList<Request> reqList = new ArrayList<Request>();
    Reader reader = new Reader();
    Writer writer = new Writer();
    
    private RequestsDB() {}
    public static RequestsDB getInstance() {
        return instance;
    }

    public RequestsDB(String RequestsFilePath, ArrayList<SystemUser> userList) {
        reqList = reader.readRequests(RequestsFilePath, userList);
    }

    public ArrayList<Request> getRequestDB() {
        return reqList;
    }

    public void ViewDB() {
        System.out.println("\nAll requests in order by index: ");
        int index = 0;
        for (Request r : reqList) {
            System.out.println("================ " + index + " ================");
            System.out.println(r.getRequestDetails());
            index++;
        }
    }

    public void ViewDB(IFilter filter) {
        //if filtering by flat type, show all withdrawals with current booking of that flat type and all bookings trying to book that flat type
        if (filter instanceof Filter_FlatType) {
            System.out.println("Filter by bookings and withdrawals that are booked/trying to book flat type: " + ((Filter_FlatType)filter).flatType);
        }
        else if (filter instanceof Filter_Marital) {
            System.out.println("Filter by requests with initiators that are of marital status: " + ((Filter_Marital)filter).maritalStatus);
        }
        else if (filter instanceof Filter_Alphabetic) {
            System.out.println("Filter by request initiator's name starting with: " + (((Filter_Alphabetic)filter).first_char == null ? "any character" : ((Filter_Alphabetic)filter).first_char)
            + "\nin " + ((Filter_Alphabetic)filter).order + " order");           
        }
        else {
            throw new UnsupportedOperationException("This filter type is not supported for requests");
        }

        //special case for Filter_Alphabetic, as it needs to sort the requests in ascending or descending order
        if (filter instanceof Filter_Alphabetic) {
            Filter_Alphabetic filter_alpha = (Filter_Alphabetic)filter;
            ArrayList<Request> sortedRequests = new ArrayList<>();
            for (Request r : reqList) {
                sortedRequests.add(r);
            }
            sortedRequests.removeIf(r -> !filter.FilterBy(r));
            if (filter_alpha.order == IFilter.orderBy.ASCENDING) {
                sortedRequests.sort((r1, r2) -> r1.initiator.name.compareToIgnoreCase(r2.initiator.name));
            }
            else if (filter_alpha.order == IFilter.orderBy.DESCENDING) {
                sortedRequests.sort((r1, r2) -> r2.initiator.name.compareToIgnoreCase(r1.initiator.name));
            }
            for (Request r : sortedRequests) {
                for (int i = 0; i < reqList.size(); i++) {
                    if (reqList.get(i).initiator.userID.equals(r.initiator.userID)) {
                        System.out.println("================ " + i + " ================");
                        System.out.println(r.getRequestDetails());
                        break;
                    }
                }
            }
        }

        else {
            int index = 0;
            for (Request r : reqList) {
                if (filter.FilterBy(r)) {
                    System.out.println("================ " + index + " ================");
                    System.out.println(r.getRequestDetails());
                }
                index++;
            }            
        }
    }

    //modify request by index. Works in conjunction with ViewDB().
    public void ModifyDB(int index, DB_Action action) {
        switch (action) {
            case ADD -> throw new UnsupportedOperationException("Adding Requests not supported with index method");
            case DELETE -> {
                writer.DeleteRequest(reqList.get(index));
                reqList.remove(index);
            }
            case EDIT -> throw new UnsupportedOperationException("Editing Requests not supported with index method");
        }
    }

    //modift request by object. Meant for use in Manager classes. Not meant to use with ViewDB().
    public void ModifyDB(Request request, DB_Action action) {
        switch (action) {
            case ADD -> {
                writer.WriteNewRequest(request);
                reqList.add(request);
            }
            case DELETE -> {
                writer.DeleteRequest(request);
                reqList.remove(request);
            }
            case EDIT -> {
                writer.RewriteRequest(request);
                reqList.set(reqList.indexOf(request), request);
            }   
        }
    }
}
