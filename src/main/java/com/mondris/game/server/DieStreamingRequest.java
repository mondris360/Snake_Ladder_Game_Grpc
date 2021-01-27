package com.mondris.game.server;

import com.mondris.service.Die;
import com.mondris.service.GameState;
import com.mondris.service.Player;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.ThreadLocalRandom;

public class DieStreamingRequest implements StreamObserver<Die> {
    private Player client;
    private Player server;
    private StreamObserver<GameState> gameStateStreamObserver;

    // constructor
    public DieStreamingRequest(Player client, Player server, StreamObserver<GameState> gameStateStreamObserver) {
        this.client = client;
        this.server = server;
        this.gameStateStreamObserver = gameStateStreamObserver;
    }

    @Override
    public void onNext(Die die) {
        // since the client has played, update the new client position
        client =  getPlayerNewPosition(client, die.getValue());
        // if the client has not won the game, the server should play
        if(client.getPosition() < 100){
            // generate a random number from 1 to 6 and use it as server die value
           int serverDieValue = ThreadLocalRandom.current().nextInt(1,7);
           server =  getPlayerNewPosition(server, serverDieValue);
        }
        gameStateStreamObserver.onNext(getGameState());
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        gameStateStreamObserver.onCompleted();
    }

    // method to build the game state object
    private GameState getGameState(){
        return GameState.newBuilder()
                .addPlayer(client)
                .addPlayer(server)
                .build();
    }

    // method to return new player position
    private  Player getPlayerNewPosition(Player player, int dieValue){
        int newPlayerPosition =  (player.getPosition() + dieValue) > 100 ? player.getPosition() : player.getPosition() + dieValue;
        // apply the snake head and ladder base to the new player position
        newPlayerPosition =  SnakesAndLaddersRules.getPosition(newPlayerPosition);
        // check if the new position is less than or equal to the max score of the game
        if(newPlayerPosition <= 100){
            player = player.toBuilder()
                    .setPosition(newPlayerPosition)
                    .build();
        }
        return player;
    }
}
