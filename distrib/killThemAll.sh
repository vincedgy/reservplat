for PID in $(cat pids.txt)
do
    kill $PID
done
