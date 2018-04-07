package cn.dustray.dispose;

import java.io.File;
import java.io.FileInputStream;
import java.util.Timer;
import java.util.TimerTask;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;

public class MainDispose {
	private static int sumTime = -1;// ��ʱ��
	private static int countTime = 0;// ��ʱʱ��
	private static int timePeroid = -1;// ���ʱ��

	/**
	 * @param args
	 */
	public static void musicSynchronize(String musicName, int timePeroids) {
		sumTime = getAudioPlayTime("D:\\musiclist\\" + musicName + ".mp3");// ��ȡ���ֳ���
		timePeroid = timePeroids;// ����ʱ��ͬ��Ƶ�ʣ���λ��
		timer(timePeroid);
	}

	private static int getAudioPlayTime(String mp3) {
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
			System.out.println("�����ܳ��ȣ�" + i / 60 + ":" + i % 60);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rtTime;
	}

	// �趨ָ������task��ָ���ӳ�delay����й̶��ӳ�peroid��ִ��
	// schedule(TimerTask task, long delay, long period)
	private static void timer(int s) {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				if (countTime >= sumTime) {
					System.out.println("���Ž�����");

					timer.cancel();
				}
				System.out.println("��ʱ��" + countTime);
				// �˴��������ͽ��ȸ��û�
				countTime += timePeroid;
			}
		}, 0, s * 1000);
	}

}
