# Prometheus set up

ref: https://sysdig.com/blog/monitoring-and-securing-java-apps-at-quby/

We use a prometheus deployment suite (check prometheus-operator submodule) to 
make use of prometheus to monitor our cluster.

Following the reference website, we use Grafana to visualize cluster metrics.
To do this, we need to map the Grafana ports to outside world to view that results (in http web page).
Specifically, in `kube-prometheus/manifests/grafana-service.yaml`, add "type" attribute with value "NodePort",
under "targetPort" attribute, add a parallel attribute named "nodePort" with the port we are going to map for outside access.
Then we can access the Grafana page with <instance-ip>:<mapped-grafana-port> outside the port.

Note: we only monitor cluster/k8s metrics now (with help of kube-state-metrics, etc.), 
for user-defined metricts, use [promethes exporters](https://sysdig.com/blog/kubernetes-monitoring-prometheus/).
 
