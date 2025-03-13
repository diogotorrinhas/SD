CODEBASE="file:///home/"$1"/test/MuseumHeist/dirMuseum/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerMuseum 22144 localhost 22141
