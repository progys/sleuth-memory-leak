# sleuth-memory-leak
Minimal example to reproduce sleuth memory leak with netty + rsocket: https://github.com/spring-cloud/spring-cloud-sleuth/issues/2102

To run it with sleuth enabled and memory leak effect execute: 
`mvnw test`

To run it with sleuth disabled change `application.yml` to contain the below fragment:
```
spring:
  sleuth:
    enabled: false
```
after change running same command `mvnw test` does not print any memory leak reports in the log.

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
