package hua.oop2;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

// this class tells the Jlist how to draw its cells
// the logic of this class is that you provide an index
// that corresponds to an element inside the Jlist of songs
// and changes its background color to yellow while the rest
// of the elements remain in the default background color
@SuppressWarnings("serial")
public class CustomCellRenderer extends DefaultListCellRenderer{
	private final int index;

	public CustomCellRenderer(int index) {
		this.index = index;
	}

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Component component;
		if (index == this.index) {
			component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			component.setBackground(Color.yellow);
		}
		else {
			component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			component.setBackground(Color.gray);
		}
		return component;
	}
}
