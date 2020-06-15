load("@rules_java//java:defs.bzl", "java_binary")
load("@io_bazel_rules_docker//java:image.bzl", "java_image")
load("@io_bazel_rules_docker//container:container.bzl", "container_push")
load("@com_github_atlassian_bazel_tools//multirun:def.bzl", "command", "multirun")

MAIN_DEPS = [
    "@com_github_bptlab_cepta//models/events:live_train_data_java_proto",
    "@com_github_bptlab_cepta//models/events:location_data_java_proto",
    "@com_github_bptlab_cepta//models/events:planned_train_data_java_proto",
    "@com_github_bptlab_cepta//models/events:event_java_proto",
    "//models/events:aggregate_java_proto",
    "@maven//:com_google_protobuf_protobuf_java",
    "@maven//:info_picocli_picocli",
    "@maven//:org_apache_flink_flink_connector_kafka_0_11_2_11",
    "@maven//:org_apache_flink_flink_core",
    "@maven//:org_apache_flink_flink_java",
    "@maven//:org_apache_flink_flink_streaming_java_2_11",
    "@maven//:org_apache_kafka_kafka_clients",
    "@maven//:org_slf4j_slf4j_api",
    "@maven//:org_slf4j_slf4j_log4j12",
    "//prepro/src/main/java/org/skydivin4ng3l/config:java_default_library",
    "//prepro/src/main/java/org/skydivin4ng3l/serialization:java_default_library",
    "//prepro/src/main/java/org/skydivin4ng3l/operators:java_default_library",
]

java_binary(
    name = "prepro",
    srcs = glob(["src/main/java/**/*.java"]),
    main_class = "org.skydivin4ng3l.cepmodemon.Main",
    resources = glob([
        "src/main/resources/**",
    ]),
    stamp = True,
    visibility = ["//visibility:public"],
    deps = MAIN_DEPS,
)

java_image(
    name = "image",
    srcs = glob(["src/main/java/**/*.java"]),
    main_class = "org.skydivin4ng3l.cepmodemon.Main",
    resources = glob([
        "src/main/resources/**",
    ]),
    visibility = ["//visibility:public"],
    deps = MAIN_DEPS,
)

command(
    name = "build-image-cmd",
    arguments = [
        "--norun",
    ],
    command = ":image",
)

multirun(
    name = "build-image",
    commands = [
        ":build-image-cmd",
    ],
    visibility = ["//visibility:public"],
)

container_push(
    name = "publish",
    format = "Docker",
    image = ":image",
    registry = "index.docker.io",
    repository = "cepmodemon/prepro",
    tag = "{STABLE_DOCKER_TAG}",
    visibility = ["//visibility:public"],
)

sub_tests = [
]

test_suite(
    name = "smoke",
    tags = [
        "-docker",
        "-internal",
    ],
    tests = sub_tests,
)

test_suite(
    name = "unit",
    tags = [
        "-internal",
        "unit",
    ],
    tests = sub_tests,
)

test_suite(
    name = "integration",
    tags = [
        "-internal",
        "integration",
    ],
    tests = sub_tests,
)

test_suite(
    name = "internal",
    tests = sub_tests,
)
