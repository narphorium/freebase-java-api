package com.narphorium.freebase.services;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import com.narphorium.freebase.services.exceptions.FreebaseServiceException;

public class TransService extends AbstractFreebaseService {
	
	public TransService() {
		super();
	}
	
	public TransService(URL baseUrl) {
		super(baseUrl);
	}
	
	public Image fetchImage(String guid) throws FreebaseServiceException {
		return null;
	}
	
	public String fetchArticle(String id) throws IOException, FreebaseServiceException {
		if (id.startsWith("#")) {
			id = "/guid/" + id.substring(1);
		}
		String url = getBaseUrl() + "/trans/raw" + id;
		String content = fetchPage(url);
		return content;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TransService trans = new TransService();
		try {
			String view = trans.fetchArticle("/base/ottawa/views/ottawa_parks");
			System.out.println(view);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FreebaseServiceException e) {
			e.printStackTrace();
		}
		
	}

}
