//IFilter is the interface for all filter subclasses. Meant to be used with viewDB()
package Filter;

public interface IFilter {
    //FilterBy is the method that will be used to filter the database
    //it will return true if the object passed in matches the filter criteria
    //How it works works dynamically based on the type of object passed in and the filter subclass
    public enum orderBy {
        ASCENDING,
        DESCENDING
    }
    
    public boolean FilterBy(Object o);        
}