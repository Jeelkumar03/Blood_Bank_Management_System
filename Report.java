import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

class Report
{
    void displayDonorReport()
    {
        try
        {
            DBConnection db = new DBConnection();
            Connection con = db.connect();

            FileWriter fw = new FileWriter("E:\\Sem_II Project\\Blood Bank and Donor Management System\\Donor_Report.txt");
            fw.write("========== DONOR REPORT ==========\n\n");

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Donor");
            while (rs.next())
            {
                fw.write("-------------------------------\n");
                fw.write("Donor ID           : " + rs.getString("DonorID") + "\n");
                fw.write("Name               : " + rs.getString("Name") + "\n");
                fw.write("Age                : " + rs.getInt("Age") + "\n");
                fw.write("Gender             : " + rs.getString("Gender") + "\n");
                fw.write("Blood Group        : " + rs.getString("BloodGroup") + "\n");
                fw.write("Mobile             : " + rs.getString("Mobile") + "\n");
                fw.write("City               : " + rs.getString("City") + "\n");
                fw.write("Last Donation Date : " + rs.getString("LastDonationDate") + "\n");
                fw.write("-------------------------------\n\n");
            }
            fw.close();
            db.closeConnection(con);
            System.out.println("Donor Report Generated Successfully.");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    void displayHospitalReport()
    {
        try
        {
            DBConnection db = new DBConnection();
            Connection con = db.connect();

            FileWriter fw = new FileWriter("E:\\Sem_II Project\\Blood Bank and Donor Management System\\Hospital_Report.txt");

            fw.write("========== HOSPITAL REPORT ==========\n\n");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Hospital");

            while (rs.next())
            {
                fw.write("-------------------------------\n");
                fw.write("Hospital ID   : " + rs.getString("HospitalID") + "\n");
                fw.write("Hospital Name : " + rs.getString("HospitalName") + "\n");
                fw.write("City          : " + rs.getString("City") + "\n");
                fw.write("Phone         : " + rs.getString("Phone") + "\n");
                fw.write("Email         : " + rs.getString("Email") + "\n");
                fw.write("-------------------------------\n\n");
            }
            fw.close();
            db.closeConnection(con);

            System.out.println("Hospital Report Generated Successfully.");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    void displayBloodStock()
    {
        try
        {
            DBConnection db = new DBConnection();
            Connection con = db.connect();

            FileWriter fw = new FileWriter("E:\\Sem_II Project\\Blood Bank and Donor Management System\\Blood_Stock_Report.txt");
            fw.write("========== BLOOD STOCK REPORT ==========\n\n");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Blood_Stock");

            while (rs.next())
            {
                fw.write("-------------------------------\n");
                fw.write("Blood Group     : " + rs.getString("BloodGroup") + "\n");
                fw.write("Units Available : " + rs.getInt("UnitsAvailable") + "\n");
                fw.write("Last Updated    : " + rs.getString("LastUpdated") + "\n");
                fw.write("-------------------------------\n\n");
            }
            fw.close();
            db.closeConnection(con);

            System.out.println("Blood Stock Report Generated Successfully.");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    void displayEmergencyReport()
    {
        try
        {
            DBConnection db = new DBConnection();
            Connection con = db.connect();

            FileWriter fw = new FileWriter("E:\\Sem_II Project\\Blood Bank and Donor Management System\\Emergency_Report.txt");
            fw.write("========== EMERGENCY REPORT ==========\n\n");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Emergency_Request");

            while (rs.next())
            {
                fw.write("-------------------------------\n");
                fw.write("Request ID     : " + rs.getString("RequestID") + "\n");
                fw.write("Hospital ID    : " + rs.getString("HospitalID") + "\n");
                fw.write("Blood Group    : " + rs.getString("BloodGroup") + "\n");
                fw.write("Units Required : " + rs.getInt("UnitsRequired") + "\n");
                fw.write("Request Date   : " + rs.getString("RequestDate") + "\n");
                fw.write("Priority       : " + rs.getString("Priority") + "\n");
                fw.write("Status         : " + rs.getString("Status") + "\n");
                fw.write("-------------------------------\n\n");
            }

            fw.close();
            db.closeConnection(con);

            System.out.println("Emergency Report Generated Successfully.");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}