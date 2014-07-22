libpomdp
--------

libpomdpis an implementation of different offline and
online Partially Observable Markov Decision Process (POMDP)
approximation algorithms. The code is a combination of Java,
Matlab, and some Jython.

libpomdp has different dependencies, according to what algorithm you
want to run:

- the Matlab implementation of [1], 
- the Symbolic Perseus Package [5],
- matrix-toolkits-java [9].

libpomdp was started by Diego Maniloff at the University of Illinois
at Chicago and is now being jointly developed with Mauricio Araya from
INRIA at Nancy. We always welcome POMDP researchers to fork the
project and help us out.

Copyright (c) 2009, 2010, 2011 Diego Maniloff.  
Copyright (c) 2010, 2011 Mauricio Araya.


Contents
--------

- Getting Started
- Implemented algorithms
- Documentation
- References

Getting Started
---------------
```
$ git clone git@github.com:dmaniloff/libpomdp.git
$ cd libpomdp
$ ant dist
```

Implemented algorithms
----------------------
On its way.

Documentation
-------------
On its way.

Some References (growing list)
------------------------------
[1] Spaan, M. T.J, and N. Vlassis. "Perseus: Randomized point-based
value iteration for POMDPs." Journal of Artificial Intelligence
Research 24 (2005): 195-220.

[2] Ross, S., J. Pineau, S. Paquet, and B. Chaib-draa. "Online
planning algorithms for POMDPs." Journal of Artificial Intelligence
Research 32 (2008): 663-704.

[4] Hansen, Eric A. "Solving POMDPs by Searching in Policy Space"
(1998): 211-219.

[5] Poupart, Pascal. "Exploiting structure to efficiently solve large
scale partially observable markov decision processes." University of
Toronto, 2005.

[6] Milos Hauskrecht, "Value-function approximations for partially
observable Markov decision processes." Journal of Artificial
Intelligence Research (2000).

[7] T. Smith and R. Simmons, "Heuristic search value iteration for
POMDPs." in Proceedings of the 20th conference on Uncertainty in
artificial intelligence, 2004, 520-527.

[8] Universal Java Matrix Package, http://www.ujmp.org/

[8] matrix-toolkits-java, http://code.google.com/p/matrix-toolkits-java/
