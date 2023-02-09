package bigdata;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class KmerReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
	
	private int numberOfKmers = 0;

	public void reduce(Text _key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		// process values
		int sum = 0;
		
		for (IntWritable val : values) {
			sum += val.get();
		}
			
		if(_key.toString().contains("T") && sum > 100) {
			numberOfKmers += sum;
			context.write(_key, new IntWritable(sum));
		}		
	}
	
	public void cleanup(Context context) throws IOException, InterruptedException {
		context.write(new Text("TOTAL: "), new IntWritable(numberOfKmers));
	}

}
