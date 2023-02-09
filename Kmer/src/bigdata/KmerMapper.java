package bigdata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class KmerMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	private final IntWritable one = new IntWritable(1);
	
	public void map(LongWritable ikey, Text ivalue, Context context) throws IOException, InterruptedException {
		
		String kmer = ivalue.toString();
		int kmer_length = kmer.length();
		
		for(int i = 3; i <= kmer_length; i++) {
			context.write(new Text(kmer.substring(i - 3, i)), one);
		}
	}

}
