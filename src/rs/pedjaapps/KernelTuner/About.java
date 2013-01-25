package rs.pedjaapps.KernelTuner;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class About extends Activity
{
	
	SharedPreferences sharedPrefs;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String theme = preferences.getString("theme", "light");
		
		if(theme.equals("light")){
			setTheme(R.style.Theme_Sherlock_Light);
		}
		else if(theme.equals("dark")){
			setTheme(R.style.Theme_Sherlock);
			
		}
		else if(theme.equals("light_dark_action_bar")){
			setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
			
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		
		
		TextView versiontext = (TextView)findViewById(R.id.textView1);
		
		TextView mail = (TextView)findViewById(R.id.textView6);
		ImageView gp = (ImageView)findViewById(R.id.imageView1);
		ImageView fb = (ImageView)findViewById(R.id.imageView2);
		ImageView tw = (ImageView)findViewById(R.id.imageView3);
		ImageView gpl = (ImageView)findViewById(R.id.imageView4);
		ImageView xda = (ImageView)findViewById(R.id.imageView5);
		ImageView anthrax = (ImageView)findViewById(R.id.imageView6);
		ImageView kt = (ImageView)findViewById(R.id.imageView7);
		
		gp.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v){
		        Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.setData(Uri.parse("market://details?id=rs.pedjaapps.KernelTuner"));
		        startActivity(intent);
		    }
		});
		
		fb.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v){
		        Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.addCategory(Intent.CATEGORY_BROWSABLE);
		        intent.setData(Uri.parse("http://www.facebook.com/KernelTuner"));
		        startActivity(intent);
		    }
		});
		
		tw.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v){
		        Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.addCategory(Intent.CATEGORY_BROWSABLE);
		        intent.setData(Uri.parse("http://twitter.com/kerneltuner"));
		        startActivity(intent);
		    }
		});
		
		gpl.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v){
		        Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.addCategory(Intent.CATEGORY_BROWSABLE);
		        intent.setData(Uri.parse("https://plus.google.com/u/0/b/115273553144904124119/115273553144904124119"));
		        startActivity(intent);
		    }
		});
		
		kt.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v){
		        Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.addCategory(Intent.CATEGORY_BROWSABLE);
		        intent.setData(Uri.parse("http://kerneltuner.pedjaapps.in.rs"));
		        startActivity(intent);
		    }
		});
		
		xda.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v){
		        Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.addCategory(Intent.CATEGORY_BROWSABLE);
		        intent.setData(Uri.parse("http://forum.xda-developers.com/showthread.php?t=1719934"));
		        startActivity(intent);
		    }
		});
		
		anthrax.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v){
		        Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.addCategory(Intent.CATEGORY_BROWSABLE);
		        intent.setData(Uri.parse("http://anthrax-kernels.us/forum/showthread.php?t=574"));
		        startActivity(intent);
		    }
		});
		
		if (mail != null)
		{
			mail.setMovementMethod(LinkMovementMethod.getInstance());
		}

	

		

		
		try
		{
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String	version = pInfo.versionName;
			
			versiontext.setText(getResources().getString(R.string.version) + version);
		}
		catch (PackageManager.NameNotFoundException e)
		{}


	}

	
	
}
