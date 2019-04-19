# Microk8s proxy setup

# terminal

export http_proxy=<your-proxy>
export https_proxy=<your-proxy>

# snap

```
mkdir -p /etc/systemd/system/snapd.service.d/
```

新建两个文件`http-proxy.conf`和`https-proxy.conf`，分别填上：

```
[Service]
Environment="http_proxy=<your-proxy>"
```

和

```
[Service]
Environment="https_proxy=<your-proxy>"
```

然后重启：

```
sudo systemctl daemon-reload
sudo systemctl restart snapd
```

# docker

```
mkdir /etc/systemd/system/docker.service.d
touch /etc/systemd/system/docker.service.d/http-proxy.conf
```

文件里写：

```
[Service]
Environment="HTTP_PROXY=<your-proxy>"
Environment="HTTPS_PROXY=<your-proxy>"
```

重启：

```
sudo systemctl daemon-reload
sudo systemctl show --property Environment docker
sudo systemctl restart docker
```

### microk8s

To let MicroK8s use a proxy enter the proxy details in `${SNAP_DATA}/args/containerd-env` and restart the containerd daemon service with:

```
sudo systemctl restart snap.microk8s.daemon-containerd.service
```

Normally, `${SNAP_DATA}` points to /*var*/*snap*/*microk8s*/*current*.