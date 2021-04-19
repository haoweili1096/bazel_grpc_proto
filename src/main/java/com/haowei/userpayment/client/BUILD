package(default_visibility = ["//visibility:public"])

java_binary(
    name = "user_payment_client",
    main_class = "com.haowei.userpayment.client.UserPaymentClient",
    runtime_deps = [
        ":client",
    ],
)

java_library(
    name = "client",
    srcs = ["UserPaymentClient.java"],
    deps = ["//src/main/proto:user_payment_java_grpc"],
)

filegroup(
    name = "srcs",
    srcs = ["BUILD"] + glob(["**/*.java"]),
)