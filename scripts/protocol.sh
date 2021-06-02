#! /usr/bin/bash

argc=$#

if (( argc < 3 )) 
then
	echo "Usage: $0 <peer_ip> <peer_port> BACKUP|RESTORE|DELETE|RECLAIM [<opnd_1> [<optnd_2]]"
	exit 1
fi


# Assign input arguments to nicely named variables
peer_ip=$1
peer_port=$2
oper=$3

case $oper in
BACKUP)
	if(( argc != 5 )) 
	then
		echo "Usage: $0 <peer_ip> <peer_port> BACKUP <filename> <rep degree>"
		exit 1
	fi
	opernd_1=$4
	opernd_2=$5
	;;
RESTORE)
	if(( argc != 4 ))
	then
		echo "Usage: $0 <peer_ip> <peer_port> RESTORE <filename>"
	fi
	opernd_1=$4
	opernd_2=""
	;;
DELETE)
	if(( argc != 4 ))
	then
		echo "Usage: $0 <peer_ip> <peer_port> DELETE <filename>"
		exit 1
	fi
	opernd_1=$4
	opernd_2=""
	;;
RECLAIM)
	if(( argc != 5 ))
	then
		echo "Usage: $0 <peer_ip> <peer_port> RECLAIM <peer_id> <max space>"
		exit 1
	fi
	opernd_1=$4
    opernd_2=$5
	;;
*)
	echo "Usage: $0 <peer_ip> <peer_port> BACKUP|RESTORE|DELETE|RECLAIM [<opnd_1> [<optnd_2]]"
	exit 1
	;;
esac


sudo java -Djavax.net.ssl.keyStore=../keystore -Djavax.net.ssl.keyStorePassword=123456 -Djavax.net.ssl.trustStore=../truststore -Djavax.net.ssl.trustStorePassword=123456 app/Client ${peer_ip} ${peer_port} ${oper} ${opernd_1} ${opernd_2}

