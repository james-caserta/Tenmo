package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;


import com.techelevator.tenmo.dao.AccountsDao;
import com.techelevator.tenmo.dao.TransfersDao;
import com.techelevator.tenmo.model.Transfers;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.User;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("account")
public class AccountController {


    private final AccountsDao accountsDao;
    private final UserDao userDao;
    private final TransfersDao transfersDao;


    public AccountController(AccountsDao accountsDao, UserDao userDao, TransfersDao transfersDao) {
        this.accountsDao = accountsDao;
        this.userDao = userDao;
        this.transfersDao = transfersDao;
    }
	@RequestMapping(path = "/{id}/balance", method = RequestMethod.GET)
	public BigDecimal getBalance(@PathVariable int id) {
		return accountsDao.viewCurrentBalance(id);
	}


    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public Accounts getBalance(Principal principal) {
        String userName = principal.getName();
        int userId = userDao.findIdByUsername(userName);
        Accounts account = accountsDao.findAccountByUserId(userId);
        return account;
    }

    @RequestMapping(path = "/sendbucks", method = RequestMethod.PUT)
    public void sendBucks(@RequestBody Transfers transfer) {
        Accounts accounts = convertClientInitiatorToServerInitiator(transfer);
        Accounts send = convertClientReactorToServerReactor(transfer);
        if (accounts.hasEnoughMoney(transfer.getAmount())) {
            accountsDao.deductBalance(accounts, transfer.getAmount());
            accountsDao.creditBalance(send, transfer.getAmount());
            transfersDao.addRowToTransfer(transfer);
        }
    }

//

    private Accounts convertClientInitiatorToServerInitiator(Transfers transfer) {
        // From the client side, transferIdFrom and transferIdTo are both USER IDs,
        // NOT account IDs. Must find accounts by user ID 1st.
        // Then change the Transfer object before writing to database.

        // Account of User who initiated a send/request
        Accounts initiatorAccount = accountsDao.findAccountByUserId(transfer.getAccountIdFrom());
        transfer.setAccountIdFrom(initiatorAccount.getAccountId());
        return initiatorAccount;
    }

    private Accounts convertClientReactorToServerReactor(Transfers transfer) {
        // From the client side, transferIdFrom and transferIdTo are both USER IDs,
        // NOT account IDs. Must find accounts by user ID 1st.
        // Then change the Transfer object before writing to database.

        // Account of User who gets a send/request
        Accounts reactorAccount = accountsDao.findAccountByUserId(transfer.getAccountIdTo());
        transfer.setAccountIdTo(reactorAccount.getAccountId());
        return reactorAccount;
    }

    @RequestMapping(path = "/transfers/history", method = RequestMethod.GET)
    public Transfers[] getTransferHistory(Principal principal) {
        User user = userDao.findByUsername(principal.getName());
        Accounts account = accountsDao.findAccountByUserId(user.getId());
        List<Transfers> transferList = transfersDao.getAllTransfers(account);
        for (Transfers serverTransfer : transferList) {
            convertServerTransferToClient(serverTransfer);
        }
        Transfers[] transfers = new Transfers[transferList.size()];
        transfers = transferList.toArray(transfers);
        return transfers;
    }

    private void convertServerTransferToClient(Transfers transfer) {
        User userFrom = userDao.findUserByAccountId(transfer.getAccountIdFrom());
        User userTo = userDao.findUserByAccountId(transfer.getAccountIdTo());
        transfer.setAccountIdFrom(userFrom.getId());
        transfer.setAccountIdTo(userTo.getId());
    }

    @RequestMapping(path = "/finduser", method = RequestMethod.GET)
    public User[] getAllUsers() {
        List<User> userList = userDao.findAll();
        User[] users = new User[userList.size()];
        users = userList.toArray(users);
        return users;
    }
}
