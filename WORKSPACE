load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "rules_proto",
    sha256 = "602e7161d9195e50246177e7c55b2f39950a9cf7366f74ed5f22fd45750cd208",
    strip_prefix = "rules_proto-97d8af4dc474595af3900dd85cb3a29ad28cc313",
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/rules_proto/archive/97d8af4dc474595af3900dd85cb3a29ad28cc313.tar.gz",
        "https://github.com/bazelbuild/rules_proto/archive/97d8af4dc474595af3900dd85cb3a29ad28cc313.tar.gz",
    ],
)

load("@rules_proto//proto:repositories.bzl", "rules_proto_dependencies", "rules_proto_toolchains")

rules_proto_dependencies()

rules_proto_toolchains()

http_archive(
    name = "build_bazel_rules_nodejs",
    sha256 = "b6670f9f43faa66e3009488bbd909bc7bc46a5a9661a33f6bc578068d1837f37",
    urls = ["https://github.com/bazelbuild/rules_nodejs/releases/download/1.3.0/rules_nodejs-1.3.0.tar.gz"],
)

#@unused
load("@build_bazel_rules_nodejs//:index.bzl", "node_repositories")

http_archive(
    name = "io_bazel_rules_go",
    sha256 = "7b9bbe3ea1fccb46dcfa6c3f3e29ba7ec740d8733370e21cdc8937467b4a4349",
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/rules_go/releases/download/v0.22.4/rules_go-v0.22.4.tar.gz",
        "https://github.com/bazelbuild/rules_go/releases/download/v0.22.4/rules_go-v0.22.4.tar.gz",
    ],
)

load("@io_bazel_rules_go//go:deps.bzl", "go_register_toolchains", "go_rules_dependencies")

go_rules_dependencies()

go_register_toolchains()

http_archive(
    name = "rules_proto_grpc",
    sha256 = "5f0f2fc0199810c65a2de148a52ba0aff14d631d4e8202f41aff6a9d590a471b",
    strip_prefix = "rules_proto_grpc-1.0.2",
    urls = ["https://github.com/rules-proto-grpc/rules_proto_grpc/archive/1.0.2.tar.gz"],
)

load("@rules_proto_grpc//:repositories.bzl", "rules_proto_grpc_repos", "rules_proto_grpc_toolchains")

rules_proto_grpc_toolchains()

rules_proto_grpc_repos()

load("@rules_proto_grpc//github.com/grpc/grpc-web:repositories.bzl", rules_proto_grpc_grpc_web_repos = "grpc_web_repos")

rules_proto_grpc_grpc_web_repos()

load("@io_bazel_rules_closure//closure:repositories.bzl", "rules_closure_dependencies", "rules_closure_toolchains")

rules_closure_dependencies(
    # omit_com_google_protobuf = True,
)

rules_closure_toolchains()

http_archive(
    name = "io_grpc_grpc_java",
    sha256 = "c9ef39599b613a812843f1c43c90db9767f203b9a2ae6787f6bc715198e7dcb9",
    strip_prefix = "grpc-java-1.28.1",
    url = "https://github.com/grpc/grpc-java/archive/v1.28.1.zip",
)

http_archive(
    name = "rules_typescript_proto",
    sha256 = "0c76ae0d04eaa4d4c5f12556615cb70d294082ee672aee6dd849fea4ec2075ee",
    strip_prefix = "rules_typescript_proto-0.0.3",
    urls = [
        "https://github.com/Dig-Doug/rules_typescript_proto/archive/0.0.3.tar.gz",
    ],
)

load("@rules_typescript_proto//:index.bzl", "rules_typescript_proto_dependencies")

rules_typescript_proto_dependencies()

load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

git_repository(
    name = "com_github_atlassian_bazel_tools",
    commit = "1d1765561cf3baaeaf91ae4123b8290c589b93f8",
    remote = "https://github.com/atlassian/bazel-tools.git",
    shallow_since = "1578869856 +1100",
)

git_repository(
    name = "com_github_bptlab_cepta",
    # branch = "dev",
    commit = "5c5a95c234e04f59cf9726911385e5deb99d35c3",
    remote = "https://github.com/bptlab/cepta.git",
    # shallow_since = "1578869856 +1100",
)

load("@com_github_atlassian_bazel_tools//multirun:deps.bzl", "multirun_dependencies")

multirun_dependencies()

# Download the rules_docker repository at release v0.14.1
http_archive(
    name = "io_bazel_rules_docker",
    sha256 = "dc97fccceacd4c6be14e800b2a00693d5e8d07f69ee187babfd04a80a9f8e250",
    strip_prefix = "rules_docker-0.14.1",
    urls = ["https://github.com/bazelbuild/rules_docker/releases/download/v0.14.1/rules_docker-v0.14.1.tar.gz"],
)

load(
    "@io_bazel_rules_docker//repositories:repositories.bzl",
    container_repositories = "repositories",
)

container_repositories()

load(
    "@io_bazel_rules_docker//java:image.bzl",
    _java_image_repos = "repositories",
)

_java_image_repos()

load(
    "@io_bazel_rules_docker//go:image.bzl",
    _go_image_repos = "repositories",
)

_go_image_repos()

http_archive(
    name = "go_proto_gql",
    sha256 = "7ae7e80b0e76e82e136053461144d8038faee9a2bf4d2c89cba1358ab16da3c6",
    strip_prefix = "go-proto-gql-0.7.4",
    urls = ["https://github.com/romnnn/go-proto-gql/archive/v0.7.4.tar.gz"],
)

