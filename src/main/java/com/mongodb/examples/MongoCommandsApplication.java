package com.mongodb.examples;

import com.mongodb.MongoClientException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MongoCommandsApplication implements CommandLineRunner {

	@Value("${mongodb.database}")
	private String dbName;
	@Value("${mongodb.collection}")
	private String collectionName;
	@Value("${truststore.path}")
	private String trustStorePath;
	@Value("${truststore.pwd}")
	public String trustStorePwd;

	@Value("${mongodb.uri}")
	private String mongoURI;

	@Autowired
	MongoConfig config;

	Integer integer = Integer.MAX_VALUE;

	static MongoClient client;
	private static Logger LOG = LoggerFactory
			.getLogger(MongoCommandsApplication.class);


	public static void main(String[] args) throws Exception {
		SpringApplication.run(MongoCommandsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.info("Application Started !!");


		try {
			MongoDatabase db = getMongoClient().getDatabase(dbName);
			Document stats = db.runCommand(new Document("collStats", collectionName));

			LOG.info("CollStats: " + stats);
			Long estimatedRowCount = Long.valueOf(stats.get("count", 0));
			Long estimatedDataBytes = Long.valueOf(stats.get("size", 0));

		} catch (MongoClientException e) {
			LOG.error(e.getMessage());
		}

		LOG.info("Done");

	}

	private MongoClient getMongoClient() {

		if (client == null)
			client = config.mongo();

		return client;
	}

}
