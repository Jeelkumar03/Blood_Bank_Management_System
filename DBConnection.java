import java.sql.*;

class DBConnection
{
    Connection connect() throws ClassNotFoundException, SQLException
    {
        //Step 1: Load and Register Driver
        String dn = "com.mysql.cj.jdbc.Driver";
        Class.forName(dn);

        //Step 2: Create Connection
        String url = "jdbc:mysql://localhost:3306/blood_bank";
        String user = "root";
        String password = "";
        Connection con = DriverManager.getConnection(url, user, password);

        return con;
    }

    void closeConnection(Connection con)
    {
        try
        {
            if (con != null)
                con.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    boolean checkAdmin(String username, String password) throws SQLException, ClassNotFoundException
    {
        Connection con = connect();
        try
        {
            String sql = "SELECT * FROM Admin WHERE Username=? AND Password=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
        finally
        {
            closeConnection(con);
        }
    }

    boolean checkHospitalEmail(String email) throws SQLException, ClassNotFoundException
    {
        Connection con = connect();
        try
        {
            String sql = "SELECT HospitalID FROM Hospital WHERE Email=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
        finally
        {
            closeConnection(con);
        }
    }

    Hospital_Entry getHospitalLogin(String email, String password) throws SQLException, ClassNotFoundException
    {
        Connection con = connect();
        try
        {
            String sql = "SELECT * FROM Hospital WHERE Email=? AND Password=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                return new Hospital_Entry(
                        rs.getString("HospitalID"),
                        rs.getString("HospitalName"),
                        rs.getString("City"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Password")
                );
            }
            return null;
        }
        finally
        {
            closeConnection(con);
        }
    }

    void insertHospital(Hospital_Entry hospital) throws SQLException, ClassNotFoundException
    {
        Connection con = connect();
        try
        {
            String sql = "INSERT INTO Hospital " + "(HospitalID,HospitalName,City,Phone,Email,Password) " + "VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, hospital.hospitalID);
            ps.setString(2, hospital.hospitalName);
            ps.setString(3, hospital.city);
            ps.setString(4, hospital.phone);
            ps.setString(5, hospital.email);
            ps.setString(6, hospital.password);
            ps.executeUpdate();
            System.out.println("Hospital Saved to Database.");
        }
        finally
        {
            closeConnection(con);
        }
    }

    int getNextHospitalID() throws SQLException, ClassNotFoundException
    {
        Connection con = connect();
        try
        {
            String sql = "SELECT MAX(CAST(SUBSTRING(HospitalID,2) AS UNSIGNED)) " + "AS MaxID FROM Hospital";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next())
            {
                int maxID = rs.getInt("MaxID");
                if (rs.wasNull())
                    return 101;
                return maxID + 1;
            }
            return 101;
        }
        finally
        {
            closeConnection(con);
        }
    }

    void updateHospital(Hospital_Entry hospital)
            throws SQLException, ClassNotFoundException
    {
        Connection con = connect();

        try
        {
            String sql = "UPDATE Hospital SET HospitalName=?,City=?,Phone=?," + "Email=?,Password=? WHERE HospitalID=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, hospital.hospitalName);
            ps.setString(2, hospital.city);
            ps.setString(3, hospital.phone);
            ps.setString(4, hospital.email);
            ps.setString(5, hospital.password);
            ps.setString(6, hospital.hospitalID);
            ps.executeUpdate();
        }
        finally
        {
            closeConnection(con);
        }
    }

    ResultSet getAllHospitals() throws SQLException, ClassNotFoundException
    {
        Connection con = connect();
        Statement st = con.createStatement();
        return st.executeQuery("SELECT * FROM Hospital");
    }

    void insertDonor(DonorEntry donor) throws SQLException, ClassNotFoundException
    {
        Connection con = connect();
        try
        {
            String sql = "INSERT INTO Donor VALUES(?,?,?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, donor.donorID);
            ps.setString(2, donor.name);
            ps.setInt(3, donor.age);
            ps.setString(4, donor.gender);
            ps.setString(5, donor.bloodGroup);
            ps.setString(6, donor.mobile);
            ps.setString(7, donor.city);
            ps.setString(8, donor.lastDonationDate);
            ps.executeUpdate();
        }
        finally
        {
            closeConnection(con);
        }
    }

    int getNextDonorID() throws SQLException, ClassNotFoundException
    {
        Connection con = connect();
        try
        {
            String sql = "SELECT MAX(CAST(SUBSTRING(DonorID,2) AS UNSIGNED)) " + "AS MaxID FROM Donor";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next())
            {
                int maxID = rs.getInt("MaxID");

                if (rs.wasNull())
                    return 101;

                return maxID + 1;
            }
            return 101;
        }
        finally
        {
            closeConnection(con);
        }
    }

    void updateDonor(DonorEntry donor) throws SQLException, ClassNotFoundException
    {
        Connection con = connect();
        try
        {
            String sql = "UPDATE Donor SET Name=?,Age=?,Gender=?,BloodGroup=?," + "Mobile=?,City=?,LastDonationDate=? WHERE DonorID=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, donor.name);
            ps.setInt(2, donor.age);
            ps.setString(3, donor.gender);
            ps.setString(4, donor.bloodGroup);
            ps.setString(5, donor.mobile);
            ps.setString(6, donor.city);
            ps.setString(7, donor.lastDonationDate);
            ps.setString(8, donor.donorID);

            ps.executeUpdate();
        }
        finally
        {
            closeConnection(con);
        }
    }

    void deleteDonor(String donorID) throws SQLException, ClassNotFoundException
    {
        Connection con = connect();
        try
        {
            PreparedStatement ps = con.prepareStatement("DELETE FROM Donor WHERE DonorID=?");
            ps.setString(1, donorID);
            ps.executeUpdate();
        }
        finally
        {
            closeConnection(con);
        }
    }

    ResultSet getAllDonors() throws SQLException, ClassNotFoundException
    {
        Connection con = connect();
        Statement st = con.createStatement();
        return st.executeQuery("SELECT * FROM Donor");
    }

    void searchDonor(String name1) throws SQLException, ClassNotFoundException
    {
        String sql = "SELECT * FROM Donor WHERE Name=?";
        Connection con = connect();
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, name1);
        ResultSet rs = pst.executeQuery();
        while (rs.next())
        {
            String name = rs.getString("Name");
            int age = rs.getInt("Age");
            String gender = rs.getString("Gender");
            String bloodGroup = rs.getString("BloodGroup");
            String mobile = rs.getString("Mobile");
            String city = rs.getString("City");
            String lastDonationDate = rs.getString("LastDonationDate");

            System.out.println("Name: " + name);
            System.out.println("Age: " + age);
            System.out.println("Gender: " + gender);
            System.out.println("BloodGroup: " + bloodGroup);
            System.out.println("Mobile: " + mobile);
            System.out.println("City: " + city);
            System.out.println("LastDonationDate: " + lastDonationDate);
        }
    }

