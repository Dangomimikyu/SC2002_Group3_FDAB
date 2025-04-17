//Database is the superclass of all the database classes
//it will be used to store all the data of the system
package Database;
import java.util.ArrayList;

public abstract class Database {

    public enum DB_Action {
        ADD, DELETE, EDIT
    }

    //ViewDB without filter as an argument. Since by default, we view by alphabetical order, should we still use it?
    public void ViewDB() {};
    public ArrayList<Object> getDB() {return null;};
    //ModifyDB takes in an index and an action to perform on the database.
    //ModifyDB has two different overloads, one for modifying by index and one for modifying by object.
    //The index method is meant to be used with the ViewDB() method to allow the user to select an index to modify (thus, edit and add are not supported).
    //The object method is meant to be used in the Manager classes to modify the database directly.
    public void ModifyDB(int index, DB_Action action) {};
    public void ModifyDB(Object object, DB_Action action) {};

}
    

