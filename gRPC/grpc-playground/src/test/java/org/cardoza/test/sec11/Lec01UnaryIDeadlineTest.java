package org.cardoza.test.sec11;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.grpc.Deadline;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.cardoza.models.sec11.AccountBalance;
import org.cardoza.models.sec11.BalanceCheckRequest;
import org.cardoza.test.common.ResponseObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Lec01UnaryIDeadlineTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec01UnaryIDeadlineTest.class);

    @Test
    public void blockingDeadlineTest(){

        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var request = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(1)
                    .build();

            var response = this.blockingStub
                    .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                    .getAccountBalance(request);
        });

        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED, ex.getStatus().getCode());

    }

    @Test
    public void asyncDeadlineTest(){
        var observer = ResponseObserver.<AccountBalance>create();
        var request = BalanceCheckRequest.newBuilder().setAccountNumber(2).build();

        this.stub
                .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                .getAccountBalance(request, observer);

        observer.await();

        Assertions.assertTrue(observer.getItems().isEmpty());
//        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED, ((StatusRuntimeException) observer.getThrowable()).getStatus().getCode());
        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED, Status.fromThrowable(observer.getThrowable()).getCode());
    }

}
