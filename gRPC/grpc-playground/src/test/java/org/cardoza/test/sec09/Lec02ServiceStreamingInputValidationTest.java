package org.cardoza.test.sec09;


import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.cardoza.models.sec09.AccountBalance;
import org.cardoza.models.sec09.BalanceCheckRequest;
import org.cardoza.models.sec09.Money;
import org.cardoza.models.sec09.WithdrawRequest;
import org.cardoza.test.common.ResponseObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

public class Lec02ServiceStreamingInputValidationTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec02ServiceStreamingInputValidationTest.class);


    @ParameterizedTest
    @MethodSource("testData")
    public void blockingInputValidationTest(WithdrawRequest request, Status.Code code){

        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var response = this.blockingStub.withdraw(request).hasNext();
        });

        Assertions.assertEquals(code, ex.getStatus().getCode());
    }

    @ParameterizedTest
    @MethodSource("testData")
    public void asyncInputValidationTest(WithdrawRequest request, Status.Code code) {

        var observer = ResponseObserver.<Money>create();
        this.stub.withdraw(request, observer);
        observer.await();

        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertEquals(code, ((StatusRuntimeException) observer.getThrowable()).getStatus().getCode());
    }

    private Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(11).setAmount(10).build(), Status.Code.INVALID_ARGUMENT),
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(17).build(), Status.Code.INVALID_ARGUMENT),
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(110).build(), Status.Code.FAILED_PRECONDITION)

        );
    }




}
