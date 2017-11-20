package com.imagine.go.util;

import java.text.DecimalFormat;

/**
 * SeekBarUtil:�����϶�������ֵ�ͷ�Χֵ
 * 
 * @author Jinhu
 * @date 2016/3/25
 */
public class SeekBarUtil {

	/**
	 * ��������ֵ���㷶Χ����СֵΪ 300��0-12�Ĳ���Ϊ 50��13-22 �Ĳ���Ϊ 100��23-30 �Ĳ���Ϊ 200�����ֵΪ 3400����
	 * 
	 * @return ��Χ��
	 */
	public static int calcRadius(int progress) {
		int radius = 0;
		if (progress <= 12) {
			radius = progress * 50 + 300;
		} else if (progress > 12 && progress <= 22) {
			radius = progress * 100 - 300;
		} else {
			radius = progress * 200 - 2600;
		}
		return radius;
	}

	/**
	 * ���ݰ뾶��Χ��������ֵ
	 * 
	 * @return
	 */
	public static int calcProgress(int radius) {
		int progress = 0;
		if (radius <= 900) {
			progress = (radius - 300) / 50;
		} else if (radius >= 1000 && radius <= 1900) {
			progress = (radius + 300) / 100;
		} else {
			progress = (radius + 2600) / 200;
		}
		return progress;
	}

	/**
	 * �������ʽ��Ϊ�ַ�����
	 * 
	 * @param distance
	 *            ���루��λΪ�ף���
	 * @return ��ʽ������ַ��������磺960.69 ��ʽ��Ϊ 960 m��1230.321 ��ʽ��Ϊ 1.2 km��12321.123 ��ʽ��Ϊ
	 *         12 km����
	 */
	public static String formatDistance(double distance) {
		StringBuilder builder = new StringBuilder();
		if (distance < 1000) {
			builder.append((int) distance);
			builder.append(" m");
		} else if (distance < 10000) {
			DecimalFormat format = new DecimalFormat("#.#");
			builder.append(format.format(distance / 1000));
			builder.append(" km");
		} else {
			builder.append((int) (distance / 1000));
			builder.append(" km");
		}
		return builder.toString();
	}
}
