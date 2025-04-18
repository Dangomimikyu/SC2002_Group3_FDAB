//ProjectListingDB is the database class that will be used to store all the enquiry information within the system
// it will be used to retrieve, modify, add and delete projects to/from the csv files

package Database;

import java.util.ArrayList;

import InteractableAttributePackage.Project;
import InteractableAttributePackage.Request.FlatType;
import Service.*;
import User.SystemUser;
import Filter.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProjectListingDB extends Database {

    private static ProjectListingDB instance;

    private ArrayList<Project> projList = new ArrayList<Project>();
    Reader reader = new Reader();
    Writer writer = new Writer();

    private ProjectListingDB() {}
    public static ProjectListingDB getInstance() {
        if (instance == null) { instance = new ProjectListingDB(); }
        return instance;
    }

    public void InitialiseDB(String ProjectFilePath, ArrayList<SystemUser> userList) {
        projList = reader.readProjects(ProjectFilePath, userList);
    }

    public ArrayList<Project> getProjectDB() {
        return projList;
    }

    public ArrayList<Project> ViewDB() {
        System.out.println("\nAll projects in order by index: ");
        int index = 0;
        for (Project p : projList) {
            System.out.println("================ " + index + " ================");
            System.out.println(p.getProjectDetails());
            index++;
        }
        return null;
    }

    public void ViewDB(IFilter filter) {

        displayFilterInformation(filter);

        if (filter instanceof Filter_Alphabetic) { SortInOrder((Filter_Alphabetic)filter); }
        else if (filter instanceof Filter_SellingPrice) { SortInOrder((Filter_SellingPrice)filter); }
        else if (filter instanceof Filter_FlatType) { SortInOrder((Filter_FlatType)filter); }
        else if (filter instanceof Filter_Age) { SortInOrder((Filter_Age)filter); }

        //for filters with no orderBy attribute
        else {
            int index = 0;
            for (Project p : projList) {
                if (filter.FilterBy(p)) {
                    System.out.println("================ " + index + " ================");
                    System.out.println(p.getProjectDetails());
                }
                index++;
                }            
        }
    }

    //modify project by index. Works in conjunction with ViewDB().
    public void ModifyDB(int index, DB_Action action) {
        switch (action) {
            case ADD -> throw new UnsupportedOperationException("Adding Projects not supported with index method");
            case DELETE -> {
                writer.DeleteProject(projList.get(index));
                projList.remove(index);
            }
            case EDIT -> throw new UnsupportedOperationException("Editing Projects not supported with index method");
        }
    }

    //modift project by object. Meant for use in Manager classes. Not meant to use with ViewDB().
    public void ModifyDB(Project project, DB_Action action) {
        switch (action) {
            case ADD -> {
                writer.WriteNewProject(project);
                projList.add(project);
            }
            case DELETE -> {
                writer.DeleteProject(project);
                projList.remove(project);
            }
            case EDIT -> {
                writer.RewriteProject(project);
                projList.set(projList.indexOf(project), project);
            }
        }
    }

    //prints the headers and information for which project will be filtered by
    private void displayFilterInformation(IFilter filter) {
        if (filter instanceof Filter_FlatType) {
            System.out.println("Filter by project's availability of units of flat type: " + ((Filter_FlatType)filter).flatType);
        }
        else if (filter instanceof Filter_Marital) {
            System.out.println("Filter by project's open to user group of marital status: " + ((Filter_Marital)filter).maritalStatus);
        }
        else if (filter instanceof Filter_Visibility) {
            System.out.println("Filter by project if active status/visibility is: " + ((Filter_Visibility)filter).isVisible);
        }
        else if (filter instanceof Filter_Location) {
            System.out.println("Filter by project if is in neighbourhood: " + ((Filter_Location)filter).location);
        }
        else if (filter instanceof Filter_Age) {
            System.out.println("Filter by project if it's age(years) is between: " + 
            ((Filter_Age)filter).minAge + " and " + ((Filter_Age)filter).maxAge + "\nin " + ((Filter_Age)filter).order + " order");
        }
        else if (filter instanceof Filter_Alphabetic) {
            System.out.println("Filter by project name starting with: " + (((Filter_Alphabetic)filter).first_char == null ? "any character" : ((Filter_Alphabetic)filter).first_char)
             + "\nin " + ((Filter_Alphabetic)filter).order + " order");
        }
    }

    //Sorts lexicographyically in specified order
    private void SortInOrder(Filter_Alphabetic filter) {
        ArrayList<Project> SortedProjects = new ArrayList<>(projList);
        SortedProjects.removeIf(p -> !filter.FilterBy(p));
        
        if (filter.order == IFilter.orderBy.ASCENDING) {
            SortedProjects.sort((p1, p2) -> p1.Details.ProjectName.compareToIgnoreCase(p2.Details.ProjectName));
        }
        else if (filter.order == IFilter.orderBy.DESCENDING) {
            SortedProjects.sort((p1, p2) -> p2.Details.ProjectName.compareToIgnoreCase(p1.Details.ProjectName));
        }
        displaySortedWithOriginalIndex(SortedProjects);
    }

    //Sorts selling price in specified order
    private void SortInOrder(Filter_SellingPrice filter) {
        ArrayList<Project> SortedProjects = new ArrayList<>(projList);
        SortedProjects.removeIf(p -> !filter.FilterBy(p));

        if (filter.order == IFilter.orderBy.ASCENDING && filter.flatType == FlatType.TWO_ROOM) {
            SortedProjects.sort((p1, p2) -> Integer.compare(p1.Details.SellingPrice_2Room,p2.Details.SellingPrice_2Room));
        }
        else if (filter.order == IFilter.orderBy.DESCENDING && filter.flatType == FlatType.TWO_ROOM) {
            SortedProjects.sort((p1, p2) -> Integer.compare(p2.Details.SellingPrice_2Room,p1.Details.SellingPrice_2Room));
        }
        if (filter.order == IFilter.orderBy.ASCENDING && filter.flatType == FlatType.THREE_ROOM) {
            SortedProjects.sort((p1, p2) -> Integer.compare(p1.Details.SellingPrice_3Room,p2.Details.SellingPrice_3Room));
        }
        else if (filter.order == IFilter.orderBy.DESCENDING && filter.flatType == FlatType.THREE_ROOM) {
            SortedProjects.sort((p1, p2) -> Integer.compare(p2.Details.SellingPrice_3Room,p1.Details.SellingPrice_3Room));
        }
        if (filter.order == IFilter.orderBy.ASCENDING && filter.flatType == FlatType.NULL) {
            SortedProjects.sort((p1, p2) -> Integer.compare(p1.Details.SellingPrice_2Room + p1.Details.SellingPrice_3Room
                                                         ,p2.Details.SellingPrice_2Room + p2.Details.SellingPrice_3Room));
        }
        else if (filter.order == IFilter.orderBy.DESCENDING && filter.flatType == FlatType.NULL) {
            SortedProjects.sort((p1, p2) -> Integer.compare(p2.Details.SellingPrice_2Room + p2.Details.SellingPrice_3Room
                                                         ,p1.Details.SellingPrice_2Room + p1.Details.SellingPrice_3Room));
        }
        displaySortedWithOriginalIndex(SortedProjects);
    }

    //Sorts selling price in specified order
    private void SortInOrder(Filter_FlatType filter) {
        ArrayList<Project> SortedProjects = new ArrayList<>(projList);
        SortedProjects.removeIf(p -> !filter.FilterBy(p));

        if (filter.order == IFilter.orderBy.ASCENDING && filter.flatType == FlatType.TWO_ROOM) {
            SortedProjects.sort((p1, p2) -> Integer.compare(p1.Details.NoOfUnitsLeft_2Room,p2.Details.NoOfUnitsLeft_2Room));
        }
        else if (filter.order == IFilter.orderBy.DESCENDING && filter.flatType == FlatType.TWO_ROOM) {
            SortedProjects.sort((p1, p2) -> Integer.compare(p2.Details.NoOfUnitsLeft_2Room,p1.Details.NoOfUnitsLeft_2Room));
        }
        if (filter.order == IFilter.orderBy.ASCENDING && filter.flatType == FlatType.THREE_ROOM) {
            SortedProjects.sort((p1, p2) -> Integer.compare(p1.Details.NoOfUnitsLeft_3Room,p2.Details.NoOfUnitsLeft_3Room));
        }
        else if (filter.order == IFilter.orderBy.DESCENDING && filter.flatType == FlatType.THREE_ROOM) {
            SortedProjects.sort((p1, p2) -> Integer.compare(p2.Details.NoOfUnitsLeft_3Room,p1.Details.NoOfUnitsLeft_3Room));
        }
        if (filter.order == IFilter.orderBy.ASCENDING && filter.flatType == FlatType.NULL) {
            SortedProjects.sort((p1, p2) -> Integer.compare(p1.Details.NoOfUnitsLeft_2Room + p1.Details.NoOfUnitsLeft_3Room
                                                         ,p2.Details.NoOfUnitsLeft_2Room + p2.Details.NoOfUnitsLeft_3Room));
        }
        else if (filter.order == IFilter.orderBy.DESCENDING && filter.flatType == FlatType.NULL) {
            SortedProjects.sort((p1, p2) -> Integer.compare(p2.Details.NoOfUnitsLeft_2Room + p2.Details.NoOfUnitsLeft_3Room
                                                         ,p1.Details.NoOfUnitsLeft_2Room + p1.Details.NoOfUnitsLeft_3Room));
        }
        displaySortedWithOriginalIndex(SortedProjects);
    }

    //Sorts age in specified order
    private void SortInOrder(Filter_Age filter) {
        ArrayList<Project> SortedProjects = new ArrayList<>(projList);
        SortedProjects.removeIf(p -> !filter.FilterBy(p));

        if (filter.order == IFilter.orderBy.ASCENDING) {
            SortedProjects.sort((p1, p2) -> Double.compare(ChronoUnit.DAYS.between(p1.Details.OpenDate, LocalDate.now()) / 365.0,
            ChronoUnit.DAYS.between(p2.Details.OpenDate, LocalDate.now()) / 365.0));
        }
        else if (filter.order == IFilter.orderBy.DESCENDING) {
            SortedProjects.sort((p1, p2) -> Double.compare(ChronoUnit.DAYS.between(p2.Details.OpenDate, LocalDate.now()) / 365.0, 
            ChronoUnit.DAYS.between(p1.Details.OpenDate, LocalDate.now()) / 365.0));
        }
        displaySortedWithOriginalIndex(SortedProjects);
    }

    //print the projects in sorted order, as well as its original index in the original list
    //this is to show the user the original index of that project in the original list, so that they can modify it if needed
    private void displaySortedWithOriginalIndex(ArrayList<Project> SortedProjects) {
        for (Project p : SortedProjects) {
            for (int i = 0; i < projList.size(); i++) {
                if (projList.get(i).Details.ProjectName.equals(p.Details.ProjectName)) {
                    System.out.println("================ " + i + " ================");
                    System.out.println(p.getProjectDetails());
                    break;
                }
            }
        }
    }

}