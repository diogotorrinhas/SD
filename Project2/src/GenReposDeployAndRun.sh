
# global execution
echo "Transfering data to the GeneralRepos node."
sshpass -f password ssh sd105@l040101-ws07.ua.pt 'mkdir -p test/MuseumHeist'
sshpass -f password ssh sd105@l040101-ws07.ua.pt 'rm -rf test/MuseumHeist/*'
sshpass -f password scp dirGeneralRepos.zip sd105@l040101-ws07.ua.pt:test/MuseumHeist
echo "Decompressing data sent to the GeneralRepos node."
sshpass -f password ssh sd105@l040101-ws07.ua.pt 'cd test/MuseumHeist ; unzip -uq dirGeneralRepos.zip'
sshpass -f password scp genclass.zip sd105@l040101-ws07.ua.pt:test/MuseumHeist/dirGeneralRepos
sshpass -f password ssh sd105@l040101-ws07.ua.pt 'cd test/MuseumHeist/dirGeneralRepos ; unzip -uq genclass.zip'
echo "Executing program at the GeneralRepos node."
sshpass -f password ssh sd105@l040101-ws07.ua.pt 'cd test/MuseumHeist/dirGeneralRepos ; java serverSide.main.ServerGeneralRepos 22147'
