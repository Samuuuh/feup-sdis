#! /usr/bin/bash

# Check number input arguments
argc=$#

if (( argc == 2 )) 
then
    # Assign input arguments to nicely named variables
    ip=$1
    port=$2
    
    # Execute the program
    java -Djavax.net.ssl.keyStore=../keystore -Djavax.net.ssl.keyStorePassword=123456 -Djavax.net.ssl.trustStore=../truststore -Djavax.net.ssl.trustStorePassword=123456 network/Main ${ip} ${port}
fi

if (( argc == 4 )) 
then
    # Assign input arguments to nicely named variables
    ip=$1
    port=$2
    chord_ip=$3
    chord_port=$4
	
	if [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
		sudo java -Djavax.net.ssl.keyStore=../keystore -Djavax.net.ssl.keyStorePassword=123456 -Djavax.net.ssl.trustStore=../truststore -Djavax.net.ssl.trustStorePassword=123456 network/Main ${ip} ${port} ${chord_ip} ${chord_port}
	elif [ "$(expr substr $(uname -s) 1 10)" == "MINGW32_NT" ]; then
		java -Djavax.net.ssl.keyStore=../keystore -Djavax.net.ssl.keyStorePassword=123456 -Djavax.net.ssl.trustStore=../truststore -Djavax.net.ssl.trustStorePassword=123456 network/Main ${ip} ${port} ${chord_ip} ${chord_port}
	elif [ "$(expr substr $(uname -s) 1 10)" == "MINGW64_NT" ]; then
		java -Djavax.net.ssl.keyStore=../keystore -Djavax.net.ssl.keyStorePassword=123456 -Djavax.net.ssl.trustStore=../truststore -Djavax.net.ssl.trustStorePassword=123456 network/Main ${ip} ${port} ${chord_ip} ${chord_port}
	fi
    # Execute the program
    java -Djavax.net.ssl.keyStore=../keystore -Djavax.net.ssl.keyStorePassword=123456 -Djavax.net.ssl.trustStore=../truststore -Djavax.net.ssl.trustStorePassword=123456 network/Main ${ip} ${port} ${chord_ip} ${chord_port}
fi
