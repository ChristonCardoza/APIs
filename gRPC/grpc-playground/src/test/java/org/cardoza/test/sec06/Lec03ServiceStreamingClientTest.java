package org.cardoza.test.sec06;

import com.google.protobuf.Empty;
import org.cardoza.models.sec06.*;
import org.cardoza.test.common.ResponseObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec03ServiceStreamingClientTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec03ServiceStreamingClientTest.class);


    @Test
    public void blockingClientWithdrawTest() throws InterruptedException {
        var request = WithdrawRequest.newBuilder().setAccountNumber(2).setAmount(20).build();
        var iterator = this.blockingStub.withdraw(request);
        int count = 0;

        while(iterator.hasNext()){
            log.info("received money: {}", iterator.next());
            count ++;
        }

        Assertions.assertEquals(2, count);

    }

    @Test
    public void asyncClientWithdrawTest() {
        var request = WithdrawRequest.newBuilder().setAccountNumber(2).setAmount(20).build();
        var observer = ResponseObserver.<Money>create();
        this.stub.withdraw(request, observer);
        observer.await();

        Assertions.assertEquals(2, observer.getItems().size());
        Assertions.assertEquals(10, observer.getItems().getFirst().getAmount());
        Assertions.assertNull(observer.getThrowable());
    }




}
