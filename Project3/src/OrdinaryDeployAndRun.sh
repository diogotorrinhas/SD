echo "Transfering data to the Ordinary node."
sshpass -f password ssh sd105@l040101-ws09.ua.pt 'mkdir -p test/MuseumHeist'
sshpass -f password ssh sd105@l040101-ws09.ua.pt 'rm -rf test/MuseumHeist/*'
sshpass -f password scp dirOrdinary.zip sd105@l040101-ws09.ua.pt:test/MuseumHeist
echo "Decompressing data sent to the Ordinary node."
sshpass -f password ssh sd105@l040101-ws09.ua.pt 'cd test/MuseumHeist ; unzip -uq dirOrdinary.zip'
sshpass -f password scp genclass.zip sd105@l040101-ws09.ua.pt:test/MuseumHeist/dirOrdinary
sshpass -f password ssh sd105@l040101-ws09.ua.pt 'cd test/MuseumHeist/dirOrdinary ; unzip -uq genclass.zip'
echo "Executing program at the Ordinary node."
sshpass -f password ssh sd105@l040101-ws09.ua.pt 'cd test/MuseumHeist/dirOrdinary ; ./ordinary_com_d.sh'
