package(default_visibility = ["//visibility:public"])

java_binary(
    name = "user_payment_server",
    main_class = "com.haowei.userpayment.server.UserPaymentServer",
    runtime_deps = [
        ":server",
    ],
)

java_library(
    name = "server",
    srcs = ["UserPaymentServer.java"],
    deps = ["//src/main/proto:user_payment_java_grpc"],
    resources = ["//src/main/resources:db"],
)

filegroup(
    name = "srcs",
    srcs = ["BUILD"] + glob(["**/*.java"]),
)