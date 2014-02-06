avro-java-sample
================

mvn clean package

mvn exec:java -q -Dexec.mainClass=example.GenerateData

hadoop jar ./target/avro-java-sample-1.0-SNAPSHOT-jar-with-dependencies.jar com.cloudera.avro.MapReduceAvroWordCount /tmp/input/ /tmp/out10/

hadoop jar ./target/avro-java-sample-1.0-SNAPSHOT-jar-with-dependencies.jar com.cloudera.avro.WriteAvroMapOnlyJob /user/cloudera/sample.txt /tmp/out1/
hadoop jar ./target/avro-java-sample-1.0-SNAPSHOT-jar-with-dependencies.jar com.cloudera.avro.ReadAvroMapOnlyJob /tmp/out1/ /tmp/out2/

java -jar /home/cloudera/Downloads/avro-tools-1.6.1.jar

CREATE TABLE dist_data
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.avro.AvroSerDe'
STORED AS INPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat'
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat'
LOCATION '/tmp/out2/'
TBLPROPERTIES ('avro.schema.literal'='{"namespace": "dist_data.avro",
                "type": "record",
                "name": "DistData",
                 "fields": [
                   {"name": "name", "type": "string"},
                   {"name": "favorite_number",  "type": ["int", "null"]},
                   {"name": "favorite_color", "type": ["string", "null"]},
                   {"name": "favorite_movie", "type": ["string", "null"]}
                ]}');


DROP TABLE IF EXISTS your_table;
CREATE EXTERNAL TABLE your_table
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.avro.AvroSerDe' STORED AS
INPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat'
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat'
LOCATION '/user/your_data/'
TBLPROPERTIES ( 'avro.schema.url'='hdfs:///user/schema/your_data_schema.json' );


