# Apache SeaTunnel(incubating) Manual

Seatunnel is next-generation high-performance, distributed, massive data integration framework, scaleph integrates seatunnel for providing click-and-drag data integration web.

Seatunnel supports a lot of datasources for satisfying various data integration scenes, but too many datasources are also challenging scaleph how to verify data integration accuracy, stability and efficiency, so scaleph must be more good at seatunnel than other.

First of all, scaleph must provide a practicable way for setuping local environment such as kafka, mysql, doris, elasticsearch and so on. Luckily, docker frees people from annoying local environment setup and we can find most datasource image on [Docker Hub](https://hub.docker.com/).

Then, scaleph combining various sources and sinks to run on real service and find out what they can do and not.

This doc records above process.