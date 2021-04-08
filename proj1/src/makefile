VERSION = 1

CC = javac

find_dir = $(wildcard $(dir)/*.java)
find_class = $(wildcard $(dir)/*.class)
PACKAGES := channel main main/etc process/answer process/request process/postAnswer send/TCP state send tasks tasks/restore tasks/backup process
CLASSES = $(foreach dir, $(PACKAGES), $(find_class))
FILES = $(foreach dir, $(PACKAGES), $(find_dir))

PEER = main.Peer
CLIENT = main.Client

MCAST_PORT = 1889
MCAST_ADDR = 230.0.0.1


.PHONY: all
# Main compilation.
all: $(CLASSES)
	$(CC) $(FILES)


.PHONY: clean
clean:
	$(foreach dir, $(PACKAGES), rm $(dir)/*.class;)

