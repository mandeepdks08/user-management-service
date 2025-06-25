package com.users.config;

import javax.sql.DataSource;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DataSourceConfig {

	@Autowired
	private AwsSecretsManager awsSecretManager;

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Value("${spring.datasource.username}")
	private String dbUsername;

	@Value("${spring.datasource.password}")
	private String dbPassword;

	@Value("${aws.datasource.secret.name}")
	private String awsSecretName;

	@Bean
	public DataSource dataSource() {
		// Try fetching credentials from AWS Secrets Manager
		String secretJson = awsSecretManager.getSecret(awsSecretName);

		String url = dbUrl; // Default to application.properties
		String username = dbUsername; // Default to application.properties
		String password = dbPassword; // Default to application.properties

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
