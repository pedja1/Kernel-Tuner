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
package rs.pedjaapps.kerneltuner.filemanager;


import rs.pedjaapps.kerneltuner.MainApp;
import rs.pedjaapps.kerneltuner.R;

import android.content.*;
import android.view.*;
import android.widget.*;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public final class FMAdapter extends ArrayAdapter<FMEntry>
{

    private final int itemsItemLayoutResource;

    public FMAdapter(final Context context, final int itemsItemLayoutResource)
    {
        super(context, 0);
        this.itemsItemLayoutResource = itemsItemLayoutResource;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)
    {
        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final FMEntry entry = getItem(position);

        viewHolder.nameView.setText(entry.getName());
        viewHolder.summaryView.setText(entry.getDate() + " " + (entry.isFolder() ? "" : entry.getSizeHr()));

        if (entry.isFolder())
        {
            viewHolder.imageView.setImageResource(entry.getType() == FMActivity.TYPE_DIRECTORY_LINK
                    ? R.drawable.fm_folder_link : R.drawable.fm_folder);
        }
        else
        {
            if (entry.getType() == FMActivity.TYPE_LINK)
            {
                viewHolder.imageView.setImageResource(R.drawable.fm_file_link);
            }
            else
            {

                switch (entry.getMimeType())
                {
                    case image:
                        ImageLoader.getInstance().displayImage("file://" + entry.getPath(), viewHolder.imageView, imageDisplayOptions());
                        break;
                    case text:
                        viewHolder.imageView.setImageResource(R.drawable.fm_text);
                        break;
                    case video:
                        VideoThumbLoader.getInstance(getContext()).displayImage(new BaseImageLoader.ImageData(entry.getPath(), viewHolder.imageView, R.drawable.fm_video));
                        break;
                    case application:
                        APKImageLoader.getInstance(getContext()).displayImage(new BaseImageLoader.ImageData(entry.getPath(), viewHolder.imageView, R.drawable.fm_apk));
                        break;
                    case database:
                        viewHolder.imageView.setImageResource(R.drawable.fm_db);
                        break;
                    case archive:
                        viewHolder.imageView.setImageResource(R.drawable.fm_archive);
                        break;
                    case web:
                        viewHolder.imageView.setImageResource(R.drawable.fm_web);
                        break;
                    case audio:
                        viewHolder.imageView.setImageResource(R.drawable.fm_audio);
                        break;
                    case doc:
                        viewHolder.imageView.setImageResource(R.drawable.fm_doc);
                        break;
                    case pdf:
                        viewHolder.imageView.setImageResource(R.drawable.fm_pdf);
                        break;
                    case ppt:
                        viewHolder.imageView.setImageResource(R.drawable.fm_ppt);
                        break;
                    case excel:
                        viewHolder.imageView.setImageResource(R.drawable.fm_excel);
                        break;
                    case lib:
                        viewHolder.imageView.setImageResource(R.drawable.fm_lib);
                        break;
                    case script:
                        viewHolder.imageView.setImageResource(R.drawable.fm_script);
                        break;
                    case code:
                        viewHolder.imageView.setImageResource(R.drawable.fm_code);
                        break;
                    case unknown:
                    default:
                        viewHolder.imageView.setImageResource(R.drawable.fm_file);
                }

            }
        }

        return view;
    }

    private View getWorkingView(final View convertView)
    {
        View workingView = null;

        if (null == convertView)
        {
            final Context context = getContext();
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

            workingView = inflater.inflate(itemsItemLayoutResource, null);
        }
        else
        {
            workingView = convertView;
        }

        return workingView;
    }

    private ViewHolder getViewHolder(final View workingView)
    {
        final Object tag = workingView.getTag();
        ViewHolder viewHolder = null;


        if (null == tag || !(tag instanceof ViewHolder))
        {
            viewHolder = new ViewHolder();

            viewHolder.nameView = (TextView) workingView.findViewById(R.id.tvName);
            viewHolder.summaryView = (TextView) workingView.findViewById(R.id.tvSummary);
            viewHolder.imageView = (ImageView) workingView.findViewById(R.id.image);

            workingView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) tag;
        }

        return viewHolder;
    }

    private class ViewHolder
    {
        public TextView nameView;
        public TextView summaryView;
        public ImageView imageView;
    }

    public DisplayImageOptions imageDisplayOptions()
    {
        return new DisplayImageOptions.Builder().cloneFrom(MainApp.getInstance().getDefaultDisplayImageOptions())
                .showImageOnLoading(R.drawable.fm_image)
                .showImageOnFail(R.drawable.fm_image)
                .showImageForEmptyUri(R.drawable.fm_image).build();
    }

}
