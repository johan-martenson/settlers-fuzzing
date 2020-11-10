for port in {7009..7010}; do

    afl-fuzz -i in_dir/ -o sync -S fuzzer$port -t 60000 /home/johan/tools/kelinci/fuzzerside/interface -p $port @@ &

done

