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
host_id="(SELECT id FROM host_info WHERE hostname='$hostname')";
memory_free=$(echo "$mem_info" | egrep "MemFree:" | awk '{printf "%.0f\n", $2/=1000}')
cpu_idle=$(echo "$(vmstat 1 2|tail -1|awk '{print $15}')")
cpu_kernel=$(echo "$(vmstat 1 2|tail -1|awk '{print $14}')")
disk_io=$(echo "$(vmstat -d)" | tail -1 | awk '{print $10}')
disk_available=$(echo "$(df -BM /)" | tail -1 | awk '{printf "%d\n", $4}')
timestamp=$(date '+%Y-%m-%d %H:%M:%S')

insert_stmt="INSERT INTO host_usage(
  host_id, memory_free, cpu_idle, cpu_kernel, 
  disk_io, disk_available, timestamp
) 
VALUES 
  (
    $host_id, '$memory_free', '$cpu_idle', 
    '$cpu_kernel', '$disk_io', '$disk_available', 
    '$timestamp'
  );"

export PGPASSWORD=$psql_password

# Insert into DB
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"

exit $?

