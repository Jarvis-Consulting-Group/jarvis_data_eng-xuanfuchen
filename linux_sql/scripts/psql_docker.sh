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
    docker volume create psqldata

    #Create the container with entered username and password
    docker run --name jrvs-psql -e POSTGRES_USER=$db_username -e POSTGRES_PASSWORD=$db_password -d -v psqldata:/var/lib/postgresql/data -p 5432:5432 postgres:10
    exit $?
    ;;
  start|stop)
    if [ $container_status -eq 1 ]; then
      echo "Container not exist"
      exit 1
    fi

    docker container $cmd jrvs-psql
    exit $?
    ;;

  #other cases
  *)
    echo "Illegal command"
    echo "Commands: start|stop|create"
    exit 1
    ;;
esac