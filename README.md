# scaleph

[![Github Actions](https://img.shields.io/github/workflow/status/flowerfine/scaleph/CI)](https://github.com/flowerfine/scaleph/actions)
[![Total Lines](https://tokei.rs/b1/github/flowerfine/scaleph?category=lines)](https://github.com/flowerfine/scaleph)
[![Last commit](https://img.shields.io/github/last-commit/flowerfine/scaleph.svg)](https://github.com/flowerfine/scaleph)

Scaleph System

## Features

coming soon...

## Quick Start

coming soon...

## Documentation

coming soon...

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
   1. udf + sql
3. job schedule and orchestrate

### architectures

1. cloud native
   1. container and kubernetes development and runtime environment.
   2. java 17, quarkus.
2. plugins。https://dubbo.apache.org/zh/docsv2.7/dev/principals/

## Contributing

For contributions, please refer [CONTRIBUTING](https://github.com/flowerfine/scaleph)

## Contact

* Bugs and Features: [Issues](https://github.com/flowerfine/scaleph/issues)

## License

Scaleph is licenced under the Apache License Version 2.0, link is [here](https://www.apache.org/licenses/LICENSE-2.0.txt).