    void insertBlood(Blood_Entry blood) throws SQLException, ClassNotFoundException
    {
        Connection con = connect();
        try
        {
            String sql = "INSERT INTO Blood_Stock VALUES(?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, blood.bloodGroup);
            ps.setInt(2, blood.unitsAvailable);
            ps.setString(3, blood.lastUpdated);

            ps.executeUpdate();
        }
        finally
        {
            closeConnection(con);
        }
    }

    void updateBlood(Blood_Entry blood) throws SQLException, ClassNotFoundException
    {
        Connection con = connect();
        try
        {
            String sql = "UPDATE Blood_Stock SET UnitsAvailable=?," + "LastUpdated=? WHERE BloodGroup=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, blood.unitsAvailable);
            ps.setString(2, blood.lastUpdated);
            ps.setString(3, blood.bloodGroup);
            ps.executeUpdate();
        }
        finally
        {
            closeConnection(con);
        }
    }

    ResultSet getAllBloodStock() throws SQLException, ClassNotFoundException
    {
        Connection con = connect();
        Statement st = con.createStatement();
        return st.executeQuery("SELECT * FROM Blood_Stock");
    }

    void insertEmergency(Emergency_Entry emergency) throws SQLException, ClassNotFoundException
    {
        Connection con = connect();
        try
        {
            String sql = "INSERT INTO Emergency_Request " + "(RequestID,HospitalID,BloodGroup,UnitsRequired," + "RequestDate,Priority,Status) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, emergency.requestID);
            ps.setString(2, emergency.hospitalID);
            ps.setString(3, emergency.bloodGroup);
            ps.setInt(4, emergency.unitsRequired);
            ps.setString(5, emergency.requestDate);
            ps.setString(6, emergency.priority);
            ps.setString(7, emergency.status);
            ps.executeUpdate();
            System.out.println("Emergency Request Saved.");
        }
        finally
        {
            closeConnection(con);
        }
    }

    int getNextRequestID() throws SQLException, ClassNotFoundException
    {
        Connection con = connect();

        try
        {
            String sql = "SELECT MAX(CAST(SUBSTRING(RequestID,2) AS UNSIGNED)) " + "AS MaxID FROM Emergency_Request";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next())
            {
                int maxID = rs.getInt("MaxID");

                if (rs.wasNull())
                    return 101;

                return maxID + 1;
            }

            return 101;
        }
        finally
        {
            closeConnection(con);
        }
    }

    void updateEmergency(Emergency_Entry emergency) throws SQLException, ClassNotFoundException
    {
        Connection con = connect();

        try
        {
            String sql = "UPDATE Emergency_Request SET HospitalID=?," + "BloodGroup=?,UnitsRequired=?,RequestDate=?," + "Priority=?,Status=? WHERE RequestID=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, emergency.hospitalID);
            ps.setString(2, emergency.bloodGroup);
            ps.setInt(3, emergency.unitsRequired);
            ps.setString(4, emergency.requestDate);
            ps.setString(5, emergency.priority);
            ps.setString(6, emergency.status);
            ps.setString(7, emergency.requestID);
            ps.executeUpdate();
        }
        finally
        {
            closeConnection(con);
        }
    }

    ResultSet getAllEmergencyRequest() throws SQLException, ClassNotFoundException
    {
        Connection con = connect();

        String sql = "SELECT * FROM Emergency_Request " + "WHERE Status='Pending' OR Status='Waiting' " + "ORDER BY CASE " + "WHEN Priority='Critical' THEN 1 " + "WHEN Priority='High' THEN 2 " + "WHEN Priority='Normal' THEN 3 END, RequestID";
        Statement st = con.createStatement();
        return st.executeQuery(sql);
    }

    int getAvailableUnits(String bloodGroup) throws SQLException, ClassNotFoundException
    {
        Connection con = connect();
        try
        {
            String sql = "SELECT AvailableUnits(?) AS Units";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, bloodGroup);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                return rs.getInt("Units");
            }
            return 0;
        }
        finally
        {
            closeConnection(con);
        }
    }

    void callIssueBlood(String bloodGroup, int units) throws SQLException, ClassNotFoundException
    {
        Connection con = connect();
        try
        {
            CallableStatement cs = con.prepareCall("{CALL IssueBlood(?,?)}");
            cs.setString(1, bloodGroup);
            cs.setInt(2, units);
            cs.execute();
        }
        finally
        {
            closeConnection(con);
        }
    }
}