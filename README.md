# sleuth-memory-leak
Minimal example to reproduce sleuth memory leak with netty + rsocket


**Test scenario**

- Netty based rsocket server listens for incoming rocket request using request/reply pattern (even though also reproducible with channel or stream, so seems not related);
- Client sends request/reply with empty json object (can be any payload);
- Server replies with plain text;
- Client receives text;
- Test triggers System.gc();
- Netty leak report is printed to the console;
- Test finishes and client closes the connection;

That said - I do not know if this actually creates a memory leak which leads to OOM, but if I do not change anything, just disable sleuth - there are no memory leak reports printed in the logs while running the same test.
Exactly same behavior is observed in the real PROD like application.