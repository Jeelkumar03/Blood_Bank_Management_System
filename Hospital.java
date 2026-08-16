import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

class Hospital_Entry
{
    String hospitalID;
    String hospitalName;
    String city;
    String phone;
    String email;
    String password;

    Hospital_Entry(String hospitalID, String hospitalName, String city, String phone, String email, String password)
    {
        this.hospitalID = hospitalID;
        this.hospitalName = hospitalName;
        this.city = city;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString()
    {
        return "\n-------------------------------" +
                "\nHospital ID    : " + hospitalID +
                "\nHospital Name  : " + hospitalName +
                "\nCity           : " + city +
                "\nPhone          : " + phone +
                "\nEmail          : " + email +
                "\n-------------------------------";
    }
}

class Hospital
{
    Scanner sc = new Scanner(System.in);
    static LinkedList<Hospital_Entry> hospitalList = new LinkedList<>();
    HashMap<String, Hospital_Entry> hospitalMap = new HashMap<>();
    DBConnection db = new DBConnection();
    static int nextId = 101;

    String loggedInHospitalID = "";

    void initializeHospitalID() throws SQLException, ClassNotFoundException
    {
        nextId = db.getNextHospitalID();
    }

    String hospitalId()
    {
        String hospitalID = "H" + nextId;
        nextId++;
        return hospitalID;
    }

    String enterCity()
    {
        while (true)
        {
            System.out.print("Enter City: ");
            String city = sc.nextLine();

            boolean valid = true;

            for (int i = 0; i < city.length(); i++)
            {
                if (Character.isDigit(city.charAt(i)))
                {
                    valid = false;
                    break;
                }
            }

            if (valid && !city.trim().isEmpty())
            {
                return city;
            }

            System.out.println("City cannot contain numbers.");
        }
    }

    String enterPhone()
    {
        while (true)
        {
            System.out.print("Enter Phone Number: ");
            String phone = sc.nextLine();

            boolean valid = phone.length() == 10 && phone.contains("1") && phone.contains("2") && phone.contains("3") && phone.contains("4") && phone.contains("5");

            for (int i = 0; i < phone.length() && valid; i++)
            {
                if (!Character.isDigit(phone.charAt(i)))
                {
                    valid = false;
                }
            }

            if (valid)
            {
                return phone;
            }

            System.out.println("Invalid Phone Number.");
        }
    }

    String enterEmail()
    {
        while (true)
        {
            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            if (email.contains("@") && email.contains("."))
                return email;

            System.out.println("Invalid Email.");
        }
    }

    void hospitalSignup() throws SQLException, ClassNotFoundException
    {
        System.out.println("\n========== HOSPITAL SIGN UP ==========");
        String email = enterEmail();

        if (db.checkHospitalEmail(email))
        {
            System.out.println("Email Already Registered.");
            return;
        }

        System.out.print("Create Password: ");
        String password = sc.nextLine();
        System.out.print("Enter Hospital Name: ");
        String hospitalName = sc.nextLine();
        String city = enterCity();
        String phone = enterPhone();
        String hospitalID = hospitalId();

        Hospital_Entry hospital = new Hospital_Entry(hospitalID, hospitalName, city, phone, email, password);
        hospitalList.add(hospital);
        hospitalMap.put(hospitalID, hospital);

        db.insertHospital(hospital);

        System.out.println("\nHospital Registration Successful.");
        System.out.println("Your Hospital ID: " + hospitalID);
        System.out.println("You can now Login using Email and Password.");
    }

    boolean hospitalLogin() throws SQLException, ClassNotFoundException
    {
        while (true)
        {
            System.out.println("\n========== HOSPITAL LOGIN ==========");
            System.out.print("Enter Email: ");
            String email = sc.nextLine();
            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            Hospital_Entry hospital = db.getHospitalLogin(email, password);

            if (hospital != null)
            {
                loggedInHospitalID = hospital.hospitalID;
                hospitalList.clear();
                hospitalMap.clear();
                hospitalList.add(hospital);
                hospitalMap.put(hospital.hospitalID, hospital);

                System.out.println("\nLogin Successful.");
                System.out.println("Hospital ID: " + hospital.hospitalID);

                return true;
            }
            System.out.println("Invalid Email or Password.");
        }
    }

