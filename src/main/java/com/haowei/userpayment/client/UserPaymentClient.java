package com.haowei.userpayment.client;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;

import com.haowei.userpayment.UserPaymentGrpc;
import com.haowei.userpayment.User;
import com.haowei.userpayment.Payment;

public class UserPaymentClient {
    private static final Logger logger = Logger.getLogger(UserPaymentClient.class.getName());

    private final UserPaymentGrpc.UserPaymentBlockingStub blockingStub;

    public UserPaymentClient(Channel channel) {
        blockingStub = UserPaymentGrpc.newBlockingStub(channel);
    }

    // block server-to-client streaming service. Get payments for a user. Print his payment as it arrives
    public void getPayments(long id, String name) {
        logger.info("Will try to get payments for user with id: " + id + " and name: " + name + " ...");
        
        User request = User.newBuilder()
            .setId(id)
            .setName(name)
            .build();
        Iterator<Payment> payments;
        try {
            payments = blockingStub.getPayments(request);
            for (int i = 1; payments.hasNext(); i++) {
                Payment payment = payments.next();
                logger.info("Payment #" + i + ": type is : " + payment.getType() + " , cardId is : " + payment.getCardId());
            }
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
        }
    }

    // issue the request for user's payments and exits
    // If provided, the first element of {@code args} is the target server.
    public static void main(String[] args) throws InterruptedException {
        String target = "localhost:8980";
        if (args.length > 0) {
            if ("--help".equals(args[0])) {
                System.err.println("Usage: [target]");
                System.err.println("");
                System.err.println("  target  The server to connect to. Defaults to " + target);
                System.exit(1);
            }
            target = args[0];
        }

        // Create a communication channel to the server, known as a Channel. Channels are thread-safe
        // and reusable. It is common to create channels at the beginning of your application and reuse
        // them until the application shuts down.
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
            // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
            // needing certificates.
            .usePlaintext()
            .build();
        try {
            UserPaymentClient client = new UserPaymentClient(channel);
            // look for payments of a user
            client.getPayments(1, "James Smith");

        } finally {
            // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
            // resources the channel should be shut down when it will no longer be used. If it may be used
            // again leave it running.
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
        
    }
}
