#!/bin/sh
#
BASE_PATH=$PWD
find ./ -type f -name "*.xml" | xargs sed -i -e "s#BASE_PATH#$BASE_PATH#g"
echo " Current DIER : $BASE_PATH"


export PROJLIBS=$BASE_PATH/libs
export WEATHERPID="$BASE_PATH/pid/weather.pid"
export WEATHERDIR="$BASE_PATH/logs/"
export STDLOG="$WEATHERDIR/std.log"
export CURRENT_TIME=`date +%y%m%d-%H%M%S`

JAVA_HOME=/home/jboss/tool/jdk1.8.0_131

CLASSPATH="$PROJLIBS/infinispan-core-8.2.4.Final.jar:\
$PROJLIBS/infinispan-commons-8.2.4.Final.jar:\
$PROJLIBS/jgroups-3.6.10.Final.jar:\
$PROJLIBS/jboss-transaction-api_1.1_spec-1.0.1.Final.jar:\
$PROJLIBS/jboss-marshalling-osgi-1.4.10.Final.jar:\
$PROJLIBS/jboss-logging-3.3.0.Final.jar:\
$PROJLIBS/log4j-1.2.16.jar:\
$PROJLIBS/infinispan-embedded-tutorial-1.0.0-SNAPSHOT.jar:\
$PROJLIBS/:\
"

#$SANDBOX/Java/bin:
echo "Using Classspath $CLASSPATH"

ARGV="$@"
cd $ORBPF
case $ARGV in
start)
        if [ -f $WEATHERPID= ]; then
                echo "PID file $WEATHERPID found, Daemon already running ..."
                echo "Exiting ..."
                exit 1
        fi

	    echo "Starting Weather App Service ..."
        echo "$CURRENT_TIME Starting Weather App Service ...."  >>   $WEATHERDIR/status

        $JAVA_HOME/bin/java -Xmn100M -Xms256M -Xmx256M -XX:SurvivorRatio=8 -Djava.net.preferIPv4Stack=true -Djgroups.bind_addr=127.0.0.1 -classpath $CLASSPATH infinispan.tutorial.embedded.WeatherAppTest > $STDLOG 2>&1 &


         echo $! > $WEATHERPID
        ERROR=$?
        ;;
 stop)
        echo "Stopping $prog ..."
        echo "$CURRENT_TIME Stopping $prog ..." >>   $WEATHERDIR/status
        kill `cat $WEATHERPID`
        rm -f $WEATHERPID
        ;;
*)

;;

esac

