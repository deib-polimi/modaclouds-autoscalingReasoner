#runShort.sh
#!/bin/bash
#

cd %3$s

indexnIaaSMAX=%5$d
indexnIaaS=1

base="second"
input="$base/input"
output="$base/output"
data="$base/data"

while [ $indexnIaaS -le $indexnIaaSMAX ]
do
	before="$(date +%%s)"
	date
	
	# Remove previous results
	rm $output/rawData/*/*
    rm $output/rawGlobal/*

	dest="$data/IaaS_"$indexnIaaS"/rawGlobal"
	orig="$data/IaaS_"$indexnIaaS
	outputFileName="amplOut_"$indexnIaaS".out";
	timeFileName="time_"$indexnIaaS".txt";
	
	mkdir -p $dest

	#Copy of the dat file created by the parser in "$input/"
	cp $orig/staticInput.dat $input/
    cp $orig/dynamicInput.dat $input/


	#Execution of the test
	echo "Provider " $indexnIaaS"..."
	bash ./runAmpl $base/model.run > $dest/$outputFileName
	echo "...done."


	after="$(date +%%s)"
	elapsed_seconds="$(expr $after - $before)"
	echo "Elapsed time:" $elapsed_seconds
	echo $elapsed_seconds >  $dest/$timeFileName

	#Copy output file in the IaaS folder
	cp -r $output/* $orig/

	indexnIaaS=`expr $indexnIaaS + 1`
done
