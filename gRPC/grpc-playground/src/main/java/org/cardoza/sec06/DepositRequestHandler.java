package org.cardoza.sec06;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.cardoza.models.sec06.AccountBalance;
import org.cardoza.models.sec06.DepositRequest;
import org.cardoza.sec06.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DepositRequestHandler implements StreamObserver<DepositRequest> {

    private static final Logger log = LoggerFactory.getLogger(DepositRequestHandler.class);

    private final StreamObserver<AccountBalance> responseObserver;
    private int accountNumber;

    public DepositRequestHandler(StreamObserver<AccountBalance> responseObserver) {
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(DepositRequest depositRequest) {

        switch(depositRequest.getRequestCase()){
            case ACCOUNT_NUMBER -> this.accountNumber = depositRequest.getAccountNumber();
            case MONEY -> AccountRepository.addAmount(this.accountNumber, depositRequest.getMoney().getAmount());
            case REQUEST_NOT_SET -> log.warn("Received DepositRequest with no field set");
        }

    }

    @Override
    public void onError(Throwable throwable) {
        log.info("Client error {}", throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        Integer balance = AccountRepository.getBalance(this.accountNumber);
        if (balance == null) {
            this.responseObserver.onError(
                Status.NOT_FOUND
                    .withDescription("Account not found: " + this.accountNumber)
                    .asRuntimeException()
            );
            return;
        }
        var accountBalance = AccountBalance.newBuilder()
                .setAccountNumber(this.accountNumber)
                .setBalance(balance)
                .build();

        this.responseObserver.onNext(accountBalance);
        this.responseObserver.onCompleted();
    }
}
