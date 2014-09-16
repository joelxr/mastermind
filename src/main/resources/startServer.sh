#! /bin/bash

####
# Startup script
###

HOST='localhost'
PORT='1099'
BASE_DIR=$PWD
JVM_ARGS=" -Djava.security.policy=file:$BASE_DIR/security.policy -Djava.rmi.server.codebase=file:$BASE_DIR -Dfile.encoding=UTF-8 "
ARGS=" $HOST $PORT "

echo $ARGS
echo $JVM_ARGS

$JAVA_HOME/bin/java br.ifce.mastermind.startup.StartServer ${ARGS} ${JVM_ARGS} 
