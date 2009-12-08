package se.smrt.core.remote;

public interface RemoteProtocol {
	public static final String SMRT_VERSION = "1.0";
	
    void sendVersion(String smrtVersion, String protocolVersion, byte[] protocolChecksum);
}
