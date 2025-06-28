package com.users.config;

import javax.sql.DataSource;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DataSourceConfig {

	@Autowired
	private AwsSecretsManager awsSecretManager;

	@Autowired
	private Environment env;

	@Bean
	public DataSource dataSource() {
		// Try fetching credentials from AWS Secrets Manager
		String awsSecretName = env.getProperty("aws.datasource.secret.name");
		String secretJson = awsSecretManager.getSecret(awsSecretName);

		String url = env.getProperty("spring.datasource.url"); // Default to application.properties
		String username = env.getProperty("spring.datasource.username"); // Default to application.properties
		String password = env.getProperty("spring.datasource.password"); // Default to application.properties

		if (secretJson != null) {
			url = parseJson(secretJson, "url");
			username = parseJson(secretJson, "username");
			password = parseJson(secretJson, "password");
		}

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);

		return dataSource;
	}

	private String parseJson(String json, String key) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.has(key) ? jsonObject.getString(key) : null;
		} catch (Exception e) {
			log.error("Error while parsing json {}", json, e);
			return null;
		}
	}

}
