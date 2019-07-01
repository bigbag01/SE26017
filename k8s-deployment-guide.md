# Cloud Native - Multiple Nodes

We are using kubernetes to make the project cloud native.

This guide focuses on how to deploy kubernetes on multiple nodes.

All our nodes runs Ubuntu 16.04 OS.

## (on each node) Close Swap
```
sudo swapoff -a
```

## (on each node) Setup Proxy
Several steps in this guide need properly set up proxy.

Most importantly: proxy for **terminal**, **Docker**, and **apt**. See [proxy.md](./proxy.md) for more details.


## (on each node) Prepare Container Runtimes

To run containers in Pods (load-balance unit in Kubernetes), Kubernetes uses a container runtime. Typicall container runtimes include **Docker(runc)**, **CRI-O**, **Containerd**, etc.

We use Docker(runc) in this guide. To play with other runtimes, check out [this page](https://kubernetes.io/docs/setup/production-environment/container-runtimes/) for reference.

Use the following commands to install Docker:
```shell
# Install Docker CE
## Set up the repository:
### Install packages to allow apt to use a repository over HTTPS
sudo apt-get update && sudo apt-get install apt-transport-https ca-certificates curl software-properties-common

### Add Docker's official GPG key
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

### Add Docker apt repository.
add-apt-repository \
  "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) \
  stable"

## Install Docker CE.
sudo apt-get update && sudo apt-get install docker-ce=18.06.2~ce~3-0~ubuntu

# Setup daemon.
cat > /etc/docker/daemon.json <<EOF
{
  "exec-opts": ["native.cgroupdriver=systemd"],
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "100m"
  },
  "storage-driver": "overlay2"
}
EOF

sudo mkdir -p /etc/systemd/system/docker.service.d

# Restart docker.
sudo systemctl daemon-reload
sudo systemctl restart docker
```

## Proxy
refer to proxy.md

## Install Kubernetes using Kubeadm

#### Installing Kubectl, Kubeadm, and Kubelet
```
apt-get update && apt-get install -y apt-transport-https curl
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key add -
cat <<EOF >/etc/apt/sources.list.d/kubernetes.list
deb https://apt.kubernetes.io/ kubernetes-xenial main
EOF
apt-get update
apt-get install -y kubelet kubeadm kubectl
apt-mark hold kubelet kubeadm kubectl
```

#### Creating a single control-plane cluster with kubeadm
We use Calico cni in our case
```
sudo kubeadm init --pod-network-cidr=192.168.0.0/16
# apply cni
sudo kubectl apply -f https://docs.projectcalico.org/v3.7/manifests/calico.yaml
# check control plane
sudo kubectl get pods --all-namespaces
```
Note: `kubeadm init` command might fail in hundreds of ways (we experienced quite a lot!),
before execute this command again, use`kubeadm reset` to clean the previous configs.

#### Join slave nodes
ssh to salve node, execute
```
kubeadm join --token <token> <master-ip>:<master-port> --discovery-token-ca-cert-hash sha256:<hash>
```
This command also appears in outputs of `kubeadm init`.
