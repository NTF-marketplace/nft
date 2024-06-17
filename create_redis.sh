#!/bin/bash
docker-compose up --scale node1=6

NODES=$(docker network inspect nft_redis_network | jq -r '.[] | .Containers[] | "\(.IPv4Address)" | split("/")[0]')

# 노드 목록을 콜론과 함께 포맷팅
FORMATTED_NODES=$(echo "$NODES" | awk '{for (i=1; i<=NF; i++) printf $i":6379 "}')

echo $FORMATTED_NODES
# 노드 목록 확인

# Redis 클러스터 생성 명령어 실행
docker exec -it nft-node1-1 redis-cli --cluster create $FORMATTED_NODES --cluster-replicas 1 -a foobared


