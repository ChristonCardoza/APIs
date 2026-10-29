package org.cardoza.test.sec11;

import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.Deadline;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.cardoza.common.GrpcServer;
import org.cardoza.models.sec11.AccountBalance;
import org.cardoza.models.sec11.BalanceCheckRequest;
import org.cardoza.models.sec11.BankServiceGrpc;
import org.cardoza.models.sec11.WithdrawRequest;
import org.cardoza.sec11.DeadlineBankService;
import org.cardoza.test.common.AbstractChannelTest;
import org.cardoza.test.common.ResponseObserver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Lec03WaitForReadyTest extends AbstractChannelTest {

    private static final Logger log = LoggerFactory.getLogger(Lec03WaitForReadyTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());
    protected BankServiceGrpc.BankServiceBlockingStub blockingStub;

    @BeforeAll
    public void setup(){
//        this.grpcServer.start();
        Runnable runnable = () -> {
            Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
            this.grpcServer.start();
        };

        Thread.ofVirtual().start(runnable);
        this.blockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }

    @Test
    public void blockingDeadlineTest() {

        var request = WithdrawRequest.newBuilder()
                .setAccountNumber(1)
                .setAmount(50)
                .build();

        var iterator = this.blockingStub
                .withWaitForReady()
                .withdraw(request);

        while (iterator.hasNext()) {
            log.info("{}", iterator.next());
        }

    }

}
