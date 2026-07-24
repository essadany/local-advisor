#!/bin/sh
set -e

REMOTE_DIR=/home/$VPS_SSH_USER/localadvisor

cat > /tmp/localadvisor.env << EOF
POSTGRES_DB=$POSTGRES_DB
POSTGRES_USER=$POSTGRES_USER
DB_PASSWORD=$DB_PASSWORD
DB_URL=jdbc:postgresql://postgres-db:5432/$POSTGRES_DB
JWT_SECRET=$JWT_SECRET
CI_REGISTRY_IMAGE=$CI_REGISTRY_IMAGE
EOF

ssh $VPS_SSH_USER@$VPS_HOST "mkdir -p $REMOTE_DIR"
scp docker-compose.prod.yml $VPS_SSH_USER@$VPS_HOST:$REMOTE_DIR/docker-compose.yml
scp /tmp/localadvisor.env $VPS_SSH_USER@$VPS_HOST:$REMOTE_DIR/.env
rm /tmp/localadvisor.env

ssh $VPS_SSH_USER@$VPS_HOST << ENDSSH
  set -e
  cd $REMOTE_DIR
  echo "$REGISTRY_PASSWORD" | docker login -u "$REGISTRY_USER" --password-stdin "$CI_REGISTRY"
  docker compose pull
  docker compose up -d --remove-orphans
  docker image prune -f
ENDSSH
