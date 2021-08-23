package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    private final AccountsDao accountsDao;
    private final UserDao userDao;
    private final TransfersDao transfersDao;


    public AccountController(AccountsDao accountsDao, UserDao userDao, TransfersDao transfersDao) {
        this.accountsDao = accountsDao;
        this.userDao = userDao;
        this.transfersDao = transfersDao;
    }

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public Accounts getBalance(Principal principal) {
        String userName = principal.getName();
        long userId = userDao.findIdByUsername(userName);
        Accounts account = accountsDao.findAccountByUserId(userId);
        return account;
    }

    @RequestMapping(path = "/sendbucks", method = RequestMethod.PUT)
    public void sendBucks(@RequestBody Transfer transfer) {
        Accounts accounts = convertClientInitiatorToServerInitiator(transfer);
        Accounts personOwed = convertClientReactorToServerReactor(transfer);
        if (accounts.correctMoney(transfer.getAmount())) {
            accountsDao.deductBalance(accounts, transfer.getAmount());
            accountsDao.creditBalance(personOwed, transfer.getAmount());
            transfersDao.addRowToTransfer(transfer);
        }
    }


    private Accounts convertClientInitiatorToServerInitiator(Transfer transfer) {
        Accounts initiatorAccount = accountsDao.findAccountByUserId(transfer.getAccountFromId());
        transfer.setAccountFromId(initiatorAccount.getAccountId());
        return initiatorAccount;
    }

    private Accounts convertClientReactorToServerReactor(Transfer transfer) {
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