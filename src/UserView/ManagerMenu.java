package UserView;

import User.HDB_Manager;

import java.util.InputMismatchException;

public class ManagerMenu extends Menu
{
    private static HDB_Manager user;
    public static void start()
    {
        Display();
    }
    public static void SetUser(HDB_Manager u)
    {
        user = u;
    }
    private static void Display()
    {
        int choice = -1;
        while (choice != 7) {
            System.out.println("=====================================");
            System.out.println("|            Manager menu           |");
            System.out.println("| 1. Create project listing         |");
            System.out.println("| 2. Edit project listing           |");
            System.out.println("| 3. Delete project listing         |");
            System.out.println("| 4. View other projects            |");
            System.out.println("| 5. Handle officer registration    |");
            System.out.println("| 6. Handle applicant requests      |");
            System.out.println("| 7. Generate report                |");
            System.out.println("| 8. View enquiries                 |");
            System.out.println("| 9. Reply to an enquiry            |");
            System.out.println("=====================================");
            System.out.print("Enter choice: ");
            try {
                choice = sc.nextInt();
            } catch (final InputMismatchException e) {
                System.out.println("Invalid character");
                sc.nextLine(); // ignore and move the cursor to next line
                continue;
            }

            switch (choice)
            {
                case 1: // make new project
                    break;

                case 2: // edit project
                    break;

                case 3: // delete project
                    break;

                case 4: // view other project
                    break;

                case 5: // handle officer registration
                    break;

                case 6: // handle applicant requests (apply and withdrawal)
                    break;

                case 7: // generate report
                    break;

                case 8: // view enquiries
                    break;

                case 9: // reply to enquiry
                    break;

                default:
                    System.out.println("Invalid number");
                    break;

            }
        }
    }
}
