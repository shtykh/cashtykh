package shtykh.ui;

import javax.swing.*;

/**
 * Created by shtykh on 09/02/15.
 */
public class UiUtil {
	public static void showError(String errorTitle, Exception e) {
		JOptionPane.showMessageDialog(null, e.getMessage(), errorTitle, JOptionPane.ERROR_MESSAGE);
	}
}
