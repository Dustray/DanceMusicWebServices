package cn.dustray.dispose;

import java.io.File;
import java.io.FileInputStream;
import java.util.Timer;
import java.util.TimerTask;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;

public class MainDispose {
	private static int sumTime = -1;// 总时间
	private static int countTime = 0;// 计时时间
	private static int timePeroid = -1;// 间隔时间

	/**
	 * @param args
	 */
	public static void musicSynchronize(String musicName, int timePeroids) {
		sumTime = getAudioPlayTime("D:\\musiclist\\" + musicName + ".mp3");// 获取音乐长度
		timePeroid = timePeroids;// 设置时间同步频率，单位秒
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
			System.out.println("音乐总长度：" + i / 60 + ":" + i % 60);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rtTime;
	}

	// 设定指定任务task在指定延迟delay后进行固定延迟peroid的执行
	// schedule(TimerTask task, long delay, long period)
	private static void timer(int s) {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				if (countTime >= sumTime) {
					System.out.println("播放结束。");

					timer.cancel();
				}
				System.out.println("加时：" + countTime);
				// 此处将来发送进度给用户
				countTime += timePeroid;
			}
		}, 0, s * 1000);
	}

}
