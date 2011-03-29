#!/bin/sh
# Copyright 2010-present Local Matters, Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
QUOTED_ARGS=""
JPDA_OPTS="";
while [ "$1" != "" ] ; do
  if [ "$1" = "jpda" ]; then
      if [ -z "$JPDA_TRANSPORT" ]; then
        JPDA_TRANSPORT="dt_socket"
      fi
      if [ -z "$JPDA_ADDRESS" ]; then
        JPDA_ADDRESS="8000"
      fi
      if [ -z "$JPDA_OPTS" ]; then
        JPDA_OPTS="-Xdebug -Xrunjdwp:transport=$JPDA_TRANSPORT,address=$JPDA_ADDRESS,server=y"
      fi
  fi
  QUOTED_ARGS="$QUOTED_ARGS $1"
  shift
done

# OS specific support.  $var _must_ be set to either true or false.
cygwin=false;
darwin=false;
case "`uname`" in
  CYGWIN*) cygwin=true ;;
  Darwin*) darwin=true
           if [ -z "$JAVA_VERSION" ] ; then
             JAVA_VERSION="CurrentJDK"
           else
             echo "Using Java version: $JAVA_VERSION"
           fi
           if [ -z "$JAVA_HOME" ] ; then
             JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/${JAVA_VERSION}/Home
           fi
           ;;
esac

if [ -z "$JAVA_HOME" ] ; then
  if [ -e /etc/gentoo-release ] ; then
    JAVA_HOME=`java-config --jre-home`
  fi
fi

if [ -z "$CF_HOME" ] ; then
  ## resolve links - $0 may be a link to content-feeders's home
  PRG="$0"

  # need this for relative symlinks
  while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
      PRG="$link"
    else
      PRG="`dirname "$PRG"`/$link"
    fi
  done

  saveddir=`pwd`

  CF_HOME=`dirname "$PRG"`/..

  # make it fully qualified
  CF_HOME=`cd "$CF_HOME" && pwd`

  cd "$saveddir"
  # echo Using m2 at $CF_HOME
fi

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
  [ -n "$CF_HOME" ] &&
    CF_HOME=`cygpath --unix "$CF_HOME"`
  [ -n "$JAVA_HOME" ] &&
    JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
  [ -n "$CLASSPATH" ] &&
    CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

if [ -z "$JAVACMD" ] ; then
  if [ -n "$JAVA_HOME"  ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
      # IBM's JDK on AIX uses strange locations for the executables
      JAVACMD="$JAVA_HOME/jre/sh/java"
    else
      JAVACMD="$JAVA_HOME/bin/java"
    fi
  else
    JAVACMD=java
  fi
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly."
  echo "  We cannot execute $JAVACMD"
  exit 1
fi

if [ -z "$JAVA_HOME" ] ; then
  echo "Warning: JAVA_HOME environment variable is not set."
fi


# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  [ -n "$CF_HOME" ] &&
    CF_HOME=`cygpath --path --windows "$CF_HOME"`
  [ -n "$JAVA_HOME" ] &&
    JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
  [ -n "$HOME" ] &&
    HOME=`cygpath --path --windows "$HOME"`
fi

CLASSPATH=

for jar in "${CF_HOME}"/lib/*.jar; do
    if [ -z $CLASSPATH ] ; then
        CLASSPATH=$jar;
    else
        if $cygwin; then
            CLASSPATH=${CLASSPATH}\;$jar
        else
            CLASSPATH=${CLASSPATH}:$jar
        fi
    fi
done

MAIN_CLASS=org.localmatters.lesscss4j.cli.CompilerMain

exec "$JAVACMD" \
  $JAVA_OPTS \
  $JPDA_OPTS \
  -classpath "${CLASSPATH}" \
  "-Dcontent.feeder.home=${CF_HOME}"  \
  ${MAIN_CLASS} $QUOTED_ARGS
