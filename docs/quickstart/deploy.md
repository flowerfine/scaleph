# Deploy

For different deployment purpose such as develop, test or production, Scaleph make the best effort for people deploy project on local, docker and kubernetes.

## Docker

Scaleph has released images on [github packages](https://github.com/orgs/flowerfine/packages?ecosystem=container), people can use them directly.

Start scaleph is as easy as just two steps:

Firstly, clone the scaleph repository

```shell
git clone https://github.com/flowerfine/scaleph.git --depth 1
```

Then, bootstrap scaleph through docker compose

```shell
cd scaleph/tools/docker/deploy/scaleph
docker compose up -d
```

Once all containers have started, scaleph is ready at  [http://localhost](http://localhost/) and login by `sys_admin/123456`.

If user has difficult in pull image from github packages, which hosts on github [container-registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry), try to pull image firstly and then bootstrap

```shell
# pull the image firstly
docker compose pull

# after pull success, try to bootstrap again
docker compose up -d
```

Another solution is use github container registry mirror instead.

## Kubernetes

Work in progress