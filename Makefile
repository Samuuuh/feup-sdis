VERSION = 1

CC = javac

find_dir = $(wildcard $(dir)/*.java)
find_class = $(wildcard $(dir)/*.class)
PACKAGES := src/ src/app/ src/service src/service/etc src/service/client src/service/message src/service/node src/service/server/com src/service/utils
CLASSES = $(foreach dir, $(PACKAGES), $(find_class))
FILES = $(foreach dir, $(PACKAGES), $(find_dir))

.PHONY: all
# App.Main compilation.
all: $(CLASSES)
	@$(CC) $(FILES) -d out
	@echo Folders built

.PHONY: clean
clean:
	@rm -r out
	@echo Build folder removed with success!

