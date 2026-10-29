package org.cardoza.test.sec10;


import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.cardoza.models.sec10.Money;
import org.cardoza.models.sec10.ValidationCode;
import org.cardoza.models.sec10.WithdrawRequest;
import org.cardoza.test.common.ResponseObserver;
import org.junit.jupiter.api.Assertions;
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
    public void blockingInputValidationTest(WithdrawRequest request, ValidationCode code){

        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var response = this.blockingStub.withdraw(request).hasNext();
        });

        Assertions.assertEquals(code, getValidationCode(ex));
    }

    @ParameterizedTest
    @MethodSource("testData")
    public void asyncInputValidationTest(WithdrawRequest request, ValidationCode code) {

        var observer = ResponseObserver.<Money>create();
        this.stub.withdraw(request, observer);
        observer.await();

        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertEquals(code, getValidationCode(observer.getThrowable()));
    }

    private Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(11).setAmount(10).build(), ValidationCode.INVALID_ACCOUNT),
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(17).build(), ValidationCode.INVALID_AMOUNT),
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(110).build(), ValidationCode.INSUFFICIENT_BALANCE)

        );
    }




}
