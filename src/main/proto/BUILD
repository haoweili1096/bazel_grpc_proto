package(default_visibility = ["//visibility:public"])

load("@rules_proto//proto:defs.bzl", "proto_library")
load("@rules_proto_grpc//java:defs.bzl", "java_grpc_library")

java_grpc_library(
    name = "greeter_java_grpc",
    protos = [":greeter_proto"],
)

proto_library(
    name = "greeter_proto",
    srcs = ["greeter.proto"],
)

java_grpc_library(
    name = "user_payment_java_grpc",
    protos = [":user_payment_proto"],
)

proto_library(
    name = "user_payment_proto",
    srcs = ["user_payment.proto"],
)

filegroup(
    name = "srcs",
    srcs = [
        "BUILD",
        "user_payment.proto",
    ],
)