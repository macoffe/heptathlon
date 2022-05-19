# heptathlon
Java application for sport shop workers, to create commands, add stock, show turnover and alllow clients to pay a bill. This project was made to learn the RMI middleware, it contains a central server that is queried by a shop server that is queried by a client.

# create mysql databases:

In project root folder, launch mysql, and use these commands:

		Source HeptathlonCentral.sql;
		Source HeptathlonMagasin.sql;

# compile code :

In each RMI_*/ directory:

		javac -d src/ *.java

# launch each application:

In RMI_server_central/:

		java -cp ../mysql-connector-java-8.0.28.jar:src/ ServerCentral

In RMI_server/:

		java -cp ../mysql-connector-java-8.0.28.jar:src/ Server

In RMI_client/:

		java -cp src/ Client
