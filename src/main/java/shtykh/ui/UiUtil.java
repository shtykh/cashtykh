package shtykh.ui;

import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by shtykh on 09/02/15.
 */
public class UiUtil {
	private static final boolean SHOW_STACK_TRACE = true;
	
	private UiUtil(){}

	public static void showError(String errorTitle, Exception e, Component parent) {
		JOptionPane.showMessageDialog(parent, getMessage(e), errorTitle, JOptionPane.ERROR_MESSAGE);
	}

	private static String getMessage(Exception e) {
		return e.getMessage() + (SHOW_STACK_TRACE ? "\n" + ExceptionUtils.getStackTrace(e) : "");
	}
}
