package com.techelevator.tenmo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountsDao;
import com.techelevator.tenmo.dao.TransfersDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("account")
public class AccountController {

    private AccountsDao accountsDao;
    private UserDao userDao;
    private TransfersDao transfersDao;
//    private static final String STATUS_APPROVED = "Approved";
//    private static final String STATUS_PENDING = "Pending";
//    private static final String STATUS_REJECTED = "Rejected";

    public AccountController(AccountsDao accountsDao, UserDao userDao, TransfersDao transfersDao) {
        this.accountsDao = accountsDao;
        this.userDao = userDao;
        this.transfersDao = transfersDao;
    }
//	@RequestMapping(path = "/{id}/balance", method = RequestMethod.GET)
//	public BigDecimal getBalance(@PathVariable long id) {
//		return accountsDao.viewCurrentBalance(id);
//	}

    // Maybe client should send over User?
    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public Accounts getBalance(Principal principal) {
        String userName = principal.getName();
        long userId = userDao.findIdByUsername(userName);
        Accounts account = accountsDao.findAccountByUserId(userId);
        return account;
    }

    @RequestMapping(path = "/sendbucks", method = RequestMethod.PUT)
    public void sendBucks(@RequestBody Transfer transfer) {
        Accounts me = convertClientInitiatorToServerInitiator(transfer);
        Accounts personOwed = convertClientReactorToServerReactor(transfer);
        if (me.correctMoney(transfer.getAmount())) {
            accountsDao.deductBalance(me, transfer.getAmount());
            accountsDao.creditBalance(personOwed, transfer.getAmount());
            transfersDao.addRowToTransfer(transfer);
        }
    }







    private Accounts convertClientInitiatorToServerInitiator(Transfer transfer) {
        // From the client side, transferFromId and transferToId are both USER IDs,
        // NOT account IDs. Must find accounts by user ID 1st.
        // Then change the Transfer object before writing to database.

        // Accounts of User who initiatied a send/request
        Accounts initiatorAccount = accountsDao.findAccountByUserId(transfer.getAccountFromId());
        transfer.setAccountFromId(initiatorAccount.getAccountId());
        return initiatorAccount;
    }

    private Accounts convertClientReactorToServerReactor(Transfer transfer) {
        // From the client side, transferFromId and transferToId are both USER IDs,
        // NOT account IDs. Must find accounts by user ID 1st.
        // Then change the Transfer object before writing to database.

        // Accounts of User who gets a send/request
        Accounts reactorAccount = accountsDao.findAccountByUserId(transfer.getAccountToId());
        transfer.setAccountToId(reactorAccount.getAccountId());
        return reactorAccount;
    }

    @RequestMapping(path = "/transfers/history", method = RequestMethod.GET)
    public Transfer[] getTransferHistory(Principal principal) {
        User user = userDao.findByUsername(principal.getName());
        Accounts account = accountsDao.findAccountByUserId(user.getId());
        List<Transfer> transferList = transfersDao.getAllTransfers(account);
        for (Transfer serverTransfer : transferList) {
            convertServerTransferToClient(serverTransfer);
        }
        Transfer[] transfers = new Transfer[transferList.size()];
        transfers = transferList.toArray(transfers);
        return transfers;
    }

    /**
     * On the server side, IDs stored in a Transfer object are Accounts IDs.
     * Before passing the Transfer back to the client side, those must be
     * translated back into User IDs.
     */
    private void convertServerTransferToClient(Transfer transfer) {
        User userFrom = userDao.findUserByAccountId(transfer.getAccountFromId());
        User userTo = userDao.findUserByAccountId(transfer.getAccountToId());
        transfer.setAccountFromId(userFrom.getId());
        transfer.setAccountToId(userTo.getId());
    }

    @RequestMapping(path = "/finduser", method = RequestMethod.GET)
    public User[] getAllUsers() {
        List<User> userList = userDao.findAll();
        User[] users = new User[userList.size()];
        users = userList.toArray(users);
        return users;
    }
}
