//RequestDB is the database class that will be used to store all the enquiry information within the system
// it will be used to retrieve, modify, add and delete requests to/from the csv files

package Database;

import java.util.ArrayList;

import InteractableAttributePackage.Project;
import InteractableAttributePackage.Request;
import User.SystemUser;
import Filter.*;

public class RequestsDB extends Database {

    private static RequestsDB instance;

    private ArrayList<Request> reqList = new ArrayList<Request>();
    
    private RequestsDB() {}
    public static RequestsDB getInstance() {
        if (instance == null) { instance = new RequestsDB(); }
        return instance;
    }

    public void InitialiseDB(String RequestsFilePath, ArrayList<SystemUser> userList) {
        reqList = reader.readRequests(RequestsFilePath, userList);
    }

    public ArrayList<Request> getRequestDB() {
        return reqList;
    }

    public ArrayList<Project> ViewDB() {
        System.out.println("\nAll requests in order by index: ");
        int index = 0;
        for (Request r : reqList) {
            System.out.println("================ " + index + " ================");
            System.out.println(r.getRequestDetails());
            index++;
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////
    /////////////////// VIEW DB WITH FILTERS //////////////////////////////
    ///////////////////////////////////////////////////////////////////////

    public void ViewDB(ArrayList<IFilter> filters) {

        for ( IFilter filter : filters) { displayFilterInformation(filter); }

        ArrayList<Request> sortedRequests = new ArrayList<>(reqList);
        
        for (IFilter filter : filters) {
            if (filter instanceof Filter_Alphabetic) { sortedRequests = SortInOrder(sortedRequests, (Filter_Alphabetic)filter); }
            else { sortedRequests.removeIf(r -> !filter.FilterBy(r)); }
        }

        displaySortedWithOriginalIndex(sortedRequests);
    }

    /////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////// MODIFYING DB METHODS ////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    //modify request by index. Works in conjunction with ViewDB().
    public void ModifyDB(int index, DB_Action action) {
        switch (action) {
            case ADD -> System.out.println("Error: Adding Requests not supported with index method");
            case DELETE -> {
                writer.DeleteRequest(reqList.get(index));
                reqList.remove(index);
            }
            case EDIT -> System.out.println("Error: Editing Requests not supported with index method");
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

    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////// SORTING ALGORTIHMS + HELPER FUNCTIONS////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    //prints the headers and information for which requests will be filtered by
    private void displayFilterInformation(IFilter filter) {
        if (filter instanceof Filter_FlatType) {
            System.out.println("Filter by bookings and withdrawals that are booked/trying to book flat type: " + ((Filter_FlatType)filter).flatType);
            System.out.println("============================================================================");
        }
        else if (filter instanceof Filter_Marital) {
            System.out.println("Filter by requests with initiators that are of marital status: " + ((Filter_Marital)filter).maritalStatus);
            System.out.println("============================================================================");
        }
        else if (filter instanceof Filter_Alphabetic) {
            System.out.println("Filter by request initiator's name starting with: " + (((Filter_Alphabetic)filter).first_char == null ? "any character" : ((Filter_Alphabetic)filter).first_char)
            + " in " + ((Filter_Alphabetic)filter).order + " order");           
            System.out.println("============================================================================");
        }
        else {
            System.out.println("Error: This filter type is not supported for requests");
            return;
        }
    }

    //Sorts lexicographically in specified order
    private ArrayList<Request> SortInOrder(ArrayList<Request> sortedRequests, Filter_Alphabetic filter) {
        sortedRequests.removeIf(r -> !filter.FilterBy(r));
        if (filter.order == IFilter.orderBy.ASCENDING) {
            sortedRequests.sort((r1, r2) -> r1.initiator.name.compareToIgnoreCase(r2.initiator.name));
        }
        else if (filter.order == IFilter.orderBy.DESCENDING) {
            sortedRequests.sort((r1, r2) -> r2.initiator.name.compareToIgnoreCase(r1.initiator.name));
        }
        return sortedRequests;
    }

    //print the requests in sorted order, as well as its original index in the original list
    //this is to show the user the original index of that request in the original list, so that they can modify it if needed
    private void displaySortedWithOriginalIndex(ArrayList<Request> sortedRequests) {

        if (sortedRequests.size() == 0) {
            System.out.println("\nNo Requests found!");
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

    public Request SearchDB(String projectName)
    {
        return reqList.stream().filter(pp -> projectName.equals(pp.RegardingProject)).findFirst().orElse(null);
    }
}
