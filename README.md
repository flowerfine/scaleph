# scaleph

[![Gihub Actions](https://github.com/flowerfine/scaleph/actions/workflows/ci.yml/badge.svg?branch=master)](https://github.com/flowerfine/scaleph/actions) [![Total Lines](https://tokei.rs/b1/github/flowerfine/scaleph?category=lines)](https://github.com/flowerfine/scaleph) [![Last commit](https://img.shields.io/github/last-commit/flowerfine/scaleph.svg)](https://github.com/flowerfine/scaleph) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=flowerfine_scaleph&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=flowerfine_scaleph) [![codecov](https://codecov.io/gh/flowerfine/scaleph/branch/master/graph/badge.svg)](https://codecov.io/gh/flowerfine/scaleph/branch/master) [![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=flat&logo=docker&logoColor=white)](https://github.com/orgs/flowerfine/packages?repo_name=scaleph)

The Scaleph project features data integration, develop,  job schedule and orchestration and aims to run on cloud environment. 

It trys to provide one-stop data platform for simplifying development of data application. Scaleph hopes to help peoples to aggregate and analyze data, free data internal worth and make profit from them.

Scaleph is driven by personal interest and evolves actively through faithful developer, flowerfine is open and appreciates any helps.

## Features

* Provide Flink ecosystem to every data developer easily.
* Predefined expert Flink decisions and excellent step wizard.
* Job management.
  * Step job wizard
  * Multiple versions
  * Full job detail web
  * Configuration, dependency and resource

### data integration

* Web-ui click-and-drag data integration ways backended by Apache SeaTunnel(incubating)
*  Support 2.3.0-beta V2 out-of-the-box connectors
* DataSource management

### data develop

* Multiple deployment modes and  different resource providers.
  * Standalone。Session
  * YARN。Application, Per-Job, Session
  * Kubernetes。Session

## Quick Start

Whenever people want to explore Scaleph system, they want a running Scaleph application, then people can interact with Scaleph through Scaleph Admin.

Luckily, deploy Scaleph locally just takes three steps.

* Make sure Docker installed on your machine.
* Clone the repository
* Use Docker Compose and Scaleph Docker image quickly install and run Scaleph.

```shell
git clone https://github.com/flowerfine/scaleph.git
cd scaleph/tools/docker/deploy/scaleph
docker-compose up
```

Once all containers have started, the UI is ready to go at [http://localhost](http://localhost/), user can login by `sys_admin/123456`

## Documentation

please refer [website](https://flowerfine.github.io/scaleph/#/)

## Build and Deployment

* [develop](docs/develop/develop.md). This doc describes how to set up local development environment of Scaleph project.
* checkstyle. Scaleph project requires clean and robust code, which can help Scaleph go further and develop better.
* build. This doc describes how to compile `scaleph-api` or `scaleph-ui-react` from source and build corresponding docker image. For more information about build from source, refer [workflows](https://github.com/flowerfine/scaleph/tree/dev/.github/workflows) and [actions](https://github.com/flowerfine/scaleph/actions).
    * compile.  Scaleph adopts `maven` as its build system, .
        * [local](docs/build/build-local.md)。People has to install jdk, maven, node, use `mvn clean package` or `npm` command to compile project.
        * [docker](docs/build/build-docker.md)。Recommend way. Just need git and docker, after clone the source code, use docker as the build tools to compile project, which is slower than local.
    
    * docker. As more application runs in container on cloud then bare metal machine, Scaleph provides own image.
        * [docker-image-build](docs/docker/docker-image-build.md)。Choose different docker compose yaml file, people can build scalph-api, scaleph-ui or both.
        * [docker-image-build-zh](docs/docker/docker-image-build-zh.md)。For Chinese user.
    
* deploy. For different deployment purpose such as develop, test or production, Scaleph make the best effort for people deploy project on local, docker and kubernetes.
  * [docker](docs/deploy/docker/docker.md). require fluent network.
  * [kubernetes](docs/deploy/kubernetes/kubernetes.md). work in process.


## RoadMap

### features

1. data ingress and egress.
   1. Flink way data integration . Scaleph reduces complexity of integration through `seatunnel`, `flink-cdc-connectors` and other flink connectors.
   2. Friendly web-ui to newbies . 
2. data develop
   1. udf + sql.
   1. support multi-layer data warehouse development.
3. job schedule and orchestrate

### architectures

1. cloud native
   1. container and kubernetes development and runtime environment.
      1. flink operator
      2. seatunnel operator
      3. scaleph operator
   2. java 17, quarkus.
2. plugins. https://dubbo.apache.org/zh/docsv2.7/dev/principals/

## Code of Conduct

This project adheres to the Contributor Covenant [code of conduct](https://www.contributor-covenant.org/version/2/1/code_of_conduct/)

## Contributing

For contributions, please refer [CONTRIBUTING](https://github.com/flowerfine/scaleph)

Thanks for all people who already contributed to Scaleph!

<a href="https://github.com/flowerfine/scaleph/graphs/contributors">
    <img src="https://contrib.rocks/image?repo=flowerfine/scaleph" /></a>

## Contact

* Bugs and Features: [Issues](https://github.com/flowerfine/scaleph/issues)

* Chinese user:

   ![wechat](docs/image/1371669250114_.pic.jpg)

## Sponsor

Thanks to [JetBrains](https://www.jetbrains.com/?from=scaleph) for supporting us free open source licenses.

[![JetBrains](https://img.alicdn.com/tfs/TB1sSomo.z1gK0jSZLeXXb9kVXa-120-130.svg)](https://www.jetbrains.com/?from=scaleph)

## License

Scaleph is licenced under the Apache License Version 2.0, link is [here](https://www.apache.org/licenses/LICENSE-2.0.txt).