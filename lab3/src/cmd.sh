# Possible args are: comp, server, client-register, client-lookup

if [[ $1 == "comp" ]]; then
    javac -d . *.java
elif [[ $1 == "server" ]]; then
    java build.lab3.Server handleRequest
elif [[ $1 == "client-register" ]]; then
    java build.lab3.Client 1888 handleRequest REGISTER google.com 127.0.0.1
elif [[ $1 == "client-lookup" ]]; then
    java build.lab3.Client 1888 handleRequest LOOKUP google.com 
else 
    echo "Usage :: bash cmd.sh <comp | server | client>" 
fi 

