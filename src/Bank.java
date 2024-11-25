import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Bank{
    private static Bank bank = new Bank(8000);

    // instance variables
    private Map<String, Account> accounts;

    //possible list for auctionHouses class
    private Map<String, String> auctionHouses;
    private List<Agent> agents;
    private int port;
    private boolean running;

    // constructor
    public Bank(int port) {
        this.port = port;
        this.accounts = new HashMap<>();
        this.auctionHouses = new HashMap<>();
        this.agents = new ArrayList<>();
        this.running = false;
    }
    public static void main(String[] args) throws IOException {
        bank.run();
    }
    // methods

    /**
     * creates an account with an initial balance
     * @param initialBalance
     * @return String account name to send to client
     */
    public String createAccount(double initialBalance) {
        // creates a new account and adds it to the accounts map
        Random rand = new Random();
        String accountNumber = String.valueOf(rand.nextInt(0, 10000));
        while(accounts.containsKey(accountNumber)){
            accountNumber = String.valueOf(rand.nextInt());
        }
            Account account = new Account(accountNumber, initialBalance);
            accounts.put(accountNumber, account);
        // returns the account name
        return account.getAccountNumber();
    }

    //this class adds an auction house to the bank
    public void addAuctionHouse(String auctionName, String auctionAddress) {
        // adds the auction house to the auctionHouses list
        auctionHouses.put(auctionName,auctionAddress);
        System.out.println("added auction house");
    }

    //getter for auctionHouses
    public Map<String, String> getAuctionHouses() {
        // returns the list of auction houses
        return auctionHouses;
    }

    public boolean transfer(String fromAccountName, String toAccountName, double amount) throws InsufficientFundsException {
        // transfers the specified amount from one account to another
        accounts.get(toAccountName).deposit(accounts.get(fromAccountName).withdraw(amount));
        // returns true if successful, false otherwise
        return true;
    }

    public double getBalance(String accountName) {
        // returns the balance of the specified account
        return accounts.get(accountName).balance;
    }

    public double getAvailableBalance(String accountName) {
        // returns the available balance of the specified account
        return accounts.get(accountName).balance - accounts.get(accountName).blockedFunds;
    }

    public void blockFunds(String accountName, double amount) {
        // blocks the specified amount in the account
        accounts.get(accountName).blockedFunds += amount;
    }

    public void unblockFunds(String accountName, double amount) {
        // unblocks the specified amount in the account
        accounts.get(accountName).blockedFunds -= amount;
    }

    public Account getAccount(String accountName) {
        for (Map.Entry<String, Account> entry : accounts.entrySet()) {
            String name = entry.getKey();
            if (name.equals(accountName)) {
                return entry.getValue();
            }
        }
        return null;
    }
    public void run() {
        try(ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getLocalHost())) {
            System.out.println("Bank server socket created: " + serverSocket.toString());
            running = true;
            while (running) {
                Socket clientSocket = serverSocket.accept();
                Thread thread = new Thread(new BankWorker(clientSocket));
                thread.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private class BankWorker implements Runnable {

        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public BankWorker(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                this.in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
                this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            String messageIn;
            String accountNumber = "";
            try {
                // read the incoming message from the client
                messageIn = in.readLine();
                while (!messageIn.equals("END")) {
                    // process the message and send a response
                    System.out.println("the message to bank: " + messageIn);

                    switch (messageIn) {
                        case "CREATE_ACCOUNT" -> {
                            accountNumber = bank.createAccount(10000);
                            out.println(accountNumber);
                        }
                        case "GET_AUCTIONS" -> {
                            out.println(auctionHouses.values());
                            System.out.println("sent auctions: " + auctionHouses.values());
                        }
                        case "CREATE_AUCTION" -> {
                            String auction = bank.createAccount(1000);
                            out.println(auction);
                            String address = in.readLine();
                            addAuctionHouse(auction, address);
                            out.println(address);
                        }
                        case "BLOCK_FUNDS" -> {
                            accountNumber = in.readLine();
                            double amount = Double.parseDouble(in.readLine());
                            blockFunds(accountNumber, amount);
                            out.println("FUNDS_BLOCKED");
                        }
                        case "UNBLOCK_FUNDS" -> {
                            accountNumber = in.readLine();
                            double amount = Double.parseDouble(in.readLine());
                            unblockFunds(accountNumber, amount);
                            out.println("FUNDS_UNBLOCKED");
                        }
                    }
                    messageIn = in.readLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public class Account {
        private String accountNumber;
        private double balance;
        private double blockedFunds;


        public Account(String name, double balance) {
            this.accountNumber = name;
            this.balance = balance;
            this.blockedFunds = 0;
        }

        public double getBalance() {
            return balance;
        }

        public void deposit(double amount) {
            balance += amount;
            System.out.println("deposited " + amount);
        }

        public double withdraw(double amount) throws InsufficientFundsException {
            if (amount > balance) {
                throw new InsufficientFundsException();
            }
            balance -= amount;
            System.out.println("withdrew " + amount);
            return amount;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

    }
    public class InsufficientFundsException extends Exception {
        public InsufficientFundsException() {
            super("Insufficient funds");
        }
    }
}

