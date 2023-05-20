xterm  -T "General Repository" -hold -e "./GenReposDeployAndRun.sh" &
sleep 1
xterm  -T "AssaultParty0" -hold -e "./AP0DeployAndRun.sh" &
xterm  -T "AssaultParty1" -hold -e "./AP1DeployAndRun.sh" &
xterm  -T "ConcetrationSite" -hold -e "./ConcentrationSiteDeployAndRun.sh" &
xterm  -T "ControlCollectSite" -hold -e "./ControlCollectionSiteDeployAndRun.sh" &
xterm  -T "MUSEUM" -hold -e "./MuseumDeployAndRun.sh" &
sleep 1
xterm  -T "Master" -hold -e "./MasterDeployAndRun.sh" &
xterm  -T "Ordinary" -hold -e "./OrdinaryDeployAndRun.sh" &
