package com.example.remenberpassword;

import com.example.common.CommonUtils;
import com.example.common.EncrypAES;
import com.example.common.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {
	/**
	 * ���˱����û�����������ļ������ļ���xml�ļ������ǲ��üӺ�׺��ϵͳ���Զ����ϡ���
	 */
	private static final String USER_INFO="User_Info";
	
	/**
	 * �ļ��м�¼�û����Ĺؼ��֡�
	 */
	private static final String USER_NAME="User_Name";
	
	/**
	 * �ļ��м�¼����Ĺؼ��֡�
	 */
	private static final String PASSWORD="Password";
	
	/**
	 * �ļ��м�¼�Ƿ�ѡ���ס���빦�ܵĹؼ���
	 */
	private static final String IS_REMENBER_PASSWORD="Is_Remenber_Password";
	
	private User mUser;
	
	private EditText editUsername;
	private EditText editPassword;
	private CheckBox checkRememberPassword;
	private Button btnLogin;
	private SharedPreferences mSharedPreferences;
	
	/**
	 *���û�����������мӽ��� 
	 */
	private EncrypAES mAes;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        InitData();
        InitWidget();
    }

	public void InitData(){
    	mUser=new User();
    	
    	//ȡ�ñ����û����������xml�ļ�,����ļ������ڣ�ϵͳ���Զ�����
    	mSharedPreferences=getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
    	
		mAes=new EncrypAES();
	}
    
    public void InitWidget(){
    	editUsername=(EditText)findViewById(R.id.edit_username);
    	editPassword=(EditText)findViewById(R.id.edit_password);
    	checkRememberPassword=(CheckBox)findViewById(R.id.check_remenber_password);
    	btnLogin=(Button)findViewById(R.id.btn_login);
    	
    	//���ѡ���˼�ס���룬��Ӽ�ס�������л�ȡ�û���������
    	if(mSharedPreferences.getBoolean(IS_REMENBER_PASSWORD, true)){
    		editUsername.setText(mSharedPreferences.getString(USER_NAME, ""));
    		
    		//�Զ�ȡ����������н��� 
    		String password=mSharedPreferences.getString(PASSWORD, "");
    		if(0!=password.length()){
    			password=mAes.DecryptorString(password);
    		}
    		editPassword.setText(password);
    	}
    	
    	//��ס����CheckBox��������
    	checkRememberPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mSharedPreferences.edit().putBoolean(IS_REMENBER_PASSWORD, isChecked).commit();
			}
		});
    	
    	//��¼��ť��������
    	btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//�ж��Ƿ����������
				if(!CommonUtils.CheckNetwork(MainActivity.this)){
					CommonUtils.ShowToast(MainActivity.this, R.string.network_hint);
					return ;
				}
				
				if(!CheckUserInfoInput()){
					return ;
				}
				
				savaUserNameAndPassword(mUser);
				
				Intent intent=new Intent(MainActivity.this,SecondActivity.class);
				startActivity(intent);
			}
		});
    }
    
    
    /**
     * ����û�������������
     * @return
     */
    public boolean CheckUserInfoInput(){
    	String strUserName;
    	String strPassword;
    	
    	strUserName=editUsername.getText().toString();
    	strPassword=editPassword.getText().toString();
    	if(0==strUserName.length() | 0==strPassword.length()){
    		CommonUtils.ShowToast(MainActivity.this,R.string.username_password_inputhint);
    		return false;
    	}
    	
    	//�����û���������
    	mUser.setUserName(strUserName);
    	mUser.setPassword(strPassword);

    	return true;
    }

	/**
	 * ���ѡ���ˡ���ס���롱���ܣ��򱣴��û���������
	 * @param user �������û���������
	 */
	public void savaUserNameAndPassword(User user){
		if(checkRememberPassword.isChecked()){
			Editor editor=mSharedPreferences.edit();
			editor.putBoolean(IS_REMENBER_PASSWORD, true);
			editor.putString(USER_NAME, user.getUserName());
			
			//��������м��ܱ���
			String password=mAes.EncryptorString(user.getPassword());
			editor.putString(PASSWORD, password);
			editor.commit();
		}
	}
 
}
