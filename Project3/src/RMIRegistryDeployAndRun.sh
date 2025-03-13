echo "Transfering data to the RMIregistry node."
sshpass -f password ssh sd105@l040101-ws01.ua.pt 'mkdir -p test/MuseumHeist'
sshpass -f password ssh sd105@l040101-ws01.ua.pt 'rm -rf test/MuseumHeist/*'
sshpass -f password ssh sd105@l040101-ws01.ua.pt 'mkdir -p Public/classes/interfaces'
sshpass -f password ssh sd105@l040101-ws01.ua.pt 'rm -rf Public/classes/interfaces/*'
sshpass -f password scp dirRMIRegistry.zip sd105@l040101-ws01.ua.pt:test/MuseumHeist
echo "Decompressing data sent to the RMIregistry node."
sshpass -f password ssh sd105@l040101-ws01.ua.pt 'cd test/MuseumHeist ; unzip -uq dirRMIRegistry.zip'
sshpass -f password ssh sd105@l040101-ws01.ua.pt 'cd test/MuseumHeist/dirRMIRegistry ; cp interfaces/*.class /home/sd105/Public/classes/interfaces ; cp set_rmiregistry_d.sh /home/sd105'
echo "Executing program at the RMIregistry node."
sshpass -f password ssh sd105@l040101-ws01.ua.pt './set_rmiregistry_d.sh sd105 22141'
