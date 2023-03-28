package com.edh.catapp;

import javax.swing.JOptionPane;

public class App {
	public static void main(String[] args) {
		int optionMenu = -1;
		String[] buttons = {
				" 1. Show cats",
				" 2. Exit"
		};
		
		do {
			String option = (String) JOptionPane.showInputDialog(null, "Cats app java", "Menu", JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
			for(int i=0; i < buttons.length; i++) {
				if(option.equals(buttons[i])) {
					optionMenu = i;
				}
			}
			
			switch (optionMenu) {
			case 0:
				try {
					CatService.showCats();
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			default:
				break;
			}
		} while(optionMenu != 1);
	}
}