    void hospitalAuthentication() throws SQLException, ClassNotFoundException
    {
        int ch;
        do
        {
            System.out.println("\n========== HOSPITAL ACCESS ==========");
            System.out.println("1. New Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Back");
            System.out.print("Enter Choice: ");
            ch = sc.nextInt();
            sc.nextLine();

            switch (ch)
            {
                case 1:
                {
                    hospitalSignup();
                    break;
                }
                case 2:
                {
                    if (hospitalLogin())
                    {
                        hospitalMenu();
                    }
                    break;
                }
                case 3:
                {
                    System.out.println("Backing to Login Menu...");
                    break;
                }
                default:
                {
                    System.out.println("Invalid Choice.");
                }
            }
        }
        while (ch != 3);
    }

    void hospitalDetails()
    {
        Hospital_Entry hospital = hospitalMap.get(loggedInHospitalID);

        if (hospital == null)
        {
            System.out.println("Hospital Details Not Found.");
            return;
        }

        System.out.println("\n========== HOSPITAL DETAILS ==========");
        System.out.println(hospital);
    }

    void updateHospitalDetails() throws SQLException, ClassNotFoundException
    {
        Hospital_Entry hospital = hospitalMap.get(loggedInHospitalID);

        if (hospital == null)
        {
            System.out.println("Hospital Details Not Found.");
            return;
        }

        System.out.println("\n========== UPDATE HOSPITAL DETAILS ==========");
        System.out.println("1. Hospital Name");
        System.out.println("2. City");
        System.out.println("3. Phone");
        System.out.println("4. Email");
        System.out.println("5. Password");
        System.out.println("6. Back");
        System.out.print("Enter Choice: ");
        int ch = sc.nextInt();
        sc.nextLine();

        switch (ch) {
            case 1:
            {
                System.out.print("Enter New Hospital Name: ");
                hospital.hospitalName = sc.nextLine();
                break;
            }
            case 2:
            {
                hospital.city = enterCity();
                break;
            }
            case 3:
            {
                hospital.phone = enterPhone();
                break;
            }
            case 4:
            {
                hospital.email = enterEmail();
                break;
            }
            case 5:
            {
                System.out.print("Enter New Password: ");
                hospital.password = sc.nextLine();
                break;
            }

            case 6:
            {
                System.out.println("Exiting...");
                System.out.println("Exited!");
                break;
            }

            default:
            {
                System.out.println("Invalid Choice.");
                return;
            }
        }

        db.updateHospital(hospital);
        System.out.println("Hospital Details Updated Successfully.");
    }

    void loadHospitals() throws SQLException, ClassNotFoundException
    {
        hospitalList.clear();
        hospitalMap.clear();

        ResultSet rs = db.getAllHospitals();

        while (rs.next())
        {
            Hospital_Entry hospital = new Hospital_Entry(
                    rs.getString("HospitalID"),
                    rs.getString("HospitalName"),
                    rs.getString("City"),
                    rs.getString("Phone"),
                    rs.getString("Email"),
                    rs.getString("Password")
            );

            hospitalList.add(hospital);
            hospitalMap.put(hospital.hospitalID, hospital);
        }
    }

    void displayHospitals()
    {
        if (hospitalList.isEmpty())
        {
            System.out.println("No Hospital Records Found.");
            return;
        }
        System.out.println("\n========== HOSPITAL LIST ==========");

        for (Hospital_Entry hospital : hospitalList)
        {
            System.out.println(hospital);
        }
    }

    void hospitalMenu() throws SQLException, ClassNotFoundException
    {
        int ch;
        Emergency emergency = new Emergency();
        emergency.loadEmergencyRequest();
        do
        {
            System.out.println("\n========== HOSPITAL MANAGEMENT ==========");
            System.out.println("1. Create Emergency Request");
            System.out.println("2. View Own Requests");
            System.out.println("3. Hospital Details");
            System.out.println("4. Update Own Request");
            System.out.println("5. View Request Status");
            System.out.println("6. Update Hospital Details");
            System.out.println("7. Logout");
            System.out.print("Enter Choice: ");

            ch = sc.nextInt();
            sc.nextLine();

            switch (ch)
            {
                case 1:
                {
                    emergency.createRequest(loggedInHospitalID);
                    break;
                }
                case 2:
                {
                    emergency.viewOwnRequests(loggedInHospitalID);
                    break;
                }
                case 3:
                {
                    hospitalDetails();
                    break;
                }
                case 4:
                {
                    emergency.updateOwnRequest(loggedInHospitalID);
                    break;
                }
                case 5:
                {
                    emergency.viewOwnRequestStatus(loggedInHospitalID);
                    break;
                }
                case 6:
                {
                    updateHospitalDetails();
                    break;
                }
                case 7:
                {
                    loggedInHospitalID = "";
                    System.out.println("Hospital Logged Out.");
                    break;
                }
                default:
                {
                    System.out.println("Invalid Choice.");
                }
            }
        } while (ch != 7);
    }
}