package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransfersDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;

    public JdbcTransfersDao(JdbcTemplate jdbcTemplate, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }


    public List<Transfers> getAllTransfers(Accounts account){
        List<Transfers> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfers AS t INNER JOIN transfer_statuses AS ts ON t.transfer_status_id = ts.transfer_status_id INNER JOIN transfer_types AS tt ON t.transfer_type_id = tt.transfer_type_id WHERE t.account_from = ? OR t.account_to = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account.getAccountId(), account.getAccountId());
        while(results.next()) {
            Transfers transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    private int getTransferTypeId(Transfers transfer) {
        String sqlGetTransferTypeId = "SELECT transfer_type_id FROM transfer_types WHERE transfer_type_desc = ?";
        SqlRowSet transferTypeResult = jdbcTemplate.queryForRowSet(sqlGetTransferTypeId, transfer.getTransferType());
        int transferTypeId = 0;
        if (transferTypeResult.next()) {
            transferTypeId = transferTypeResult.getInt("transfer_type_id");

        }
        return transferTypeId;
    }

    private int getTransferStatusId(Transfers transfer) {
        String sqlGetTransferStatusId = "SELECT transfer_Status_id FROM transfer_statuses WHERE transfer_status_desc = ?";
        SqlRowSet transferStatusResult = jdbcTemplate.queryForRowSet(sqlGetTransferStatusId,
                transfer.getTransferStatus());
        int transferStatusId = 0;
        if (transferStatusResult.next()) {
            transferStatusId = transferStatusResult.getInt("transfer_status_id");

        }
        return transferStatusId;
    }

    // Creates new row in transfers table

    public boolean addRowToTransfer(Transfers transfer) {
        // We successfully retrieve both the status and type IDs
        int statusId = getTransferStatusId(transfer);
        int typeId = getTransferTypeId(transfer);
        if (statusId > 0 && typeId > 0) {
            String sqlAddRowToTransfer = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) "
                    + "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
            SqlRowSet transferIdResult = jdbcTemplate.queryForRowSet(sqlAddRowToTransfer,
                    typeId,
                    statusId,
                    transfer.getAccountIdFrom(),
                    transfer.getAccountIdTo(),
                    transfer.getAmount());
            if (transferIdResult.next()) {
                transfer.setTransferId(transferIdResult.getInt("transfer_id"));
                return true;
            }
        }
        return false;

    }

    private Transfers mapRowToTransfer(SqlRowSet results) {
        Transfers transfer = new Transfers();
        transfer.setAccountIdFrom(results.getInt("account_from"));
        transfer.setAccountIdTo(results.getInt("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTransferStatus(results.getString("transfer_status_desc"));
        transfer.setTransferType(results.getString("transfer_type_desc"));
        transfer.setAccountNameFrom(userDao.findUsernameByAccountId(results.getInt("account_from")));
        transfer.setAccountNameTo(userDao.findUsernameByAccountId(results.getInt("account_to")));
        return transfer;
    }


    public void updateTransfer(Transfers transfer) {
        // Pull the transfer by transfer ID and update its status
        String sql = "UPDATE transfers SET transfer_status_id = "
                + "(SELECT transfer_statuses.transfer_status_id FROM transfer_statuses "
                + "WHERE transfer_statuses.transfer_status_desc = ?) WHERE transfer_id = ?";
        // This should work regardless of whether the transfer is approved or rejected
        jdbcTemplate.update(sql, transfer.getTransferStatus(), transfer.getTransferId());
    }

}
