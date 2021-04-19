package(default_visibility = ["//visibility:public"])

java_binary(
    name = "greeter",
    main_class = "com.haowei.helloworld.server.HelloWorldServer",
    runtime_deps = [
        ":server",
    ],
)

java_library(
    name = "server",
    srcs = ["HelloWorldServer.java"],
    deps = ["//proto:greeter_java_grpc"],
)