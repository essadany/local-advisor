#!/usr/bin/env bash
set -euo pipefail

ENV_FILE="stack_my_settings"
COMPOSE_FILE="docker-compose.yml"
BACKEND_DIR="backend/localadvisor"

open_terminal() {
  local title="$1"
  local cmd="printf '\033]0;%s\007' '$title' && $2; exec bash"
  if command -v ptyxis &> /dev/null; then
    ptyxis --tab -- bash -c "$cmd" &>/dev/null &
  elif command -v gnome-terminal &> /dev/null; then
    gnome-terminal -- bash -c "$cmd" &>/dev/null &
  elif command -v xdg-terminal-exec &> /dev/null; then
    xdg-terminal-exec bash -c "$cmd" &>/dev/null &
  elif command -v xterm &> /dev/null; then
    xterm -T "$title" -hold -e "$cmd" &>/dev/null &
  elif command -v konsole &> /dev/null; then
    konsole --hold -e bash -c "$cmd" &>/dev/null &
  elif command -v x-terminal-emulator &> /dev/null; then
    x-terminal-emulator -e bash -c "$cmd" &>/dev/null &
  else
    echo "Running in current terminal (no separate terminal emulator found): $cmd"
    eval "$cmd" &
  fi
}

open_jshell() {
  open_terminal "LocalAdvisor DB Shell (JShell)" "$(pwd)/$BACKEND_DIR/jshell.sh"
}

open_logs() {
  local cmd="cd $(pwd) && docker compose --env-file $ENV_FILE -f $COMPOSE_FILE logs -f"
  open_terminal "LocalAdvisor Logs" "$cmd"
}

show_menu() {
  echo "=============================="
  echo "  LocalAdvisor Stack Manager"
  echo "=============================="
  echo "Services: backend (8080) | frontend (4173) | postgres-db (5432)"
  echo "Env file: $ENV_FILE"
  echo "------------------------------"
  echo " 1) Start stack        (docker compose --env-file $ENV_FILE up --build -d)"
  echo " 2) Stop stack         (docker compose --env-file $ENV_FILE down)"
  echo " 3) Restart stack      (down + up --build -d)"
  echo " 4) View logs          (docker compose --env-file $ENV_FILE logs -f)"
  echo " 5) Logs: new terminal (opens logs in a separate window)"
  echo " 6) Database: psql     (docker compose exec postgres-db psql)"
  echo " 8) Database: JShell Console (ad-hoc Java/JPA queries, Django shell_plus-like)"
  echo " 9) Resync password   (update postgres pwd from stack_my_settings)"
  echo "10) Exit"
  echo "=============================="
  read -rp "Choice: " choice
}

show_help() {
  echo "LocalAdvisor Stack Manager"
  echo ""
echo "Usage: $0 {menu|start|stop|logs|psql|jshell|-h}"
echo ""
echo "Commands:"
echo "  menu      Interactive menu (default)"
echo "  start     Build images and start containers (detached)"
echo "  stop      Stop and remove containers"
echo "  logs      Tail logs from all services"
echo "  psql      Open psql shell into postgres-db"
echo "  jshell    Open JShell Console with JPA repos pre-imported (new terminal)"
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
         1)
          docker_compose up --build -d && echo "Stack started."
          open_logs
          open_jshell
          ;;
        2) docker_compose down && echo "Stack stopped." ;;
         3)
          docker_compose down
          docker_compose up --build -d && echo "Stack restarted."
          open_logs
          open_jshell
          ;;
        4) docker_compose logs -f ;;
        5) open_logs ;;
        6)
          echo "Opening psql shell into postgres-db ..."
          docker_compose exec postgres-db psql -U "${POSTGRES_USER:-postgres}" "${POSTGRES_DB:-localadvisor}"
          ;;
         8) open_jshell ;;
         9)
          echo "Resyncing postgres password from $ENV_FILE ..."
          source "$ENV_FILE"
          docker_compose exec -T postgres-db psql -U "${POSTGRES_USER:-postgres}" -d "${POSTGRES_DB:-localadvisor}" -c "ALTER USER ${POSTGRES_USER:-postgres} WITH PASSWORD '${DB_PASSWORD}';"
          docker_compose restart backend
          echo "Password resynced and backend restarted."
          ;;
         10) exit 0 ;;
        *) echo "Invalid choice." ;;
      esac
      echo ""
    done
    ;;
  start)  shift; docker_compose up --build -d "$@" ;;
  stop)   shift; docker_compose down "$@" ;;
  logs)   shift; docker_compose logs -f "$@" ;;
  psql)   shift; docker_compose exec postgres-db psql -U "${POSTGRES_USER:-postgres}" "${POSTGRES_DB:-localadvisor}" "$@" ;;
  jshell) open_jshell ;;
  *)      echo "Usage: $0 {menu|start|stop|logs|psql|jshell|-h}" ;;
esac
