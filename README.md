# bazel_grpc_proto
bazel build //src/main/java/com/haowei/userpayment/server:server
bazel run //src/main/java/com/haowei/userpayment/server:user_payment_server

bazel build //src/main/java/com/haowei/userpayment/client:client
bazel run //src/main/java/com/haowei/userpayment/client:user_payment_client
