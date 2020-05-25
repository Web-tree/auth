#!/usr/bin/env bash
ENCODER_PASSWORD=$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 100 ; echo '')
ENCODER_SALT=$(head /dev/urandom | tr -dc a-e0-9 | head -c 40 ; echo '')
JWS_SECRET=$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 200 ; echo '')
openssl genpkey -algorithm RSA -out privateKey.pem -pkeyopt rsa_keygen_bits:4048
openssl rsa -pubout -in privateKey.pem -out publicKey.pem
printf ${ENCODER_PASSWORD} > encoderPassword
printf ${ENCODER_SALT} > encoderSalt
printf ${JWS_SECRET} > jwtSecret