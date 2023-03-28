package com.edh.catapp;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class CatService {
	public static void showCats() throws IOException {
		// 1. vamos a traer los datos de la API
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder().url("https://api.thecatapi.com/v1/images/search").get().build();

		Response response = client.newCall(request).execute();

		String elJson = response.body().string();

		// cortar los corchetes
		elJson = elJson.substring(1, elJson.length());
		elJson = elJson.substring(0, elJson.length() - 1);

		// crear u objeto de la clase Gson
		Gson gson = new Gson();
		Cat cat = gson.fromJson(elJson, Cat.class);

		// redimensionar en caso de necesitar
		Image image = null;
		try {
			URL url = new URL(cat.getUrl());
			image = ImageIO.read(url);

			ImageIcon backgroundCat = new ImageIcon(image);

			if (backgroundCat.getIconWidth() > 800) {
				// redimensionamos
				Image fondo = backgroundCat.getImage();
				Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
				backgroundCat = new ImageIcon(modificada);
			}

			String menu = "Options: \n" + " 1. Show another image \n" + " 2. Favorite \n" + " 3. Back \n";

			String[] buttons = { "Show another image", "Favorite", "Back" };
			String catId = cat.getId();
			String option = (String) JOptionPane.showInputDialog(null, menu, catId, JOptionPane.INFORMATION_MESSAGE,
					backgroundCat, buttons, buttons[0]);

			int selection = -1;
			// validamos que opcion selecciona el usuario
			for (int i = 0; i < buttons.length; i++) {
				if (option.equals(buttons[i])) {
					selection = i;
				}
			}

			switch (selection) {
			case 0:
				showCats();
				break;
			case 1:
				favoriteCat(cat);
				break;
			default:
				break;
			}

		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void favoriteCat(Cat cat) {
		try {
			OkHttpClient client = new OkHttpClient();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{\n\t\"image_id\":\"" + cat.getId() + "\"\n}");
			Request request = new Request.Builder().url("https://api.thecatapi.com/v1/favourites").post(body)
					.addHeader("Content-Type", "application/json").addHeader("x-api-key", cat.getApikey()).build();
			Response response = client.newCall(request).execute();

		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
