load("@rules_java//java:defs.bzl", "java_library")

java_library(
    name = "java_default_library",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
    deps = [
        "//prepro/src/main/java/org/skydivin4ng3l/config:java_default_library",
        "@com_github_bptlab_cepta//models/events:event_java_proto",
        "//models/events:aggregate_java_proto",
        "@com_github_bptlab_cepta//models/events:live_train_data_java_proto",
        "@com_github_bptlab_cepta//models/events:location_data_java_proto",
        "@com_github_bptlab_cepta//models/events:planned_train_data_java_proto",
        "@maven//:com_google_protobuf_protobuf_java",
        "@maven//:org_apache_flink_flink_cep_2_11",
        "@maven//:org_apache_flink_flink_core",
        "@maven//:org_apache_flink_flink_java",
        "@maven//:org_apache_flink_flink_streaming_java_2_11",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:org_slf4j_slf4j_log4j12",
    ],
)
