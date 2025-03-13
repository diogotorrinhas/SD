CODEBASE="file:///home/"$1"/test/MuseumHeist/dirOrdinary/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     clientSide.main.ClientOrdinary localhost 22141