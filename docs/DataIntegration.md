# DataIntegration

## Why Flink

As a stateful stream compute framework, Flink has follow advantages on data integration:

* Data consistency. Flink `async synchronous barrier snapshotting (ABS)` provides `Exactly-once` semantic for stream process, which ensures data loss or repetition will not happen when data sync job stop, upgrade, migrate, even failure.
* Low latency, high throughput. Flink has been testified by numberless companies and production examples and supports hundreds of trillions data process.
* Rich scenarios. Flink supports `batch`, `stream` process mode and even `unified batch & stream` process, which contributes to data integration scenarios significantly. In traditional way, `overall` and `incremental` data integration are two different architecture. Through Flink, developer can achieve `overall`, `incremental` or `first overall then incremental` data integration job using just one engine and one job.
* Schedule & Resource. Flink supports different resource providers, such as `YARN`, `Kubernetes` and `Standalone`. Traditional bare metal machine deployment encountered unpredictable traffic problems will be solved by dynamic resource request, schedule and release.
    * Scale up or down. It is easy for Flink increase or decrease parallelisms
    * Resource isolation. Flink provides `Session`, `Per-Job` and `Application` deploy mode, developer can choose suitable one of them dependen on resource isolation level
* Metrics. Flink plugin system supports different monitor system, such as promethues.
* BigData ecosystem. Flink has tight association with various database, message queue, bigdata storate engine and file formats.

## Which Choose

* [chunjun](https://github.com/DTStack/chunjun) (datax). 
* [bitsail](https://github.com/bytedance/bitsail).
* [seatunnel](https://github.com/apache/incubator-seatunnel).