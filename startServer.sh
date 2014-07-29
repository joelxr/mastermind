#!/bin/bash

BASE_DIR=$(pwd)
SERVER_BINARIES=${BASE_DIR}/out/production/master-mind-server/
COMMON_BINARIES=${BASE_DIR}/out/production/master-mind-common/
export CLASSPATH=${CLASSPATH}:${COMMON_BINARIES}
export CLASSPATH=${CLASSPATH}:${SERVER_BINARIES}

java br.ifce.mastermind.main.Main 
