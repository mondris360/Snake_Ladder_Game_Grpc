package com.mondris.game.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 7000;
        Server server = ServerBuilder
                .forPort(port)
                .addService(new GameService())
                .build();

        server.start();
        System.out.println("Grpc Server is Running On Port "+ port);

        // wait for the server connection to be terminated by both the client and the server
        server. awaitTermination();
    }
}
