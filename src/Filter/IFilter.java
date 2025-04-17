//IFilter is the interface for all filter subclasses. Meant to be used with viewDB()
package Filter;

public interface IFilter {

    public enum orderBy {
        ASCENDING,
        DESCENDING
    }
    
    //FilterBy returns a boolean by first recognizing the object 
    //and applying the appropriate checks to see if object passes the filter criterias 
    public boolean FilterBy(Object o);        
}