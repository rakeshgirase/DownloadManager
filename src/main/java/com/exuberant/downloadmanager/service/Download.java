package com.exuberant.downloadmanager.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

enum Status {
	DOWNLOADING, PAUSED, COMPLETE, CANCELLED, ERROR;
}

public class Download extends Observable implements Runnable {
	private URL url;
	private int size;
	private int downloaded;
	private Status status;
	
	public Download(URL url) {
		this.url = url;
	}
	public String getFileName() {
		String fileName = url.getFile();
		return fileName.substring(fileName.lastIndexOf('/') + 1);
	}

	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getDownloaded() {
		return downloaded;
	}
	public void setDownloaded(int downloaded) {
		this.downloaded = downloaded;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}

	public void run() {
		RandomAccessFile file = null;
		InputStream stream = null;
		try {
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			// Specify what portion of file is downloaded
			connection.setRequestProperty("Range:", "bytes=" + downloaded
					+ "Of " + size);
			connection.connect();
			size = connection.getContentLength();
			if (connection.getResponseCode() / 100 != 2
					|| ((size = connection.getContentLength()) < 1)) {
				this.status = Status.ERROR;
				notifyObservers();
			}
			status = Status.DOWNLOADING;
			file = new RandomAccessFile(getFileName(), "rw");
			file.seek(downloaded);
			stream = connection.getInputStream();
			final int BUFFER_SIZE = 8192;
			byte data[] = new byte[BUFFER_SIZE];
			int numRead;
			while (status.equals(Status.DOWNLOADING)
					&& ((numRead = stream.read(data, 0, BUFFER_SIZE)) != -1)) {
				// write to buffer
				file.write(data, 0, numRead);
			}
			System.out.println("Done Downloading..."+url);

		} catch (IOException e) {
			System.out.println("Error downloading File...."+url);
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("Started");
		Download d = new Download(new URL("https://www.sbicard.com/PDFs/Catalogue_reward.pdf"));
		Thread t = new Thread(d);
		t.start();
		System.out.println("Done");
	}
}