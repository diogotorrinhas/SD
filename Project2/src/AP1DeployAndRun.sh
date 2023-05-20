

# global execution
echo "Transfering data to the AssaultParty1 node."
sshpass -f password ssh sd105@l040101-ws05.ua.pt 'mkdir -p test/MuseumHeist'
sshpass -f password ssh sd105@l040101-ws05.ua.pt 'rm -rf test/MuseumHeist/*'
sshpass -f password scp dirAP1.zip sd105@l040101-ws05.ua.pt:test/MuseumHeist
echo "Decompressing data sent to the AssaultParty1 node."
sshpass -f password ssh sd105@l040101-ws05.ua.pt 'cd test/MuseumHeist ; unzip -uq dirAP1.zip'
sshpass -f password scp genclass.zip sd105@l040101-ws05.ua.pt:test/MuseumHeist/dirAP1
sshpass -f password ssh sd105@l040101-ws05.ua.pt 'cd test/MuseumHeist/dirAP1 ; unzip -uq genclass.zip'
echo "Executing program at the AssaultParty1 node."
sshpass -f password ssh sd105@l040101-ws05.ua.pt 'cd test/MuseumHeist/dirAP1 ; java serverSide.main.ServerAssaultParty 22145 l040101-ws07.ua.pt 22147'
