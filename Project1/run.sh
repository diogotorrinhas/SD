for i in $(seq 1 10)
do
echo -e "\nRun n.o " $i
java heist.concurrent.Main
done
