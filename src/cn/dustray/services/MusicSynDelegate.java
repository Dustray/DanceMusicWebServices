package cn.dustray.services;

import java.sql.Timestamp;
import cn.dustray.dao.*;
import cn.dustray.dispose.MusicLength;

@javax.jws.WebService(targetNamespace = "http://services.dustray.cn/", serviceName = "MusicSynService", portName = "MusicSynPort", wsdlLocation = "WEB-INF/wsdl/MusicSynService.wsdl")
public class MusicSynDelegate {

	cn.dustray.services.MusicSyn musicSyn = new cn.dustray.services.MusicSyn();

	public int getSynTime(String teamName, String musicName) {
		return musicSyn.getSynTime(teamName, musicName);
	}

	public String pauseMusic(String teamName, String musicName) {
		return musicSyn.pauseMusic(teamName, musicName);
	}

	public String continueMusic(String teamName, String musicName) {
		return musicSyn.continueMusic(teamName, musicName);
	}

	public int getMusicLength(String teamName, String musicName) {
		return musicSyn.getMusicLength(teamName, musicName);
	}

}