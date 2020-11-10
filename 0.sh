for port in {7008..7010}; do

    java -cp /home/johan/tools/kelinci/instrumentor/build/libs/kelinci.jar:./settlers-instrumented.jar edu.cmu.sv.kelinci.Kelinci -p $port org.appland.settlers.fuzzing.SettlersModelDriver @@ &

done
