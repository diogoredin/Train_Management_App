all:
	javac -cp po-uilib.jar:. `find mmt -name *.java`

run:
	java -cp po-uilib.jar:. mmt.app.App

clean:
	rm -rf PO_Project/* `find ./mmt -name "*.class"`

docs:
	javadoc -cp po-uilib.jar:. `find mmt -name *.java` -d docs

jar:
	jar cvf mmt.jar .