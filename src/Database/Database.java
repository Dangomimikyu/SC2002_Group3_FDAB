//Database is the superclass of all the database classes
//it will be used to store all the data of the system
package Database;

import java.util.ArrayList;

public interface Database<T> {

    enum DB_Action {
        ADD, DELETE, EDIT
    }

    public ArrayList<T> ViewDB();
    public void ModifyDB(T object, DB_Action action);

}
    

