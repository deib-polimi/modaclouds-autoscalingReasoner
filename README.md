MODAClouds AutoscalingReasoner
=======================================

#Description
AutoscalingReasoner is a self-adaptation tool, developed in the context of the [MODAClouds](http://www.modaclouds.eu/) project, for multi-Cloud applications . Currently it is able to manage IaaS-based applications during the runtime, enacting a self-adaptation mechanism by means of a Virtual Machines autoscaling policy. This policy is realized exploiting a receding horizon control, based on the solution of a resource allocation optimization, formulated as a MILP (Mixed Integer Linear Programming) problem. This problem calculates the optimal number of virtual machines to allocate at each timestep ahead considered within a given optimization window based on a workload prediction, with the aim of minimizing the overall allocation cost while respecting the QoS requirements specified as thresholds over the average service response time. For more information about the autoscaling policy you can refer to the following [link](http://weblab.ing.unimo.it/papers/MICAS2014.pdf).





#Installation

In order to install AutoscalingReasoner you need to have Java 7, maven and git installed and to run the following commands:

```
git clone https://github.com/deib-polimi/modaclouds-autoscalingReasoner.git
cd modaclouds-autoscalingReasoner
mvn clean install
```

#Usage

After that AutoscalingReasoner has been installed you can run it using the following commands, that also configure the required environmental variables, from the root of your local repository:

```
export OWN_IP=54.54.54.54
export LISTENING_PORT=81790
export PATH_TO_DESIGN_TIME_MODEL=./SP4OpsInitialModel.xml
export SSH_USER_NAME=fakeuser
export SSH_HOST=fakehost
export SSH_PASSWORD=fakepass
export OPTIMIZATION_LAUNCHER=./path-to-optimization-start-script
export OPTIMIZATION_INPUT_FOLDER=./path-to-optimization-input-foler
export OPTIMIZATION_OUTPUT_FILE=./path-to-optimization-solution-file
export CLOUDML_WEBSOCKET_IP=54.54.54.54
export CLOUDML_WEBSOCKET_PORT=9000
cd target
java -jar autoscaling-reasoner.jar
```

The required environmental variables can be also be specified with standard parameters. If you want to have a look at the variables meaning you can type:

```
java -jar autoscaling-reasoner.jar -help
```

AutoscalingReasoner is also able to retrieve his configuration from system properties or from a configuration file. The look up order if the following: 2) environmental variables 3) system properties 1) config file 4) executable parameters.

The initial adapation model file, whose path is specified using the PATH_TO_INITIAL_ADAPTATION_MODEL argument, is an xml file compliant with the XSD schema which can be found at https://github.com/deib-polimi/modaclouds-autoscalingReasoner/blob/master/src/main/resources/schemas/sarRuntimeMetamodel.xsd . An instance of this model is the following:

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<containers speedNorm="1200.0" timestepDuration="10" optimizationWindowsLenght="5">
    <container id="capacity6.5Container" capacity="6.5" nCore="2" processingRate="300.0" vmType="m3.large" maxReserved="0" reservedCost="0.0" onDemandCost="0.150">
        <applicationTier id="MIC">
            <responseTimeThreshold hour="0" value="4.152226191014051437"/>
            <responseTimeThreshold hour="1" value="4.152226191014051437"/>
            <responseTimeThreshold hour="2" value="4.152226191014051437"/>
            <responseTimeThreshold hour="3" value="4.152226193342357874"/>
            <responseTimeThreshold hour="4" value="4.152226191014051437"/>
            <responseTimeThreshold hour="5" value="4.152226191014051437"/>
            <responseTimeThreshold hour="6" value="4.15226191014051437"/>
            <responseTimeThreshold hour="7" value="4.1522226193342357874"/>
            <responseTimeThreshold hour="8" value="4.152226191014051437"/>
            <responseTimeThreshold hour="9" value="4.152226191014051437"/>
            <responseTimeThreshold hour="10" value="4.152226191014051437"/>
            <responseTimeThreshold hour="11" value="4.152226191014051437"/>
            <responseTimeThreshold hour="12" value="4.152226191014051437"/>
            <responseTimeThreshold hour="13" value="4.152226191014051437"/>
            <responseTimeThreshold hour="14" value="4.152226191014051437"/>
            <responseTimeThreshold hour="15" value="4.152226193342357874"/>
            <responseTimeThreshold hour="16" value="4.152226191014051437"/>
            <responseTimeThreshold hour="17" value="4.152226191014051437"/>
            <responseTimeThreshold hour="18" value="4.152226191014051437"/>
            <responseTimeThreshold hour="19" value="4.152226193342357874"/>
            <responseTimeThreshold hour="20" value="4.152226191014051437"/>
            <responseTimeThreshold hour="21" value="4.152226191014051437"/>
            <responseTimeThreshold hour="22" value="4.152226191014051437"/>
            <responseTimeThreshold hour="23" value="4.152226191014051437"/>
            <functionality id="register"/>
            <functionality id="saveAnswers"/>
            <functionality id="answerQuestions"/>
        </applicationTier>
    </container>
</containers>
```

In this example AutoscalingReasoner will manage a signle ```<applicationTier>``` composed of multiple ```<functionality>```, which are basically web services, and of a set of 24 ```<responseTimeThreshold>```, one for each hour of the day, that have to be respected. Application tiers are grouped by the capacity of the VM type on which they are hosted at runtime into multiple ```<containers>```. Each ```<container>``` is characterized with some attributes concerning a specific VM type. For each container AutoscalingReasoner builds and solves an optimization model, currently calling a CPLEX server, and enacts the scaling actions derived from the solution. The initial model is also globally characterized by a timestepDuration, which indicate how often (in minutes) the adaptation process has to be performed, and and optimizationWindowLenght, which indicate the nuber of timesteps ahead to consider in the receding horizon control.

##License##

Licensed under the [Apache License, Version 2.0][1]

[1]: http://www.apache.org/licenses/LICENSE-2.0
