package com.cloudera.avro;

import example.avro.DistData;
import org.apache.avro.mapred.AvroKey;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/* 
 * To define a map function for your MapReduce job, subclass 
 * the Mapper class and override the map method.
 * The class definition requires four parameters: 
 *   The data type of the input key
 *   The data type of the input value
 *   The data type of the output key (which is the input key type 
 *   for the reducer)
 *   The data type of the output value (which is the input value 
 *   type for the reducer)
 */

public class WriteAvroMapper extends Mapper<LongWritable, Text, AvroKey<DistData>, NullWritable> {
  
  /*
   * The map method runs once for each line of text in the input file.
   * The method receives a key of type LongWritable, a value of type
   * Text, and a Context object.
   */
  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    /*
     * Convert the line, which is received as a Text object,
     * to a String object.
     */
    String line = value.toString();
    String[] parts = line.split(" ");

    AvroKey<DistData> outputKey = new AvroKey<DistData>(new DistData());

    outputKey.datum().setName(parts[0]);
    outputKey.datum().setFavoriteColor(parts[2]);
    outputKey.datum().setFavoriteNumber(Integer.parseInt(parts[1]));

    context.write(outputKey, null);
      
  }

}