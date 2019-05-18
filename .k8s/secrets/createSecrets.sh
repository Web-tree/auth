#!/usr/bin/env bash
kubectl create secret generic auth-encoder --from-file=encoderPassword --from-file=encoderSalt --from-file=jwtSecret --namespace=webtree
