FROM openjdk:8-jre

WORKDIR /home/root

ENV TOMCAT_VERSION=7.0.76 \
    TOMCAT_MAJOR_VERSION=7 \
    DROOLS_TESTER_VERSION=0.0.3 \
    DROOLS_TESTER_TESTS=/home/root/tests

RUN apt-get update && \
  apt-get -y install unzip

ADD http://apache-mirror.rbc.ru/pub/apache/tomcat/tomcat-${TOMCAT_MAJOR_VERSION}/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.zip apache-tomcat.zip

RUN unzip apache-tomcat.zip && \
  mv apache-tomcat-${TOMCAT_VERSION} apache-tomcat && \
  rm -rf  apache-tomcat/webapps/ROOT && \
  chmod 777 -R apache-tomcat

RUN echo "drools.tester.tests=${DROOLS_TESTER_TESTS}" >> apache-tomcat/conf/catalina.properties

ADD https://github.com/ailabitmo/drools-tester/releases/download/v${DROOLS_TESTER_VERSION}/drools-tester-v${DROOLS_TESTER_VERSION}.war apache-tomcat/webapps/ROOT.war

# For development
#ADD target/drools-tester-v${DROOLS_TESTER_VERSION}.war apache-tomcat/webapps/ROOT.war

CMD apache-tomcat/bin/startup.sh && tail -f apache-tomcat/logs/catalina.out
