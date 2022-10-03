#! /bin/sh

#capture CLI arguments (please do not copy comments)
cmd=$1
db_username=$2
db_password=$3

#Start docker
#Make sure you understand `||` cmd
#sudo systemctl status docker || systemctl ...

#check container status (try the following cmds on terminal)
docker container inspect jrvs-psql
container_status=$?
echo $cmd
echo $container_status

