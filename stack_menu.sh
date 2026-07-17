#!/usr/bin/env bash
set -euo pipefail

ENV_FILE="stack_my_settings"
COMPOSE_FILE="docker-compose.yml"

show_menu() {
  echo "=============================="
  echo "  LocalAdvisor Stack Manager"
  echo "=============================="
  echo "Services: backend (8080) | frontend (4173) | postgres-db (5432)"
  echo "Env file: $ENV_FILE"
  echo "------------------------------"
  echo "1) Start stack        (docker compose --env-file $ENV_FILE up --build -d)"
  echo "2) Stop stack         (docker compose --env-file $ENV_FILE down)"
  echo "3) Restart stack      (down + up --build -d)"
  echo "4) View logs          (docker compose --env-file $ENV_FILE logs -f)"
  echo "5) Database: psql     (docker compose exec postgres-db psql)"
  echo "6) Exit"
  echo "=============================="
  read -rp "Choice: " choice
}

show_help() {
  echo "LocalAdvisor Stack Manager"
  echo ""
  echo "Usage: $0 {menu|start|stop|logs|psql|-h}"
  echo ""
  echo "Commands:"
  echo "  menu      Interactive menu (default)"
  echo "  start     Build images and start containers (detached)"
  echo "  stop      Stop and remove containers"
  echo "  logs      Tail logs from all services"
  echo "  psql      Open psql shell into postgres-db"
  echo "  -h, --help  Show this help"
  echo ""
  echo "All commands use: docker compose --env-file $ENV_FILE -f $COMPOSE_FILE"
}

docker_compose() {
  docker compose --env-file "$ENV_FILE" -f "$COMPOSE_FILE" "$@"
}

case "${1:-menu}" in
  -h|--help)
    show_help
    ;;
  menu)
    while true; do
      show_menu
      case "$choice" in
        1) docker_compose up --build -d && echo "Stack started." ;;
        2) docker_compose down && echo "Stack stopped." ;;
        3) docker_compose down && docker_compose up --build -d && echo "Stack restarted." ;;
        4) docker_compose logs -f ;;
        5)
          echo "Opening psql shell into postgres-db ..."
          docker_compose exec postgres-db psql -U "${POSTGRES_USER:-postgres}" "${POSTGRES_DB:-localadvisor}"
          ;;
        6) exit 0 ;;
        *) echo "Invalid choice." ;;
      esac
      echo ""
    done
    ;;
  start)  shift; docker_compose up --build -d "$@" ;;
  stop)   shift; docker_compose down "$@" ;;
  logs)   shift; docker_compose logs -f "$@" ;;
  psql)   shift; docker_compose exec postgres-db psql -U "${POSTGRES_USER:-postgres}" "${POSTGRES_DB:-localadvisor}" "$@" ;;
  *)      echo "Usage: $0 {menu|start|stop|logs|psql|-h}" ;;
esac
