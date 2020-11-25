#!/bin/sh

echo
echo "Logs for vaadin-fusion-playground on Heroku"
echo

heroku logs --app vaadin-fusion-playground -n 5000
