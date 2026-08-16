import java.sql.SQLException;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws SQLException, ClassNotFoundException
    {
        Scanner sc = new Scanner(System.in);
        Admin admin = new Admin();
        Hospital hospital = new Hospital();
        Donor donor = new Donor();
        BloodInventory bloodInventory = new BloodInventory();
        Emergency emergency = new Emergency();
        Report report = new Report();
        donor.loadDonors();
        donor.initializeDonorID();
        hospital.loadHospitals();
        hospital.initializeHospitalID();
        bloodInventory.loadBloodStock();
        emergency.loadEmergencyRequest();
        emergency.initializeRequestID();

        int choice;
        do
        {
            System.out.println("\n======================================");
            System.out.println("   BLOOD BANK & DONOR MANAGEMENT");
            System.out.println("======================================");
            System.out.println("1. Admin Login");
            System.out.println("2. Hospital Login");
            System.out.println("3. Exit");
            System.out.println("======================================");
            System.out.print("Enter Choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice)
            {
                case 1:
                {
                    if (admin.adminLogin())
                    {
                        adminMenu(sc, donor, hospital, bloodInventory, emergency, report);
                    }
                    break;
                }

                case 2:
                {
                    hospital.hospitalAuthentication();
                    break;
                }

                case 3:
                {
                    System.out.println("Exiting Blood Bank Management System...");
                    System.exit(0);
                }
                default:
                {
                    System.out.println("Invalid Choice.");
                }
            }
        } while (choice != 3);
        sc.close();
    }

    static void adminMenu(Scanner sc, Donor donor, Hospital hospital, BloodInventory bloodInventory, Emergency emergency, Report report) throws SQLException, ClassNotFoundException
    {
        int choice;
        do
        {
            System.out.println("\n======================================");
            System.out.println("          ADMIN MAIN MENU");
            System.out.println("======================================");
            System.out.println("1. Donor Management");
            System.out.println("2. Hospital Management");
            System.out.println("3. Blood Inventory");
            System.out.println("4. Emergency Management");
            System.out.println("5. Reports");
            System.out.println("6. Logout");
            System.out.println("======================================");
            System.out.print("Enter Choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice)
            {
                case 1:
                {
                    donorMenu(sc, donor);
                    break;
                }

                case 2:
                {
                    hospitalAdminMenu(sc, hospital);
                    break;
                }
                case 3:
                {
                    bloodMenu(sc, bloodInventory);
                    break;
                }
                case 4:
                {
                    emergency.adminEmergencyMenu();
                    break;
                }

                case 5:
                {
                    reportMenu(sc, report);
                    break;
                }

                case 6:
                {
                    System.out.println("Admin Logged Out.");
                    break;
                }

                default:
                {
                    System.out.println("Invalid Choice.");
                }
            }
        } while (choice != 6);
    }
    static void donorMenu(Scanner sc, Donor donor) throws SQLException, ClassNotFoundException
    {
        int ch;
        donor.loadDonors();

        do
        {
            System.out.println("\n========== DONOR MANAGEMENT ==========");
            System.out.println("1. Add Donor");
            System.out.println("2. Update Donor");
            System.out.println("3. Delete Donor");
            System.out.println("4. Search Donor");
            System.out.println("5. Display Donors");
            System.out.println("6. Back");
            System.out.print("Enter Choice: ");

            ch = sc.nextInt();
            sc.nextLine();

            switch (ch)
            {
                case 1:
                {
                    donor.registerDonor();
                    break;
                }

                case 2:
                {
                    donor.updateDonor();
                    break;

                }
                case 3:
                {
                    donor.deleteDonor();
                    break;
                }

                case 4:
                {
                    donor.searchDonor();
                    break;
                }

                case 5:
                {
                    donor.viewDonor();
                    break;
                }

                case 6:
                {
                    break;
                }

                default:
                    System.out.println("Invalid Choice.");
            }
        } while (ch != 6);
    }

    static void hospitalAdminMenu(Scanner sc, Hospital hospital) throws SQLException, ClassNotFoundException
    {
        int ch;
        hospital.loadHospitals();
        do
        {
            System.out.println("\n========== HOSPITAL MANAGEMENT ==========");
            System.out.println("1. Display Hospitals");
            System.out.println("2. Search Hospital");
            System.out.println("3. Back");
            System.out.print("Enter Choice: ");

            ch = sc.nextInt();
            sc.nextLine();

            switch (ch)
            {
                case 1:
                {
                    hospital.displayHospitals();
                    break;
                }
                case 2:
                {
                    System.out.print("Enter Hospital ID: ");
                    String id = sc.nextLine();

                    Hospital_Entry h = hospital.hospitalMap.get(id);

                    if (h != null) {
                        System.out.println(h);
                    } else {
                        System.out.println("Hospital Not Found.");
                    }
                    break;
                }


                case 3:
                {
                    break;
                }

                default:
                {
                    System.out.println("Invalid Choice.");
                }
            }
        } while (ch != 3);
    }

    static void bloodMenu(Scanner sc, BloodInventory blood) throws SQLException, ClassNotFoundException
    {
        int ch;
        blood.loadBloodStock();
        do
        {
            System.out.println("\n========== BLOOD INVENTORY ==========");
            System.out.println("1. Update Stock");
            System.out.println("2. Search Blood Group");
            System.out.println("3. Display Stock");
            System.out.println("4. Back");
            System.out.print("Enter Choice: ");
            ch = sc.nextInt();
            sc.nextLine();

            switch (ch)
            {
                case 1:
                {
                    blood.updateStock();
                    break;
                }
                case 2:
                {
                    blood.searchBloodGroup();
                    break;
                }
                case 3:
                {
                    blood.viewStock();
                    break;
                }
                case 4:
                {
                    break;
                }
                default:
                {
                    System.out.println("Invalid Choice.");
                }
            }
        } while (ch != 4);
    }

    static void reportMenu(Scanner sc, Report report)
    {
        int ch;
        do
        {
            System.out.println("\n========== REPORTS ==========");
            System.out.println("1. Donor Report");
            System.out.println("2. Hospital Report");
            System.out.println("3. Blood Stock Report");
            System.out.println("4. Emergency Report");
            System.out.println("5. Back");
            System.out.print("Enter Choice: ");
            ch = sc.nextInt();
            sc.nextLine();

            switch (ch)
            {
                case 1:
                {
                    report.displayDonorReport();
                    break;
                }
                case 2:
                {
                    report.displayHospitalReport();
                    break;
                }
                case 3:
                {
                    report.displayBloodStock();
                    break;
                }
                case 4:
                {
                    report.displayEmergencyReport();
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
        } while (ch != 5);
    }
}