xterm  -T "RMI registry" -hold -e "./RMIRegistryDeployAndRun.sh" &
sleep 8

xterm  -T "Registry" -hold -e "./RegistryDeployAndRun.sh" &
sleep 8

xterm  -T "General Repository" -hold -e "./GenReposDeployAndRun.sh" &
sleep 6

xterm  -T "Assault Party 0" -hold -e "./AP0DeployAndRun.sh" &

xterm  -T "Assault Party 1" -hold -e "./AP1DeployAndRun.sh" &
sleep 6

xterm  -T "Concentration Site" -hold -e "./ConcentrationSiteDeployAndRun.sh" &
sleep 6

xterm  -T "Control Collection Site" -hold -e "./ControlCollectionSiteDeployAndRun.sh" &

xterm  -T "MUSEUM" -hold -e "./MuseumDeployAndRun.sh" &
sleep 6

xterm  -T "Master" -hold -e "./MasterDeployAndRun.sh" &
#sleep 6
xterm  -T "Ordinary" -hold -e "./OrdinaryDeployAndRun.sh" &
