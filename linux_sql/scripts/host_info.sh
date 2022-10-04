#! /bin/sh

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

if [ $# -ne  5 ]; then
  echo 'Need 5 arguments (host, port, db name, psql user, psql pw)'
  exit 1
fi

# Set variables
hostname=$(hostname -f)
mem_info=$(cat /proc/meminfo)
lscpu_out=$(lscpu)

cpu_number=$(echo "$lscpu_out" | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | egrep "^Model name:" | cut  -c 12- | xargs)
cpu_mhz=$(echo "$lscpu_out" | egrep "^CPU MHz:" | awk '{print $3}' | xargs)
l2_cache=$(echo "$lscpu_out" | egrep "^L2 cache:" | awk '{print $3}' | cut  -c -3 | xargs)
total_mem=$(echo "$mem_info" | egrep "MemTotal:" | awk '{print $2}' | xargs)
timestamp=$(date '+%Y-%m-%d %H:%M:%S')

insert_stmt="INSERT INTO host_info(
  hostname, cpu_number, cpu_architecture, 
  cpu_model, cpu_mhz, L2_cache, total_mem, 
  timestamp
) 
VALUES 
  (
    '$hostname', '$cpu_number', '$cpu_architecture', 
    '$cpu_model', '$cpu_mhz', '$l2_cache', 
    '$total_mem', '$timestamp'
  );"

export PGPASSWORD=$psql_password

# Insert into DB
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"

exit $?
