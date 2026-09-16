package org.cardoza.common;

import org.cardoza.sec06.BankService;
import org.cardoza.sec06.TransferService;
import org.cardoza.sec07.FlowControlService;

public class Demo {

    public static void main(String[] args) {

        GrpcServer.create(new BankService(), new TransferService(), new FlowControlService())
                .start()
                .await();
    }
}
