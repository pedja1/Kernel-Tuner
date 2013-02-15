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
package rs.pedjaapps.KernelTuner.helpers;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

//import org.apache.commons.codec.binary.Base64;
//import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;

public class HttpFileUploader {

	URL connectURL;
	String params;
	String responseString;
	HttpURLConnection conn;
	// InterfaceHttpUtil ifPostBack;
	String fileName;
	byte[] dataToServer;

	private String getDate(){
		return DateFormat.getDateTimeInstance().format(new Date());
	}
	
	public HttpFileUploader(String urlString, String params, String fileName) {
		try {
			connectURL = new URL(urlString);
		} catch (Exception ex) {
			Log.i("URL FORMATION", "MALFORMATED URL");
		}
		this.params = params + "=";
		this.fileName = fileName;

	}

	public void doStart(FileInputStream stream) {
		fileInputStream = stream;
		thirdTry();
	}

	FileInputStream fileInputStream = null;

	void thirdTry() {
		String exsistingFileName = getDate()+"-"+fileName+"-"+android.os.Build.DEVICE+".txt";

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		String Tag = "3rd";
		try {
			// ------------------ CLIENT REQUEST

			Log.e(Tag, "Starting to bad things");
			// Open a HTTP connection to the URL

			conn = (HttpURLConnection) connectURL.openConnection();

			// Allow Inputs
			conn.setDoInput(true);

			// Allow Outputs
			conn.setDoOutput(true);

			// Don't use a cached copy.
			conn.setUseCaches(false);

			// Use a post method.
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Connection", "Keep-Alive");

			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
					+ exsistingFileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);

			Log.e(Tag, "Headers are written");

			// create a buffer of maximum size

			int bytesAvailable = fileInputStream.available();
			int maxBufferSize = 1024;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);
			byte[] buffer = new byte[bufferSize];

			// read file and write it into form...

			int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			// send multipart form data necesssary after file data...

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// close streams
			Log.e(Tag, "File is written");
			fileInputStream.close();
			dos.flush();

			InputStream is = conn.getInputStream();
			// retrieve the response from server
			int ch;

			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			String s = b.toString();
			Log.i("Response", s);
			dos.close();

		} catch (MalformedURLException ex) {
			Log.e(Tag, "error: " + ex.getMessage(), ex);
		}

		catch (IOException ioe) {
			Log.e(Tag, "error: " + ioe.getMessage(), ioe);
		}

		
	}

}
