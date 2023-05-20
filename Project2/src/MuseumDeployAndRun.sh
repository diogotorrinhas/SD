
# global execution
echo "Transfering data to the Museum node."
sshpass -f password ssh sd105@l040101-ws01.ua.pt 'mkdir -p test/MuseumHeist'
sshpass -f password ssh sd105@l040101-ws01.ua.pt 'rm -rf test/MuseumHeist/*'
sshpass -f password scp dirMuseum.zip sd105@l040101-ws01.ua.pt:test/MuseumHeist
echo "Decompressing data sent to the Museum node."
sshpass -f password ssh sd105@l040101-ws01.ua.pt 'cd test/MuseumHeist ; unzip -uq dirMuseum.zip'
sshpass -f password scp genclass.zip sd105@l040101-ws01.ua.pt:test/MuseumHeist/dirMuseum
sshpass -f password ssh sd105@l040101-ws01.ua.pt 'cd test/MuseumHeist/dirMuseum ; unzip -uq genclass.zip'
echo "Executing program at the Museum node."
sshpass -f password ssh sd105@l040101-ws01.ua.pt 'cd test/MuseumHeist/dirMuseum ; java serverSide.main.ServerMuseum 22141 l040101-ws07.ua.pt 22147'
