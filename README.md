Spring-Kafka Messaging app for End Point blog series: https://www.endpointdev.com/blog/tags/spring-kafka-series

Example curl commands

# activation
curl -H "Content-Type: application/json" -X POST localhost:8080/api/auth/getcode -d '{ "mobile": "01234" }'

# login and get access-token
curl -H "Content-Type: application/json" -X POST localhost:8080/api/auth/login -d '{ "mobile": "01234", "activationCode": "309652" }'

# send message REST example
curl -H "Content-Type: application/json" -X POST localhost:8080/api/message/send-message -d '{ "accessToken": "5eab27f8-8748-4fdc-a4de-ac782ce17a74", "sendTo": 5, "msg": "test message" }'


Websocket web UI example is now available.

![Messaging example web UI](https://github.com/ashemez/SpringKafkaMessaging/blob/master/kafkamessagewebui.png)

