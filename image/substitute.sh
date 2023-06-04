#!/bin/sh

SCHEMA=http://

if [ "$SSL_ENABLED" = "true" ]; then
    SCHEMA=https://
fi

export SCHEMA="${SCHEMA}"

envsubst < application.properties.tmpl > application.properties