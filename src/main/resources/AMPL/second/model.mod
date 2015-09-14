#Modello del sistema.

#Set

param nClasses; #number of classes

set K=1..nClasses; #set of classes

#General parameters

param epsilon; #precision

param Tlong; #long term time horizon, measure in hours

param Tshort; #short term time horizon, measure in hours

param Tslot; #duration of one timeslot, measure in minutes

#param nTshort; #number of time intervals Tshort within the period Tlong

param nTslot; #number of time intervals Tslot within the period Tlong

param nTon; #number of time intervals Tslot within the period Tshort

param Tw integer; #number of timeslot of the sliding window

param C>0; #capacity of 1 VM instance

param Lambda{k in K, t in 1..(nTslot+4)}>=0; #real arrival rate of class k requests

param lpred{k in K, t in 1..nTon}; #prediction of workload

param mu{k in K}>=0; #maximum service rate of 1 VM instance for class k requests

param delta; #cost of 1 on-demand VM instance for one hour

param rho; #cost of 1 flat VM instance for one hour

param D{k in K}>=0; #delay of the delay center in sec

param Rcross{k in K}>=0; #response time threshold

param Mres{k in K, t in 1..nTslot}>=0 integer; #general parameter of the number of flat VM allocated in each timeslot t, for class k request

param Mond{k in K, t in 1..nTslot}>=0 integer; #general parameter of the number of on-demand VM allocated in each timeslot t, for class k request

param Nres{k in K, t in 1..(nTslot+nTon+1)}>=0 integer; #general parameter of the number of free flat VM instances for class k requests, at timeslot t

param Nond{k in K, t in 1..(nTslot+nTon+1)}>=0 integer; #general parameter of the number of free on-demand VM instances for class k requests, at timeslot t

param r_bar{k in K, t in 1..Tw}>=0 integer;  #number of free flat VM instances for class k request, at timeslot t

param d_bar{k in K, t in 1..Tw}>=0 integer;  #number of free on-demand VM instances for class k request, at timeslot t

param ns{t in 1..Tw}; #noise

param W; #maximum number of instances at the same timeslot for all class k


#Var

var r{k in K, t in 1..Tw}>=0 integer; #number of flat VM instances to allocate for class k requests, at timeslot t

var d{k in K, t in 1..Tw}>=0 integer; #number of on-demand VM instances to allocate for class k requests, at timeslot t


#Constraints

subject to equilibrium {k in K, t in 1..Tw}: sum{tau in 1..t} (r[k,tau] + d[k,tau]) >= ((lpred[k,t] / (C*mu[k])) - r_bar[k,t] -d_bar[k,t] - epsilon);

subject to Rthreshold {k in K, t in 1..Tw}: sum{tau in 1..t} (r[k,tau] + d[k,tau]) >= -(r_bar[k,t] + d_bar[k,t]) - lpred[k,t] * (Rcross[k] - D[k]) / (1 - C*mu[k]*(Rcross[k] - D[k]));

subject to maxVm {t in 1..Tw}: sum{k in K}(r[k,t] + r_bar[k,t]) <= W;


#Objective function
minimize cost: sum{k in K} (rho*(sum{t in 1..Tw} r[k,t]) + delta*(sum{t in 1..Tw} d[k,t]));

