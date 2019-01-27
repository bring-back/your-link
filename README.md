# your-link


### For local test with docker
#### Prerequisites
Make sure you have already installed both Docker Engine and Docker Compose.  
https://docs.docker.com/compose/install/

#### Run ElasticSearch 
ElasticSearch is used for saving YourLink data.
You can run two ElasticSearch and Kibana with below command. 
```
$ docker-compose -f docker/docker-compose-es.yml up
```
ElasticSearch will use `9200` and `9201` port of your host.   
And Kibana will use `5601` port of your host.   
So You can search test data in http://127.0.0.1:5601 