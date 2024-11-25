import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.net.Socket;
import java.util.Scanner;

/**
 * represents an auction house that contains a list of Items to be bid on
 *  and bids made on the items
 */
public class AuctionHouse implements Runnable {
    private String accountNumber;
    private int auctionPort = 8000;
    private String auctionName;


    //will hold a list of items up for auction
    private List<Item> items;
    //list of bids in auction house
    private List<Bid> bids;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public AuctionHouse() {
        this.items = new ArrayList<>();
        this.bids = new ArrayList<>();
    }

    // Connect to Bank server
    public void connectToBank(String auctionName, int auctionPort) throws IOException {

        Scanner systemIn = new Scanner(System.in);
        System.out.println("enter bank hostname:");
        String hostname = systemIn.nextLine();
        System.out.println("enter bank port: ");
        int port = systemIn.nextInt();

        clientSocket = new Socket(hostname, port);
        System.out.println("AuctionHouse client: " + clientSocket);
        in =new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        //add three items to auction list
//        InputStream itemLists = AuctionHouse.class.getClassLoader().getResourceAsStream("inputFile.txt");
//        Scanner scanInputFile = new Scanner(itemLists);
        //out.println("AGENT_CONNECTED");
        addItem(Item.ps5);
        addItem(Item.xbox);
        addItem(Item.iPhone);
        // Send a new account request to the Bank server
        out.println("CREATE_AUCTION");
        // Receive response from the Bank server
        String response = in.readLine();
        this.accountNumber = response;
        out.println(auctionName + " : " + this.auctionPort);
        response = in.readLine();
        System.out.println("Received message from Bank server: " + response);
        printItems();
        out.println("END");
    }

    public void closeConnection() throws IOException {
        // Close the BufferedReader, PrintWriter, and Socket objects
        in.close();
        out.close();
        clientSocket.close();
    }

    // Add an item to the auction house
    public void addItem(Item item) {
        this.items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public void placeBid(Item item, Bid bid) {
        if (items.contains(item)) {
            // Check if the bid amount is higher than the current highest bid
            if (bid.amount() > item.getHighestBid()) {
                // Request the bank to block the funds for the bid
                boolean fundsBlocked = requestBankToBlockFunds(bid.amount());

                if (fundsBlocked) {
                    // Unblock the funds for the previous highest bid (if any)
                    unblockFundsForPreviousHighestBid(item);

                    // Set the bid as the new highest bid for the item
                    item.setHighestBid(bid.amount());
                    System.out.println("Bid placed successfully.");
                } else {
                    System.out.println("Insufficient funds in the bank to place the bid.");
                }
            } else {
                System.out.println("Bid amount is not higher than the current highest bid.");
            }
        } else {
            System.out.println("Item not found in the auction house.");
        }
    }



    // Request the bank to block the funds for the bid amount
    private boolean requestBankToBlockFunds(double amount) {
        out.println("BLOCK_FUNDS " + accountNumber + " " + amount);
        String response;
        try {
            response = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return response.equals("FUNDS_BLOCKED");
    }

    // Unblock the funds for the previous highest bid (if any)
    private void unblockFundsForPreviousHighestBid(Item item) {
        double previousHighestBid = item.getHighestBid();
        if (previousHighestBid != 0) {
            out.println("UNBLOCK_FUNDS " + accountNumber + " " + previousHighestBid);
        }
    }

    @Override
    public void run() {
        try(ServerSocket serverSocket = findGoodPort()) {
            System.out.println("Auction server socket created: " + serverSocket.toString());
            this.auctionName = serverSocket.getInetAddress().getHostName();
            this.auctionPort = serverSocket.getLocalPort();
            //connects to bank via console input
            connectToBank(this.auctionName, this.auctionPort);

            Boolean running = true;
            while (running) {
                Socket clientSocket = serverSocket.accept();
                Thread thread = new Thread(new AuctionHouse.AuctionWorker(clientSocket));
                thread.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        printItems();

    }

    private void printItems(){
        for(Item item: items){
            System.out.println(item.name()+"-"+item.description()+"-"+item.startingPrice());
        }
    }


    public class AuctionWorker implements Runnable {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;
        public AuctionWorker(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                this.in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
                this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            int bid = 0;
            try {
                // read the incoming message from the client
                String messageIn = in.readLine();
                while(!messageIn.equals("END")) {
                    // process the message and send a response
                    System.out.println("the message to auctionHouse: " + messageIn);
                    switch (messageIn) {
                        case "AGENT_CONNECTED" ->{
                            for(Item item: items){
                                out.println(item.name()+"-"+item.description()+"-"+item.startingPrice());
                            }
                            messageIn = in.readLine();
                            int itemNumber = Integer.parseInt(messageIn);
                            double bidAmount = Double.parseDouble(in.readLine());
                            Bid currentBid = new Bid(items.get(itemNumber),bidAmount);
                            placeBid(items.get(itemNumber - 1), currentBid);
                            out.println("BID_PLACED");
                        }
                        case "PLACE_BID" -> {
                            out.println("How much would you like to bid?");
                        }
                    }
                    //messageIn = in.readLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * finds an open port for the server.
     * @return Serversocket with open port
     * @throws IOException if no open ports are available
     */
    public ServerSocket findGoodPort() throws IOException {
        int[] ports = new int[1000];
        for(int i = 8000; i < 9000; i++){
            ports[i - 8000] = i;
        }
        for (int port : ports) {
            try {
                return new ServerSocket(port, 50, InetAddress.getLocalHost());
            } catch (IOException ex) {
                continue; // try next port
            }
        }

        // if the program gets here, no port in the range was found
        throw new IOException("no free port found");
    }

    public static void main(String[] args) throws IOException {
        AuctionHouse a = new AuctionHouse();
        a.run();
    }
}
