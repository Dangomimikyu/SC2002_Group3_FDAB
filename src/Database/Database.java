//Database is the superclass of all the database classes
//it will be used to store all the data of the system
package Database;

import java.util.ArrayList;

public interface Database<T> {

    public enum DB_Action {
        ADD, DELETE, EDIT
    }

    public ArrayList<T> ViewDB();
    //TODO: ViewDB(filter : IFilter) (needs filter class)
    //changed ModifyDB to take on an object rather than index
    public void ModifyDB(T object, DB_Action action);

}
    

