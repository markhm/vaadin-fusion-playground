#!/bin/sh

mvn -Ptest-and-docs test jacoco:report site
