package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfers;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TransfersDao {

    boolean addRowToTransfer(Transfers transfer);
    List<Transfers> getAllTransfers(Accounts account);
    void updateTransfer(Transfers transfer);

}
