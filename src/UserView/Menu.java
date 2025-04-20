package UserView;

import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class Menu
{
    protected static Scanner sc = new Scanner(System.in);

    public Menu() { sc = new Scanner(System.in);}
    public static void start() {}
    private static void Display() {}

    //helper function to get repeated until valid int choice user repeat message
    public static int GetIntInput(String repeat)
    {
        int choice = -1;

        while (choice < 0) {
            try {
                System.out.println(repeat);
                choice = sc.nextInt();
            } catch (final InputMismatchException e) {
                System.out.println("\nError: Invalid character");
                sc.nextLine(); // ignore and move the cursor to next line
            }
        }

        return choice;
    }

    //helper function to get repeated until valid int choice user repeat message (with the exception of -1)
    public static int GetIntInput2(String repeat)
    {
        int choice = -99;

        while (choice < 0) {
            try {
                System.out.println(repeat);
                choice = sc.nextInt();
                if (choice == -1) { return choice; }
            } catch (final InputMismatchException e) {
                System.out.println("\nError: Invalid character");
                sc.nextLine(); // ignore and move the cursor to next line
            }
        }

        return choice;
    }

    //helper function to get repeated until valid double choice user repeat message (with the exception of -1)
    public static double GetDoubleInput(String repeat)
    {
        double choice = -99;

        while (choice < 0) {
            try {
                System.out.println(repeat);
                choice = sc.nextDouble();
                if (choice == -1) { return choice; }
            } catch (final InputMismatchException e) {
                System.out.println("\nError: Invalid character");
                sc.nextLine(); // ignore and move the cursor to next line
            }
        }

        return choice;
    }

}
