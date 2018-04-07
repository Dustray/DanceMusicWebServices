package cn.dustray.dao;

import java.sql.Timestamp;

import cn.dustray.dao.Dbutil;
import cn.dustray.dispose.MusicLength;

public class MusicStateDao extends Dbutil {

	/**
	 * 添加音乐播放
	 * 
	 * @param teamName
	 * @param musicName
	 * @return
	 */
	public boolean addNewMusic(String teamName, String musicName) {
		boolean flag = false;
		MusicLength ml = new MusicLength();
		int sumTime = ml.getAudioPlayTime("D:\\musiclist\\" + musicName+".mp3");// 获取音乐长度
		try {
			getConnection();
			String sql = "insert into playstateinfo (teamName,musicName,startDateTime,pauseTime,totalTime,playState) values(?,?,?,?,?,?);";
			pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, teamName);
			pStmt.setString(2, musicName);
			pStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			pStmt.setInt(4, -1);
			pStmt.setInt(5, sumTime);
			pStmt.setInt(6, 1);

			if (pStmt.executeUpdate() > 0) {
				flag = true;
			}

		} catch (Exception e) {
			closeAll();
			System.out.println(e.toString());
		}
		return flag;
	}

	/**
	 * 验证是否已存在某条舞团播放信息
	 * 
	 * @param teamName
	 * @return
	 */
	public boolean getTeam(String teamName) {
		boolean flag = false;
		try {
			getConnection();
			String sql = "select * from playstateinfo where teamName like ?;";
			pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, teamName);
			rs = pStmt.executeQuery();
			rs.last();
			int countNum = rs.getRow();
			// System.out.println(countNum);
			if (countNum == 1) {
				flag = true;
			} else if (countNum > 1) {
				// 执行删除操作
				if (deleteTeam(teamName)) {
					System.out.println("删除成功");
				} else {
					System.out.println("删除失败");
				}
			}

		} catch (Exception e) {
			closeAll();
		}
		return flag;
	}

	/**
	 * 获取音乐播放状态
	 * 
	 * @param teamName
	 * @return
	 */
	public int getMusicState(String teamName) {
		int flag = -1;
		try {
			getConnection();
			String sql = "select playState from playstateinfo where teamName like ?;";
			pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, teamName);
			rs = pStmt.executeQuery();
			while (rs.next()) {
				flag = rs.getInt("playstate");
			}

		} catch (Exception e) {
			closeAll();
		}
		return flag;
	}
	/**
	 * 获取音乐名称
	 * 
	 * @param teamName
	 * @return
	 */
	public String getMusicName(String teamName) {
		String flag = "null";
		try {
			getConnection();
			String sql = "select musicname from playstateinfo where teamName like ?;";
			pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, teamName);
			rs = pStmt.executeQuery();
			while (rs.next()) {
				flag = rs.getString("musicname");
			}

		} catch (Exception e) {
			closeAll();
		}
		return flag;
	}
	/**
	 * 删除所有这个舞队信息
	 * 
	 * @param teamName
	 * @return
	 */
	public boolean deleteTeam(String teamName) {
		boolean flag = false;
		try {
			getConnection();
			String sql = "delete from playstateinfo where teamName like ?;";
			pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, teamName);
			if (pStmt.executeUpdate() > 0) {

				flag = true;
			}

		} catch (Exception e) {
			closeAll();
		}
		return flag;
	}

	/**
	 * 获取音乐开始播放日期时间
	 * 
	 * @param teamName
	 * @return
	 */
	public Timestamp getStartDateTime(String teamName) {
		Timestamp flag = new Timestamp(System.currentTimeMillis());
		try {
			getConnection();
			String sql = "select startDateTime from playstateinfo where teamName like ?;";
			pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, teamName);
			rs = pStmt.executeQuery();
			while (rs.next()) {
				flag = rs.getTimestamp("startDateTime");
			}

		} catch (Exception e) {
			closeAll();
		}
		return flag;
	}

	/**
	 * 获取暂停时已播放进度
	 * 
	 * @param teamName
	 * @return
	 */
	public int getPauseTime(String teamName) {
		int flag = -1;
		try {
			getConnection();
			String sql = "select pauseTime from playstateinfo where teamName like ?;";
			pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, teamName);
			rs = pStmt.executeQuery();
			while (rs.next()) {
				flag = rs.getInt("pauseTime");
			}

		} catch (Exception e) {
			closeAll();
		}
		return flag;
	}

	/**
	 * 音乐暂停（修改播放状态为0并记录已播放长度）
	 * 
	 * @param teamName
	 * @return
	 */

	public String pauseMusic(String teamName) {
		String flag = "初始flag";
		long sTime = -1;
		if (getMusicState(teamName) == 0) {
			flag = "当前已是暂停状态";
		} else {
			if (getStartDateTime(teamName) == null) {
				flag = "未找到歌曲时间";
			} else {
				sTime = new Timestamp(System.currentTimeMillis()).getTime()
						- getStartDateTime(teamName).getTime();
			}
			try {
				getConnection();
				String sql = "update playstateinfo set pauseTime = ?, playState = 0 where teamName like ?;";
				pStmt = conn.prepareStatement(sql);
				pStmt.setLong(1, sTime);
				pStmt.setString(2, teamName);
				if (pStmt.executeUpdate() > 0) {
					flag = "暂停成功";
				}
			} catch (Exception e) {
				closeAll();
			}
		}
		return flag;

	}

	/**
	 * 音乐继续（修改播放状态为1并记录继续的瞬时时间-已播放长度）
	 * 
	 * @param teamName
	 * @return
	 */
	public String continueMusic(String teamName) {
		String flag = "初始flag";
		long sTime = -1;
		if (getMusicState(teamName) == 1) {
			flag = "当前已是播放状态";
		} else {
			if (getPauseTime(teamName) == -1) {
				flag = "未找到已播放时间";
			} else {
				sTime = getPauseTime(teamName);
				// 修改播放状态为1并记录继续的瞬时时间-已播放长度
				long t = new Timestamp(System.currentTimeMillis()).getTime()
						- (long)sTime;
				Timestamp ts = new Timestamp(t);
				try {
					getConnection();
					String sql = "update playstateinfo set startDateTime = ? , pauseTime = -1, playState = 1 where teamName like ?;";
					pStmt = conn.prepareStatement(sql);
					pStmt.setTimestamp(1, ts);
					pStmt.setString(2, teamName);
					if (pStmt.executeUpdate() > 0) {
						flag = "继续成功";
					}
				} catch (Exception e) {
					closeAll();
				}

			}
		}
		return flag;

	}
}
