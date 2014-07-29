#!/bin/bash

BASE_DIR=$(pwd)
CLIENT_BINARIES=${BASE_DIR}/out/production/master-mind-client/
COMMON_BINARIES=${BASE_DIR}/out/production/master-mind-common/
export CLASSPATH=${CLASSPATH}:${COMMON_BINARIES}
export CLASSPATH=${CLASSPATH}:${CLIENT_BINARIES}

java br.ifce.mastermind.main.Main
