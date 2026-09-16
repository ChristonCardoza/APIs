package org.cardoza.test.sec08;

import org.cardoza.common.GrpcServer;
import org.cardoza.models.sec08.GuessNumberGrpc;
import org.cardoza.sec08.GuessNumberService;
import org.cardoza.test.sec06.AbstractTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GuessNumberTest extends AbstractTest {

    private final GrpcServer server = GrpcServer.create(new GuessNumberService());
    private GuessNumberGrpc.GuessNumberStub stub;

    @BeforeAll
    public void setup(){
        this.server.start();
        this.stub = GuessNumberGrpc.newStub(channel);
    }

    @AfterAll
    public void stop() {
        this.server.stop();
    }

    @Test
    public void guessNumberGame() {

        var responseObserver = new GuessResponseHandler();
        var requestObserver = this.stub.makeGuess(responseObserver);
        responseObserver.setRequestObserver(requestObserver);
        responseObserver.start();
        responseObserver.await();
    }
}
