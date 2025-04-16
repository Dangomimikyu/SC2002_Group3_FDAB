package UserView;

import User.Applicant;

import java.util.InputMismatchException;

public class ApplicantMenu extends Menu
{
    private static Applicant user;

    public static void start()
    {
        Display();
    }

    private static void Display()
    {
        int choice = -1;
        while (choice != 7) {
            System.out.println("============================");
            System.out.println("|      Applicant menu      |");
            System.out.println("| 1. Apply for a project   |");
            System.out.println("| 2. Create an enquiry     |");
            System.out.println("| 3. View your enquiries   |");
            System.out.println("| 4. Edit your enquiries   |");
            System.out.println("| 5. Delete an enquiry     |");
            System.out.println("| 6. Book a flat           |");
            System.out.println("| 7. Log out               |");
            System.out.println("============================");
            System.out.print("Enter choice: ");
            try
            {
                choice = sc.nextInt();
            }
            catch (final InputMismatchException e)
            {
                System.out.println("Invalid character");
                sc.nextLine(); // ignore and move the cursor to next line
                continue;
            }

            switch (choice)
            {
                case 1:
                    break;

                case 2:
                    break;

                case 3:
                    break;

                case 4:
                    break;

                case 5:
                    break;

                case 6:
                    break;

                case 7:
                    break;

                default:
                    System.out.println("Invalid number");
                    break;

            }
        }
    }

    public static void SetUser(Applicant a)
    {
        user = a;
    }
}
