CODEBASE="file:///home/"$1"/test/MuseumHeist/dirAP1/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerAssaultParty 22145 localhost 22141 $2
