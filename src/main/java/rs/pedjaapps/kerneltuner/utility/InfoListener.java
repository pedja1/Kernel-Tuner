package rs.pedjaapps.kerneltuner.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import rs.pedjaapps.kerneltuner.R;

public class InfoListener implements View.OnLongClickListener
{
    int icon;
    String title;
    String text;
    String url;
    boolean more;

    public InfoListener(int icon, String title, String text, String url, boolean more)
    {
        this.icon = icon;
        this.title = title;
        this.text = text;
        this.url = url;
        this.more = more;
    }

    @Override
    public boolean onLongClick(final View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        builder.setTitle(title);

        builder.setIcon(icon);
        LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.text_view_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        tv.setText(text);

        builder.setPositiveButton(v.getContext().getResources().getString(R.string.info_ok), null);
        if (more)
        {
            builder.setNeutralButton(R.string.info_more, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface arg0, int arg1)
                {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    v.getContext().startActivity(intent);
                }
            });
        }
        builder.setView(view);
        AlertDialog alert = builder.create();

        alert.show();
        return true;
    }
}
