#!/usr/bin/env bash
helm dependency update
helm upgrade auth . -n webtree
