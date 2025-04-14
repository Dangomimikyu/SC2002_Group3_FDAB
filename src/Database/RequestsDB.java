//RequestDB is the database class that will be used to store all the enquiry information within the system
// it will be used to retrieve, modify, add and delete requests to/from the csv files

package Database;

import java.util.ArrayList;

import InteractableAttributePackage.Request;
import Service.*;
import User.SystemUser;

public class RequestsDB extends Database {
    private static final RequestsDB instance = new RequestsDB();

    private ArrayList<Request> requestList = new ArrayList<Request>();
    Reader reader = new Reader();
    Writer writer = new Writer();
    
    private RequestsDB() {}
    public static RequestsDB getInstance() {
        return instance;
    }

    public RequestsDB(String RequestsFilePath, ArrayList<SystemUser> userList) {
        requestList = reader.readRequests(RequestsFilePath, userList);
    }

    public ArrayList<Request> getRequestDB() {
        return requestList;
    }

    public void ViewDB() {
        System.out.println("\nAll requests in order by index: ");
        int index = 0;
        for (Request r : requestList) {
            System.out.println("================ " + index + " ================");
            System.out.println(r.getRequestDetails());
            index++;
        }
    }

    //modify request by index
    public void ModifyDB(int index, DB_Action action) {
        switch (action) {
            case ADD -> writer.WriteNewRequest(requestList.get(index));
            case DELETE -> writer.DeleteRequest(requestList.get(index));
            case EDIT -> writer.RewriteRequest(requestList.get(index));
        }
    }
}
