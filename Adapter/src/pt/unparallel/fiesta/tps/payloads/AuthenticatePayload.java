package pt.unparallel.fiesta.tps.payloads;

public class AuthenticatePayload {
	
	private String tokenId;
	private String successUrl;
	
	public AuthenticatePayload() {
		
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}
}
