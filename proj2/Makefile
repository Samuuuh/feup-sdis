VERSION = 1

CC = javac

SRC = src
JAVA_FILES = $(wildcard $(SRC)/*.java $(SRC)/*/*.java $(SRC)/*/*/*.java $(SRC)/*/*/*/*.java)
CLASSES = $(patsubst %.java, %.class, $(JAVA_FILES))

.PHONY: all
# App.Main compilation.
all: $(JAVA_FILES)
	@$(CC) $(JAVA_FILES) -d out
	@echo Folders built

.PHONY: clean
clean:
	@rm -r out
	@echo Build folder removed with success!

