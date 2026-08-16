import java.sql.SQLException;
import java.util.Scanner;

class Admin
{
    Scanner sc = new Scanner(System.in);
    DBConnection db = new DBConnection();

    boolean adminLogin() throws SQLException, ClassNotFoundException
    {
        while (true)
        {
            System.out.println("\n========== ADMIN LOGIN ==========");
            System.out.print("Enter Username: ");
            String username = sc.nextLine();

            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            if (db.checkAdmin(username, password))
            {
                System.out.println("\nAdmin Login Successful.");
                return true;
            }
            System.out.println("\nInvalid Username or Password.");
        }
    }
}