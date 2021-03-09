javac -d teste Hello.java Server.java Client.java
java -classpath teste -Djava.rmi.server.codebase=file:teste/ src.hello.Server Hello &