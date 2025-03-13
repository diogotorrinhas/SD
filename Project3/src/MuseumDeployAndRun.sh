echo "Transfering data to the Museum node."
sshpass -f password ssh sd105@l040101-ws10.ua.pt 'mkdir -p test/MuseumHeist'
sshpass -f password ssh sd105@l040101-ws10.ua.pt 'rm -rf test/MuseumHeist/*'
sshpass -f password scp dirMuseum.zip sd105@l040101-ws10.ua.pt:test/MuseumHeist
echo "Decompressing data sent to the Museum node."
sshpass -f password ssh sd105@l040101-ws10.ua.pt 'cd test/MuseumHeist ; unzip -uq dirMuseum.zip'
sshpass -f password scp genclass.zip sd105@l040101-ws10.ua.pt:test/MuseumHeist/dirMuseum
sshpass -f password ssh sd105@l040101-ws10.ua.pt 'cd test/MuseumHeist/dirMuseum ; unzip -uq genclass.zip'
echo "Executing program at the Museum node."
sshpass -f password ssh sd105@l040101-ws10.ua.pt 'cd test/MuseumHeist/dirMuseum ; ./museum_com_d.sh sd105'
