CODEBASE="file:///home/"$1"/test/MuseumHeist/dirAP0/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerAssaultParty 22146 localhost 22141 $2
