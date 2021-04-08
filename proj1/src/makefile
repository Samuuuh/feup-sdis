VERSION = 1

CC = javac

find_dir = $(wildcard $(dir)/*.java)
find_class = $(wildcard $(dir)/*.class)
PACKAGES := channel main main/etc process/answer process/request process/postAnswer send/TCP state send tasks tasks/restore tasks/backup process
CLASSES = $(foreach dir, $(PACKAGES), $(find_class))
FILES = $(foreach dir, $(PACKAGES), $(find_dir))

.PHONY: all
# Main compilation.
all: $(CLASSES)
	@$(CC) $(FILES) -d build
	@echo Folders built

.PHONY: clean
clean:
	@rm -r build
	@echo Build folder removed with success!

# Personalized input
# java main.Peer version peer_no mcast_port mcast_addr
peer: all
	@read -p "Input peer number: " peer_no;
	$(CC) $(PEER) $(VERSION) $$peer_no $(MCAST_PORT) $(MCAST_ADDR)

# java Client <peer_ap> BACKUP <file> <replication_degree>
backup: all
	@read -p "Input file path: " file_path;
	@read -p "Peer number: " peer_no;
	@read -p "Replication degree: " rep_deg;
	$(CC) $(CLIENT) $$peer_no BACKUP $$file_path  $$rep_deg;