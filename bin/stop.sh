#!/bin/bash
PID=$(ps -ef | grep knowledge-app-3.2.jar | grep -v grep | awk '{ print $2 }') 
if [ -z "$PID" ] 
then 
   echo Application is already stopped 
else 
   echo kill $PID
   kill $PID 
fi
