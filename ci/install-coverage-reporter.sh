#!/bin/bash

if [[ ! $(builtin command -v jpm) ]]; then
  curl https://www.jpm4j.org/install/script | sudo sh
fi

if [[ ! $(builtin command -v codacy-coverage-reporter) ]]; then
  sudo jpm install com.codacy:codacy-coverage-reporter:assembly
fi
