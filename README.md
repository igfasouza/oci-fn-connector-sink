## Sink Kafka connector

This is not ORACLE official product.

This is a example of Kafka Connector to the OCI functions API.


### Deploy

   mvn package
   
   
### run Docker

```
docker run -it --rm --name fn-oci-connect-demo -p 8083:8083 -e GROUP_ID=1 \
    -e BOOTSTRAP_SERVERS="bootstrap_URL" \
    -e CONFIG_STORAGE_TOPIC=”ID-config” \
    -e OFFSET_STORAGE_TOPIC=”ID-offset” \
    -e STATUS_STORAGE_TOPIC=”ID-status” \
    -v fn-sink-kafka-connector/target/fn-sink-kafka-connector-0.0.1-SNAPSHOT-jar-with-dependencies.jar/:/kafka/connect/fn-connector \
    debezium/connect:latest
```


### Configure

```
curl -X POST \
    http://localhost:8082/connectors \
    -H 'content-type: application/json' \
    -d '{
    "name": "FnSinkConnector",
    "config": {
      "connector.class": "com.fn.sink.kafka.connect.FnSinkConnector",
      "tasks.max": "1",
      "topics": "test-sink-topic",
      "tenant_ocid": "<tenant_ocid>",
      "user_ocid": "<user_ocid>",
      "public_fingerprint": "<public_fingerprint>",
      "private_key_location": "/path/to/kafka-connect/secrets/<private_key_name>",
      "function_url": "<FUNCTION_URL>"
    }
  }'
```

    
### License

Free - Open


## Disclaimer
This is a personal repository. Any code, views or opinions represented here are personal and belong solely to me and do not represent those of people, institutions or organizations that I may or may not be associated with in professional or personal capacity, unless explicitly stated.
