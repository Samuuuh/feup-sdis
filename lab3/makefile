VPATH = ./example
OPATH = ./

CC = javac 
CFLAGS = -g -d $(OPATH)

# Use "del" for windows. "rm" for linux. 
ifeq ($(OS),Windows_NT)
	RM:= del
else
	RM:= rm  
endif

CLIENT:= Client.java
SERVER:= Hello.java Server.java 
COMMON:=     
TARGETS:= $(CLIENT) $(SERVER) $(COMMON)

all: $(TARGETS)
	@$(CC) $(CFLAGS) $^
	@echo "All files generated successfully!" 

client: $(CLIENT) $(COMMON)
	@$(CC) $(CFLAGS) $^ 
	@echo "Client generated successfully!" 

server: $(SERVER) $(COMMON)
	@$(CC) $(CFLAGS) $^
	@echo "Server generated successfully!"  

.PHONY: clean 
clean: 
	@$(RM) *.class 
	@echo "All .class files were removed" 



