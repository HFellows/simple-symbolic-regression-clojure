#This is an assignment for CSci 4409 (Programming for Parallel Architectures)
Henry Fellows && Dalton Gusaas

##Baseline on Multivac (i7-4790)

real	1m52.013s

user	2m12.227s

sys	0m1.286s

##Pmap on score-population:

real	0m54.179s

user	3m13.988s

sys	0m3.958s

##Pmap on score-population and uniform-crossover:

real	1m9.619s

user	4m5.698s

sys	0m8.542s

------------------------

real	0m55.830s

user	3m24.403s

sys	0m7.007s


As you can see, not much faster, but it does limit the thrashing in the startup (watch system monitor).

------------------------

##Pmap on score-population and r/reduce with r/map on total-score-on:

real	0m56.648s

user	3m28.797s

sys	0m3.794s

------------------------

real	0m51.014s

user	3m9.447s

sys	0m3.713s

------------------------

##Pmap on score-population and pmap on total-score-on:

real	1m9.542s

user	5m36.062s

sys	0m25.781s

------------------------

real	1m1.684s

user	5m2.215s

sys	0m24.171s

------------------------

Interestingly, this uses 80% on all cores, whereas the other methods use ~40%

There isn't really an amazing improvement with any of the methods we used. ~~We didn't use futures or promises because we would have had to restructure a lot of the code, but it wasn't likely to do much better.~~ A quick test based on Nic's comments does indeed show that adding futures is really easy (in a way that was counterintuitive to me). It doesn't improve much over baseline, however. It thrashes enough to kill any advantage it may have had, and ultimately, pmap seems to be the winner in any case.
