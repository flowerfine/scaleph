# Build in local

## Requirements
* OS: Linux or other Unix like os.
* JDK: JDK 17 or later.

## Get the source code.
```shell
git clone https://github.com/flowerfine/scaleph.git
```

## Build

Run the following command in `Project ROOT directory`.

```shell
./mvnw -B clean package -Pdist -DskipTests
```

Check the binary dist file: `scaleph-dist/target/scaleph-${project-version}-bin.tar.gz`