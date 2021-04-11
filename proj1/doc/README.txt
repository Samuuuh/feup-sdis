# README SDIS
Under de ./src directory run the following command to compile the code:

$ sh ../scripts/compile.sh

After compiling, go to the Build folder located in src folder.
In that folder you can run the following commands:

- Initialize the peer running the following command:
$ sh ../../scripts/peer.sh <version> <peer_id> <svc_access_point> <mc_addr> <mc_port> <mdb_addr> <mdb_port> <mdr_addr> <mdr_port>

- To run the TestApp you should run the command:
$ sh ../../scripts/test.sh <peer_ap> BACKUP|RESTORE|DELETE|RECLAIM|STATE [<opnd_1> [<optnd_2]] 

- To cleanup the files run the following code on Build folder
$ sh ../../scripts/cleanup.sh <Peer_Id>