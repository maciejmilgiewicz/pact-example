#!/usr/bin/env bash

docker container run -d \
  --name pact-postgres \
  --rm \
  -e POSTGRES_PASSWORD=pact-postgres \
  -p 15432:5432 \
  postgres

docker container run -d \
  --name pact-broker \
  --rm \
  -e PACT_BROKER_DATABASE_USERNAME=postgres \
  -e PACT_BROKER_DATABASE_PASSWORD=pact-postgres \
  -e PACT_BROKER_DATABASE_HOST=host.docker.internal \
  -e PACT_BROKER_DATABASE_NAME=postgres \
  -e PACT_BROKER_DATABASE_PORT=15432 \
  -p 9292:9292 \
  pactfoundation/pact-broker
