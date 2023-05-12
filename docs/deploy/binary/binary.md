# Deploy from binary file

## Requirements

* OS: Linux or other Unix like os with at least 2GiB free space.
* JDK: JDK 17 or later.
* Mysql: version 8+ recommended
* Redis

## Get the binary file

You can get the binary file from [github](https://github.com/flowerfine/scaleph/releases)
or [build yourself](../../build/build-local.md).

## Unpackage the binary file

Unpackage the binary file using the following command:

```shell
tar -zxf scaleph-1.0.6-bin.tar.gz
```

You'll see the following directory structure.

```text
scaleph-1.0.6
├── bin
├── conf
├── libs
├── sql
├── ui
├── LICENSE
└── README.md
```

## Init mysql

Execute the sql scripts under `sql` directory.

> We will use tools to do this later.

## Config the project

Edit `bin/config.sh`

Uncomment the following lines:

```shell
# export JAVA_HOME=
```

Set the `JAVA_HOME` env.

```shell
export JAVA_HOME=path/to/your/java_home
```

Edit `conf/application.yml` and `conf/application-dev.yml` to suite your environment.

## Start/Stop the process

You cant start the process in background by default or frontend.

### Start in background 

```shell
./bin/scaleph.sh start
```

You'll see outputs like:

```text
Staring in backend
Running with pid 45193
```

### Start in frontend

```shell
./bin/scaleph.sh start-frontend
```

### Check if the process is running

```shell
./bin/scaleph.sh status
```

### Stop

> You can only stop the progress when it was started in background mode.

```shell
./bin/scaleph.sh stop
```

## Log
The logs are stored in `logs/scaleph`

```text
logs
└── scaleph
    ├── scaleph-error.log
    ├── scaleph-info.log
    ├── scaleph-server.log
    └── scaleph-warn.log
```