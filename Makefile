all:
	javac -cp po-uilib.jar:. `find mmt -name *.java`

run:
	java -Dimport=default.imp -cp po-uilib.jar:. mmt.app.App

test:
	sh runtests.sh

clean:
	rm -rf PO_Project/* `find ./mmt -name "*.class"`
	rm -rf PO_Project/* `find ./ -name "*.diff"`
	rm -rf PO_Project/* `find ./ -name "*.outhyp"`
	rm -rf PO_Project/* `find ./ -name "*.ser"`
	clear

docs:
	javadoc -cp po-uilib.jar:. `find mmt -name *.java` -d docs

jar:
	jar cvf mmt.jar .