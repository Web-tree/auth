#!/usr/bin/env bash
kubectl create secret generic auth --from-file=encoderPassword --from-file=encoderSalt --from-file=jwtSecret --from-file=repoToken --from-file=repoUrl --from-file=privateKey.pem --from-file=publicKey.pem --namespace=webtree
