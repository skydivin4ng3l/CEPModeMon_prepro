#!/usr/bin/env bash

cepmodemon::version::get_version_vars() {
    local projGit=(git --work-tree "${CEPMODEMON_ROOT}")

    if [[ -n ${CEPMODEMON_GIT_COMMIT-} ]] || CEPMODEMON_GIT_COMMIT=$("${projGit[@]}" rev-parse "HEAD^{commit}" 2>/dev/null); then
        if [[ -z ${CEPMODEMON_GIT_TREE_STATE-} ]]; then
            # Check if the tree is dirty.  default to dirty
            if git_status=$("${projGit[@]}" status --porcelain 2>/dev/null) && [[ -z ${git_status} ]]; then
            CEPMODEMON_GIT_TREE_STATE="clean"
            else
            CEPMODEMON_GIT_TREE_STATE="dirty"
            fi
        fi

        # echo ${CEPMODEMON_GIT_COMMIT}
        # echo ${projGit[@]}" describe --tags --match='v*' --abbrev=14 "${CEPMODEMON_GIT_COMMIT}"^{commit}"

        # Use git describe to find the version based on tags.
        if [[ -n ${CEPMODEMON_GIT_VERSION-} ]] || CEPMODEMON_GIT_VERSION=$("${projGit[@]}" describe --tags --match='v*' --abbrev=14 "${CEPMODEMON_GIT_COMMIT}^{commit}" 2>/dev/null); then
            # This translates the "git describe" to an actual semver.org
            # compatible semantic version that looks something like this:
            #   v1.1.0-alpha.0.6+84c76d1142ea4d
            #
            # echo ${CEPMODEMON_GIT_VERSION}

            DASHES_IN_VERSION=$(echo "${CEPMODEMON_GIT_VERSION}" | sed "s/[^-]//g")
            if [[ "${DASHES_IN_VERSION}" == "---" ]] ; then
            # shellcheck disable=SC2001
            # We have distance to subversion (v1.1.0-subversion-1-gCommitHash)
            CEPMODEMON_GIT_VERSION=$(echo "${CEPMODEMON_GIT_VERSION}" | sed "s/-\([0-9]\{1,\}\)-g\([0-9a-f]\{14\}\)$/.\1\+\2/")
            elif [[ "${DASHES_IN_VERSION}" == "--" ]] ; then
            # shellcheck disable=SC2001
            # We have distance to base tag (v1.1.0-1-gCommitHash)
            CEPMODEMON_GIT_VERSION=$(echo "${CEPMODEMON_GIT_VERSION}" | sed "s/-g\([0-9a-f]\{14\}\)$/+\1/")
            fi
            if [[ "${CEPMODEMON_GIT_TREE_STATE}" == "dirty" ]]; then
            # git describe --dirty only considers changes to existing files, but
            # that is problematic since new untracked .go files affect the build,
            # so use our idea of "dirty" from git status instead.
            CEPMODEMON_GIT_VERSION+="-dirty"
            fi


            # Try to match the "git describe" output to a regex to try to extract
            # the "major" and "minor" versions and whether this is the exact tagged
            # version or whether the tree is between two tagged versions.
            if [[ "${CEPMODEMON_GIT_VERSION}" =~ ^v([0-9]+)\.([0-9]+)(\.[0-9]+)?([-].*)?([+].*)?$ ]]; then
            CEPMODEMON_GIT_MAJOR=${BASH_REMATCH[1]}
            CEPMODEMON_GIT_MINOR=${BASH_REMATCH[2]}
            if [[ -n "${BASH_REMATCH[4]}" ]]; then
                CEPMODEMON_GIT_MINOR+="+"
            fi
            fi

            # echo ${CEPMODEMON_GIT_VERSION}

            # If CEPMODEMON_GIT_VERSION is not a valid Semantic Version, then refuse to build.
            if ! [[ "${CEPMODEMON_GIT_VERSION}" =~ ^v([0-9]+)\.([0-9]+)(\.[0-9]+)?(-[0-9A-Za-z.-]+)?(\+[0-9A-Za-z.-]+)?$ ]]; then
                echo "CEPMODEMON_GIT_VERSION should be a valid Semantic Version. Current value: ${CEPMODEMON_GIT_VERSION}"
                echo "Please see more details here: https://semver.org"
                exit 1
            fi
        fi
    fi
}