#!/usr/bin/env bash
helm dependency update
helm install --name=auth --namespace=webtree .