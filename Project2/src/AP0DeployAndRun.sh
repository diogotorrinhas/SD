
# global execution
echo "Transfering data to the AssaultParty0 node."
sshpass -f password ssh sd105@l040101-ws06.ua.pt 'mkdir -p test/MuseumHeist'
sshpass -f password ssh sd105@l040101-ws06.ua.pt 'rm -rf test/MuseumHeist/*'
sshpass -f password scp dirAP0.zip sd105@l040101-ws06.ua.pt:test/MuseumHeist
echo "Decompressing data sent to the AssaultParty0 node."
sshpass -f password ssh sd105@l040101-ws06.ua.pt 'cd test/MuseumHeist ; unzip -uq dirAP0.zip'
sshpass -f password scp genclass.zip sd105@l040101-ws06.ua.pt:test/MuseumHeist/dirAP0
sshpass -f password ssh sd105@l040101-ws06.ua.pt 'cd test/MuseumHeist/dirAP0 ; unzip -uq genclass.zip'
echo "Executing program at the AssaultParty0 node."
sshpass -f password ssh sd105@l040101-ws06.ua.pt 'cd test/MuseumHeist/dirAP0 ; java serverSide.main.ServerAssaultParty 22146 l040101-ws07.ua.pt 22147'
