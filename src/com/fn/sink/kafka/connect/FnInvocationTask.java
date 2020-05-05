package com.fn.sink.kafka.connect;

import com.fn.sink.kafka.connect.http.FnHTTPPost;

import java.util.Collection;
import java.util.Map;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;

public class FnInvocationTask extends SinkTask {

	private FnInvocationConfig config;

	@Override
	public String version() {
		return "v1.0";
	}

	@Override
	public void start(Map<String, String> props) {
		config = new FnInvocationConfig(props);
	}

	@Override
	public void put(Collection<SinkRecord> records) {

		for (SinkRecord record : records) {

			//check business logic
			if(triggerFn(record)) {
				try {
					FnHTTPPost fnPOST = new FnHTTPPost(config.getTenantOcid(), config.getUserOcid(), 
							config.getPublicFingerprint(), config.getPrivateKeyLocation());
					String fnPOSTResult = fnPOST.invoke(config.getFunctionUrl(), (String) record.value());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	@Override
	public void stop() {
		//
	}

	@Override
	public void open(Collection<TopicPartition> partitions) {
		super.open(partitions);
		
		//test
		this.context.assignment().stream()
		.forEach((tp) -> System.out.println("Task assigned partition "+ tp.partition() + " in topic "+ tp.topic()));
	}

	/**
	 * @param SinkRecord record 
	 * here is the business logic to decide if trigger or not a FN function.
	 */
	private boolean triggerFn(SinkRecord record){

		boolean trigger  = true;

		if(record.value() != null){
			// do some validation here
		}

		return trigger;
	}

}