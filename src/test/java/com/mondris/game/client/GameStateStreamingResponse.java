package com.mondris.game.client;

import com.google.common.util.concurrent.Uninterruptibles;
import com.mondris.service.Die;
import com.mondris.service.GameState;
import com.mondris.service.Player;
import io.grpc.stub.StreamObserver;

import java.sql.SQLOutput;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class GameStateStreamingResponse implements StreamObserver<GameState> {
    private int initialClientDie;
    private StreamObserver<Die> requestStreamObserver;
    private CountDownLatch latch;

    public GameStateStreamingResponse(CountDownLatch latch) {
        this.latch = latch;
    }


    @Override
    public void onNext(GameState gameState) {
        List<Player> players =  gameState.getPlayerList();
        players.forEach(player -> System.out.println("Player Name: " +  player.getName() + "Player Position: " +  player.getPosition()));

        // check if the client or server has won the game
        boolean isGameOver =  players.stream()
                .anyMatch(player -> player.getPosition() == 100);

        if(isGameOver){
            System.out.println("Game over");
            requestStreamObserver.onCompleted();
        } else {
            // delay for 1 second before the client can send the next die. this will make us see the log message
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
            // if the game is not over yet, the client should roll a new die and send it to the server
            rollDie();
        }
        System.out.println("==================================================================");
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println("Server has closed the connection");
        latch.countDown();
    }

    // method to generate  new die value for the client(1 to 6)
    public  void rollDie(){
        int dieValue = ThreadLocalRandom.current().nextInt(1, 7);
        Die die =   Die.newBuilder()
                .setValue(dieValue)
                .build();

        // send the die to the server
        requestStreamObserver.onNext(die);

    }

    // method to set Client Stream observer so that we can send the die to the server via these call back functions
    public void setDieStreamObserver(StreamObserver<Die> streamObserver){
        requestStreamObserver =  streamObserver;
    }
}
