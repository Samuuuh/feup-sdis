VERSION = 1

CC = javac

SRC := src  
find_dir = $(wildcard $(($(SRC)dir)/*.java)
find_class = $(wildcard $($(SRC)dir)/*.class)
PACKAGES := 
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
