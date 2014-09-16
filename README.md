mastermind
==========

Run th e follow steps:

1. mvn clean package install
2. cd target/classes
3. $JAVA_HOME/bin/rmic br.ifce.mastermind.remote.ServerImpl
4. $JAVA_HOME/bin/rmic br.ifce.mastermind.remote.ClientImpl
5. $JAVA_HOME/bin/rmiregistry &
6. chmod +x startServer.sh
7. chmod +x startClient.sh
8. ./startServer &
9. ./startClient &
10. ./startClient &


You can change server and client hostname and port just editing the scripts: startServer and startClient.

Have fun! :)
