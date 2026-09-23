#!/usr/bin/env sh
set -eu

PROJECT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
cd "$PROJECT_DIR"

JAR_PATH="target/automata-finito-1.0.0-SNAPSHOT.jar"

if [ ! -f "$JAR_PATH" ]; then
    echo "No existe el JAR. Compilando el proyecto..."
    sh ./mvnw clean package
fi

exec java -jar "$JAR_PATH"
