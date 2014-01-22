import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.conf.Configuration;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyOutputFormat;
import org.apache.avro.generic.GenericRecord;

import example.avro.DistData;

/* 
 * MapReduce jobs are typically implemented by using a driver class.
 * The purpose of a driver class is to set up the configuration for the
 * MapReduce job and to run the job.
 * Typical requirements for a driver class include configuring the input
 * and output data formats, configuring the map and reduce classes,
 * and specifying intermediate data formats.
 * 
 * The following is the code for the driver class:
 */
public class ReadOnlyJob {

  public static void main(String[] args) throws Exception {

    /*
     * The expected command-line arguments are the paths containing
     * input and output data. Terminate the job if the number of
     * command-line arguments is not exactly 2.
     */
    if (args.length != 2) {
      System.out.printf(
          "Usage: ReadOnly <input dir> <output dir>\n");
      System.exit(-1);
    }

    /*
     * Instantiate a Job object for your job's configuration.  
     */
    Job job = new Job();
    
    /*
     * Specify the jar file that contains your driver, mapper, and reducer.
     * Hadoop will transfer this jar file to nodes in your cluster running
     * mapper and reducer tasks.
     */
    job.setJarByClass(ReadOnlyJob.class);
    
    /*
     * Specify an easily-decipherable name for the job.
     * This job name will appear in reports and logs.
     */
    job.setJobName("Read only benchmark");

    /*
     * Specify the paths to the input and output data based on the
     * command-line arguments.
     */
    FileInputFormat.setInputPaths(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    
    /*
     * Specify the mapper and reducer classes.
     */
    job.setMapperClass(ReadOnlyMapper.class);
  
    AvroJob.setOutputKeySchema(job, DistData.getClassSchema());
    //job.setMapOutputKeyClass(AvroKey.class);
    //job.setMapOutputValueClass(NullWritable.class);
    //job.setOutputFormatClass(AvroKeyOutputFormat.class);
    job.setNumReduceTasks(0);


    //AvroJob.setOutputKeySchema(job, getAvroSchema(schemaFile))
    job.setOutputFormatClass(AvroKeyOutputFormat.class);
    job.setOutputKeyClass(AvroKey.class);
    job.setOutputValueClass(NullWritable.class);
    FileOutputFormat.setCompressOutput(job, true);

    /*
     * Start the MapReduce job and wait for it to finish.
     * If it finishes successfully, return 0. If not, return 1.
     */
    boolean success = job.waitForCompletion(true);
    System.exit(success ? 0 : 1);
  }
}
