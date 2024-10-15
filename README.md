# scaleph

[![Gihub Actions](https://github.com/flowerfine/scaleph/actions/workflows/ci-maven.yml/badge.svg?branch=master)](https://github.com/flowerfine/scaleph/actions) [![Total Lines](https://tokei.rs/b1/github/flowerfine/scaleph?category=lines)](https://github.com/flowerfine/scaleph) [![Last commit](https://img.shields.io/github/last-commit/flowerfine/scaleph.svg)](https://github.com/flowerfine/scaleph) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=flowerfine_scaleph&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=flowerfine_scaleph) [![codecov](https://codecov.io/gh/flowerfine/scaleph/branch/master/graph/badge.svg)](https://codecov.io/gh/flowerfine/scaleph/branch/master) [![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=flat&logo=docker&logoColor=white)](https://github.com/orgs/flowerfine/packages?repo_name=scaleph)

The Scaleph project features data integration, develop,  job schedule and orchestration and aims to run on cloud environment. 

It trys to provide one-stop data platform for simplifying development of data application. Scaleph hopes to help peoples to aggregate and analyze data, free data internal worth and make profit from them.

Scaleph is driven by personal interest and evolves actively through faithful developer, flowerfine is open and appreciates any helps.

## Features

* Data Integration
  * Web-ui click-and-drag data integration ways backended by [Apache SeaTunnel](https://seatunnel.apache.org/) on Flink engine.
  
  * Support the latest 2.3.8 V2 out-of-the-box connectors and transforms.

  * DataSource management.
  
* Data Development
  * Jar. User develops Flink job using DataStream or Table API and package it as a jar.
  * SQL. Support online Flink SQL editor backended by [Flink SQL Gateway](https://nightlies.apache.org/flink/flink-docs-release-1.18/docs/dev/table/sql-gateway/overview/).
* Data Warehouse
  * Doris. Support Doris Cluster management on Kubernetes backended by [doris-operator](https://github.com/selectdb/doris-operator).

* Other
  * Kubernetes. Manage Flink through [Flink Kubernetes Operator](https://nightlies.apache.org/flink/flink-kubernetes-operator-docs-stable/) and Doris through [doris-operator](https://github.com/selectdb/doris-operator).


## Documentation

Learn more about Scaleph at https://github.com/flowerfine/scaleph

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

   ![wechat](docs/image/1581680135004_.pic.jpg)

## Sponsor

Thanks to [JetBrains](https://www.jetbrains.com/?from=scaleph) for supporting us free open source licenses.

[![JetBrains](https://img.alicdn.com/tfs/TB1sSomo.z1gK0jSZLeXXb9kVXa-120-130.svg)](https://www.jetbrains.com/?from=scaleph)

## License

Scaleph is licenced under the Apache License Version 2.0, link is [here](https://www.apache.org/licenses/LICENSE-2.0.txt).