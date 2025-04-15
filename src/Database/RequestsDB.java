//RequestDB is the database class that will be used to store all the enquiry information within the system
// it will be used to retrieve, modify, add and delete requests to/from the csv files

package Database;

import java.util.ArrayList;
import Service.*;
import Entity.Request;
import User.SystemUser;

public class RequestsDB implements Database<Request> {
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

    public ArrayList<Request> ViewDB() {
        return requestList;
    }

    public void ModifyDB(Request r, DB_Action action) {
        switch (action) {
            case ADD -> writer.WriteNewRequest(r);
            case DELETE -> writer.DeleteRequest(r);
            case EDIT -> writer.RewriteRequest(r);
        }
    }

}
