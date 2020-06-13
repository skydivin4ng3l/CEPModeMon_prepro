#!/usr/bin/env bash

export CEPMODEMON_ROOT=$(dirname "${BASH_SOURCE}")/..
cd $CEPMODEMON_ROOT

# Assure all internal tests succeed
#echo "Running tests and checks"
#bazel test :internal --test_output=errors
#rc=$?; if [[ $rc != 0 ]]; then exit $rc; fi

# Bump the version
echo "Incrementing the version"
bump2version "$@"
rc=$?; if [[ $rc != 0 ]]; then exit $rc; fi

# Read version info
source ./ci/print-workspace-status.sh > /dev/null

echo "Preparing for release of ${STABLE_DOCKER_TAG}"


# Build and push the docker containers (requires docker registry access)
echo "Building and publishing docker images to docker hub"
bazel run :publish
rc=$?; if [[ $rc != 0 ]]; then exit $rc; fi

echo "Release completed"
