package cn.dustray.dao;

import java.sql.Timestamp;

import cn.dustray.dao.Dbutil;
import cn.dustray.dispose.MusicLength;

public class MusicStateDao extends Dbutil {

	/**
	 * ������ֲ���
	 * 
	 * @param teamName
	 * @param musicName
	 * @return
	 */
	public boolean addNewMusic(String teamName, String musicName) {
		boolean flag = false;
		MusicLength ml = new MusicLength();
		int sumTime = ml.getAudioPlayTime("D:\\musiclist\\" + musicName+".mp3");// ��ȡ���ֳ���
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
	 * ��֤�Ƿ��Ѵ���ĳ�����Ų�����Ϣ
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
				// ִ��ɾ������
				if (deleteTeam(teamName)) {
					System.out.println("ɾ���ɹ�");
				} else {
					System.out.println("ɾ��ʧ��");
				}
			}

		} catch (Exception e) {
			closeAll();
		}
		return flag;
	}

	/**
	 * ��ȡ���ֲ���״̬
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
	 * ��ȡ��������
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
	 * ɾ��������������Ϣ
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
	 * ��ȡ���ֿ�ʼ��������ʱ��
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
	 * ��ȡ��ͣʱ�Ѳ��Ž���
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
	 * ������ͣ���޸Ĳ���״̬Ϊ0����¼�Ѳ��ų��ȣ�
	 * 
	 * @param teamName
	 * @return
	 */

	public String pauseMusic(String teamName) {
		String flag = "��ʼflag";
		long sTime = -1;
		if (getMusicState(teamName) == 0) {
			flag = "��ǰ������ͣ״̬";
		} else {
			if (getStartDateTime(teamName) == null) {
				flag = "δ�ҵ�����ʱ��";
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
					flag = "��ͣ�ɹ�";
				}
			} catch (Exception e) {
				closeAll();
			}
		}
		return flag;

	}

	/**
	 * ���ּ������޸Ĳ���״̬Ϊ1����¼������˲ʱʱ��-�Ѳ��ų��ȣ�
	 * 
	 * @param teamName
	 * @return
	 */
	public String continueMusic(String teamName) {
		String flag = "��ʼflag";
		long sTime = -1;
		if (getMusicState(teamName) == 1) {
			flag = "��ǰ���ǲ���״̬";
		} else {
			if (getPauseTime(teamName) == -1) {
				flag = "δ�ҵ��Ѳ���ʱ��";
			} else {
				sTime = getPauseTime(teamName);
				// �޸Ĳ���״̬Ϊ1����¼������˲ʱʱ��-�Ѳ��ų���
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
						flag = "�����ɹ�";
					}
				} catch (Exception e) {
					closeAll();
				}

			}
		}
		return flag;

	}
}
