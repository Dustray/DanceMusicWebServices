package cn.dustray.services;

import java.sql.Timestamp;

import cn.dustray.dao.*;
import cn.dustray.dispose.MusicLength;

public class MusicSyn {

	public int getSynTime(String teamName, String musicName) {

		MusicStateDao msd = new MusicStateDao();
		// ��֤�Ƿ��Ѵ���ĳ�����Ų�����Ϣ
		if (msd.getTeam(teamName)) {// trueΪ�Ѵ���
			// ��ȡ���ֲ���״̬
			if (msd.getMusicState(teamName) == 1) {// 1-�����У�0-��ͣ��-1-Ĭ��״̬
				// ��ȡ���ֿ�ʼ��������ʱ��
				Timestamp ts = msd.getStartDateTime(teamName);
				String tsStr = ts.toString();// ��ʼ���ŵ�����ʱ��
				long s = new Timestamp(System.currentTimeMillis()).getTime()
						- ts.getTime();

				return (int)s;//���Ž���
			} else if (msd.getMusicState(teamName) == 0) {
				long s = msd.getPauseTime(teamName);
				return -1;//"����"+s+"���봦��ͣ";
			} else {
				return -2;//"����״̬����";
			}
		} else {
			// ���
			System.out.println(msd.getTeam(teamName));
			if (msd.addNewMusic(teamName, musicName)) {
				return 0;//"����ӳɹ�";
			}
			return -3;//"���ʧ��";
		}

	}

	/**
	 * ������ͣ
	 * 
	 * @param teamName
	 * @param musicName
	 * @return
	 */
	public String pauseMusic(String teamName, String musicName) {
		MusicStateDao msd = new MusicStateDao();
		return msd.pauseMusic(teamName);
	}

	/**
	 * ��������
	 * 
	 * @param teamName
	 * @param musicName
	 * @return
	 */
	public String continueMusic(String teamName, String musicName) {
		MusicStateDao msd = new MusicStateDao();
		return msd.continueMusic(teamName);
	}

	/**
	 * ��ȡ���ֳ���
	 * 
	 * @param musicName
	 * @return
	 */
	public int getMusicLength(String teamName, String musicName) {
		MusicLength ml = new MusicLength();
		int sumTime = ml.getAudioPlayTime("D:\\musiclist\\" + musicName
				+ ".mp3");// ��ȡ���ֳ���
		return sumTime;
	}

}
