
# global execution
echo "Transfering data to the Ordinary node."
sshpass -f password ssh sd105@l040101-ws09.ua.pt 'mkdir -p test/MuseumHeist'
sshpass -f password ssh sd105@l040101-ws09.ua.pt 'rm -rf test/MuseumHeist/*'
sshpass -f password scp dirOrdinary.zip sd105@l040101-ws09.ua.pt:test/MuseumHeist
echo "Decompressing data sent to the Ordinary node."
sshpass -f password ssh sd105@l040101-ws09.ua.pt 'cd test/MuseumHeist ; unzip -uq dirOrdinary.zip'
sshpass -f password scp genclass.zip sd105@l040101-ws09.ua.pt:test/MuseumHeist/dirOrdinary
sshpass -f password ssh sd105@l040101-ws09.ua.pt 'cd test/MuseumHeist/dirOrdinary ; unzip -uq genclass.zip'
echo "Executing program at the Ordinary node."
sshpass -f password ssh sd105@l040101-ws09.ua.pt 'cd test/MuseumHeist/dirOrdinary ; java clientSide.main.ClientOrdinary l040101-ws06.ua.pt 22146 l040101-ws05.ua.pt 22145 l040101-ws03.ua.pt 22143 l040101-ws02.ua.pt 22142 l040101-ws01.ua.pt 22141 l040101-ws07.ua.pt 22147'
