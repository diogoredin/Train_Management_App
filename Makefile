all:
	javac -cp po-uilib.jar:. `find mmt -name *.java`

run:
	java -Dimport=default.imp -cp po-uilib.jar:. mmt.app.App

test:
	java -Dimport=test.imp -Dout=test.outhyp -cp po-uilib.jar:. mmt.app.App

clean:
	rm -rf PO_Project/* `find ./mmt -name "*.class"`
	clear

docs:
	javadoc -cp po-uilib.jar:. `find mmt -name *.java` -d docs

jar:
	jar cvf mmt.jar .