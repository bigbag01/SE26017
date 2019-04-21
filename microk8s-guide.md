# Cloud Native 

We are using kubernetes to make the project cloud native.

To play with single node Kubernetes, there are two general methods for Linux: **minikube** and **microk8s** (for Windows users, Docker-for-Desktop is still an option, [find out yourself](https://docs.docker.com/docker-for-windows/kubernetes/)).

This deployment guide is based on CenOS 7.

## minikube

Minikube implements a local Kubernetes cluster on macOS, Linux, and Windows. Its [goal](https://github.com/kubernetes/minikube/blob/master/docs/contributors/principles.md) is to enable fast local development and to support all Kubernetes features that fit.

To play with minikube refer to [this doc](https://kubernetes.io/docs/setup/minikube/).

Minikube requires running kubernetes cluster in a VM (even with the option --vm-driver=none VirtualBox driver is still needed). 

Do remember to setup proxy correctly.

With some efforts with minikube, we turned to microk8s instead :)

## microk8s

Microk8s is a single package of k8s that installs on Linux. It’s not elastic, but it is on rails. Use it for offline development, prototyping, testing, or use it on a VM as a small, cheap, reliable k8s for CI/CD.

Follow this [guide](https://tutorials.ubuntu.com/tutorial/install-a-local-kubernetes-with-microk8s#0) to install microk8s.

A proxy is needed, refer to [this file](./microk8s-proxy.md)

The sections below are based on success installation of microk8s.

### clean start after reboot

```shell
sudo iptables -P FORWARD ACCEPT
microk8s.start
```

###### check everything goes well:

```
microk8s.inspect
microk8s.kubectl get all --all-namespace
microk8s.kubectl cluster-info
```

###### if pod STATUS is not "Running", use the following command to diagnose:

```
microk8s.kubectl describe -n <namespace> pod <pod-name>
```

###### other userful commands

Note: kubectl commands are best references.

view nodes: `microk8s.kubectl get nodes`

delete resources: `microk8s.kubectl delete -f <config-files>` or use `microk8s.kubectl delete --help` for other options.

###### stop microk8s:

```
microk8s.stop
```

###### start microk8s:

```
microk8s.start
```

### Removing MicroK8s

Before removing MicroK8s, use `microk8s.reset` to stop al running pods.

```
microk8s.reset
snap remove microk8s
```
Note: life cycle of PersistentVolume is independent of pods, thus you need to manually remove these resources, otherwise it will persist (even after a system reboot!):

```
microk8s.kubectl delete pv <pv-name>
```

### Port existing docker compose to Kubernetes:

Based on existing docker-compose.yml we have successfully run with docker, we are looking for methods to directly porting it to kubernetes. We choose to use kompose tool. Steps for CentOS are listed below. To use kompose on other systems, please refer to [this guide](https://kubernetes.io/docs/tasks/configure-pod-container/translate-compose-kubernetes/).
###### Install Kompose
Kompose is in EPEL CentOS repository. If you don’t have EPEL repository already installed and enabled you can do it by running `sudo yum install epel-release`

If you have EPEL enabled in your system, you can install Kompose like any other package.

```
sudo yum -y install kompose
```

###### Use Kompose
Go to the directory containing your `docker-compose.yml` file. To convert the `docker-compose.yml` file to files that you can use with kubectl, run `kompose convert` and then `kubectl apply -f <output file>`.

```
  $ kompose convert                           
  INFO Kubernetes file "frontend-service.yaml" created         
  INFO Kubernetes file "redis-master-service.yaml" created     
  INFO Kubernetes file "redis-slave-service.yaml" created      
  INFO Kubernetes file "frontend-deployment.yaml" created      
  INFO Kubernetes file "redis-master-deployment.yaml" created  
  INFO Kubernetes file "redis-slave-deployment.yaml" created    
```

```
 $ kubectl apply -f frontend-service.yaml,redis-master-service.yaml,redis-slave-service.yaml,frontend-deployment.yaml,redis-master-deployment.yaml,redis-slave-deployment.yaml
  service/frontend created
  service/redis-master created
  service/redis-slave created
  deployment.apps/frontend created
  deployment.apps/redis-master created
  deployment.apps/redis-slave created
```
Your deployments are running in Kubernetes.

Check everything goes well:

```
microk8s.kubectl get all
microk8s.kubectl get pv
microk8s.kubectl get pvc
microk8s.kubectl describe <resource-name>
```

#### Trouble shooting

1. `drone-server` pod stuck with status `pending`. `microk8s.kubectl describe`  shows that [PersistentVolume](https://kubernetes.io/docs/concepts/storage/persistent-volumes/) claimed by [PersistentVolumeClaim](https://kubernetes.io/docs/concepts/storage/persistent-volumes/#persistentvolumeclaims) not found.

   This is because kubernetes does not support local storage, and the original docker-compose.yml file specifies volume with host path. These volume configurations cannot be automatically convert to corresponding .yaml file used to create PersistentVolume in  kubernetes. 

   To make the cluster run you have to setup PersistentVolume manually. Kubernetes supports hostPath for development and testing on a single-node cluster if a host-based volume is still what you want. See [this guide](https://kubernetes.io/docs/tasks/configure-pod-container/configure-persistent-volume-storage/) for detailed configurations.

