package UserView;

import Entity.Enquiry;
import Entity.Project;

import java.util.ArrayList;
import java.util.Scanner;

public class Menu
{
    protected static Scanner sc = new Scanner(System.in);

    public Menu()
    {
        sc = new Scanner(System.in);
    }

    public static void start()
    {

    }

    private static void Display()
    {

    }

    // chose to separate because they don't share a super class
    protected static void PrintProjects(ArrayList<Project> list)
    {
        for (Project p : list)
        {
            System.out.println(p.getProjectDetails());
        }
    }

    protected static void PrintEnquiries(ArrayList<Enquiry> list)
    {
        for (Enquiry e : list)
        {
            System.out.println(e.getEnquiryDetails());
        }
    }
}
