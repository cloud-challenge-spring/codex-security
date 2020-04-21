#!/usr/bin/env bash

java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /home/spring/app.jar --spring.profiles.active=$SPRING_PROFILES_ACTIVE