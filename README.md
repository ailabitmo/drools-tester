Drools Tester
=============

A web app that automatically runs verification tests against a [Drools](http://www.drools.org/) rules package created in Drools Guvnor.

Drools [5.6.0.Final](http://download.jboss.org/drools/release/5.6.0.Final/) is supported.

Build from source code
-------------
1. Clone the repository:

        git clone git@github.com:ailabitmo/drools-tester.git
        
2. Create MySQL database and configure the connection. Take a look at "Configure database" section below.

3. Compile and package to `.war` file:

        mvn clean install

4. Find the `.war` file in `/target/` folder.

Installation
-------------
1. Create and configure MySQL. Take a look at "Configure database" section below.

2. Compile the app from source code or download the latest pre-packaged `.war` file.

3. Copy the `.war` file in `/webapp/` folder in [Tomcat 7](http://tomcat.apache.org/download-70.cgi)

4. Create `tests` folder in Tomcat 7's root folder and a folder for each test, e.g. `assignment-<test number>`. Example folder structure:

        /opt/tomcat7/tests/
        /opt/tomcat7/assignment-1/
        /opt/tomcat7/assignment-2/
        ...

5. Run Tomcat 7

Configure database
-------------
By default the application connects to MySQL on `localhost` to `dt-db` database with `dt-user` username and `dt-password` password. The all tables are created automatically.

If you want to change the database settings then edit `src/main/resources/META-INF/persistence.xml` file and recompile the application.

Contacts
-------------
Maxim Kolchin (kolchinmax@gmail.com)
