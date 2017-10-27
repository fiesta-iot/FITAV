#!/bin/bash
 
testerPath=/Users/path_to_tester/Test_Generator
scriptPath=/Users/path_to_script/Testing_Script
ptd=/Users/path_to_write_data/TestResults
OpenAmUser=_$_USERNAME_$_
OpenAmPassword=_$_PASSWORD_$_
FiestaInstanceURL=_$_FIESTA_URL_$_
FiestaInstanceUser=_$_FIESTA_USER_$_
FiestaUserMachinePassword=_$_FIESTA_USER_PASSWORD_$_
FiestaSudoMachinePassword=_$_FIESTA_SUDO_PASSWORD_$_

testNumb=1
queryPath=$scriptPath
dataFolder=/Data
queryFolder=/Queries
FiestaPrompName=$( echo $FiestaInstanceURL | cut -d '.' -f 1 )

rm -rf $ptd/*/
cd $scriptPath
while read testType var1 var2 var3 var4
do    

cd $ptd
mkdir "Test$testNumb"
folder="/Test$testNumb"
cd Test$testNumb
mkdir "Data"
mkdir "Queries"

cd $testerPath
case "$testType" in

random) 
    java -jar Test_Generator.jar random test -ResourceMaximum $var1 -ResourceMinimum $var2 -ObservationsPeriod $var3 -TimeInterval $var4 -PathToData $ptd$folder$dataFolder
        ;;
*)
    continue
    ;;
esac

#delete the local DB
/usr/bin/expect << End1
    #exp_internal 1     #debug flag
    set timeout 600
    spawn ssh $FiestaInstanceUser@$FiestaInstanceURL
    expect "password:"
    send "$FiestaUserMachinePassword\r"
    expect "$FiestaInstanceUser@$FiestaPrompName:"
    send "sudo service wildfly stop\r"
    expect "assword for $FiestaInstanceUser:"
    send "$FiestaSudoMachinePassword\r"
    expect { 
        "Stopping WildFly Application Server wildfly * OK" { 
            send "sudo rm -rf /var/opt/wildfly/iot-registry/triple-store/* \r"
        }
        "WildFly Application Server is not running" {
            send "sudo rm -rf /var/opt/wildfly/iot-registry/triple-store/* \r"
        }
    }
    expect "$FiestaInstanceUser@$FiestaPrompName:"
    send "sudo service wildfly restart \r"
    expect {
        "Starting WildFly Application Server wildfly * OK" {
            set control 1;
            send "logout \r"
        }
        timeout {
             send \x03 ; expect -re ".*" ; send "sudo service wildfly restart \r" ;  exp_continue 
        }         
    }
    expect eof
End1

printf "\nSubmit testbeds\n\n"
resp1=$(curl  -s -o /dev/null -w %{http_code} -X POST -H "Content-Type:text/plain" -d "$ptd$folder$dataFolder/Testbeds.txt" http://localhost:8080/Adapter/AutomatedInsert/Testbeds )
if [ $resp1 -eq 000 ] 
then 
    echo "Adapter is not running"
    exit
fi
printf "\nSubmit devices\n\n"
curl -s -o /dev/null -X POST -H "Content-Type:text/plain" -d "$ptd$folder$dataFolder/Devices.txt" http://localhost:8080/Adapter/AutomatedInsert/Devices  

printf "\nSubmit observations\n\n"
curl -s -o /dev/null -X POST -H "Content-Type:text/plain" -d "$ptd$folder$dataFolder/Observations.txt" http://localhost:8080/Adapter/AutomatedInsert/Observations

printf "\nIformation is ready to make queries!!\n"

authRequest=$( curl -s -X POST -H "X-OpenAM-Username:$OpenAmUser" -H "X-OpenAM-Password:$OpenAmPassword" https://$FiestaInstanceURL/openam/json/authenticate )
authentication=$( echo $authRequest | cut -d '"' -f 4 )

printf "\nAll status query\n"
curl  -H "iPlanetDirectoryPro:$authentication" -H "Content-Type:text/plain" -H "Accept:application/json" -d "@$queryPath/globalStatusQuery.txt" -o "$ptd$folder$queryFolder/globalStatusOutput.json" https://$FiestaInstanceURL/iot-registry/api/queries/execute/global

printf "\nNumber of observations that match the query\n"
curl  -H "iPlanetDirectoryPro:$authentication" -H "Content-Type:text/plain" -H "Accept:application/json" -d "@$queryPath/FiestaQuery.txt" -o "$ptd$folder$queryFolder/FiestaQueryOutput.json" https://$FiestaInstanceURL/iot-registry/api/queries/execute/global

cd $ptd/Test$testNumb

path=$(pwd)

GeneratedFile=$path/Data/Observations2Compare.json
FiestaQueryResult=$path/Queries/FiestaQueryOutput.json

result=$(json-diff $FiestaQueryResult $GeneratedFile)

cd ..

echo -n ";$([ -z "$result" ] && echo "Pass" || echo "Fail")" >> TestsData.csv

printf "\n Test nrº $testNumb is complete \n"

((testNumb++))

done < Configs.txt

printf "\nDone!!\n"
exit