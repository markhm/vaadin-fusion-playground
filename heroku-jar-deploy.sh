#!/bin/sh

echo
echo "Deploying to Vaadin Fusion Playground JAR on Heroku"
echo

mvn clean -Pheroku-jar heroku:deploy
