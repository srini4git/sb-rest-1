#!/bin/bash

function usage() {
echo "Usage:  $0 host req-count api port delay"
echo "Example: $0 localhost 1000 api/v1/students 8090 0.0005";
}

if [ $# -ne 5 ]; then
usage;
exit;
fi

host=$1
count=$2
api=$3
port=$4
delay=$5

eho `Performance test: API to test: http://${host}:${port}/${api} With Req Count:${count} With Delay:${delay}`
let i=$count-1
tot=0
while [ $i -ge 0 ];
do
#res=`curl -w "$i: %{time_total} %{http_code} %{size_download} %{url_effective}\n" -o "/dev/null" -s http://${host}:${port}/${size}_test.html`

res=`curl -w "$i: %{time_total} %{http_code} %{url_effective}\n" -o "/dev/null" -s http://${host}:${port}/${api}`

echo $res
val=`echo $res | cut -f2 -d' '`
tot=`echo "scale=3;${tot}+${val}" | bc`
let i=i-1
	sleep ${delay} 
done

avg=`echo "scale=3; ${tot}/${count}" |bc`
echo "   ........................."
echo "   AVG: $tot/$count = $avg"