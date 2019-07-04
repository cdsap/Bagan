gcloud container clusters create bagan --zone us-central1-a  --machine-type=n1-standard-8


 helm init

 kubectl --namespace kube-system create serviceaccount tiller
 kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller
 sleep 10
 kubectl --namespace kube-system patch deploy tiller-deploy -p '{"spec":{"template":{"spec":{"serviceAccount":"tiller"}}}}'
 kubectl create clusterrolebinding cluster-admin-binding --clusterrole=cluster-admin --user=$(gcloud config get-value account)
 sleep 10
 helm repo update

helm install -n bagan-influxdb  -f ../k8s/influxdb/values.yaml ../k8s/influxdb/
helm install -n bagan-grafana  -f ../k8s/grafana/values.yaml ../k8s/grafana/

kubectl delete service bagan-grafana-experiments
kubectl expose deployment  bagan-grafana-experiments  --type=LoadBalancer

kubectl port-forward --namespace default $(kubectl get pods --namespace default -l app=bagan-influxdb -o jsonpath='{ .items[0].metadata.name }') 8086:8086 &
