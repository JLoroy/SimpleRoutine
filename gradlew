#!/usr/bin/env sh

# Copyright 2015 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# This script is a wrapper around the Gradle command. It sets up the
# environment and passes the command line arguments to the Gradle wrapper.

# Get the path of the script
PRG="$0"

# Resolve any symlinks to the script
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

# Get the path to the Gradle wrapper
saveddir=`pwd`
GRADLE_WRAPPER=`dirname "$PRG"`/gradlew

# Change to the directory containing the script
cd "`dirname \"$PRG\"`"

# Run the Gradle wrapper with the command line arguments
exec "$GRADLE_WRAPPER" "$@"

# Change back to the original directory
cd "$saveddir"