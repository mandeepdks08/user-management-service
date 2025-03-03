package com.users.config;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Service
@Slf4j
public class AwsSecretsManager {

	private final SecretsManagerClient secretsManagerClient;

	public AwsSecretsManager() {
		this.secretsManagerClient = SecretsManagerClient.builder().region(Region.AP_SOUTH_1) // Replace with your region
				.credentialsProvider(DefaultCredentialsProvider.create()).build();
	}

	public String getSecret(String secretName) {
		try {
			GetSecretValueRequest request = GetSecretValueRequest.builder().secretId(secretName).build();
			GetSecretValueResponse response = secretsManagerClient.getSecretValue(request);
			return response.secretString(); // Returns the secret value as a JSON string
		} catch (Exception e) {
			log.error("Error while fetching secret {} from AWS", secretName, e);
			return null;
		}
	}
}
