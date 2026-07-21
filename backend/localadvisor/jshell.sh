#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

if [ ! -d "target/classes" ]; then
  echo "Compiling..."
  ./mvnw compile -DskipTests -q
fi

source ../../stack_my_settings

export DB_URL="jdbc:postgresql://localhost:5432/${POSTGRES_DB}"
export DB_PASSWORD
export POSTGRES_USER
export JWT_SECRET

CP=$(./mvnw -q dependency:build-classpath -DincludeScope=runtime -Dmdep.outputFile=/dev/stdout 2>/dev/null):target/classes

exec java --add-modules jdk.jshell -cp "$CP" com.essadany.localadvisor.shell.JShellConsole
