package org.cardoza.sec06.requestHandler;

import io.grpc.stub.StreamObserver;
import org.cardoza.models.sec06.AccountBalance;
import org.cardoza.models.sec06.TransferRequest;
import org.cardoza.models.sec06.TransferResponse;
import org.cardoza.models.sec06.TransferStatus;
import org.cardoza.sec06.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferRequestHandler implements StreamObserver<TransferRequest> {

    private static final Logger log = LoggerFactory.getLogger(TransferRequestHandler.class);
    private final StreamObserver<TransferResponse> responseObserver;

    public TransferRequestHandler(StreamObserver<TransferResponse> responseObserver){
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(TransferRequest transferRequest) {
        var status = this.transfer(transferRequest);
        var response = TransferResponse.newBuilder()
                .setFromAccount(this.toAccountBalance(transferRequest.getFromAccount()))
                .setToAccount(this.toAccountBalance(transferRequest.getToAccount()))
                .setStatus(status)
                .build();

        this.responseObserver.onNext(response);

//        // send the response only if transfer is success
//        if(TransferStatus.COMPLETED.equals(status)){
//            var response = TransferResponse.newBuilder()
//                    .setFromAccount(this.toAccountBalance(transferRequest.getFromAccount()))
//                    .setToAccount(this.toAccountBalance(transferRequest.getToAccount()))
//                    .setStatus(status)
//                    .build();
//
//            this.responseObserver.onNext(response);
//        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.info("client error {}", throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        log.info("transfer request stream completed");
        this.responseObserver.onCompleted();
    }

    private TransferStatus transfer(TransferRequest request){
        var amount = request.getAmount();
        var fromAmount = request.getFromAccount();
        var toAccount = request.getToAccount();
        var status = TransferStatus.REJECTED;

        if(AccountRepository.getBalance(fromAmount) >= amount && (fromAmount != toAccount)){
            AccountRepository.deductAmount(fromAmount, amount);
            AccountRepository.addAmount(toAccount, amount);
            status = TransferStatus.COMPLETED;
        }

        return status;
    }

    private AccountBalance toAccountBalance(int accountNumber) {
        return AccountBalance.newBuilder()
                .setAccountNumber(accountNumber)
                .setBalance(AccountRepository.getBalance(accountNumber))
                .build();
    }
}
