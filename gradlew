#!/bin/sh
  APP_HOME=`pwd -P`
  APP_BASE_NAME=`basename "$0"`
  DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

  for i in "$@"; do
    case "$i" in
      --)
        APP_ARGS=${APP_ARGS:+"$APP_ARGS "}"$@"
        break
      ;;
    esac
  done

  CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
  JAVACMD=java
  exec "$JAVACMD" $DEFAULT_JVM_OPTS -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
  