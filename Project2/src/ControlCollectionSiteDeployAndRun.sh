
# global execution
echo "Transfering data to the ControlCollectionSite node."
sshpass -f password ssh sd105@l040101-ws02.ua.pt 'mkdir -p test/MuseumHeist'
sshpass -f password ssh sd105@l040101-ws02.ua.pt 'rm -rf test/MuseumHeist/*'
sshpass -f password scp dirCCS.zip sd105@l040101-ws02.ua.pt:test/MuseumHeist
echo "Decompressing data sent to the ControlCollectionSite node."
sshpass -f password ssh sd105@l040101-ws02.ua.pt 'cd test/MuseumHeist ; unzip -uq dirCCS.zip'
sshpass -f password scp genclass.zip sd105@l040101-ws02.ua.pt:test/MuseumHeist/dirCCS
sshpass -f password ssh sd105@l040101-ws02.ua.pt 'cd test/MuseumHeist/dirCCS ; unzip -uq genclass.zip'
echo "Executing program at the ControlCollectionSite node."
sshpass -f password ssh sd105@l040101-ws02.ua.pt 'cd test/MuseumHeist/dirCCS ; java serverSide.main.ServerControlCollectionSite 22142 l040101-ws07.ua.pt 22147'
