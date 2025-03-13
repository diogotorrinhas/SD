echo "Transfering data to the registry node."
sshpass -f password ssh sd105@l040101-ws01.ua.pt 'mkdir -p test/MuseumHeist'
sshpass -f password scp dirRegistry.zip sd105@l040101-ws01.ua.pt:test/MuseumHeist
echo "Decompressing data sent to the registry node."
sshpass -f password ssh sd105@l040101-ws01.ua.pt 'cd test/MuseumHeist ; unzip -uq dirRegistry.zip'
echo "Executing program at the registry node."
sshpass -f password ssh sd105@l040101-ws01.ua.pt 'cd test/MuseumHeist/dirRegistry ; ./registry_com_d.sh sd105'
