import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLOutput;
import java.util.*;

public class Agent implements Runnable{
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;
    private double currentBalance;

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(double totalBalance) {
        this.totalBalance = totalBalance;
    }

    private double totalBalance;
    private double availableBalance;
    private String accountNumber;
    private Map<String, List<Integer>> auctionHouses;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public Agent() {
        this.username = username;
        this.currentBalance = currentBalance;
        this.auctionHouses = new HashMap<>();
    }

    public static void main(String[] args) throws IOException {
        Agent a = new Agent();
        Scanner systemIn = new Scanner(System.in);
        System.out.println("enter bank hostname:");
        String hostname = systemIn.nextLine();
        System.out.println("enter bank port: ");
        int port = systemIn.nextInt();
        a.connectToBank(hostname, port);
        a.connectToAuctionHouse();
    }


    /**
     * Method to connect to the desired Auction House
     * @throws IOException
     */
    public void connectToAuctionHouse() {
        Scanner sysin = new Scanner(System.in);
        if (!auctionHouses.isEmpty()) {
            System.out.println("Please enter the hostname of the auction house you would like to connect to:");
            String hostname = sysin.nextLine();
            System.out.println("Please enter the port number of the auction house:");
            int portNum = Integer.parseInt(sysin.nextLine());

            try {
                clientSocket = new Socket(hostname, portNum);
                System.out.println("Connected to auction house: " + hostname + ":" + portNum);

                // Send "AGENT_CONNECTED" message to the auction house
                PrintWriter auctionHouseOut = new PrintWriter(clientSocket.getOutputStream(), true);
                auctionHouseOut.println("AGENT_CONNECTED");
                System.out.println("Sent AGENT_CONNECTED message to the auction house.");

                // Get the list of items for sale from the auction house
                BufferedReader auctionHouseIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String itemsForSale = auctionHouseIn.readLine();
                System.out.println("Items for sale:");
                System.out.println("1. " + itemsForSale);
                itemsForSale = auctionHouseIn.readLine();
                System.out.println("2. " + itemsForSale);
                itemsForSale = auctionHouseIn.readLine();
                System.out.println("3. " + itemsForSale);
                System.out.println("which item would you like to bid on?");
                int itemNumber = sysin.nextInt();
                auctionHouseOut.println(itemNumber);
                System.out.println("how much would you like to bid?");
                int bidAmount = sysin.nextInt();
                auctionHouseOut.println(bidAmount);
                String bidSuccessful = in.readLine();
                System.out.println(bidSuccessful);

            } catch (IOException e) {
                System.out.println("Failed to connect to the auction house.");
                e.printStackTrace();
            }
        }
    }







    public void bidOnItem(AuctionHouse auctionHouse, Item item, double amount) {
        // Place a bid on the given item at the given auction house
    }

    public double getBalance() {
        return currentBalance;
    }

    public String getAccountName() {
        return accountNumber;
    }

    public Map<String, List<Integer>> getAuctionHouses() {
        return auctionHouses;
    }



    public void setBalance(double balance) {
        this.currentBalance = balance;
    }

    public void connectToBank(String hostname, int port) throws IOException {
        clientSocket = new Socket(hostname, port);
        in =new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello! What's your name?");
        username = scanner.nextLine();
        System.out.println("Please enter the amount you have " + username);
        totalBalance = Integer.parseInt(scanner.nextLine());

        System.out.println("Total Balance: " + totalBalance);
        // Send a new account request to the Bank server
        out.println("CREATE_ACCOUNT");

        // Receive response from the Bank server
        String response = in.readLine();
        this.accountNumber = response;
        System.out.println(response);

        //get list of available auction houses from bank
        out.println("GET_AUCTIONS");
        String auctionsString;

        //receive and parse list of available auctions
        auctionsString = in.readLine();
        auctionsString = auctionsString.replace("[", "").replace("]", "");
        String[] pairs = auctionsString.split(", ");
        for (String pair : pairs) {
            // Split the pair into its hostName and value components
            String[] components = pair.split(": ");
            String hostName = components[0];
            Integer value = Integer.parseInt(components[1]);
            // Check if the hostName already exists in the map
            if (auctionHouses.containsKey(hostName)) {
                // If the hostName exists, add the value to its corresponding List
                auctionHouses.get(hostName).add(value);
            } else {
                // If the hostName does not exist, create a new List and add the value
                List<Integer> auctionHousePortNumber = new ArrayList<>();
                auctionHousePortNumber.add(value);
                auctionHouses.put(hostName, auctionHousePortNumber);
            }
        }
        System.out.println("auctions available: ");
        printAuctions();
        out.println("END");
    }

    public void printAuctions(){
        int i = 1;
        for (Map.Entry<String, List<Integer>> entry : auctionHouses.entrySet()) {
            String key = entry.getKey();
            List<Integer> values = entry.getValue();
            for (Integer value : values) {
                System.out.println(i + ". Host Name: " + key + " Port: " + value);
                i++;
            }
        }
    }

    @Override
    public void run() {
        Agent a = new Agent();
        Scanner systemIn = new Scanner(System.in);
        System.out.println("enter bank hostname:");
        String hostname = systemIn.nextLine();
        System.out.println("enter bank port: ");
        int port = systemIn.nextInt();
        try {
            a.connectToBank(hostname, port);
        } catch (SocketException | EOFException e){
            System.out.println("Connection Closed");
            System.exit(0);
        }catch (IOException e){
            e.printStackTrace();
        }

        a.connectToAuctionHouse();

    }

    //TODO: Modify the worker class for Agent (Make each for bank and auctionHouse)
    public class AgentWorker implements Runnable {
        public AgentWorker (Socket clientSocket) {
        }

        @Override
        public void run() {
            String messageIn;
            int bid = 0;
            try {
                // read the incoming message from the client
                messageIn = in.readLine();
                while(!messageIn.equals("END")) {
                    // process the message and send a response
                    System.out.println("the message to auctionHouse: " + messageIn);
                    switch (messageIn) {
                        case "PLACE_BID" -> {
                            System.out.println("How much would you like to bid?");
                        }
                    }
                    messageIn = in.readLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}

