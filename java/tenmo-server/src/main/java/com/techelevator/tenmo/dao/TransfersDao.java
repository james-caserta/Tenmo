package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Accounts;

import com.techelevator.tenmo.model.Transfer;

public interface TransfersDao {



    boolean addRowToTransfer(Transfer transfer);



    List<Transfer> getAllTransfers(Accounts account);



    void updateTransfer(Transfer transfer);

}