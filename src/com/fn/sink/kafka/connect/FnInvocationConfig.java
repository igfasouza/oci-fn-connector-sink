package com.fn.sink.kafka.connect;

import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;

public final class FnInvocationConfig extends AbstractConfig {

	public static final String TENANT_OCID_CONFIG = "tenant_ocid";
	private static final String TENANT_OCID_CONFIG_DESC = "OCI Root Tenant OCID";

	public static final String USER_OCID_CONFIG = "user_ocid";
	private static final String USER_OCID_CONFIG_DESC = "User OCID";

	public static final String PUBLIC_KEY_FINGERPRINT_CONFIG = "public_fingerprint";
	private static final String PUBLIC_KEY_FINGERPRINT_CONFIG_DESC = "Public Key Fingerprint";

	public static final String PRIVATE_KEY_CONFIG = "private_key_location";
	private static final String PRIVATE_KEY_CONFIG_DESC = "Private Key (.pem) location";

	public static final String FUNCTION_URL_CONFIG = "function_url";
	private static final String FUNCTION_URL_CONFIG_DESC = "Function endpoint URL";

	private final String tenantOcid;
	private final String userOcid;
	private final String publicFingerprint;
	private final String privateKeyLocation;
	private final String functionUrl;

	public FnInvocationConfig(Map<String, ?> originals) {
		super(getConfigDef(), originals);
		this.tenantOcid = this.getString(TENANT_OCID_CONFIG);
		this.userOcid = this.getString(USER_OCID_CONFIG);
		this.publicFingerprint = this.getString(PUBLIC_KEY_FINGERPRINT_CONFIG);
		this.privateKeyLocation = this.getString(PRIVATE_KEY_CONFIG);
		this.functionUrl = this.getString(FUNCTION_URL_CONFIG);
	}

	public static ConfigDef getConfigDef() {
		return new ConfigDef()
				.define(TENANT_OCID_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, TENANT_OCID_CONFIG_DESC)
				.define(USER_OCID_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, USER_OCID_CONFIG_DESC)
				.define(PUBLIC_KEY_FINGERPRINT_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, PUBLIC_KEY_FINGERPRINT_CONFIG_DESC)
				.define(PRIVATE_KEY_CONFIG, ConfigDef.Type.STRING, ConfigDef.NO_DEFAULT_VALUE, new PrivateKeyFileTypeValidator(), ConfigDef.Importance.HIGH, PRIVATE_KEY_CONFIG_DESC)
				.define(FUNCTION_URL_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, FUNCTION_URL_CONFIG_DESC);
	}
	
	public String getTenantOcid() {
		return tenantOcid;
	}

	public String getUserOcid() {
		return userOcid;
	}

	public String getPublicFingerprint() {
		return publicFingerprint;
	}

	public String getPrivateKeyLocation() {
		return privateKeyLocation;
	}

	public String getFunctionUrl() {
		return functionUrl;
	}


	private static class PrivateKeyFileTypeValidator implements ConfigDef.Validator {

		 /**
         * example to add some validation in the parameters
         * check if key is a *.pem file
         */
		@Override
		public void ensureValid(String configName, Object privateKeyLocation) {
			if (!((String)privateKeyLocation).endsWith(".pem")) {
				throw new ConfigException(configName, privateKeyLocation, "Private key should be of type PEM with a .pem extension");
			}
		}

	}
	
}