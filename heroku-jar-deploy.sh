#!/bin/sh

echo
echo "Deploying to Vaadin Fusion Playground JAR on Heroku"
echo

# mvn clean package -Pheroku-jar heroku:deploy

mvn clean -Pheroku-jar heroku:deploy
