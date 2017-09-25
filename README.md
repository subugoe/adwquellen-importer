# Data importer for AdW Quellen

## Installation (Solr)

- Create a new directory and change into it:

``` mkdir mysolr ```

``` cd mysolr ```

- Clone the project into that directory:

``` git clone <URL> . ```

- Make the core directory writable:

``` chmod a+w solr/adwquellen ```

- Change the outside port in the docker-compose.yml, in section 'ports', e. g. to 4321:

``` - 4321:8983 ```

- Start Solr:

``` docker-compose up -d solr ```
