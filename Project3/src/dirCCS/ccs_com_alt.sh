CODEBASE="file:///home/"$1"/test/MuseumHeist/dirCCS/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerControlCollectionSite 22148 localhost 22141
