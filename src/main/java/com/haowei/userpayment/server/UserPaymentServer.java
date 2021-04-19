package com.haowei.userpayment.server;

import com.google.protobuf.util.JsonFormat;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.Collection;
import java.util.Scanner;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import com.haowei.userpayment.UserPaymentGrpc;
import com.haowei.userpayment.User;
import com.haowei.userpayment.Payment;
import com.haowei.userpayment.PaymentDatabase;

public class UserPaymentServer {
    private static final Logger logger = Logger.getLogger(UserPaymentServer.class.getName());

    private final int port;
    private final Server server;

    // create a UserPayment server listening on {@code port} using default database
    public UserPaymentServer(int port) throws IOException {
        this(ServerBuilder.forPort(port), port, UserPaymentServer.parsePayments());
    }

    // create a UserPayment server using serverbuilder as a base and payments as data.
    public UserPaymentServer(ServerBuilder<?> serverBuilder, int port, Collection<Payment> payments) {
        this.port = port;
        server = serverBuilder.addService(new UserPaymentService(payments))
            .build();
    }

    // start serving requests
    public void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    UserPaymentServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });
    }

    // stop serving requests and shutdown resources
    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    // await termination on the main thread since the grpc library uses daemon threads
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    // public static String convertStreamToString(InputStream is) throws IOException {
    //     try (Scanner s = new Scanner(is)) {
    //         s.useDelimiter("\n");
    //         return s.hasNext() ? s.next() : "";
    //     }
    // }

    // parse the JSON input file containing the list of payments
    public static List<Payment> parsePayments() throws IOException {
        InputStream stream = UserPaymentServer.class.getResourceAsStream("/user_payment_db.json");
        // logger.info("data:" + stream);
        // String greeting = "Hello";
        // try {
        //     greeting = convertStreamToString(stream).trim();
        // } finally {

        // }
        // return new ArrayList<>();
        logger.info("data: " + UserPaymentServer.class.getResourceAsStream("/user_payment_db.json"));
        try {
            Reader reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
            try {
                PaymentDatabase.Builder database = PaymentDatabase.newBuilder();
                JsonFormat.parser().merge(reader, database);
                return database.getPaymentList();
            } finally {
                reader.close();
            }
        } finally {
            stream.close();
        }
    }

    // main method
    public static void main(String[] args) throws Exception {
        UserPaymentServer server = new UserPaymentServer(8980);
        server.start();
        server.blockUntilShutdown();
    }

    // implementation of UserPayment service
    private static class UserPaymentService extends UserPaymentGrpc.UserPaymentImplBase {
        // is Guice used here?
        // but here payments are got from parsePayments()
        // I think Guice is not needed here
        private final Collection<Payment> payments;
        
        UserPaymentService(Collection<Payment> payments) {
            this.payments = payments;
        }

        // get all payments for requested {@link User}
        @Override
        public void getPayments(User request, StreamObserver<Payment> responseObserver) {
            long id = request.getId();
            
            for (Payment payment : payments) {
                if (payment.getUser().getId() == id) {
                    responseObserver.onNext(payment);
                }
            }
            responseObserver.onCompleted();
        }
    }

}

