echo "Compiling source code."
javac -cp ".:./genclass.jar" --release 8 */*.java */*/*.java

echo "Distributing intermediate code to the different execution environments."

echo "  RMI registry"
rm -rf dirRMIRegistry/interfaces
mkdir -p dirRMIRegistry/interfaces
cp interfaces/*.class dirRMIRegistry/interfaces

echo "  Register Remote Objects"
rm -rf dirRegistry/serverSide dirRegistry/interfaces
mkdir -p dirRegistry/serverSide \
         dirRegistry/serverSide/main \
         dirRegistry/serverSide/objects \
         dirRegistry/interfaces
cp serverSide/main/ServerRegisterRemoteObject.class dirRegistry/serverSide/main
cp serverSide/objects/RegisterRemoteObject.class dirRegistry/serverSide/objects
cp interfaces/Register.class dirRegistry/interfaces

echo "  General Repository"
rm -rf dirGeneralRepos/serverSide dirGeneralRepos/clientSide dirGeneralRepos/interfaces
mkdir -p dirGeneralRepos/serverSide \
         dirGeneralRepos/serverSide/main \
         dirGeneralRepos/serverSide/objects \
         dirGeneralRepos/interfaces \
         dirGeneralRepos/clientSide \
         dirGeneralRepos/clientSide/entities
cp serverSide/main/SimulConsts.class \
   serverSide/main/ServerGeneralRepos.class \
   dirGeneralRepos/serverSide/main
cp serverSide/objects/GeneralRepos.class dirGeneralRepos/serverSide/objects
cp interfaces/Register.class \
   interfaces/GeneralReposInterface.class \
   dirGeneralRepos/interfaces
cp clientSide/entities/MasterStates.class \
   clientSide/entities/OrdinaryStates.class \
   dirGeneralRepos/clientSide/entities

echo "  Assault Party 0"
rm -rf dirAP0/serverSide \
       dirAP0/clientSide \
       dirAP0/interfaces \
       dirAP0/commInfra
mkdir -p dirAP0/serverSide \
         dirAP0/serverSide/main \
         dirAP0/serverSide/objects \
         dirAP0/interfaces \
         dirAP0/clientSide \
         dirAP0/clientSide/entities \
         dirAP0/commInfra
cp serverSide/main/SimulConsts.class \
   serverSide/main/ServerAssaultParty.class \
   dirAP0/serverSide/main
cp serverSide/objects/AssaultParty.class dirAP0/serverSide/objects
cp interfaces/*.class dirAP0/interfaces
cp clientSide/entities/MasterStates.class \
   clientSide/entities/OrdinaryStates.class \
   dirAP0/clientSide/entities
cp commInfra/*.class dirAP0/commInfra

echo "  Assault Party 1"
rm -rf dirAP1/serverSide \
       dirAP1/clientSide \
       dirAP1/interfaces \
       dirAP1/commInfra
mkdir -p dirAP1/serverSide \
         dirAP1/serverSide/main \
         dirAP1/serverSide/objects \
         dirAP1/interfaces \
         dirAP1/clientSide \
         dirAP1/clientSide/entities \
         dirAP1/commInfra
cp serverSide/main/SimulConsts.class \
   serverSide/main/ServerAssaultParty.class \
   dirAP1/serverSide/main
cp serverSide/objects/AssaultParty.class dirAP1/serverSide/objects
cp interfaces/*.class dirAP1/interfaces
cp clientSide/entities/MasterStates.class \
   clientSide/entities/OrdinaryStates.class \
   dirAP1/clientSide/entities
cp commInfra/*.class dirAP1/commInfra

echo "  Concentration Site"
rm -rf dirCS/serverSide \
       dirCS/clientSide \
       dirCS/interfaces \
       dirCS/commInfra
mkdir -p dirCS/serverSide \
         dirCS/serverSide/main \
         dirCS/serverSide/objects \
         dirCS/interfaces \
         dirCS/clientSide \
         dirCS/clientSide/entities \
         dirCS/commInfra
cp serverSide/main/SimulConsts.class \
   serverSide/main/ServerConcentrationSite.class \
   dirCS/serverSide/main
cp serverSide/objects/ConcentrationSite.class dirCS/serverSide/objects
cp interfaces/*.class dirCS/interfaces
cp clientSide/entities/MasterStates.class \
   clientSide/entities/OrdinaryStates.class \
   dirCS/clientSide/entities
cp commInfra/*.class dirCS/commInfra

echo "  Control Collection Site"
rm -rf dirCCS/serverSide \
       dirCCS/clientSide \
       dirCCS/interfaces \
       dirCCS/commInfra
mkdir -p dirCCS/serverSide \
         dirCCS/serverSide/main \
         dirCCS/serverSide/objects \
         dirCCS/interfaces \
         dirCCS/clientSide \
         dirCCS/clientSide/entities \
         dirCCS/commInfra
cp serverSide/main/SimulConsts.class \
   serverSide/main/ServerControlCollectionSite.class \
   dirCCS/serverSide/main
cp serverSide/objects/ControlCollectionSite.class dirCCS/serverSide/objects
cp interfaces/*.class dirCCS/interfaces
cp clientSide/entities/MasterStates.class \
   clientSide/entities/OrdinaryStates.class \
   dirCCS/clientSide/entities
cp commInfra/*.class dirCCS/commInfra

echo "  Museum"
rm -rf dirMuseum/serverSide \
       dirMuseum/clientSide \
       dirMuseum/interfaces \
       dirMuseum/commInfra
mkdir -p dirMuseum/serverSide \
         dirMuseum/serverSide/main \
         dirMuseum/serverSide/objects \
         dirMuseum/interfaces \
         dirMuseum/clientSide \
         dirMuseum/clientSide/entities \
         dirMuseum/commInfra
cp serverSide/main/SimulConsts.class \
   serverSide/main/ServerMuseum.class \
   dirMuseum/serverSide/main
cp serverSide/objects/Museum.class dirMuseum/serverSide/objects
cp interfaces/*.class dirMuseum/interfaces
cp clientSide/entities/MasterStates.class \
   clientSide/entities/OrdinaryStates.class \
   dirMuseum/clientSide/entities
cp commInfra/*.class dirMuseum/commInfra


echo "  Master"
rm -rf dirMaster/serverSide \
       dirMaster/clientSide \
       dirMaster/interfaces
mkdir -p dirMaster/serverSide \
         dirMaster/serverSide/main \
         dirMaster/interfaces \
         dirMaster/clientSide \
         dirMaster/clientSide/entities \
         dirMaster/clientSide/main
cp serverSide/main/SimulConsts.class dirMaster/serverSide/main 
cp clientSide/main/ClientMaster.class dirMaster/clientSide/main
cp clientSide/entities/Master.class clientSide/entities/MasterStates.class dirMaster/clientSide/entities
cp interfaces/AssaultPartyInterface.class \
   interfaces/GeneralReposInterface.class \
   interfaces/ConcentrationSiteInterface.class \
   interfaces/ControlCollectionSiteInterface.class \
   interfaces/ReturnBoolean.class \
   interfaces/ReturnInt.class \
   dirMaster/interfaces


echo "  Ordinary"
rm -rf dirOrdinary/serverSide \
       dirOrdinary/clientSide \
       dirOrdinary/interfaces
mkdir -p dirOrdinary/serverSide \
         dirOrdinary/serverSide/main \
         dirOrdinary/interfaces \
         dirOrdinary/clientSide \
         dirOrdinary/clientSide/entities \
         dirOrdinary/clientSide/main
cp serverSide/main/SimulConsts.class dirOrdinary/serverSide/main 
cp clientSide/main/ClientOrdinary.class dirOrdinary/clientSide/main
cp clientSide/entities/Ordinary.class clientSide/entities/OrdinaryStates.class dirOrdinary/clientSide/entities
cp interfaces/AssaultPartyInterface.class \
   interfaces/GeneralReposInterface.class \
   interfaces/ConcentrationSiteInterface.class \
   interfaces/ControlCollectionSiteInterface.class \
   interfaces/MuseumInterface.class \
   interfaces/ReturnBoolean.class \
   interfaces/ReturnInt.class \
   dirOrdinary/interfaces



echo "Compressing execution environments."

echo "  RMI registry"
rm -f  dirRMIRegistry.zip
zip -rq dirRMIRegistry.zip dirRMIRegistry

echo "  Register Remote Objects"
rm -f  dirRegistry.zip
zip -rq dirRegistry.zip dirRegistry

echo "  General Repository"
rm -f  dirGeneralRepos.zip
zip -rq dirGeneralRepos.zip dirGeneralRepos

echo "  Assault Party 0"
rm -f  dirAP0.zip
zip -rq dirAP0.zip dirAP0

echo "  Assault Party 1"
rm -f  dirAP1.zip
zip -rq dirAP1.zip dirAP1

echo "  Concentration Site"
rm -f  dirCS.zip
zip -rq dirCS.zip dirCS

echo "  Control Collection Site"
rm -f  dirCCS.zip
zip -rq dirCCS.zip dirCCS

echo "  Museum"
rm -f  dirMuseum.zip
zip -rq dirMuseum.zip dirMuseum

echo "  Master"
rm -f  dirMaster.zip
zip -rq dirMaster.zip dirMaster

echo "  Ordinary"
rm -f  dirOrdinary.zip
zip -rq dirOrdinary.zip dirOrdinary

echo "  Genclass"
rm -f  genclass.zip
zip -rq genclass.zip genclass.jar
