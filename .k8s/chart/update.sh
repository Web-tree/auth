#!/usr/bin/env bash
helm dependency update
helm upgrade --recreate-pods auth .
