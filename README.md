# full-stack-scala

TodoMVC with Akka-http, Scala.js, Autowire and React.

This project is essentially a fork of [Scalajs-React TodoMVC Example](https://github.com/tastejs/todomvc/tree/gh-pages/examples/scalajs-react) enhanced with a back-end and the proper sbt configuration for development/deployment.

- `model`: Shared Todo model
- `web-client`: Scala.js/React client
- `web-server`: Akka-http server
- `web-static`: Static files

#### Development:
    
```
sbt ~web-server/re-start
```

#### Deployment:

```
sbt web-server/assembly
java -jar target/scala-2.11/web-server-assembly-1.0-SNAPSHOT.jar
```
