load("@io_bazel_rules_go//proto:def.bzl", "go_proto_library")
load("@rules_proto//proto:defs.bzl", "proto_library")
load("@rules_java//java:defs.bzl", "java_proto_library")
load("@rules_proto_grpc//github.com/grpc/grpc-web:defs.bzl", "ts_grpc_compile")
load("@rules_proto_grpc//nodejs:defs.bzl", "nodejs_proto_compile")

proto_library(
    name = "aggregate_proto",
    srcs = ["Aggregate.proto"],
    visibility = ["//visibility:public"],
    deps = ["@com_google_protobuf//:timestamp_proto"],
)

java_proto_library(
    name = "aggregate_java_proto",
    visibility = ["//visibility:public"],
    deps = [":aggregate_proto"],
)

filegroup(
    name = "events",
    srcs = [
        ":aggregate_java_proto",
    ],
    visibility = ["//visibility:public"],
)
