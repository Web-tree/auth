#!/usr/bin/env bash
helm install --name=auth-dev --namespace=webtree-dev . -f values.dev.yaml
