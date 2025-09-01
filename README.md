# company-profile-delta-consumer

Transforms company profile deltas into an entity sent to company-profile-api.

## System requirements

* [Git](https://git-scm.com/downloads)
* [Java](http://www.oracle.com/technetwork/java/javase/downloads)
* [Maven](https://maven.apache.org/download.cgi)
* [MongoDB](https://www.mongodb.com/)
* [Apache Kafka](https://kafka.apache.org/)

## Running integration tests locally

These can be run using the command:

```shell
mvn integration-test -Dskip.unit.tests=true failsafe:verify
```

Note that they are not currently run as part of the pipeline 'analyse-pull-request' build task.

## Building and Running Locally using Docker

1. Clone [Docker CHS Development](https://github.com/companieshouse/docker-chs-development) and follow the steps in the
   README.
2. Enable the following services using the command `./bin/chs-dev services enable <service>`.
   * company-profile-delta-consumer
   * chs-delta-api
   * company-profile-api
3. Enable the streaming module using the command `./bin/chs-dev modules enable streaming`
4. Boot up the services' containers on docker using chs-dev `chs-dev up`.
5. Messages can be produced to the topic company-profile-delta using the instructions given in
   [CHS Kafka API](https://github.com/companieshouse/chs-kafka-api).

## Environment variables

| Variable                                     | Description                                                                         | Example(from docker-chs-development)                   |
|----------------------------------------------|-------------------------------------------------------------------------------------|--------------------------------------------------------|
| SERVER_PORT                                  | The server port of this service                                                     | 8081                                                   |
| SPRINGFOX_DOCUMENTATION_AUTO_STARTUP         | A boolean value to enable or disable the auto-startup of Springfox documentation    | false                                                  |
| LOGGER_NAMESPACE                             | The namespace used for logging                                                      | company-profile-delta-consumer                         |
| SPRING_KAFKA_BOOTSTRAP_SERVERS               | The URL to the Kafka broker                                                         | kafka:9092                                             |
| SPRING_KAFKA_LISTENER_CONCURRENCY            | The number of listeners run in parallel for the consumer                            | 1                                                      |
| COMPANY_PROFILE_DELTA_GROUP_ID               | The group ID for the Kafka consumer                                                 | company-profile-delta-consumer                         |
| COMPANY_DELTA_TOPIC                          | The topic name for company profile delta updates                                    | company-profile-delta                                  |
| COMPANY_PROFILE_DELTA_ATTEMPTS               | The number of times a message will be retried before being moved to the error topic | 5                                                      |
| COMPANY_PROFILE_DELTA_BACKOFF_DELAY          | The incremental time delay (in milliseconds) between message retries                | 15000                                                  |
| MANAGEMENT_ENDPOINTS_ENABLED_BY_DEFAULT      | A boolean value to enable or disable all management endpoints by default            | false                                                  |
| MANAGEMENT_ENDPOINTS_WEB_BASE_PATH           | The base path for management endpoints                                              | /                                                      |
| MANAGEMENT_ENDPOINTS_WEB_PATH_MAPPING_HEALTH | The specific path for the health endpoint                                           | company-profile-delta-consumer/healthcheck             |
| MANAGEMENT_ENDPOINT_HEALTH_SHOW_DETAILS      | The level of detail shown in the health endpoint                                    | never                                                  |
| MANAGEMENT_ENDPOINT_HEALTH_ENABLED           | A boolean value to enable or disable the health endpoint                            | true                                                   |
| API_COMPANY_PROFILE_API_KEY                  | The API key for accessing the company profile API                                   | g9yZIA81Zo9J46Kzp3JPbfld6kOqxR47EAYqXbRV               |
| API_URL                                      | The URL of the external API                                                         | [http://api.chs.local:4001](http://api.chs.local:4001) |
| INTERNAL_API_URL                             | The URL of the internal API                                                         | [http://api.chs.local:4001](http://api.chs.local:4001) |
| HUMAN_LOG                                    | A boolean value to enable more readable log messages                                | 1                                                      |
| KAFKA_AUTO_COMMIT                            | A boolean value to enable or disable Kafka auto-commit                              | false                                                  |
| KAFKA_GROUP_NAME                             | The name of the Kafka group                                                         | company-profile-delta-consumer                         |
| KAFKA_POLLING_DURATION_MS                    | The duration in milliseconds for Kafka polling                                      | 5000                                                   |
| KAFKA_POLLING_INITIAL_DELAY_MS               | The initial delay in milliseconds before Kafka polling starts                       | 1000                                                   |
| KAFKA_TOPICS_LIST                            | A comma-separated list of Kafka topics                                              | company-profile-delta                                  |
| LOGLEVEL                                     | The level of log messages output to the logs                                        | debug                                                  |
| MAXIMUM_RETRY_ATTEMPTS                       | The number of times a message will be retried before being moved to the error topic | 5                                                      |
| RETRY_THROTTLE_RATE_SECONDS                  | The delay in seconds between retry attempts                                         | 5                                                      |
| COMPANY_PROFILE_DATA_API_KEY                 | The API key for accessing the company profile data API                              | abyZIA81Zo9J47Kzp3JPbfld6kOqxR47EAYqXbRW               |

## Building the docker image

    mvn compile jib:dockerBuild -Dimage=416670754337.dkr.ecr.eu-west-2.amazonaws.com/company-profile-delta-consumer

## To make local changes

Development mode is available for this service
in [Docker CHS Development](https://github.com/companieshouse/docker-chs-development).

    ./bin/chs-dev development enable company-profile-delta-consumer

This will clone the company-profile-delta-consumer into the repositories folder. Any changes to the code, or resources
will
automatically trigger a rebuild and relaunch.
