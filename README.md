# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```


Sequence Diagram: https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+uB5afJCIJqTsXzQo8wHiVQSIwAgQnihignCQSRJgKSKrBhqymGNAMDGQCMDZDAMzZKo4r2NuhhJr61T+gAQiGzlqGAUYxnGhRaVF8DIKmMDpvhoxjDmqh5vM0FFiW9R6OuKKEklDb0RFgrDvyDJMlOXlzjS+73sKYoSm6MpymW7xKpg7nqhuI1zEYEBqDAaAQMwVpojeXV3plb5dvNvb9ptIExaWzLTJe0BIAAXigHApSgsYKeh8LJtl2G5U4ACMBFFSVBZjOV0D1D4x16qdF27HRTbNYulnWQNW57VDLowDUSAAGaWE0+h-DsGJ+Rwaww-ILg425qoaujWw7DNc0LcwxxgCA8SNRtB2uoD8TA5d123fGGWPSUYBpm9H38l9ZXFn9Cqs+zoONgxcNUreLUwIecgoM+8Tnpe14QwKS7Co+Aaa7DzqRR+5n+o54oZKoAGYDpD3RRJYGEfp8wkah3wUVR9Zu7R92YU9-MwLh+V6VNrukR7l5e8hPtoQ1jHeH4-heCg6AxHEiQp2njm+FgomCqB9QNNIEb8RG7QRt0PRyaFBTDJ7iHoH7puwrpYwN9RkJma38IO1ZCO2fYucOUJueJSSjPy2tivI+KHBqxr8GN2gnW8utlTLn1T6G-Isryh3TfjRqavR5TArUzAy05Kta8OkznbdjtjOWf6R0UVLnNpc3vM5em70FZ9fMosKoS3flAc6l16rgwVpDPu1kKKvmNpSbW9QOAoG4MeS8i9KLL1XguHWG9hQyHQUyQwatBr7yjsvZ+lQ7b1BzqeK2Ns7b7Udi8TSB1-Z8xwnhLMYMGKeETgEFE65-DYHFBqfiaIYAAHElQaHzi-UsDQZHlyrvYJU9cqFIWbrQs2TsD7IS7rbfRBdjb1GQDkOROYHJomsWocerlJ4yBgaOGAs9OAL0MXg7qutRRzwNi+bQe9jTaMPiTUJS8kJn3motS+1pr7a1Yf3LaPY+w0I7DUFmYCIFXWjDdL+PNKiiQFv-bMwsgGFjFqWAGOSQZQIYkk++KT6gIKNp2JqriLF2PkRiHx68hT1C3pWBAsilQeiafDLaGicwZLoQJHpOYmEIEAqY5pullgzLUAWBo4wtkAElpAFleuEYIgQQSbHiLqFAbpOR7G+MkUAapbmQUWN8LZAA5V5YwoQwE6Bwx2XCcrByzJs+ROy9lKkOcc055zliXOuS8gyPyQSPJAM8oipUUVgrmF85Fvz-kywTsxfwHAADsbgnAoCcDECMwQ4BcQAGzwAnIYexMAigBzMWwxorQOjqM0azaOWZPlKgBYmPRPdWlhMUgVUV+Lu5fm5S0pWrL7EYjgGqpUjjSRyxcdPek7i55eJlf0u+hD-ESnIcEoahixoRMMTEi+V8sCTLgQjNJu0kHrMOpLcBINP53SKVlbhL0ymFQqaVKpIDaknX9ZA-hN98HJPgTvYAziUGqqPCgdV8quT2o8gFNAVATRIHXHmzAAwUnJLiglFAtUciBu5pwzKJSXohwjbmSpP1qmVRDDAGqLkGlJt8RagMa4YBbJlBiI4sZLBmtgZk6yBzpBzP0fUTV2b7HLNWT3GtYEcUoGhfUE5ZyYDiowq2gOPCQ6HuPTAU9gRz1EsESSyw6DbKbHTkgBIYB319ggF+gAUhAcUYzpr+DRWqTlfNlWSSaMyGSPQtlaKiegLM2AEDAHfVAOAEBbJQDWCui9vcHhSsiTgpCoKfhYZw3hgjewADqLB9kVx6LFfiCg4AAGl3lQqOSe2Fz6WFTPqAAK1A2gdVEnxTbvrS5XV3qp630NR4+eWDvEjoGZvAJ1rd62plQWiajqOCzXPnEl1Wm76ie2ukuGSjslxtyU29KLaf7PT-kLLtUae0xr9bk4dbql0IzafIDNXTJ1MlzfxhdBDBkwGGVOm18oV1GY1Fsp1FmEmutcSmj1T97N939NIMzHzFpRjRC57+xTr3tq88Vbtv1SxVQHfJuqiagubQZOM9p1abP0xQOipotHKD0egBiJL8hVIjdw-h6AxNC2DqSpdJWc3YCBRNGaGs4ZXMdJ9fUQMW2wxISq8Gtt6ZMwAMjd9JrrSqzmhgLWesia9VdZgAYMAw3sOYkm8Aab33ZsEbWKF4ALgOrhYNW4vwWh0TRbmFrXLfjJTYBh2ynrYXCtka-PUEDsntX-hWSYvd+2YAjBI0C56ILRiJtfUnLw2Hv2-vp-KRAwZYDAGwJhwgeQCgcsUUV5RJcy4VyrsYXRWO4T1BANwPAChOfIBADztAfSOUifdVtaXbO5fYD6RDlTbi0EYJQDyXXTSx3SBIeiEZw1VBEfkcDiUhiNBHyMNoPQBgYAQGwJQJX7jwS2kRw5-V+vTtuZq6GgArLwq73mbu9td-Id3hhkbgh8CiYdxhOmQ-qNbfcVnF1vdzyONd5HWd4B3UTpVJOyfVZDcC6PNeGpAA
