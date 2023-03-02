#!/bin/bash
cmd=$1
db_username=$2
db_password=$3

#if the docker service is not started, start it
sudo systemctl status docker || sudo systemctl start docker

#inspect container jrvs-psql and provides detailed info about it
docker container inspect jrvs-psql
#Store the exit status of the command above to container_status variable
container_status=$?

case $cmd in
  create)
    #Check if the container is already exist
    if [ $container_status -eq 0 ]; then
      echo "Container already exists"
      exit 1
    fi

    #Check the number of CLI arguments
    if [ $# -n 3 ]; then
      echo "Requires username and password to create a container"
      exit 1
    fi

    #Create the volume if not existed
    docker volume inspect pgdata
    volume_status=$?
    if [ $volume_status -ne 0 ]; then
      echo "Creating volume 'pgdata'"
      docker volume create pgdata
    else
      echo "Volume 'pgdata' already exists"
    fi

    #Create the container
    docker run --name jrvs-psql -2

    ;;
  start|stop)
    ;;

  #other cases
  *)
    echo "Illegal command"
    echo "Commands: start|stop|create"
    exit 1
    ;;
esac