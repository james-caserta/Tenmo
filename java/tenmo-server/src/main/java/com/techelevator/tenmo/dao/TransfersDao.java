package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Account;

import com.techelevator.tenmo.model.Transfer;

public interface TransfersDao {



    boolean addRowToTransfer(Transfer transfer);



    List<Transfer> getAllTransfers(Account account);



    void updateTransfer(Transfer transfer);

}