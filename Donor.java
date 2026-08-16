import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

class DonorEntry
{
    String donorID;
    String name;
    int age;
    String gender;
    String bloodGroup;
    String mobile;
    String city;
    String lastDonationDate;

    DonorEntry(String donorID, String name, int age, String gender, String bloodGroup, String mobile, String city, String lastDonationDate)
    {
        this.donorID = donorID;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.mobile = mobile;
        this.city = city;
        this.lastDonationDate = lastDonationDate;
    }

    @Override
    public String toString()
    {
        return "\n-------------------------------" +
                "\nDonor ID           : " + donorID +
                "\nName               : " + name +
                "\nAge                : " + age +
                "\nGender             : " + gender +
                "\nBlood Group        : " + bloodGroup +
                "\nMobile             : " + mobile +
                "\nCity               : " + city +
                "\nLast Donation Date : " + lastDonationDate +
                "\n-------------------------------";
    }
}

class Donor
{
    Scanner sc = new Scanner(System.in);
    static LinkedList<DonorEntry> donorList = new LinkedList<>();
    HashMap<String, DonorEntry> donorMap = new HashMap<>();
    DBConnection db = new DBConnection();
    static int nextID = 101;

    void initializeDonorID() throws SQLException, ClassNotFoundException
    {
        nextID = db.getNextDonorID();
    }

    String donorId()
    {
        String id = "D" + nextID;
        nextID++;
        return id;
    }

    String selectGender()
    {
        int ch;
        do
        {
            System.out.println("\n=== SELECT GENDER ===");
            System.out.println("1. Male");
            System.out.println("2. Female");
            System.out.println("3. Others");
            System.out.println("Enter your choice: ");
            ch = sc.nextInt();
            sc.nextLine();

            switch (ch)
            {
                case 1: return "Male";
                case 2: return "Female";
                case 3: return "Others";
                default: System.out.println("Invalid choice");
            }
        }while(true);
    }

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
            System.out.print("Enter Last Donation Date (DD/MM/YYYY): ");
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

    String enterMobile()
    {
        while (true)
        {
            System.out.print("Enter Contact Number: ");
            String mobile = sc.nextLine();

            boolean valid = mobile.length() == 10;

            for (int i = 0; i < mobile.length() && valid; i++)
            {
                if (!Character.isDigit(mobile.charAt(i)))
                {
                    valid = false;
                }
            }

            if (valid)
            {
                return mobile;
            }
            System.out.println("Invalid Contact Number.");
        }
    }

    void registerDonor() throws SQLException, ClassNotFoundException
    {
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        int age;
        do
        {
            System.out.print("Enter Age: ");
            age = sc.nextInt();
            sc.nextLine();

            if (age <= 0)
            {
                System.out.println("Invalid Age.");
            }
        }
        while (age <= 0);

        String gender = selectGender();

        String bloodGroup = selectBloodGroup();
        String mobile = enterMobile();

        System.out.print("Enter City: ");
        String city = sc.nextLine();
        String lastDonationDate = enterDate();
        String donorID = donorId();

        DonorEntry donor = new DonorEntry(donorID, name, age, gender, bloodGroup, mobile, city, lastDonationDate);
        donorList.add(donor);
        donorMap.put(donorID, donor);
        db.insertDonor(donor);

        System.out.println("\nDonor Registered Successfully.");
        System.out.println("Generated Donor ID: " + donorID);
    }

    void loadDonors() throws SQLException, ClassNotFoundException
    {
        donorList.clear();
        donorMap.clear();
        ResultSet rs = db.getAllDonors();
        while (rs != null && rs.next())
        {
            DonorEntry donor = new DonorEntry(
                    rs.getString("DonorID"),
                    rs.getString("Name"),
                    rs.getInt("Age"),
                    rs.getString("Gender"),
                    rs.getString("BloodGroup"),
                    rs.getString("Mobile"),
                    rs.getString("City"),
                    rs.getString("LastDonationDate"));

            donorList.add(donor);
            donorMap.put(donor.donorID, donor);
        }
    }

    void viewDonor()
    {
        if (donorList.isEmpty())
        {
            System.out.println("No Donor Records Found.");
            return;
        }

        System.out.println("\n========== DONOR LIST ==========");
        for (DonorEntry donor : donorList)
        {
            System.out.println(donor);
        }
    }

    void searchDonor() throws SQLException, ClassNotFoundException {
        System.out.print("Enter Donor Name: ");
        String name1 = sc.nextLine();

        db.searchDonor(name1);
    }

    void updateDonor() throws SQLException, ClassNotFoundException
    {
        System.out.print("Enter Donor ID: ");
        String id = sc.nextLine();
        DonorEntry donor = donorMap.get(id);

        if (donor == null)
        {
            System.out.println("Donor Not Found.");
            return;
        }

        System.out.println("\n========== UPDATE DONOR ==========");
        System.out.println("1. Mobile");
        System.out.println("2. City");
        System.out.println("3. Blood Group");
        System.out.println("4. Last Donation Date");
        System.out.println("5. Back");
        System.out.print("Enter Choice: ");

        int ch = sc.nextInt();
        sc.nextLine();

        switch (ch)
        {
            case 1:
            {
                donor.mobile = enterMobile();
                break;
            }
            case 2:
            {
                System.out.print("Enter New City: ");
                donor.city = sc.nextLine();
                break;
            }
            case 3:
            {
                donor.bloodGroup = selectBloodGroup();
                break;
            }
            case 4:
            {
                donor.lastDonationDate = enterDate();
                break;
            }
            case 5:
            {
                break;
            }
            default:
            {
                System.out.println("Invalid Choice.");
            }
        }

        db.updateDonor(donor);
        System.out.println("Donor Updated Successfully.");
    }

    void deleteDonor() throws SQLException, ClassNotFoundException
    {
        System.out.print("Enter Donor ID: ");
        String id = sc.nextLine();
        DonorEntry donor = donorMap.get(id);

        if (donor == null)
        {
            System.out.println("Donor Not Found.");
            return;
        }

        donorList.remove(donor);
        donorMap.remove(id);
        db.deleteDonor(id);

        System.out.println("Donor Deleted Successfully.");
    }
}