import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

class Emergency_Entry
{
    String requestID;
    String hospitalID;
    String bloodGroup;
    int unitsRequired;
    String requestDate;
    String priority;
    String status;

    Emergency_Entry(String requestID, String hospitalID, String bloodGroup, int unitsRequired, String requestDate, String priority, String status)
    {
        this.requestID = requestID;
        this.hospitalID = hospitalID;
        this.bloodGroup = bloodGroup;
        this.unitsRequired = unitsRequired;
        this.requestDate = requestDate;
        this.priority = priority;
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "\n-------------------------------" +
                "\nRequest ID      : " + requestID +
                "\nHospital ID     : " + hospitalID +
                "\nBlood Group     : " + bloodGroup +
                "\nUnits Required  : " + unitsRequired +
                "\nRequest Date    : " + requestDate +
                "\nPriority        : " + priority +
                "\nStatus          : " + status +
                "\n-------------------------------";
    }
}

class Emergency
{
    Scanner sc = new Scanner(System.in);
    static PriorityQueueDS emergencyQueue = new PriorityQueueDS();
    DBConnection db = new DBConnection();
    static int nextID = 101;

    void initializeRequestID() throws SQLException, ClassNotFoundException
    {
        nextID = db.getNextRequestID();
    }

    String generateRequestID()
    {
        String requestID = "R" + nextID;
        nextID++;
        return requestID;
    }

