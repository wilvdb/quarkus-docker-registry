# quarkus-docker-dockerregistry

Basic Docker registry implementation using Quarkus.

To enable it, first edit your file `/etc/hosts` and add the host `registry.me` to localhost:

```
127.0.0.1       localhost       registry.me
255.255.255.255 broadcasthost
```

To pull or push images, they should have this form : `registry.me:8080/alpine:latest`