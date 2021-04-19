# bazel_grpc_proto
build server and client:  
bazel build //src/main/java/com/haowei/userpayment/server:server  
bazel build //src/main/java/com/haowei/userpayment/client:client  
  
run server and client:  
bazel run //src/main/java/com/haowei/userpayment/server:user_payment_server  
bazel run //src/main/java/com/haowei/userpayment/client:user_payment_client  
  
you can see the client get the payments of the given user from server.
