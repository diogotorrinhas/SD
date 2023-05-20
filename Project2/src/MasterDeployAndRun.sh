
# global execution
echo "Transfering data to the Master node."
sshpass -f password ssh sd105@l040101-ws08.ua.pt 'mkdir -p test/MuseumHeist'
sshpass -f password ssh sd105@l040101-ws08.ua.pt 'rm -rf test/MuseumHeist/*'
sshpass -f password scp dirMaster.zip sd105@l040101-ws08.ua.pt:test/MuseumHeist
echo "Decompressing data sent to the Master node."
sshpass -f password ssh sd105@l040101-ws08.ua.pt 'cd test/MuseumHeist ; unzip -uq dirMaster.zip'
sshpass -f password scp genclass.zip sd105@l040101-ws08.ua.pt:test/MuseumHeist/dirMaster
sshpass -f password ssh sd105@l040101-ws08.ua.pt 'cd test/MuseumHeist/dirMaster ; unzip -uq genclass.zip'
echo "Executing program at the Master node."
sshpass -f password ssh sd105@l040101-ws08.ua.pt 'cd test/MuseumHeist/dirMaster ; java clientSide.main.ClientMaster l040101-ws06.ua.pt 22146 l040101-ws05.ua.pt 22145 l040101-ws03.ua.pt 22143 l040101-ws02.ua.pt 22142 l040101-ws07.ua.pt 22147'
