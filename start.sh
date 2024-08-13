#!/bin/sh

echo "Warte auf MySQL..."
until nc -z -v -w30 mysql_gd_guild_manager 3306; do
  echo "Warte auf MySQL..."
  sleep 2
done

# Starte das Java-Programm
java -jar /app/DC-GuildManager-1.0.jar