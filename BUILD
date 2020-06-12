load("@com_github_atlassian_bazel_tools//multirun:def.bzl", "multirun")
load("@rules_proto_grpc//:plugin.bzl", "proto_plugin")

proto_plugin(
    name = "proto_gql_plugin",
    exclusions = [
        "google/api",
        "google/protobuf",
    ],
    options = [
        "paths=source_relative",
        "plugins=gql",
    ],
    outputs = ["{protopath}.pb.graphqls"],
    protoc_plugin_name = "gql",
    tool = "@go_proto_gql//protoc-gen-gql",
    visibility = ["//visibility:public"],
)

filegroup(
    name = "cepmodemon",
    srcs = [
        "//prepro",
    ],
)

multirun(
    name = "publish",
    commands = [
        "//prepro:publish",
    ],
    parallel = True,
)

# Build the docker images and tags them for use in local dev env
multirun(
    name = "build-images",
    commands = [
        "//prepro:build-images",
    ],
    parallel = True,
)

# Test Suites:
# Smoke tests (fast and light)
# Unit tests (superset of the smoke tests)
# Integration tests (Mostly long tests that require docker and can only be run on linux)
# Internal

test_suite(
    name = "smoke",
    tags = ["smoke"],
    tests = [
        "//prepro:smoke",
    ],
)

test_suite(
    name = "unit",
    tags = ["unit"],
    tests = [
        "//prepro:unit",
    ],
)

test_suite(
    name = "integration",
    tags = ["integration"],
    tests = [
        "//prepro:integration",
    ],
)

test_suite(
    name = "internal",
    tags = ["internal"],
    tests = [
        "//prepro:internal",
    ],
)
