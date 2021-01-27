package com.mondris.game.client;

import com.mondris.game.server.DieStreamingRequest;
import com.mondris.service.Die;
import com.mondris.service.GameServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameServiceTest {
    private ManagedChannel managedChannel;
    private GameServiceGrpc.GameServiceStub nonBlockStub;


    @BeforeAll
    public void setup(){
        // create a channel
        managedChannel = ManagedChannelBuilder.forAddress("localhost", 7000)
                .usePlaintext()
                .build();
        // use the created channel to establish a connection to the server
        nonBlockStub = GameServiceGrpc.newStub(managedChannel);
    }

    @Test
    public void GameTest() throws InterruptedException {
        CountDownLatch latch =  new CountDownLatch(1);
        // implement the response streaming from the server
        GameStateStreamingResponse gameStateStreamingResponse =  new GameStateStreamingResponse(latch);
        StreamObserver<Die> dieStreamObserver = this.nonBlockStub.rollDie(gameStateStreamingResponse);
        // since we want to communicate with the server  via the gameStreamingResponse, we need to  to pass the
        //dieStreamObserver to the implemetation via a setter
        gameStateStreamingResponse.setDieStreamObserver(dieStreamObserver);
        // client should start the game by running the die and sending it to the server
        gameStateStreamingResponse.rollDie();
        latch.await();

    }
}
