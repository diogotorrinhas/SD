CODEBASE="file:///home/"$1"/test/MuseumHeist/dirCS/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerConcentrationSite 22143 localhost 22141
