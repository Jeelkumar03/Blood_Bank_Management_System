import java.io.FileWriter;
import java.io.IOException;

class PriorityNode
{
    Emergency_Entry data;
    PriorityNode next;

    PriorityNode(Emergency_Entry data)
    {
        this.data = data;
        this.next = null;
    }
}

class PriorityQueueDS
{
    PriorityNode front;
    PriorityNode rear;

    boolean isEmpty()
    {
        return front == null;
    }

    int getPriority(String priority)
    {
        if (priority.equalsIgnoreCase("Critical"))
            return 1;

        if (priority.equalsIgnoreCase("High"))
            return 2;

        return 3;
    }

    void enqueue(Emergency_Entry data)
    {
        PriorityNode newNode = new PriorityNode(data);

        if (front == null)
        {
            front = rear = newNode;
            return;
        }

        int newPriority = getPriority(data.priority);
        int frontPriority = getPriority(front.data.priority);

        if (newPriority < frontPriority)
        {
            newNode.next = front;
            front = newNode;
            return;
        }

        PriorityNode temp = front;

        while (temp.next != null &&
                getPriority(temp.next.data.priority) <= newPriority)
        {
            temp = temp.next;
        }

        newNode.next = temp.next;
        temp.next = newNode;

        if (newNode.next == null)
            rear = newNode;
    }

    Emergency_Entry dequeue()
    {
        if (isEmpty())
            return null;

        Emergency_Entry data = front.data;
        front = front.next;

        if (front == null)
            rear = null;

        return data;
    }

    Emergency_Entry peek()
    {
        if (isEmpty())
            return null;

        return front.data;
    }

    Emergency_Entry search(String requestID)
    {
        PriorityNode temp = front;

        while (temp != null)
        {
            if (temp.data.requestID.equalsIgnoreCase(requestID))
                return temp.data;

            temp = temp.next;
        }

        return null;
    }

    boolean remove(String requestID)
    {
        if (isEmpty())
            return false;

        if (front.data.requestID.equalsIgnoreCase(requestID))
        {
            dequeue();
            return true;
        }

        PriorityNode previous = front;
        PriorityNode current = front.next;

        while (current != null)
        {
            if (current.data.requestID.equalsIgnoreCase(requestID))
            {
                previous.next = current.next;

                if (current == rear)
                    rear = previous;

                return true;
            }

            previous = current;
            current = current.next;
        }

        return false;
    }

    void display()
    {
        if (isEmpty())
        {
            System.out.println("No Emergency Requests.");
            return;
        }

        PriorityNode temp = front;

        while (temp != null)
        {
            System.out.println(temp.data);
            temp = temp.next;
        }
    }

    int size()
    {
        int count = 0;
        PriorityNode temp = front;

        while (temp != null)
        {
            count++;
            temp = temp.next;
        }

        return count;
    }

    void clear()
    {
        front = null;
        rear = null;
    }

    void writeToFile(FileWriter fw) throws IOException
    {
        PriorityNode temp = front;

        while (temp != null)
        {
            fw.write(temp.data.toString());
            fw.write("\n");
            temp = temp.next;
        }
    }
}