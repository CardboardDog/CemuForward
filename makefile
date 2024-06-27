SOURCE := source
BUILD := build
OUT := dist
BIN := CemuForwarder
MANIFEST := manifest
all:
	rm -r -f $(BUILD)
	rm -r -f $(OUT)
	mkdir $(BUILD)
	mkdir $(OUT)
	javac -d $(BUILD) $(SOURCE)/*.java 
	cp $(MANIFEST).mf $(BUILD)/$(BIN).mf
	cd $(BUILD); jar cfm ../$(BIN).jar $(BIN).mf .
	mv $(BIN).jar $(OUT)/$(BIN).jar 