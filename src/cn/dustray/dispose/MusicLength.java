package cn.dustray.dispose;

import java.io.File;
import java.io.FileInputStream;
import java.util.Timer;
import java.util.TimerTask;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;

public class MusicLength {

	/**
	 * ��ȡ��������
	 * 
	 * @param musicName
	 * @return
	 */
//	public static int getMusicLenth(String musicName) {
//		int sumTime = getAudioPlayTime("D:\\musiclist\\" + musicName + ".mp3");// ��ȡ���ֳ���
//		return sumTime;
//	}

	public int getAudioPlayTime(String mp3) {
		int rtTime = -1;
		File file = new File(mp3);
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			int b = fis.available();
			Bitstream bt = new Bitstream(fis);
			Header h = bt.readFrame();
			int time = (int) h.total_ms(b);
			int i = time / 1000;
			rtTime = i;
			// System.out.println("�����ܳ��ȣ�" + i / 60 + ":" + i % 60);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rtTime;
	}

}
