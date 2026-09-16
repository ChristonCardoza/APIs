package org.cardoza.sec06;

import io.grpc.stub.StreamObserver;
import org.cardoza.models.sec06.TransferRequest;
import org.cardoza.models.sec06.TransferResponse;
import org.cardoza.models.sec06.TransferServiceGrpc;
import org.cardoza.sec06.requestHandler.TransferRequestHandler;

public class TransferService extends TransferServiceGrpc.TransferServiceImplBase {

    @Override
    public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver){
        return new TransferRequestHandler(responseObserver);
    }
}
