package Entity; 

import Database.HDB_System;
import Managers.OfficerManager;
import User.HDB_Officer;
import User.HDB_Manager;
import InteractableAttributePackage.Project as IPProject; 

import java.util.List;

public class ListingManager {

    private HDB_System hdbSystem = HDB_System.getInstance();
    private OfficerManager officerManager = OfficerManager.getInstance();


    public void viewAllProjectListings() {
        hdbSystem.projects.getProjectDB().forEach(project -> System.out.println(project.getProjectDetails()));
    }

   
    public ProjectDetails viewProjectDetails(String projectName) {
        IPProject project = hdbSystem.projects.getProjectDB().stream()
                .filter(p -> p.Details.ProjectName.equals(projectName))
                .findFirst()
                .orElse(null);

        if (project != null) {
            System.out.println(project.getProjectDetails());
            return project.Details;
        } else {
            System.out.println("Error: Project '" + projectName + "' not found.");
            return null;
        }
    }

   
    public void applyJoinProject(HDB_Officer officer, String projectName) {
        officerManager.ApplyJoinProject(officer); 
    }

   
    public void checkProjectApplicationStatus(HDB_Officer officer) {
        officerManager.CheckProjectApplicationStatus(officer); 
    }


    public void viewOfficerRequests(HDB_Manager manager) {
        officerManager.ViewRequests(manager); 
    }


    public void approveOfficerRequest(HDB_Manager manager) {
        officerManager.ApproveRequest(manager); 
    }


    public void denyOfficerRequest(HDB_Manager manager) {
        officerManager.DenyRequest(manager); 
    }

  
    public void viewProjectsWithOpenings() {
        hdbSystem.projects.getProjectDB().stream()
                .filter(project -> project.Details.OfficerList.size() < project.Details.OfficerSlots)
                .forEach(project -> System.out.println(project.Details.ProjectName + " (Open Slots: " + (project.Details.OfficerSlots - project.Details.OfficerList.size()) + ")"));
    }

