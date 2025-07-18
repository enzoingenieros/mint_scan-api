#!/bin/bash
# Wrapper script to run login.py with environment variables

# Get the directory where this script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Load environment variables from .env file if it exists
if [ -f "$SCRIPT_DIR/.env" ]; then
    export $(cat "$SCRIPT_DIR/.env" | xargs)
fi

# Run the login.py script with all arguments passed to this wrapper
python3 "$SCRIPT_DIR/login.py" "$@"