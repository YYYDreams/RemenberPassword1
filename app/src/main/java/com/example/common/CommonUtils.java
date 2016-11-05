package com.example.common;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * ����������
 * @author zcw
 *
 */
public class CommonUtils {
	private static Toast toast=null;
	
	/**
	 * ��ȡ��ǰʱ�� 
	 * @param format ��ȡ��ʱ���ʽ������yyyy-MM-dd HH:mm:ss
	 * @return	���ص�ǰʱ����ַ���
	 */
	public static String GetCurrentTime(String format){
		SimpleDateFormat formatter = new SimpleDateFormat (format);
		Date curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��
		String str = formatter.format(curDate);
		
		return str;
	}
	
	/**
     * ����豸�Ƿ���������
     * @return	�Ѿ��������緵��true�����򷵻�false
     * @���������� 2015-04-22
     */
    public static boolean CheckNetwork(Activity activity){
    	ConnectivityManager con=(ConnectivityManager)activity.getSystemService(Activity.CONNECTIVITY_SERVICE);  
    	boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();  
    	boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
    
    	return wifi | internet;
    }
    
    /**
     * ����Toast�ظ���ʾ
     * @param context
     * @param str	Ҫ��ʾ������
     * @���������� 2015-04-22
     */
  	public static void ShowToast(Context context,String str){
		if (toast == null) {
            toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        } else {
            toast.setText(str);
        }
		toast.show();
  	}
  	
  	/**
     * ����Toast�ظ���ʾ
     * @param context
     * @param resId	Ҫ��ʾ��Id,���resIdΪ-1�������κβ�����ֱ�ӷ���
     * @���������� 2015-05-25
     */
  	public static void ShowToast(Context context,int resId){
  		if(-1==resId){
  			return ;
  		}
  		
		if (toast == null) {
            toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        } else {
            toast.setText(resId);
        }
		toast.show();
  	}
  	
  	/**
  	 * ��ȡ��Ļ�ߴ�
  	 * @param context
  	 * @return	������Ļ�Ŀ�Ⱥ͸߶�
  	 */
  	public static int[] getDisplaySize(Context context){
  		int[] displaySize={-1,-1};
  		
  		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
  		displaySize[0]= wm.getDefaultDisplay().getWidth();
  		displaySize[1]= wm.getDefaultDisplay().getHeight();
  		
  		return displaySize;
  	}
}
