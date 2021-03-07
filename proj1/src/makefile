
CC = javac


find_dir = $(wildcard $(dir)/*.java)
find_class = $(wildcard $(dir)/*.class)
PACKAGES := main file factory channel
CLASSES = $(foreach dir, $(PACKAGES), $(find_class))
FILES = $(foreach dir, $(PACKAGES), $(find_dir))

define NEWLINE

endef


.PHONY: all
all: $(CLASSES)
	$(CC) $(FILES)

.PHONY: clean
clean:
	$(foreach dir, $(PACKAGES), rm $(dir)/*.class;)

