package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import com.techelevator.view.ConsoleService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

public class AccountService {

    private final String API_BASE_URL = "http://localhost:8080";
    public static String AUTH_TOKEN = "";
    public RestTemplate restTemplate = new RestTemplate();

    public AccountService(String apiBaseUrl) {
    }


    public BigDecimal getBalance(AuthenticatedUser user) {
        AUTH_TOKEN = user.getToken();
        BigDecimal balance = new BigDecimal("0.00");
        Accounts account = new Accounts();
        try {
            account = restTemplate.exchange(API_BASE_URL + "account/balance", HttpMethod.GET, makeAccountEntity(user), Accounts.class).getBody();
        } catch (RestClientResponseException ex) {
            System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
        }

        return account.getBalance();
    }

    // Send Transfers
    public void sendTransfer(AuthenticatedUser user, Transfers transfers) {
        AUTH_TOKEN = user.getToken();
        try {
            restTemplate.exchange(API_BASE_URL + "account/sendbucks", HttpMethod.PUT, makeTransferEntity(transfers),
                    Transfers.class);
        } catch (RestClientResponseException ex) {
            System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
        }
    }


    // Find Users
    public User[] getAllUsers(AuthenticatedUser user) {
        AUTH_TOKEN = user.getToken();
        User[] users = null;
        try {
            users = restTemplate.exchange(API_BASE_URL + "account/finduser", HttpMethod.GET, makeAuthEntity(), User[].class)
                    .getBody();
        } catch (RestClientResponseException ex) {
            System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
        }
        return users;
    }

    // Transfer History
    public Transfers[] getTransferHistoryClient(AuthenticatedUser user) {
        AUTH_TOKEN = user.getToken();
        Transfers[] transfers = null;
        try {
            transfers = restTemplate.exchange(API_BASE_URL + "account/transfers/history", HttpMethod.GET, makeAuthEntity(),
                    Transfers[].class).getBody();
        } catch (RestClientResponseException ex) {
            System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
        }
        return transfers;
    }



    private HttpEntity<Transfers> makeTransferEntity(Transfers transfers) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity<Transfers> entity = new HttpEntity<>(transfers, headers);
        return entity;
    }

    private HttpEntity<AuthenticatedUser> makeAccountEntity(AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity<AuthenticatedUser> entity = new HttpEntity<>(user, headers);
        return entity;
    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}
