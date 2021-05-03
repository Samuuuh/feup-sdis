VERSION = 1

CC = javac

find_dir = $(wildcard $(dir)/*.java)
find_class = $(wildcard $(dir)/*.class)
PACKAGES := src/etc src/node src/Server
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

