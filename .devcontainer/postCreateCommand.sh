#!/bin/bash

if ! command -v devbox &> /dev/null
then
    echo "Devbox could not be found, installing..."
    curl -fsSL https://get.jetify.com/devbox | bash -s -- --force    
    export PATH="$HOME/.devbox/bin:$PATH"
    echo "Installing devbox packages..."
    mkdir -p /nix
    sudo chown -R $(whoami) /nix
    yes | devbox install 
    sudo chown -R $(whoami) /nix
fi  
