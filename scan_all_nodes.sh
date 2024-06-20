#!/bin/bash

# Define an array of all Redis nodes
nodes=(
    "172.24.0.2:6379"
    "172.24.0.3:6379"
    "172.24.0.4:6379"
    "172.24.0.5:6379"
    "172.24.0.6:6379"
    "172.24.0.7:6379"
    "172.24.0.8:6379"
    "172.24.0.9:6379"
    "172.24.0.10:6379"
)

# Function to scan all keys in a Redis node
scan_keys() {
    local host=$1
    local port=$2
    local cursor=0

    echo "Scanning keys on node $host:$port"

    while :
    do
        # Use redis-cli to scan keys
        result=$(docker exec -it nft-redis-1-1 redis-cli -h $host -p $port SCAN $cursor)

        # Extract the new cursor and keys
        cursor=$(echo "$result" | head -n 1)
        keys=$(echo "$result" | tail -n +2)

        # Print keys
        if [ -n "$keys" ]; then
            echo "$keys"
        fi

        # Break if cursor is 0
        if [ "$cursor" == "0" ]; then
            break
        fi
    done
}

# Loop through each node and scan keys
for node in "${nodes[@]}"
do
    host=$(echo $node | cut -d':' -f1)
    port=$(echo $node | cut -d':' -f2)
    scan_keys $host $port
done
