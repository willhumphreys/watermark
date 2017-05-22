#!/usr/bin/env bash

cd $(dirname $0)

dev_build() {
  ./gradlew build
  true
}

dev_run() {
  java -jar build/libs/watermark-0.0.1-SNAPSHOT.jar
}


docker_build() {
  docker build -t watermark:devtest .
}

docker_run() {
  docker run --rm -it -p 8080:8080 watermark:devtest
}


usage() {
  cat <<EOF
Usage:
  $0 <command> <args>
Local machine commands:
  dev_build        : builds and packages your app
  dev_run <file>   : starts your app in the foreground
Docker commands:
  docker_build     : packages your app into a docker image
  docker_run       : runs your app using a docker image
EOF
}

action=$1
action=${action:-"usage"}
action=${action/help/usage}
shift
if type -t $action >/dev/null; then
  echo "Invoking: $action"
  $action $*
else
  echo "Unknown action: $action"
  usage
  exit 1
fi
