#runShort.sh
#!/bin/bash
#

indexnIaaSMAX=$1
indexnIaaS=1

base="second"
input="$base/input"
output="$base/output"
data="$base/data"

while [ $indexnIaaS -le $indexnIaaSMAX ]
do
	before="$(date +%%s)"
	date

	dest="$data/IaaS_"$indexnIaaS"/rawGlobal"
	orig="$data/IaaS_"$indexnIaaS
	outputFileName="amplOut_"$indexnIaaS".out";
	timeFileName="time_"$indexnIaaS".txt";

	#Copy of the dat file created by the parser in "$input/"
	cp $orig/staticInput.dat $input/
    cp $orig/dynamicInput.dat $input/
	cp $orig/initialVM.dat $input/


	#Execution of the test
	echo "Provider " $indexnIaaS"..."
	bash ./runAmpl $base/model.run > $dest/$outputFileName
	echo "...done."


	after="$(date +%%s)"
	elapsed_seconds="$(expr $after - $before)"
	echo "Elapsed time:" $elapsed_seconds
	echo $elapsed_seconds >  $dest/$timeFileName


	#Removing the dat file
	rm $input/*

	#Copy output file in the IaaS folder
	cp -r $output/* $orig/
	rm $output/rawData/*/*
	rm $output/rawGlobal/*

	indexnIaaS=`expr $indexnIaaS + 1`
done
