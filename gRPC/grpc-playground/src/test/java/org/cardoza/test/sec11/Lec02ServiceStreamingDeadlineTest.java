package org.cardoza.test.sec11;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.grpc.Deadline;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.cardoza.models.sec11.Money;
import org.cardoza.models.sec11.WithdrawRequest;
import org.cardoza.test.common.ResponseObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Lec02ServiceStreamingDeadlineTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec02ServiceStreamingDeadlineTest.class);

    @Test
    public void blockingDeadlineTest() {

        var ex = Assertions.assertThrows(StatusRuntimeException.class,() -> {

            var request = WithdrawRequest.newBuilder()
                    .setAccountNumber(1)
                    .setAmount(50)
                    .build();

            var iterator = this.blockingStub
                    .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                    .withdraw(request);

            while (iterator.hasNext()) {
                log.info("{}", iterator.next());
            }
        });

        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED, ex.getStatus().getCode());

    }

    @Test
    public void asyncDeadlineTest() {
        var observer = ResponseObserver.<Money>create();
        var request = WithdrawRequest.newBuilder()
                .setAccountNumber(1)
                .setAmount(50)
                .build();

        this.stub
                .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                .withdraw(request, observer);

        observer.await();

        Assertions.assertEquals(2, observer.getItems().size());
        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED, Status.fromThrowable(observer.getThrowable()).getCode());
    }
}
