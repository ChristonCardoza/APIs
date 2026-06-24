package org.cardoza.common;

import org.cardoza.sec06.BankService;

public class Demo {

    public static void main(String[] args) {

        GrpcServer.create(new BankService())
                .start()
                .await();
    }
}
