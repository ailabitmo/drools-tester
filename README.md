Drools Tester
=============

A web app that automatically runs verification tests against a [Drools](http://www.drools.org/) rules package created in Drools Guvnor.

Drools [5.6.0.Final](http://download.jboss.org/drools/release/5.6.0.Final/) is supported. More details about Drools Guvnor can be found in [documentation](http://docs.jboss.org/drools/release/5.6.0.Final/drools-guvnor-docs/html_single/).

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
---------------------------
By default the application connects to MySQL on `localhost` to `dt-db` database with `dt-user` username and `dt-password` password. The all tables are created automatically.

If you want to change the database settings then edit `src/main/resources/META-INF/persistence.xml` file and recompile the application.

## Write a test
A verification test is set of files containing one `models.json` file and several `test<number>.json` files, e.g. `test1.json`, `test2.json`. Each verification test should be placed in a folder with `assignment-<number>` name in the `${CATALINA.HOME}/tests` folder.

### Specification for `models.json`
Sets requirements to classes and their fields, such the class name, the name and the type of a field. Some supported types: BigDecimal, String, Boolean and etc. More in [documentation](http://docs.jboss.org/drools/release/5.6.0.Final/drools-guvnor-docs/html_single/). An example:

    [
        {
          "className": "Input",
          "fields": {
              "id": "BigDecimal",
              "color": "String"
          }
      },
      {
          "className": "Output",
          "fields": {
              "isTarget": "Boolean"
          }
      }
    ]

### Specification for `test<number>.json`
This file consists of two parts: `insertions` and `expectations`. The `insertions` is a list of instances that are inserted in the working memory, each instance have:

* `className` - name of the class
* `variable` - name of the variable that the instance is assigned to
* `values` - list of the fields and its values

The `expectations` is a list of instances that are expected to be in the working memory at the end of execution.

An example:

    {
        "insertions": [
            {
                "className": "Input",
                "variable": "input",
                "values": {
                    "constancy": 0.5,
                    "consistency": 2,
                    "color": "белый"
                }
            }
        ],
        "expectations": [
            {
                "className": "Output",
                "values": {
                    "isTarget": false
                }
            }
        ]
    }

Contacts
-------------
Maxim Kolchin (kolchinmax@gmail.com)
