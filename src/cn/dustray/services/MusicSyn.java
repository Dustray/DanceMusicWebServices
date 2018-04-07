package cn.dustray.services;

import java.sql.Timestamp;

import cn.dustray.dao.*;
import cn.dustray.dispose.MusicLength;

public class MusicSyn {

	public int getSynTime(String teamName, String musicName) {

		MusicStateDao msd = new MusicStateDao();
		// 验证是否已存在某条舞团播放信息
		if (msd.getTeam(teamName)) {// true为已存在
			// 获取音乐播放状态
			if (msd.getMusicState(teamName) == 1) {// 1-播放中；0-暂停；-1-默认状态
				// 获取音乐开始播放日期时间
				Timestamp ts = msd.getStartDateTime(teamName);
				String tsStr = ts.toString();// 开始播放的日期时间
				long s = new Timestamp(System.currentTimeMillis()).getTime()
						- ts.getTime();

				return (int)s;//播放进度
			} else if (msd.getMusicState(teamName) == 0) {
				long s = msd.getPauseTime(teamName);
				return -1;//"已在"+s+"毫秒处暂停";
			} else {
				return -2;//"播放状态错误";
			}
		} else {
			// 添加
			System.out.println(msd.getTeam(teamName));
			if (msd.addNewMusic(teamName, musicName)) {
				return 0;//"已添加成功";
			}
			return -3;//"添加失败";
		}

	}

	/**
	 * 歌曲暂停
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
	 * 歌曲播放
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
	 * 获取音乐长度
	 * 
	 * @param musicName
	 * @return
	 */
	public int getMusicLength(String teamName, String musicName) {
		MusicLength ml = new MusicLength();
		int sumTime = ml.getAudioPlayTime("D:\\musiclist\\" + musicName
				+ ".mp3");// 获取音乐长度
		return sumTime;
	}

}