    String selectBloodGroup()
    {
        String bloodGroup = "";
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
                case 1: bloodGroup = "A+"; break;
                case 2: bloodGroup = "A-"; break;
                case 3: bloodGroup = "B+"; break;
                case 4: bloodGroup = "B-"; break;
                case 5: bloodGroup = "AB+"; break;
                case 6: bloodGroup = "AB-"; break;
                case 7: bloodGroup = "O+"; break;
                case 8: bloodGroup = "O-"; break;
                default: System.out.println("Invalid Choice.");
            }
        }
        while (ch < 1 || ch > 8);
        return bloodGroup;
    }

    String selectPriority()
    {
        int ch;
        do
        {
            System.out.println("\n=== SELECT PRIORITY ===");
            System.out.println("1. Critical");
            System.out.println("2. High");
            System.out.println("3. Normal");
            System.out.print("Enter Choice: ");

            ch = sc.nextInt();
            sc.nextLine();

            switch (ch)
            {
                case 1: return "Critical";
                case 2: return "High";
                case 3: return "Normal";
                default: System.out.println("Invalid Choice.");
            }
        }
        while (true);
    }

    String enterDate()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while (true)
        {
            System.out.print("Enter Request Date (DD/MM/YYYY): ");
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

    void createRequest(String hospitalID) throws SQLException, ClassNotFoundException
    {
        String bloodGroup = selectBloodGroup();
        int unitsRequired;

        do
        {
            System.out.print("Enter Units Required: ");
            unitsRequired = sc.nextInt();
            sc.nextLine();

            if (unitsRequired <= 0)
            {
                System.out.println("Invalid Units.");
            }
        }
        while (unitsRequired <= 0);

        String requestDate = enterDate();
        String priority = selectPriority();
        String requestID = generateRequestID();

        Emergency_Entry request = new Emergency_Entry(requestID, hospitalID, bloodGroup, unitsRequired, requestDate, priority, "Pending");

        emergencyQueue.enqueue(request);
        db.insertEmergency(request);

        System.out.println("\nEmergency Request Created Successfully.");
        System.out.println(request);
    }

    void loadEmergencyRequest()
            throws SQLException, ClassNotFoundException
    {
        emergencyQueue.clear();

        ResultSet rs = db.getAllEmergencyRequest();

        while (rs != null && rs.next())
        {
            Emergency_Entry request = new Emergency_Entry(
                    rs.getString("RequestID"),
                    rs.getString("HospitalID"),
                    rs.getString("BloodGroup"),
                    rs.getInt("UnitsRequired"),
                    rs.getString("RequestDate"),
                    rs.getString("Priority"),
                    rs.getString("Status")
            );

            emergencyQueue.enqueue(request);
        }
    }

    void viewOwnRequests(String hospitalID)
    {
        PriorityNode temp = emergencyQueue.front;
        boolean found = false;
        System.out.println("\n========== YOUR REQUESTS ==========");

        while (temp != null)
        {
            if (temp.data.hospitalID.equalsIgnoreCase(hospitalID))
            {
                System.out.println(temp.data);
                found = true;
            }
            temp = temp.next;
        }
        if (!found)
            System.out.println("No Requests Found.");
    }

    void viewOwnRequestStatus(String hospitalID)
    {
        String requestID;
        System.out.print("Enter Request ID: ");
        requestID = sc.nextLine();
        Emergency_Entry request = emergencyQueue.search(requestID);

        if (request == null)
        {
            System.out.println("Request Not Found.");
            return;
        }

        if (!request.hospitalID.equalsIgnoreCase(hospitalID))
        {
            System.out.println("You can only view your own requests.");
            return;
        }

        System.out.println("\n========== REQUEST STATUS ==========");
        System.out.println("Request ID : " + request.requestID);
        System.out.println("Priority   : " + request.priority);
        System.out.println("Status     : " + request.status);
    }

    void updateOwnRequest(String hospitalID) throws SQLException, ClassNotFoundException
    {
        System.out.print("Enter Request ID: ");
        String requestID = sc.nextLine();

        Emergency_Entry request = emergencyQueue.search(requestID);

        if (request == null)
        {
            System.out.println("Request Not Found.");
            return;
        }

        if (!request.hospitalID.equalsIgnoreCase(hospitalID))
        {
            System.out.println("You can only update your own requests.");
            return;
        }

        if (!request.status.equalsIgnoreCase("Pending") &&
                !request.status.equalsIgnoreCase("Waiting"))
        {
            System.out.println("Only Pending or Waiting requests can be updated.");
            return;
        }

        System.out.println("\n========== UPDATE REQUEST ==========");
        request.bloodGroup = selectBloodGroup();
        do
        {
            System.out.print("Enter New Units Required: ");
            request.unitsRequired = sc.nextInt();
            sc.nextLine();

            if (request.unitsRequired <= 0)
                System.out.println("Invalid Units.");
        }
        while (request.unitsRequired <= 0);

        request.requestDate = enterDate();
        request.priority = selectPriority();
        request.status = "Pending";

        emergencyQueue.remove(requestID);
        emergencyQueue.enqueue(request);
        db.updateEmergency(request);

        System.out.println("Request Updated Successfully.");
    }

    void displayPendingRequests()
    {
        if (emergencyQueue.isEmpty())
        {
            System.out.println("No Emergency Requests.");
            return;
        }
        emergencyQueue.display();
    }

    void processRequest() throws SQLException, ClassNotFoundException
    {
        if (emergencyQueue.isEmpty())
        {
            System.out.println("No Emergency Requests.");
            return;
        }
        Emergency_Entry request = emergencyQueue.peek();

        System.out.println("\n========== NEXT PRIORITY REQUEST ==========");
        System.out.println(request);
        System.out.println("\n1. Approve");
        System.out.println("2. Reject");
        System.out.println("3. Back");
        System.out.print("Enter Choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice)
        {
            case 1:
            {
                approveRequest(request);
                break;
            }
            case 2:
            {
                rejectRequest(request);
                break;
            }
            case 3:
            {
                break;
            }
            default:
                System.out.println("Invalid Choice.");
        }
    }

    void approveRequest(Emergency_Entry request) throws SQLException, ClassNotFoundException
    {
        int availableUnits = db.getAvailableUnits(request.bloodGroup);

        System.out.println("\nAvailable Units: " + availableUnits);
        System.out.println("Required Units : " + request.unitsRequired);

        if (availableUnits < request.unitsRequired)
        {
            request.status = "Waiting";
            db.updateEmergency(request);
            emergencyQueue.dequeue();
            System.out.println("Insufficient Blood Stock.");
            System.out.println("Request moved to Waiting status.");
            return;
        }

        request.status = "Approved";
        db.updateEmergency(request);
        emergencyQueue.dequeue();
        db.callIssueBlood(request.bloodGroup, request.unitsRequired);
        request.status = "Issued";
        db.updateEmergency(request);

        System.out.println("\nRequest Approved Successfully.");
        System.out.println("Blood Issued Automatically.");
        System.out.println(request);
    }

    void rejectRequest(Emergency_Entry request) throws SQLException, ClassNotFoundException
    {
        request.status = "Rejected";
        db.updateEmergency(request);
        emergencyQueue.dequeue();
        System.out.println("Emergency Request Rejected Successfully.");
    }

    void viewRequest()
    {
        System.out.print("Enter Request ID: ");
        String requestID = sc.nextLine();

        Emergency_Entry request = emergencyQueue.search(requestID);

        if (request == null)
        {
            System.out.println("Request Not Found.");
            return;
        }
        System.out.println(request);
    }

    void updateRequest() throws SQLException, ClassNotFoundException
    {
        System.out.print("Enter Request ID: ");
        String requestID = sc.nextLine();

        Emergency_Entry request = emergencyQueue.search(requestID);

        if (request == null)
        {
            System.out.println("Request Not Found.");
            return;
        }

        if (!request.status.equalsIgnoreCase("Pending") && !request.status.equalsIgnoreCase("Waiting"))
        {
            System.out.println("Only Pending or Waiting requests can be updated.");
            return;
        }

        System.out.print("Enter Hospital ID: ");
        request.hospitalID = sc.nextLine();
        request.bloodGroup = selectBloodGroup();

        do
        {
            System.out.print("Enter Units Required: ");
            request.unitsRequired = sc.nextInt();
            sc.nextLine();
        }
        while (request.unitsRequired <= 0);

        request.requestDate = enterDate();
        request.priority = selectPriority();
        emergencyQueue.remove(requestID);
        emergencyQueue.enqueue(request);

        db.updateEmergency(request);

        System.out.println("Emergency Request Updated Successfully.");
    }

    void approveSpecificRequest()
            throws SQLException, ClassNotFoundException
    {
        System.out.print("Enter Request ID: ");
        String requestID = sc.nextLine();
        Emergency_Entry request = emergencyQueue.search(requestID);

        if (request == null)
        {
            System.out.println("Request Not Found.");
            return;
        }

        if (!request.status.equalsIgnoreCase("Pending") &&
                !request.status.equalsIgnoreCase("Waiting"))
        {
            System.out.println("Request cannot be approved.");
            return;
        }
        approveRequest(request);
    }

    void rejectSpecificRequest()
            throws SQLException, ClassNotFoundException
    {
        System.out.print("Enter Request ID: ");
        String requestID = sc.nextLine();
        Emergency_Entry request = emergencyQueue.search(requestID);

        if (request == null)
        {
            System.out.println("Request Not Found.");
            return;
        }
        if (!request.status.equalsIgnoreCase("Pending") &&
                !request.status.equalsIgnoreCase("Waiting"))
        {
            System.out.println("Request cannot be rejected.");
            return;
        }
        rejectRequest(request);
    }

    void adminEmergencyMenu() throws SQLException, ClassNotFoundException
    {
        int ch;
        loadEmergencyRequest();
        do
        {
            System.out.println("\n========== EMERGENCY MENU ==========");
            System.out.println("1. View Pending Requests");
            System.out.println("2. Process Next Request");
            System.out.println("3. Approve Request");
            System.out.println("4. Reject Request");
            System.out.println("5. View Request Details");
            System.out.println("6. Update Request");
            System.out.println("7. Back");
            System.out.print("Enter Choice: ");

            ch = sc.nextInt();
            sc.nextLine();

            switch (ch)
            {
                case 1:
                {
                    displayPendingRequests();
                    break;
                }
                case 2:
                {
                    processRequest();
                    break;
                }
                case 3:
                {
                    approveSpecificRequest();
                    break;
                }
                case 4:
                {
                    rejectSpecificRequest();
                    break;
                }
                case 5:
                {
                    viewRequest();
                    break;
                }
                case 6:
                {
                    updateRequest();
                    break;
                }
                case 7:
                {
                    System.out.println("Exiting Emergency Menu...");
                    System.out.println("Exited Emergency Menu");
                    break;
                }
                default:
                    System.out.println("Invalid Choice.");
            }
        }
        while (ch != 7);
    }
}