psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

#Check the number of arguments
if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters."
    echo "you need <hostname> <port> <database name> <psql_username> <psql_password>"
    exit 1
fi

#save the output of vmstat into a variable for future use, set the unit to MB
vmstat_mb=$(vmstat --unit M)
#get the VM host name
hostname=$(hostname -f)

#Get hardware specification variables
#Get the amount of free memory in MB
memory_free=$(echo "$vmstat_mb"| tail -1 | awk '{print $4}' | xargs)
#Get the percentage of CPU time spent idle
cpu_idle=$(echo "$vmstat_mb"| tail -1 | awk '{print $15}' | xargs)
#Get the percentage of CPU time spent on system processes (i.e. processes running in kernel space)
cpu_kernel=$(echo "$vmstat_mb"| tail -1 | awk '{print $13}' | xargs)
#Get disk IO info
disk_io=$(vmstat -d --unit M | tail -1 | awk '{print $10}')
#Get how many storage space are available for root ("/") directory, and remove the suffix M
disk_available=$(df -BM / | tail -1 | awk '{print $4}' | sed 's/M$//')
#get current UTC time in YYYY-MM-DD HH:MM:SS format
timestamp=$(date +"%Y-%m-%d %H:%M:%S")

#SubQuery for get matching id from host_info table by hostname
host_id="(SELECT id FROM host_info WHERE hostname='$hostname')";

#sql command to insert server usage info into host_usage table
insert_cmd="INSERT INTO host_usage("timestamp", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
VALUES('$timestamp', $host_id, '$memory_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '$disk_available')";

#set up environment variable for psql password
export PGPASSWORD=$psql_password
#Execute the sql command to insert server usage info into host_usage table
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_cmd"
exit $?