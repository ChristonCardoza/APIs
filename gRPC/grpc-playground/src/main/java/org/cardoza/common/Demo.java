package org.cardoza.common;

//import org.cardoza.sec06.BankService;
//import org.cardoza.sec06.TransferService;
//import org.cardoza.sec07.FlowControlService;

//import org.cardoza.sec09.BankService;

import org.cardoza.sec10.BankService;

public class Demo {

    public static void main(String[] args) {

//        GrpcServer.create(new BankService(), new TransferService(), new FlowControlService())
//                .start()
//                .await();

        GrpcServer.create(new BankService())
                .start()
                .await();
    }
}
