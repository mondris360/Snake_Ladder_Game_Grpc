package com.mondris.game.server;

import com.mondris.service.Die;
import com.mondris.service.GameServiceGrpc;
import com.mondris.service.GameState;
import com.mondris.service.Player;
import io.grpc.stub.StreamObserver;

public class GameService  extends GameServiceGrpc.GameServiceImplBase {
    @Override
    public StreamObserver<Die> rollDie(StreamObserver<GameState> responseObserver) {
        // initialize the initial positions of  both players;
        Player client = Player.newBuilder()
                .setName("Client")
                .setPosition(0)
                .build();

        Player server =  Player.newBuilder()
                .setName("Server")
                .setPosition(0)
                .build();

        return new DieStreamingRequest(client, server, responseObserver);
    }
}
