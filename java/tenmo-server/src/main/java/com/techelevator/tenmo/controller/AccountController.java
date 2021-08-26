package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransfersDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("account")
public class AccountController {

    private final AccountDao accountDao;
    private final UserDao userDao;
    private final TransfersDao transfersDao;


    public AccountController(AccountDao accountDao, UserDao userDao, TransfersDao transfersDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transfersDao = transfersDao;
    }

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) {
        String userName = principal.getName();
        long userId = userDao.findIdByUsername(userName);
        Account account = accountDao.findAccountByUserId(userId);
        return account.getAccountBalance();
    }

    @RequestMapping(path = "/sendbucks", method = RequestMethod.PUT)
    public void sendBucks(@RequestBody Transfer transfer) {
        Account accounts = clientToServerInitiator(transfer);
        Account personOwed = clientToServerReactor(transfer);
        if (accounts.correctMoney(transfer.getAmount())) {
            accountDao.deductBalance(accounts, transfer.getAmount());
            accountDao.creditBalance(personOwed, transfer.getAmount());
            transfersDao.addRowToTransfer(transfer);
        }
    }


    private Account clientToServerInitiator(Transfer transfer) {
        Account initiatorAccount = accountDao.findAccountByUserId(transfer.getAccountFromId());
        transfer.setAccountFromId(initiatorAccount.getAccountId());
        return initiatorAccount;
    }

    private Account clientToServerReactor(Transfer transfer) {
        Account reactorAccount = accountDao.findAccountByUserId(transfer.getAccountToId());
        transfer.setAccountToId(reactorAccount.getAccountId());
        return reactorAccount;
    }

    @RequestMapping(path = "/transfers/history", method = RequestMethod.GET)
    public Transfer[] getTransferHistory(Principal principal) {
        User user = userDao.findByUsername(principal.getName());
        Account account = accountDao.findAccountByUserId(user.getId());
        List<Transfer> transferList = transfersDao.getAllTransfers(account);
        for (Transfer serverTransfer : transferList) {
            serverTransferToClient(serverTransfer);
        }
        Transfer[] transfers = new Transfer[transferList.size()];
        transfers = transferList.toArray(transfers);
        return transfers;
    }

    private void serverTransferToClient(Transfer transfer) {
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