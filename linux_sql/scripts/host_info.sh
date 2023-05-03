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

#put the result of lscpu into a variable for future reuse
lscpu_out=`lscpu`
hostname=$(hostname -f)

#hardware info
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out"  | egrep "^Architecture:" | awk '{print $2}' | xargs)
#get cpu model name
cpu_model=$(echo "$lscpu_out"  | egrep "^Model name:" | cut -d ':' -f 2-| xargs)
#get cpu frequency
cpu_mhz=$(echo "$lscpu_out"  | egrep "^CPU MHz:" | awk '{print $3}' | xargs)
#get L2 cache and remove the K suffix
l2_cache=$(echo "$lscpu_out"  | egrep "^L2 cache:" | awk '{print $3}' | sed 's/K$//')
#get total memory in kb
total_mem=$(vmstat | tail -1 | awk '{print $4}')
#get current UTC time in YYYY-MM-DD HH:MM:SS format
timestamp=$(date +"%Y-%m-%d %H:%M:%S")

#built sql command for insert data into host_info table
psql_cmd="INSERT INTO host_info(hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, timestamp, total_mem)
VALUES('$hostname', '$cpu_number', '$cpu_architecture', '$cpu_model', '$cpu_mhz', '$l2_cache', '$timestamp', '$total_mem')
";

#set up environment variable for psql password
export PGPASSWORD=$psql_password
#execute the sql command to insert data into host_info table
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$psql_cmd"
exit $?