FLINK_VERSION = "1.9.0"

SCALA_VERSION = "2.11"

RULES_JVM_EXTERNAL_TAG = "3.2"

RULES_JVM_EXTERNAL_SHA = "82262ff4223c5fda6fb7ff8bd63db8131b51b413d26eb49e3131037e79e324af"

GRPC_JAVA_VERSION = "1.28.1"

http_archive(
    name = "rules_jvm_external",
    sha256 = RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")
load("@rules_jvm_external//:defs.bzl", "artifact")
load("@rules_jvm_external//:specs.bzl", "maven")
load("@io_grpc_grpc_java//:repositories.bzl", "IO_GRPC_GRPC_JAVA_ARTIFACTS")
load("@io_grpc_grpc_java//:repositories.bzl", "IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS")

maven_install(
    artifacts = [
        "org.apache.commons:commons-lang3:3.9",
        "org.apache.commons:commons-text:1.8",
        "org.javatuples:javatuples:1.2",
        "junit:junit:4.13",
        "org.testcontainers:testcontainers:1.14.1",
        "org.testcontainers:kafka:1.14.1",
        "org.testcontainers:postgresql:1.14.1",
        "commons-io:commons-io:2.6",
        "com.google.code.findbugs:jsr305:1.3.9",
        "com.google.guava:guava:28.0-jre",
        "com.google.errorprone:error_prone_annotations:2.0.18",
        "com.google.j2objc:j2objc-annotations:1.1",
        "com.google.protobuf:protobuf-java:3.11.1",
        "com.google.protobuf:protobuf-java-util:3.6.1",
        "info.picocli:picocli:4.1.0",
        "org.slf4j:slf4j-log4j12:1.7.5",
        "org.slf4j:slf4j-api:1.7.28",
        "com.github.jasync-sql:jasync-postgresql:1.0.11",
        "com.github.jasync-sql:jasync-common:1.0.11",
        "org.postgresql:postgresql:42.2.5",
        "org.mongodb:mongodb-driver-sync:4.0.3",
        "org.mongodb:mongodb-driver-reactivestreams:4.0.2",
        "org.mongodb:mongodb-driver-core:4.0.2",
        "org.mongodb:bson:4.0.2",
        "org.reactivestreams:reactive-streams:1.0.3",
        "joda-time:joda-time:2.9.7",
        "org.apache.kafka:kafka-clients:2.4.0",
        "org.apache.flink:flink-core:%s" % FLINK_VERSION,
        "org.apache.flink:flink-java:%s" % FLINK_VERSION,
        "org.apache.flink:flink-streaming-java_%s:%s" % (SCALA_VERSION, FLINK_VERSION),
        "org.apache.flink:flink-connector-kafka-0.11_%s:%s" % (SCALA_VERSION, FLINK_VERSION),
        "org.apache.flink:flink-cep_2.11:%s" % FLINK_VERSION,
    ] + IO_GRPC_GRPC_JAVA_ARTIFACTS,
    generate_compat_repositories = True,
    override_targets = IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS,
    repositories = [
        "https://jcenter.bintray.com/",
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
        # https://repo.maven.apache.org/maven2
        # https://maven-central-eu.storage-download.googleapis.com/repos/central/data/
    ],
)

maven_install(
    name = "testing",
    artifacts = [
        maven.artifact(
            group = "org.apache.flink",
            artifact = "flink-runtime_%s" % SCALA_VERSION,
            version = FLINK_VERSION,
            classifier = "tests",
            packaging = "test-jar",
            testonly = True,
        ),
        maven.artifact(
            group = "org.apache.flink",
            artifact = "flink-streaming-java_%s" % SCALA_VERSION,
            version = FLINK_VERSION,
            classifier = "tests",
            packaging = "test-jar",
            testonly = True,
        ),
        maven.artifact(
            group = "org.apache.flink",
            artifact = "flink-test-utils-junit",
            version = FLINK_VERSION,
            testonly = True,
        ),
        maven.artifact(
            group = "org.apache.flink",
            artifact = "flink-test-utils_%s" % SCALA_VERSION,
            version = FLINK_VERSION,
            testonly = True,
        ),
        maven.artifact(
            group = "org.apache.flink",
            artifact = "flink-tests",
            version = FLINK_VERSION,
            classifier = "tests",
            packaging = "test-jar",
            testonly = True,
        ),
    ],
    repositories = [
        "https://jcenter.bintray.com/",
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
        # https://repo.maven.apache.org/maven2
        # https://maven-central-eu.storage-download.googleapis.com/repos/central/data/
    ],
)

load("@maven//:compat.bzl", "compat_repositories")

compat_repositories()

load("@io_grpc_grpc_java//:repositories.bzl", "grpc_java_repositories")

# Run grpc_java_repositories after compat_repositories to ensure the
# maven_install-selected dependencies are used.
grpc_java_repositories()

load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")

protobuf_deps()

http_archive(
    name = "bazel_gazelle",
    urls = [
        "https://storage.googleapis.com/bazel-mirror/github.com/bazelbuild/bazel-gazelle/releases/download/v0.20.0/bazel-gazelle-v0.20.0.tar.gz",
        "https://github.com/bazelbuild/bazel-gazelle/releases/download/v0.20.0/bazel-gazelle-v0.20.0.tar.gz",
    ],
)

load("@bazel_gazelle//:deps.bzl", "gazelle_dependencies", "go_repository")

gazelle_dependencies()
