#!/usr/bin/env bash
helm dependency update
helm install auth --namespace=webtree .
