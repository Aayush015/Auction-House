import java.io.IOException;

public class BankTest {

    public void testOpenAccount() throws IOException {
        Bank bank = new Bank(62952);
        //Thread bankThread = new Thread(bank);
        //bankThread.start();
        AuctionHouse house = new AuctionHouse();
        //house.connectToBank();

//        accountName = "John Doe";
//        double initialBalance = 100.0;
//        String accountNumber = bank.createAccount(accountName, initialBalance);
//
//        // check if account was created
//        if (accountNumber == null) {
//            System.out.println("Failed to open account");
//            return;
//        }
//
//        // check if account details are correct
//        Bank.Account account = bank.getAccount(accountName);
//        if (account == null || !account.getName().equals(accountName) ||
//                account.getBalance() != initialBalance) {
//            System.out.println("Account details are incorrect");
//            return;
//        }
//
//        System.out.println("Account opened successfully");
    }

    public static void main(String[] args) throws Bank.InsufficientFundsException, IOException {
        Bank bank = new Bank(62952);
        //Thread bankThread = new Thread(bank);
        //bankThread.start();
        AuctionHouse house = new AuctionHouse();
        //house.connectToBank();
    }
}
