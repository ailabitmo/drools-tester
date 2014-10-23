Drools Tester
=============

A web app that automatically runs verification tests against a [Drools](http://www.drools.org/) rules package created in Drools Guvnor.

Drools [5.6.0.Final](http://download.jboss.org/drools/release/5.6.0.Final/) is supported.

Build from source code
-------------
1. Clone the repository:

        git clone git@github.com:ailabitmo/drools-tester.git

2. Compile and package to `.war` file:

        mvn clean install

3. Find the `.war` file in `/target/` folder.

Installation
-------------
1. Compile the app from source code

2. Copy the `.war` file in `/webapp/` folder in [Tomcat 7](http://tomcat.apache.org/download-70.cgi)

3. Create `tests` folder in Tomcat 7's root folder and a folder for each test, e.g. `assignment-<test number>`. Example folder structure:

        /opt/tomcat7/tests/
        /opt/tomcat7/assignment-1/
        /opt/tomcat7/assignment-2/
        ...

3. Run Tomcat 7

Contacts
-------------
Maxim Kolchin (kolchinmax@gmail.com)
