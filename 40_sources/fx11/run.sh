#!/bin/bash

if [ ! -d target/classes ]; then
    mvn compile
fi

java -p /usr/lib/javafx/javafx-sdk-11.0.2/lib --add-modules=javafx.controls -cp target/classes HelloFX
