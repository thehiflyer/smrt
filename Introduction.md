# Introduction #

SMRT is a toolkit used for sending messages in Java.
Messages can be sent in a multitude of ways:
  * internally synchronously in the same thread.
  * internally asynchronously in the same thread or across threads.
  * across a pair of inputstream and outputstream, which implies that it works over networking too.
  * over Apache Mina.
  * to an object, which can then be converted back to a method call.
  * to multiple receivers at once.
  * through a log decorators, useful for debugging.
  * ... and much more, the SMRT messaging system is fully pluggable.


A message in SMRT is represented by a method call, which means it's very easy to use as a developer, and your IDE can utilize its code analysis to follow message usages.

SMRT does not use any reflection based code, instead it generates the classes and interfaces that it needs.

The code is still under development being prepared and polished for its initial release.