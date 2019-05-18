#!/usr/bin/env bash
ENCODER_PASSWORD=$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 100 ; echo '')
ENCODER_SALT=$(head /dev/urandom | tr -dc a-e0-9 | head -c 40 ; echo '')
JWS_SECRET=$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 200 ; echo '')
printf ${ENCODER_PASSWORD} > encoderPassword
printf ${ENCODER_SALT} > encoderSalt
printf ${JWS_SECRET} > jwtSecret