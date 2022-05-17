# scaleph

[![Github Actions](https://img.shields.io/github/workflow/status/flowerfine/scaleph/CI)](https://github.com/flowerfine/scaleph/actions)
[![Total Lines](https://tokei.rs/b1/github/flowerfine/scaleph?category=lines)](https://github.com/flowerfine/scaleph)
[![Last commit](https://img.shields.io/github/last-commit/flowerfine/scaleph.svg)](https://github.com/flowerfine/scaleph)

The Scaleph project features data integration, develop,  job schedule and orchestration and trys to provide one-stop data platform for developers and enterprises. Scaleph hopes to help peoples to aggregate data, analyze data, free data internal worth and make profit from them.

Scaleph is drived by personal interest and evolves actively through faithful developer, flowerfine is open and appreciates any helps.

## Features

* Web-ui click-and-drag data integration ways backended by out-of-the-box connectors.
* Multiple versions, different deployment mode and different resource provider flink job execution ways, where we develop [flinkful](https://github.com/flowerfine/flinkful) for solving these troubles.
* Job version management.
* Project configuration, dependency and resource.

## Quick Start

Whenever people want to explore Scaleph system, they want a running Scaleph application, then people can interact with Scaleph through Scaleph Admin.

Luckily, deploy Scaleph locally just takes three steps.

* Make sure Docker installed on your machine.
* Clone the repository
* Use Docker Compose and Scaleph Docker image quickly install and run Scaleph.

```shell
git clone https://github.com/flowerfine/scaleph.git
scaleph/tools/docker/deploy
docker-compose up
```

Once all container have started, the UI is ready to go at [http://localhost](http://localhost/)!

## Documentation

comming soon...

please refer [wiki](https://github.com/flowerfine/scaleph/wiki)

## Build and Deployment

* [develop](https://github.com/flowerfine/scaleph/blob/master/docs/develop.md). This doc describes how to set up local development environment of Scaleph project.
* checkstyle. Scaleph project requires clean and robust code, which can help Scaleph go further and develop better.
* build. This doc describes how to build the Scaleph project from source. Scaleph adopts `maven` as its build system, for more information about build from source and deployment, please refer [build](docs/build/build.md).
* docker. As more application runs in container on cloud then bare metal machine, Scaleph provides own image.
* deployment. For different deployment purpose such as develop, test or production, Scaleph make the best effort for people deploy project on local, docker and kubernetes.

## RoadMap

### features

1. data ingress and egress.
   1. data integration in flink way. Scaleph features `seatunnel`, `flink-cdc-connectors` and other flink connectors.
   2. friendly to newbies web-ui. 
2. data develop
   1. udf + sql.
   1. support multi-layer data warehouse development.
3. job schedule and orchestrate

### architectures

1. cloud native
   1. container and kubernetes development and runtime environment.
   2. java 17, quarkus.
2. plugins. https://dubbo.apache.org/zh/docsv2.7/dev/principals/

## Contributing

For contributions, please refer [CONTRIBUTING](https://github.com/flowerfine/scaleph)

## Contact

* Bugs and Features: [Issues](https://github.com/flowerfine/scaleph/issues)

## License

Scaleph is licenced under the Apache License Version 2.0, link is [here](https://www.apache.org/licenses/LICENSE-2.0.txt).