package com.techelevator.tenmo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.view.ConsoleService;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
    private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
    private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN,
            MENU_OPTION_EXIT };
    private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
    private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
    private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
    private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
    private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS,
            MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS,
            MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };

    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService;

    private static final String TRANSFER_TYPE_SEND = "Send";
    private static final String TRANSFER_STATUS_APPROVED = "Approved";



    public static void main(String[] args) {
        App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL),
                new AccountService(API_BASE_URL));
        app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService) {
        this.console = console;
        this.authenticationService = authenticationService;
        this.accountService = accountService;
    }

    public void run() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");

        registerAndLogin();
        mainMenu();
    }

    private void mainMenu() {
        while (true) {
            String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
            if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
                viewCurrentBalance();
            } else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
                viewTransferHistory();
            } else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
                viewPendingRequests();
            } else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
                sendBucks();
            } else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
                requestBucks();
            } else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else {
                // the only other option on the main menu is to exit
                exitProgram();
            }
        }
    }

    private void viewCurrentBalance() {

        BigDecimal balance = accountService.getBalance(currentUser);
        System.out.println("\nYour current balance is: $ " + balance);

    }

    private void viewTransferHistory() {
        Transfer[] pastTransfers = accountService.getTransferHistoryClient(currentUser);
// print transaction
        String transferMenu = String.format("\nTransfer History\n"+"ID     "+"   From/To         "+"              Status"+"     Amount");
        for (Transfer transfer : pastTransfers) {

            String fromTo = (transfer.getAccountFromId() == currentUser.getUser().getId()) ?
                    "To: " + transfer.getAccountToName() : "From: " + transfer.getAccountFromName();
            System.out.print(transferMenu +"\n"+ transfer.getTransferId()+ "      " + fromTo+
                    "                    " + transfer.getTransferStatus()+ "       " + transfer.getAmount());
        }

        transferMenu += "\nEnter transfer ID for details (Cancel: 0)";
        int choice = 0;
        try {
            choice = console.getUserInputInteger(transferMenu);

        } catch (NumberFormatException ex) {
            System.out.println("Invalid transfer ID number");
        }

// print transaction history
        boolean found = false;
        if (choice != 0){
            for (Transfer transfer : pastTransfers) {
                if (transfer.getTransferId() == choice) {
                    System.out.println("\nTransfer Details: ");
                    System.out.println("ID: " + transfer.getTransferId());
                    System.out.println("From: " + transfer.getAccountFromName());
                    System.out.println("To: " + transfer.getAccountToName());
                    System.out.println("Type: " + transfer.getTransferType());
                    System.out.println("Status: " + transfer.getTransferStatus());
                    System.out.println("Amount: $" + transfer.getAmount());
                    found = true;
                }
            }
            if (!found) {

                System.out.println("Please choose a valid transfer ID (Cancel: 0)");
                System.out.println("Transfer not found.");

            }
        }
    }

    private void viewPendingRequests() {
//       not happening
    }


    private void sendBucks() {
// send menu
        String userMenu = String.format("\nUsers\n%-10s%-30s", "ID", "Name");
        User[] users = accountService.getAllUsers(currentUser);
        for (User user : users) {
            userMenu = String.format(userMenu + "\n%-10d%-30s", user.getId(), user.getUsername());
        }
        userMenu += "\n\nEnter ID of user you are sending to (Cancel: 0)";

//user chooses id
        int sendToChoice = 0;
        try {
            sendToChoice = console.getUserInputInteger(userMenu);
        } catch (NumberFormatException ex) {
            System.out.println("Please choose a valid userID number.");
        }
        if (sendToChoice == 0)
            return;
        BigDecimal amount = BigDecimal.ZERO;
        try {
            amount = new BigDecimal(console.getUserInput("Enter amount of TEBucks"));
        } catch (NumberFormatException ex) {
            System.out.println("Invalid money amount.");
        }

// posts to server
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            Transfer sendTransfer = new Transfer(TRANSFER_TYPE_SEND, TRANSFER_STATUS_APPROVED,
                    currentUser.getUser().getId(), (long) sendToChoice, amount);
            accountService.sendTransfer(currentUser, sendTransfer);
            viewCurrentBalance();
        }

    }

    private void requestBucks() {
        // No no no no no noooooope
    }

    private void exitProgram() {
        System.exit(0);
    }

    private void registerAndLogin() {
        while (!isAuthenticated()) {
            String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
            if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
                register();
            } else {
                // the only other option on the login menu is to exit
                exitProgram();
            }
        }
    }

    private boolean isAuthenticated() {
        return currentUser != null;
    }

    private void register() {
        System.out.println("Please register a new user account");
        boolean isRegistered = false;
        while (!isRegistered) // will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
                authenticationService.register(credentials);
                isRegistered = true;
                System.out.println("Registration successful. You can now login.");
            } catch (AuthenticationServiceException e) {
                System.out.println("REGISTRATION ERROR: " + e.getMessage());
                System.out.println("Please attempt to register again.");
            }
        }
    }

    private void login() {
        System.out.println("Please log in");
        currentUser = null;
        while (currentUser == null) // will keep looping until user is logged in
        {
            UserCredentials credentials = collectUserCredentials();
            try {
                currentUser = authenticationService.login(credentials);
            } catch (AuthenticationServiceException e) {
                System.out.println("LOGIN ERROR: " + e.getMessage());
                System.out.println("Please attempt to login again.");
            }
        }
    }

    private UserCredentials collectUserCredentials() {
        String username = console.getUserInput("Username");
        String password = console.getUserInput("Password");
        return new UserCredentials(username, password);
    }
}