# Complex event logic 
##Introduction
The purpose of complex event logic is to create Boolean expressions on event steams (timed events). This expression contains Boolean operator and time indicator. 
A Complexe expression is formed by
* **Primary expressions** : a primary expression applies on one event a simple Boolean expression (ex: bid > 1.5$ ).
* **Temporal expressions** : a temporal expression applies on one primary expression a timed validity period and an Boolean aggregation function. Ex :
    * Never during last 2 minutes
    * at least once during last 30 minutes
    * at least 90 percent of the time during 10 minutes.
    * The primary expression is true for the last event
* **An aggregation of temporal expression with logic operator** ex :
    * Bid > 10 during last 30 minutes and then (Bid < 10 on last tick and ask < 9 never during last 3 minutes)

## Chain of true
The process of a expression is based on the chain of true. 

The chain of true is a way to simplify and optimize computation of an expression. 

## Principle 
When an event happens, it gets it's primary expressions evaluated : It is a direct evaluation based on event payload
After the primary evaluation is computed, the temporal evaluation is computed :
- Primary evaluation is stored in a cache with a time-stamp.
- Outdated evaluation is removed from the cache (depends on the temporal expressions)
- Temporal expression is evaluated and stored in a cache.

When a request is done to evaluate a complex expression, the process works with a leaf to trunk approach on the expression graph.
It uses temporal evaluation previously computed 

After evaluation a list of time-stamps when the expression started to be true is given 


|type of expression |Start to be true if asked at timestamp t1|
|-------------------|-----------------------------------------|
|Temporal expression|if last expression before t1 was evaluated true, timestamp of this evaluation else empty|
|Or expression	|Set of all resulting timestamps of sub expression evalated at date t1|
|And expression	|"scalar product" of all timestamp's results of all sub-expressions. Min time stamp is taken ex given A = {t1,t2} , B = {t3,t4} then A and B -> {min(t1,t3),min(t1,t4),min(t2,t3),min(t2,t4) }|
|And Then Expression	| if A and then B, evaluate B at t1 and for each timestamp's results evaluate A at this timestamp.|

The chain of true prevent usage of **not** in the global expression

##Some examples

Simple OR
![or](https://raw.githubusercontent.com/pocamin/ComplexEventLogic/master/readme/Algo%20-%20cbl%20-%20or.png)

true if
* B is true at 0 and A is true at -4
* C is true at 0 and A is true at -4

##Simple and
![And](https://raw.githubusercontent.com/pocamin/ComplexEventLogic/master/readme/ALGO%20-%20CBL%20-%20AND.png)

true if
* B is true at 0 and C is true at 0 (conventional) and A is true at 5

##AND and then OR
![And then or](https://raw.githubusercontent.com/pocamin/ComplexEventLogic/master/readme/And%20and%20then%20OR.png)

True if
* B is true at 0 and D is true at 0 and C is true at -5 and A is true at -8
* B is true at 0 and E is true at 0 and C is true at -6 and A is true at -9
