package shtykh.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by shtykh on 09/02/15.
 */
public class UiUtil {
	public static void showError(String errorTitle, Exception e, Component parent) {
		JOptionPane.showMessageDialog(null, e.getMessage(), errorTitle, JOptionPane.ERROR_MESSAGE);
	}
}
