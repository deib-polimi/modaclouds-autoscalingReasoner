MODAClouds AutoscalingReasoner
=======================================

#Description
AutoscalingReasoner is a self-adaptation tool, developed in the context of the [MODAClouds](http://www.modaclouds.eu/) project, for multi-Cloud applications . Currently it is able to manage IaaS-based applications during the runtime, enacting a self-adaptation mechanism by means of a Virtual Machines autoscaling policy. This policy is realized exploiting a receding horizon control, based on the solution of a resource allocation optimization, formulated as a MILP (Mixed Integer Linear Programming) problem. For more information about the autoscaling policy you can refer to the following publication:

http://weblab.ing.unimo.it/papers/MICAS2014.pdf



#Installation

In order to install AutoscalingReasoner you need to have Java 7, maven and git installed and to run the following commands:

```
git clone https://github.com/deib-polimi/modaclouds-autoscalingReasoner.git
cd modaclouds-autoscalingReasoner
mvn clean install
```

#Usage


#Examples
