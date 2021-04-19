java_binary(
    name = "greeter_client",
    main_class = "com.haowei.helloworld.client.HelloWorldClient",
    runtime_deps = [
        ":client",
    ],
)

java_library(
    name = "client",
    srcs = ["HelloWorldClient.java"],
    deps = ["//proto:greeter_java_grpc"],
)