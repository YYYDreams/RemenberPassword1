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
	 * 用了保存用户名和密码的文件名，文件是xml文件，但是不用加后缀，系统会自动加上。。
	 */
	private static final String USER_INFO="User_Info";
	
	/**
	 * 文件中记录用户名的关键字。
	 */
	private static final String USER_NAME="User_Name";
	
	/**
	 * 文件中记录密码的关键字。
	 */
	private static final String PASSWORD="Password";
	
	/**
	 * 文件中记录是否选择记住密码功能的关键字
	 */
	private static final String IS_REMENBER_PASSWORD="Is_Remenber_Password";
	
	private User mUser;
	
	private EditText editUsername;
	private EditText editPassword;
	private CheckBox checkRememberPassword;
	private Button btnLogin;
	private SharedPreferences mSharedPreferences;
	
	/**
	 *对用户名和密码进行加解密 
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
    	
    	//取得保存用户名和密码的xml文件,如果文件不存在，系统会自动创建
    	mSharedPreferences=getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
    	
		mAes=new EncrypAES();
	}
    
    public void InitWidget(){
    	editUsername=(EditText)findViewById(R.id.edit_username);
    	editPassword=(EditText)findViewById(R.id.edit_password);
    	checkRememberPassword=(CheckBox)findViewById(R.id.check_remenber_password);
    	btnLogin=(Button)findViewById(R.id.btn_login);
    	
    	//如果选中了记住密码，则从记住的密码中获取用户名和密码
    	if(mSharedPreferences.getBoolean(IS_REMENBER_PASSWORD, true)){
    		editUsername.setText(mSharedPreferences.getString(USER_NAME, ""));
    		
    		//对读取到的密码进行解密 
    		String password=mSharedPreferences.getString(PASSWORD, "");
    		if(0!=password.length()){
    			password=mAes.DecryptorString(password);
    		}
    		editPassword.setText(password);
    	}
    	
    	//记住密码CheckBox监听函数
    	checkRememberPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mSharedPreferences.edit().putBoolean(IS_REMENBER_PASSWORD, isChecked).commit();
			}
		});
    	
    	//登录按钮监听函数
    	btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//判断是否打开网络连接
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
     * 检查用户名和密码输入
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
    	
    	//设置用户名和密码
    	mUser.setUserName(strUserName);
    	mUser.setPassword(strPassword);

    	return true;
    }

	/**
	 * 如果选中了“记住密码”功能，则保存用户名和密码
	 * @param user 包含了用户名和密码
	 */
	public void savaUserNameAndPassword(User user){
		if(checkRememberPassword.isChecked()){
			Editor editor=mSharedPreferences.edit();
			editor.putBoolean(IS_REMENBER_PASSWORD, true);
			editor.putString(USER_NAME, user.getUserName());
			
			//对密码进行加密保存
			String password=mAes.EncryptorString(user.getPassword());
			editor.putString(PASSWORD, password);
			editor.commit();
		}
	}
 
}
