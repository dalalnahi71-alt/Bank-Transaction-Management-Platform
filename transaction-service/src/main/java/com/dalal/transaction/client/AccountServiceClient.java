package com.dalal.transaction.client;

import com.dalal.transaction.dto.AccountMoneyRequest;
import com.dalal.transaction.dto.AccountTransferRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AccountServiceClient {
    private final RestClient restClient;

    public AccountServiceClient(RestClient.Builder builder,
                                @Value("${services.account.base-url}") String accountServiceUrl) {
        this.restClient = builder.baseUrl(accountServiceUrl).build();
    }

    public void deposit(Long accountId, AccountMoneyRequest request) {
        restClient.post().uri("/accounts/{id}/deposit", accountId).body(request).retrieve().toBodilessEntity();
    }

    public void withdraw(Long accountId, AccountMoneyRequest request) {
        restClient.post().uri("/accounts/{id}/withdraw", accountId).body(request).retrieve().toBodilessEntity();
    }

    public void transfer(AccountTransferRequest request) {
        restClient.post().uri("/accounts/transfer").body(request).retrieve().toBodilessEntity();
    }
}
