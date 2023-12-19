#!/bin/bash
POM=$GL_PATH/gl38/pom.xml
mvn clean -f $POM & mvn compile -f $POM & mvn test-compile -f $POM
