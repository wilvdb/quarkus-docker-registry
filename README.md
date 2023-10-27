# quarkus-docker-dockerregistry

Basic Docker registry implementation using Quarkus.

To enable it, first edit your file `/etc/hosts` and add the host `registry.me` to localhost:

```
127.0.0.1       localhost       registry.me
255.255.255.255 broadcasthost
```

To pull or push images, they should have this form : `registry.me:8080/alpine:latest`

## Roadmap

### Storage

* Local file system storage extension
* S3 storage extension (Ceph)[https://docs.ceph.com/en/latest/dev/developer_guide/intro/]

### Tests / Conformance

* OCI Distribution conformance tests (https://github.com/opencontainers/distribution-spec/tree/main/conformance)
* Manifest conformance
* Improve 

### UI

* Add a basic UI

### Monitoring

* Monitoring stack (Tempo/Prometheus/Grafana)

### Misc

* Switch to Gradle
* Bum to Quarkus 3.5.0
* Vault integration
* Automate build (CircleCI, Travis, Github action)
* Native build