//Database is the superclass of all the database classes
//it will be used to store all the data of the system
package Database;

public abstract class Database {

    public enum DB_Action {
        ADD, DELETE, EDIT
    }

    //TODO: ViewDB(filter : IFilter) for filter classes (needs filter class)
    public void ViewDB() {};
    //ModifyDB takes in an index and an action to perform on the database.
    //ModifyDB only takes in index so that database has full control over what gets changed (through viewDB)
    public void ModifyDB(int index, DB_Action action) {};

}
    

