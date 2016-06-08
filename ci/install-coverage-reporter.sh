#!/bin/bash

if [[ -d "~/.jpm/jpm" ]]; then
    sudo cp -r ~/.jpm/jpm /var/
fi

if [[ -d "~/.jpm/bin" ]]; then
    sudo cp ~/.jpm/bin/* /usr/local/bin/
fi

if [[ ! $(builtin command -v jpm) ]]; then
  curl https://www.jpm4j.org/install/script | sudo sh
fi

if [[ ! $(builtin command -v codacy-coverage-reporter) ]]; then
  sudo jpm install com.codacy:codacy-coverage-reporter:assembly
fi

sudo mkdir -p ~/.jpm/bin

if [[ ! -d "~/.jpm/jpm" ]]; then
  sudo cp -r /var/jpm ~/.jpm/
fi

if [[ ! -d "~/.jpm/bin" ]]; then
  sudo cp /usr/local/bin/{jpm,codacy-coverage-reporter} ~/.jpm/bin/
fi
