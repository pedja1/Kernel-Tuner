/*
* This file is part of the Kernel Tuner.
*
* Copyright Predrag Čokulov <predragcokulov@gmail.com>
*
* Kernel Tuner is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Kernel Tuner is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Kernel Tuner. If not, see <http://www.gnu.org/licenses/>.
*/
package rs.pedjaapps.kerneltuner.ui;

import rs.pedjaapps.kerneltuner.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class About extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        final TextView versiontext = (TextView) findViewById(R.id.textView1);

        final TextView mail = (TextView) findViewById(R.id.textView6);
        final ImageView gp = (ImageView) findViewById(R.id.imageView1);
        final ImageView gpl = (ImageView) findViewById(R.id.imageView4);
        final ImageView xda = (ImageView) findViewById(R.id.imageView5);
        final ImageView github = (ImageView) findViewById(R.id.imageView8);
        final ImageView kt = (ImageView) findViewById(R.id.imageView7);

        gp.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=rs.pedjaapps.kerneltuner"));
                startActivity(intent);
            }
        });

        gpl.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://plus.google.com/u/0/b/115273553144904124119/115273553144904124119"));
                startActivity(intent);
            }
        });

        kt.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://pedjaapps.net"));
                startActivity(intent);
            }
        });

        xda.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://forum.xda-developers.com/showthread.php?t=1719934"));
                startActivity(intent);
            }
        });

        github.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://github.com/pedja1/Kernel-Tuner"));
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
            String version = pInfo.versionName;
            versiontext.setText(getResources().getString(R.string.version) + version);
        }
        catch (PackageManager.NameNotFoundException e)
        {
        }
    }
}
