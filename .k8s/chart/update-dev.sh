#!/usr/bin/env bash
helm upgrade auth --recreate-pods . -f values.dev.yaml
