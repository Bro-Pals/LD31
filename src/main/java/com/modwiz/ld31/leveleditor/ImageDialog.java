package com.modwiz.ld31.leveleditor;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JDialog;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.modwiz.ld31.utils.assets.AssetRegistry;

/**
* A simple dialog for choosing images. Should only be used by PropertyPanel.
*/
public class ImageDialog extends JDialog implements ActionListener {
	
	private JTextField field;
	private JButton button;
	private JList<String> list;
	private LevelEditorMain lem;
	private PropertyPanel pal;
	
	/**
	* Creates an image dialog for setting the value of the given field.
	* @param f the parent of the JDialog
	* @param affected the JTextField that is to have its text changed
	*/
	public ImageDialog(LevelEditorMain f, JTextField affected, PropertyPanel pal) {
		super(f, "Choose an Image", true);
		lem = f;
		this.field = affected;
		button = new JButton("Accept");
		button.addActionListener(this);
		setSize(400, 300);
		setLocationRelativeTo(f);
		list = new JList<String>(
			(String[])AssetRegistry.bufferedImageRegistry.getAssetKeys().toArray(new String[0])
		);
		this.pal = pal;
		add(list, BorderLayout.CENTER);
		add(button, BorderLayout.SOUTH);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String string = null;
		string = list.getSelectedValue();
		if (string!=null) {
			field.setText(string);
			pal.setImage();
		}
		dispose();
	}
}