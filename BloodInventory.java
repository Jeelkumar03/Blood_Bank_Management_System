import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

class Blood_Entry
{
    String bloodGroup;
    int unitsAvailable;
    String lastUpdated;

    Blood_Entry(String bloodGroup, int unitsAvailable, String lastUpdated)
    {
        this.bloodGroup = bloodGroup;
        this.unitsAvailable = unitsAvailable;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString()
    {
        return "\n-------------------------------" +
                "\nBlood Group     : " + bloodGroup +
                "\nUnits Available : " + unitsAvailable +
                "\nLast Updated    : " + lastUpdated +
                "\n-------------------------------";
    }
}

class BloodInventory
{
    Scanner sc = new Scanner(System.in);
    static LinkedList<Blood_Entry> blood_InventoryList = new LinkedList<>();
    HashMap<String, Blood_Entry> blood_HashMap = new HashMap<>();
    DBConnection db = new DBConnection();

    String selectBloodGroup()
    {
        int ch;
        do
        {
            System.out.println("\n=== SELECT BLOOD GROUP ===");
            System.out.println("1. A+");
            System.out.println("2. A-");
            System.out.println("3. B+");
            System.out.println("4. B-");
            System.out.println("5. AB+");
            System.out.println("6. AB-");
            System.out.println("7. O+");
            System.out.println("8. O-");
            System.out.print("Enter Choice: ");

            ch = sc.nextInt();
            sc.nextLine();

            switch (ch)
            {
                case 1: return "A+";
                case 2: return "A-";
                case 3: return "B+";
                case 4: return "B-";
                case 5: return "AB+";
                case 6: return "AB-";
                case 7: return "O+";
                case 8: return "O-";
                default:
                    System.out.println("Invalid Choice.");
            }
        }
        while (true);
    }

    String enterDate()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while (true)
        {
            System.out.print("Enter Date (DD/MM/YYYY): ");
            String input = sc.nextLine();

            try
            {
                LocalDate date = LocalDate.parse(input, formatter);
                return date.toString();
            }
            catch (DateTimeParseException e)
            {
                System.out.println("Invalid Date.");
            }
        }
    }

    void updateStock() throws SQLException, ClassNotFoundException
    {
        String bloodGroup = selectBloodGroup();
        Blood_Entry blood = blood_HashMap.get(bloodGroup);

        if (blood == null)
        {
            System.out.println("Blood Group Not Found.");
            return;
        }

        int units;
        do
        {
            System.out.print("Enter New Units: ");
            units = sc.nextInt();
            sc.nextLine();

            if (units <= 0)
                System.out.println("Invalid Units.");
        }
        while (units <= 0);

        blood.unitsAvailable = units;
        blood.lastUpdated = enterDate();
        db.updateBlood(blood);

        System.out.println("Stock Updated Successfully.");
    }

    void loadBloodStock() throws SQLException, ClassNotFoundException
    {
        blood_InventoryList.clear();
        blood_HashMap.clear();
        ResultSet rs = db.getAllBloodStock();

        while (rs != null && rs.next())
        {
            Blood_Entry blood =
                    new Blood_Entry(
                            rs.getString("BloodGroup"),
                            rs.getInt("UnitsAvailable"),
                            rs.getString("LastUpdated")
                    );

            blood_InventoryList.add(blood);
            blood_HashMap.put(blood.bloodGroup, blood);
        }
    }

    void searchBloodGroup()
    {
        String bloodGroup = selectBloodGroup();
        Blood_Entry blood = blood_HashMap.get(bloodGroup);

        if (blood != null)
        {
            System.out.println(blood);
        }
        else
        {
            System.out.println("Blood Group Not Found.");
        }
    }

    void viewStock()
    {
        if (blood_InventoryList.isEmpty())
        {
            System.out.println("Blood Inventory List is Empty.");
            return;
        }

        System.out.println("\n========== AVAILABLE BLOOD STOCK ==========");

        for (Blood_Entry blood : blood_InventoryList)
        {
            System.out.println(blood);
        }
    }
}