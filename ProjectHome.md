SMRT is a simple framework for Java to support messaging.

## Terminology ##
### Message ###
The foundation of SMRT is the idea that a message is the same thing as a method call.
You send a message by invoking a method.
You receive a message by implementing the method of the protocol.

### Protocol ###
A protocol is the ruleset for a one way communication.
If X wants to send a message to Y, they need to use a protocol (let's call it X2Y).

You probably want Y to be able to send messages to X as well, and in that case you need a new protocol for that, Y2X.
For a completely symmetrical relationship (such as a peer to peer environment), the protocols may be the same.

A protocol in SMRT is made by writing an interface and annotating it with `@SmrtProtocol`.

There are some rules that apply to such interfaces:
  1. No methods may throw exceptions.
  1. Methods may either return void or return an interface that's also annotated with `@SmrtProtocol`. A method that returns a protocol must not take any parameters.
  1. Root protocols must have a defined name: `@SmrtProtocol("MyName")`

Root protocols are the only protocols that produce generated files with the templates.

Here's a short example:
```
@SmrtProtocol("MyProtocol")
public interface MyProtocol {
  void sendChatMessage(String from, String message);
  FileTransferProtocol filetransfer();
}
@SmrtProtocol
public interface FileTransferProtocol {
  void sendFile(String fileName, byte[] data);
}
```

## The good ##
SMRT has a couple of strong selling points, shown below.

### Small and lean ###
The SMRT source code is very small, which could indicate that it has few bugs.
It also means it's easier for someone new to get into it and modify it.

### Efficient communication ###
Sending messages remotely uses codecs you write yourself, so you can make them as efficient as you like.
It provides default codecs for the primitive java types as well as for arrays, maps and lists.

SMRT only sends the raw data, never any type information, which makes the bandwidth usage lower than many other messaging frameworks.

### Easy to extend ###
SMRT comes with a set of default useful templates, but it's very easy to add your own.
Simply write a new template and place it in the appropriate package directory and the SMRT generator will apply it automatically next time you build.

See the default templates for examples of what you can do.

### No reflection ###
SMRT uses no reflection based code. There's no `instanceof`, no proxies, no magical class lookups and no reflection method invocation.
This makes the code a lot cleaner and probably a lot faster.

### Easier debugging ###
The lack of reflection also improves debugging. Stepping through the runtime code is just like stepping through your own classes, since there's no magic involved.

### Good IDE integration ###
Defining protocols is just a matter of defining your own interfaces, which means that your IDE will assist with refactorings.
The actual protocol is never generated, that's your own interface.
The only thing generated is the helper classes for easier protocol usage.

## The bad ##
SMRT is not optimal for all purposes, here are some of the issues that SMRT does not intend to solve.

### Methods with return values ###
All messages in SMRT are asynchronous by design. That means that messages do not have return values or exceptions. There are two major reasons for this;
  1. Consider having multiple receivers on the same message, how would return values be aggregated?
  1. Methods with return values would need to block until the return value is gathered, possibly from server side. This would complicate the remote communication immensely.

### Backwards compatibility ###
SMRT does not try to handle communication with systems with other versions of SMRT or the protocol in question. A lot of other communication frameworks do this and is a better option if those needs are required.

Thus, SMRT is not useful in cases:
  1. where you want to store messages in some sort of historic log.
  1. where you can't easily control the version of your client.