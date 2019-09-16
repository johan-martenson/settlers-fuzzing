#!/usr/bin/bash

cd /home/johan/projects/settlers
git pull --rebase
mvn install

cd /home/johan/projects/settlers-map-manager/
git pull --rebase
mvn install

cd /home/johan/projects/settlers-fuzzing/
git pull --rebase
mvn package
java -cp /home/johan/tools/kelinci/instrumentor/build/libs/kelinci.jar:target/settlers-model-fuzzing-jar-with-dependencies.jar edu.cmu.sv.kelinci.instrumentor.Instrumentor -i target/settlers-model-fuzzing-jar-with-dependencies.jar -o settlers-instrumented.jar --prefix-to-instrument org/appland/settlers
