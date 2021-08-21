package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.view.ConsoleService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

	private static final String API_BASE_URL = "http://localhost:8080/";

	private static final String MENU_OPTION_EXIT = "Exit";
	private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };

	private AuthenticatedUser currentUser;
	private ConsoleService console;
	private AuthenticationService authenticationService;
    private AccountService accountService;

	private static final String TRANSFER_TYPE_SEND = "Send";
//	private static final String TRANSFER_TYPE_REQUEST = "Request";
	private static final String TRANSFER_STATUS_APPROVED = "Approved";
//	private static final String TRANSFER_STATUS_PENDING = "Pending";
//	private static final String TRANSFER_STATUS_REJECTED = "Rejected";



	public static void main(String[] args) {
		App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new AccountService((API_BASE_URL)));
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
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
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

		Transfers[] pastTransfers = accountService.getTransferHistoryClient(currentUser);
		String transferMenu = String.format("\nTransfer History\n%-10s%-30s%-10s%-7s", "ID", "From/To", "Status",
				"Amount");
		for (Transfers transfer : pastTransfers) {
			// Send/request should print To: person who needs to react
			// If they initiate, print From: person who started the transfer
			String fromTo = (transfer.getAccountIdFrom() == currentUser.getUser().getId()) ?
					"To: " + transfer.getAccountNameTo() : "From: " + transfer.getAccountNameFrom();
			transferMenu = String.format(transferMenu + "\n%-10d%-30s%-10s$%7.2f", transfer.getTransferId(), fromTo,
					transfer.getTransferStatus(), transfer.getAmount());
		}
		transferMenu += "\nPlease enter transfer ID to view details (0 to cancel.) ";
		int choice = 0;
		try {
			choice = console.getUserInputInteger(transferMenu);

		} catch (NumberFormatException ex) {
			System.out.println("*** Invalid transfer ID number ***");
		}
		boolean found = false;
		//if (choice == 0);
		//if(found) {
			for (Transfers transfer : pastTransfers) {
				if (transfer.getTransferId() == choice) {
					System.out.println("\nTransfer Details: ");
					System.out.println("ID: " + transfer.getTransferId());
					System.out.println("From: " + transfer.getAccountNameFrom());
					System.out.println("To: " + transfer.getAccountNameTo());
					System.out.println("Type: " + transfer.getTransferType());
					System.out.println("Status: " + transfer.getTransferStatus());
					System.out.println("Amount: $" + transfer.getAmount());
					found = true;
				}
			}
			if (!found) {
				System.out.println("Choose a valid transfer ID or 0 to cancel");
				System.out.println("Transfer not found.");

			}
		}
	//}


	private void viewPendingRequests() {
			// NOT A CHANCE
	}




	private void sendBucks() {
		// Makes menu
		String userMenu = String.format("\nUsers\n%-10s%-30s", "ID", "Name");
		User[] users = accountService.getAllUsers(currentUser);

		//issue with for each loop
		for (User user : users) {
			userMenu = String.format(userMenu + "\n%-10d%-30s", user.getId(), user.getUsername());
		}

		userMenu += "\n\nEnter ID of user you are sending to (0 to cancel)";

		// Gets user choice
		int sendToChoice = 0;
		try {
			sendToChoice = console.getUserInputInteger(userMenu);
		} catch (NumberFormatException ex) {
			System.out.println("Choose a valid userID number.");
		}
//		if (sendToChoice == 0)
//			return;
		BigDecimal amount = BigDecimal.ZERO;
		try {
			amount = new BigDecimal(console.getUserInput("Enter amount of TEBucks"));
		} catch (NumberFormatException ex) {
			System.out.println("Invalid amount.");
		}

		// If the amount being sent is positive
		// POST transfer object to server
		if (amount.compareTo(BigDecimal.ZERO) > 0) {
			Transfers sendTransfer = new Transfers(TRANSFER_TYPE_SEND, TRANSFER_STATUS_APPROVED, currentUser.getUser().getId(), (long) sendToChoice, amount);
			accountService.sendTransfer(currentUser, sendTransfer);
			viewCurrentBalance();
		}
	}
//		List<AccountUser> userList = new ArrayList<AccountUser>();
//
//		userList = applicationService.listUsers(currentUser.getToken());
//
//		for (int i = 0; i < userList.size(); i++) {
//			System.out.println(userList.get(i));
//		}
//
//		System.out.println("-------------------------------------");
//		System.out.print("Please select the ID of the user you would like to send TE bucks to: ");
//
//
//		Scanner newScanner = new Scanner(System.in);
//		String userInput = newScanner.nextLine();
//
//		int accountTo = 0;
//
//		Transfer transfer = new Transfer();
//		accountTo = Integer.parseInt(userInput);
//		transfer.setTransfer_to(accountTo);
//
//
//		System.out.print("Please enter how much would you like to send: $ ");
//		userInput = newScanner.nextLine();
//		double amount = Double.parseDouble(userInput);
//		transfer.setAmount(amount);
//
//
//		applicationService.makeTransfer(currentUser.getToken(), transfer);
//		System.out.println("Transfer completed!");
//
//	}



	private void requestBucks() {
		//NOT A CHANCE
	}

	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
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
		while (!isRegistered) //will keep looping until user is registered
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				authenticationService.register(credentials);
				isRegistered = true;
				System.out.println("Registration successful. You can now login.");
			} catch(AuthenticationServiceException e) {
				System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
			}
		}
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
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