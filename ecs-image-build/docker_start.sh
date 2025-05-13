#!/bin/bash
#
# Start script for company-profile-delta-consumer

PORT=8080
exec java -jar -Dserver.port="${PORT}" "company-profile-delta-consumer"
