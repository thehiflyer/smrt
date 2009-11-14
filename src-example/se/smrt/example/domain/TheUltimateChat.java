package se.smrt.example.domain;

import se.smrt.core.SmrtProtocol;

@SmrtProtocol
public interface TheUltimateChat {
	void login(String loginName, String password);
	void logout();
	void say(String message);
	void whisper(int whoId, String message);
}
