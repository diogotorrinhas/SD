
# global execution
echo "Transfering data to the ConcentrationSite node."
sshpass -f password ssh sd105@l040101-ws03.ua.pt 'mkdir -p test/MuseumHeist'
sshpass -f password ssh sd105@l040101-ws03.ua.pt 'rm -rf test/MuseumHeist/*'
sshpass -f password scp dirCS.zip sd105@l040101-ws03.ua.pt:test/MuseumHeist
echo "Decompressing data sent to the ConcentrationSite node."
sshpass -f password ssh sd105@l040101-ws03.ua.pt 'cd test/MuseumHeist ; unzip -uq dirCS.zip'
sshpass -f password scp genclass.zip sd105@l040101-ws03.ua.pt:test/MuseumHeist/dirCS
sshpass -f password ssh sd105@l040101-ws03.ua.pt 'cd test/MuseumHeist/dirCS ; unzip -uq genclass.zip'
echo "Executing program at the ConcentrationSite node."
sshpass -f password ssh sd105@l040101-ws03.ua.pt 'cd test/MuseumHeist/dirCS ; java serverSide.main.ServerConcentrationSite 22143 l040101-ws07.ua.pt 22147'
