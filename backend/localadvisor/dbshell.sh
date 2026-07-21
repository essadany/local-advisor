#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

JAR="target/localadvisor-0.0.1-SNAPSHOT.jar"
if [ ! -f "$JAR" ]; then
  echo "Building JAR..."
  ./mvnw package -DskipTests -q
fi

source ../../stack_my_settings

export DB_URL="jdbc:postgresql://localhost:5432/${POSTGRES_DB}"
export DB_PASSWORD
export POSTGRES_USER
export JWT_SECRET
export SERVER_PORT=0
export SPRING_SHELL_INTERACTIVE_ENABLED=true

script -q -c "java -jar $JAR" /dev/null
exec bash
