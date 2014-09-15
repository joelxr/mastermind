package br.ifce.mastermind.window;

/*
 *  StackLayout.java
 *  2005-07-15
 */

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;

import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

/**
 * Similar to BoxLayout, uses an orientation to determine if the contents should
 * be arranged horizontally or vertically. By default, Resizes each item to
 * equal width or height (depending on the orientation) based on the maximum
 * preferred width or height of all items.
 *
 * @author Christopher Bach
 */
public class StackLayout implements LayoutManager, Serializable {
	private static final long	serialVersionUID	= 1L;
	public static final int		HORIZONTAL			= SwingConstants.HORIZONTAL;
	private int					ourOrientation		= HORIZONTAL;
	public static final int		VERTICAL			= SwingConstants.VERTICAL;
	private int					ourSpacing			= 0;
	
	private boolean				ourDepthsMatched	= true;
	private boolean				ourLengthsMatched	= false;
	private boolean				ourFill				= false;
	private boolean				ourDrop				= false;
	
	private int					ourSqueezeFactor	= 100;
	
	/**
	 * Creates a new StackLayout with a horizontal orientation.
	 */
	public StackLayout() {
		
	}
	
	/**
	 * Creates a new StackLayout with the specified orientation.
	 */
	public StackLayout(int orientation) {
		setOrientation(orientation);
	}
	
	/**
	 * Creates a new StackLayout with the specified orientation and spacing.
	 */
	public StackLayout(int orientation, int spacing) {
		setOrientation(orientation);
		setSpacing(spacing);
	}
	
	/**
	 * Creates a new StackLayout matching the component lengths and depths as
	 * indicated.
	 */
	public StackLayout(boolean matchLengths, boolean matchDepths) {
		setMatchesComponentLengths(matchLengths);
		setMatchesComponentDepths(matchDepths);
	}
	
	/**
	 * Creates a new StackLayout with the specified orientation and spacing,
	 * matching the component lengths and depths as indicated.
	 */
	public StackLayout(int orientation, int spacing, boolean matchLengths,
			boolean matchDepths) {
		setOrientation(orientation);
		setSpacing(spacing);
		setMatchesComponentLengths(matchLengths);
		setMatchesComponentDepths(matchDepths);
	}
	
	/**
	 * Returns this StackLayout's orientation, either SwingConstants.HORIZONTAL
	 * or SwingConstants.VERTICAL.
	 */
	public int getOrientation() {
		return ourOrientation;
	}
	
	/**
	 * Sets this StackLayout's orientation, either SwingConstants.HORIZONTAL or
	 * SwingConstants.VERTICAL.
	 */
	public void setOrientation(int orientation) {
		if (orientation == HORIZONTAL || orientation == VERTICAL) {
			ourOrientation = orientation;
		}
	}
	
	/**
	 * Returns the spacing between components that this StackLayout uses when
	 * laying out the components.
	 */
	public int getSpacing() {
		return ourSpacing;
	}
	
	/**
	 * Sets the spacing between components that this StackLayout uses when
	 * laying out the components.
	 */
	public void setSpacing(int spacing) {
		ourSpacing = Math.max(0, spacing);
	}
	
	/**
	 * Sets whether or not the last component in the stack should be stretched
	 * to fill any remaining space within the parent container. The default
	 * value is false.
	 */
	public void setFillsTrailingSpace(boolean shouldFill) {
		ourFill = shouldFill;
	}
	
	/**
	 * Returns whether or not the last component in the stack should be
	 * stretched to fill any remaining space within the parent container.
	 */
	public boolean fillsTrailingSpace() {
		return ourFill;
	}
	
	/**
	 * Sets whether or not components in the stack that do not fit in the parent
	 * container should be left out of the layout. The default value is false;
	 */
	public void setDropsPartialComponents(boolean shouldDrop) {
		ourDrop = shouldDrop;
	}
	
	/**
	 * Returns whether or not components in the stack that do not fit in the
	 * parent container should be left out of the layout.
	 */
	public boolean dropsPartialComponents() {
		return ourDrop;
	}
	
	/**
	 * Sets whether or not all components in the stack will be sized to the same
	 * height (when in a horizontal orientation) or width (when in a vertical
	 * orientation). The default value is true.
	 */
	public void setMatchesComponentDepths(boolean match) {
		ourDepthsMatched = match;
	}
	
	/**
	 * Returns whether or not all components in the stack will be sized to the
	 * same height (when in a horizontal orientation) or width (when in a
	 * vertical orientation).
	 */
	public boolean matchesComponentDepths() {
		return ourDepthsMatched;
	}
	
	/**
	 * Sets whether or not all components in the stack will be sized to the same
	 * width (when in a horizontal orientation) or height (when in a vertical
	 * orientation). The default value is false.
	 */
	public void setMatchesComponentLengths(boolean match) {
		ourLengthsMatched = match;
	}
	
	/**
	 * Returns whether or not all components in the stack will be sized to the
	 * same width (when in a horizontal orientation) or height (when in a
	 * vertical orientation).
	 */
	public boolean matchesComponentLengths() {
		return ourLengthsMatched;
	}
	
	/**
	 * Returns the percentage of a component's preferred size that it may be
	 * squeezed in order to attempt to fit all components into the layout.
	 */
	public int getSqueezeFactor() {
		return ourSqueezeFactor;
	}
	
	/**
	 * Sets the percentage of a component's preferred size that it may be
	 * squeezed in order to attempt to fit all components into the layout. The
	 * squeeze factor will only be applied when this layout is set to match
	 * component lengths.
	 * <p/>
	 * For example, if the parent container is 100 pixels wide and holds two
	 * buttons, the largest having a preferred width of 80 pixels, a squeeze
	 * factor of 50 will allow each button to be sized to as small as 40 pixels
	 * wide (50 percent of the preferred width.
	 * <p/>
	 * The default value is 100.
	 */
	public void setSqueezeFactor(int factor) {
		if (factor < 0) ourSqueezeFactor = 0;
		else if (factor > 100) ourSqueezeFactor = 100;
		else
			ourSqueezeFactor = factor;
	}
	
	// //// LayoutManager implementation //////
	
	/**
	 * Adds the specified component with the specified name to this layout.
	 */
	public void addLayoutComponent(String name, Component comp) {
		
	}
	
	/**
	 * Removes the specified component from this layout.
	 */
	public void removeLayoutComponent(Component comp) {
		
	}
	
	/**
	 * Returns the preferred size for this layout to arrange the indicated
	 * parent's children.
	 */
	public Dimension preferredLayoutSize(Container parent) {
		if (parent instanceof JToolBar) {
			setOrientation(((JToolBar) parent).getOrientation());
		}
		
		return preferredLayoutSize(parent, ourOrientation);
	}
	
	/**
	 * Returns the preferred size for this layout to arrange the indicated
	 * parent's children at the specified orientation.
	 */
	// public, because it's useful - not one of the LayoutManager methods
	public Dimension preferredLayoutSize(Container parent, int orientation) {
		synchronized (parent.getTreeLock()) {
			Component[] comps = parent.getComponents();
			Dimension total = new Dimension(0, 0);
			
			int depth = calculatePreferredDepth(comps, orientation);
			
			int length = (ourLengthsMatched ? calculateAdjustedLength(comps,
					orientation, ourSpacing) : calculatePreferredLength(comps,
					orientation, ourSpacing));
			
			total.width = (orientation == HORIZONTAL ? length : depth);
			total.height = (orientation == HORIZONTAL ? depth : length);
			
			Insets in = parent.getInsets();
			total.width += in.left + in.right;
			total.height += in.top + in.bottom;
			
			return total;
		}
	}
	
	/**
	 * Returns the minimum size for this layout to arrange the indicated
	 * parent's children at the specified orientation.
	 */
	public Dimension minimumLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			if (parent instanceof JToolBar) {
				setOrientation(((JToolBar) parent).getOrientation());
			}
			
			Component[] comps = parent.getComponents();
			Dimension total = new Dimension(0, 0);
			
			int depth = calculatePreferredDepth(comps, ourOrientation);
			int length = calculateMinimumLength(comps, ourOrientation,
					ourSpacing);
			
			total.width = (ourOrientation == HORIZONTAL ? length : depth);
			total.height = (ourOrientation == HORIZONTAL ? depth : length);
			
			Insets in = parent.getInsets();
			total.width += in.left + in.right;
			total.height += in.top + in.bottom;
			
			return total;
		}
	}
	
	/**
	 * Lays out the child components within the indicated parent container.
	 */
	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			if (parent instanceof JToolBar) {
				setOrientation(((JToolBar) parent).getOrientation());
			}
			
			layoutComponents(parent);
			
		}
	}
	
	private void layoutComponents(Container parent) {
		Component[] components = parent.getComponents();
		Insets in = parent.getInsets();
		
		int maxHeight = parent.getHeight() - in.top - in.bottom;
		int maxWidth = parent.getWidth() - in.left - in.right;
		boolean horiz = (ourOrientation == HORIZONTAL);
		
		int totalDepth = calculatePreferredDepth(components, ourOrientation);
		totalDepth = Math.max(totalDepth, (horiz ? maxHeight : maxWidth));
		
		int prefLength = (ourLengthsMatched ? calculateAdjustedLength(
				components, ourOrientation, ourSpacing)
				: calculatePreferredLength(components, ourOrientation,
						ourSpacing));
		int totalLength = Math.min(prefLength, (horiz ? maxWidth : maxHeight));
		
		int a = (horiz ? in.left : in.top);
		int b = (horiz ? in.top : in.left);
		int l = 0, d = 0, sum = 0;
		int matchedLength = 0;
		Dimension prefsize = null;
		
		if (ourLengthsMatched) {
			matchedLength = (horiz ? getMaxPrefWidth(components)
					: getMaxPrefHeight(components));
			
			if (prefLength > totalLength && ourSqueezeFactor < 100) {
				int minLength = calculateMinimumLength(components,
						ourOrientation, ourSpacing);
				
				if (minLength >= totalLength) {
					matchedLength = (matchedLength * ourSqueezeFactor) / 100;
				} else {
					int numSeparators = countSeparators(components);
					int numComponents = components.length - numSeparators;
					int diff = (prefLength - totalLength) / numComponents;
					if ((prefLength - totalLength) % numComponents > 0) diff++;
					matchedLength -= diff;
				}
			}
		}
		
		for (int i = 0; i < components.length; i++) {
			prefsize = components[i].getPreferredSize();
			if (!ourLengthsMatched) l = (horiz ? prefsize.width : prefsize.height);
			else
				l = matchedLength;
			
			if (components[i] instanceof JSeparator) {
				// l = Math.min(prefsize.width, prefsize.height);
				l = (horiz ? prefsize.width : prefsize.height);
				d = totalDepth;
				sum += l;
				if (ourDrop && sum > totalLength) l = 0;
			} else {
				sum += l;
				if (ourDrop && sum > totalLength) l = 0;
				
				else if (ourFill && !ourLengthsMatched
						&& i == components.length - 1) {
					l = Math.max(l, (horiz ? maxWidth : maxHeight));
				}
				
				if (ourDepthsMatched) d = totalDepth;
				else
					d = (horiz ? prefsize.height : prefsize.width);
			}
			
			if (horiz) components[i].setBounds(a, b + (totalDepth - d) / 2, l, d);
			else
				components[i].setBounds(b + (totalDepth - d) / 2, a, d, l);
			
			a += l + ourSpacing;
			sum += ourSpacing;
		}
		
	}
	
	/**
	 * Returns the largest preferred width of the provided components.
	 */
	private int getMaxPrefWidth(Component[] components) {
		int maxWidth = 0;
		int componentWidth = 0;
		Dimension d = null;
		
		for (int i = 0; i < components.length; i++) {
			d = components[i].getPreferredSize();
			componentWidth = d.width;
			
			if (components[i] instanceof JSeparator) {
				componentWidth = Math.min(d.width, d.height);
			}
			
			maxWidth = Math.max(maxWidth, componentWidth);
		}
		
		return maxWidth;
	}
	
	/**
	 * Returns the largest preferred height of the provided components.
	 */
	private int getMaxPrefHeight(Component[] components) {
		int maxHeight = 0;
		int componentHeight = 0;
		Dimension d = null;
		
		for (int i = 0; i < components.length; i++) {
			d = components[i].getPreferredSize();
			componentHeight = d.height;
			
			if (components[i] instanceof JSeparator) {
				componentHeight = Math.min(d.width, d.height);
			} else
				maxHeight = Math.max(maxHeight, componentHeight);
		}
		
		return maxHeight;
	}
	
	/**
	 * Calculates the preferred "length" of this layout for the provided
	 * components based on the largest component preferred size.
	 */
	private int calculateAdjustedLength(Component[] components,
			int orientation, int spacing) {
		int total = 0;
		int componentLength = (orientation == HORIZONTAL ? getMaxPrefWidth(components)
				: getMaxPrefHeight(components));
		
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof JSeparator) {
				Dimension d = components[i].getPreferredSize();
				// total += Math.min(d.width, d.height);
				total += (orientation == HORIZONTAL ? d.width : d.height);
			} else
				total += componentLength;
		}
		
		int gaps = Math.max(0, spacing * (components.length - 1));
		total += gaps;
		
		return total;
	}
	
	/**
	 * Calculates the minimum "length" of this layout for the provided
	 * components, taking the squeeze factor into account when necessary.
	 */
	private int calculateMinimumLength(Component[] components, int orientation,
			int spacing) {
		if (!ourLengthsMatched) return calculatePreferredLength(components, orientation, spacing);
		
		if (ourSqueezeFactor == 100) return calculateAdjustedLength(components, orientation, spacing);
		
		int total = 0;
		int componentLength = (orientation == HORIZONTAL ? getMaxPrefWidth(components)
				: getMaxPrefHeight(components));
		
		componentLength = (componentLength * ourSqueezeFactor) / 100;
		
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof JSeparator) {
				Dimension d = components[i].getPreferredSize();
				// total += Math.min(d.width, d.height);
				total += (orientation == HORIZONTAL ? d.width : d.height);
			} else
				total += componentLength;
		}
		
		int gaps = Math.max(0, spacing * (components.length - 1));
		total += gaps;
		
		return total;
	}
	
	/**
	 * Calculates the preferred "length" of this layout for the provided
	 * components.
	 */
	private int calculatePreferredLength(Component[] components,
			int orientation, int spacing) {
		int total = 0;
		Dimension d = null;
		
		for (int i = 0; i < components.length; i++) {
			d = components[i].getPreferredSize();
			
			// if (components[i] instanceof JSeparator)
			// {
			// total += Math.min(d.width, d.height);
			// }
			//
			// else
			total += (orientation == HORIZONTAL ? d.width : d.height);
		}
		
		int gaps = Math.max(0, spacing * (components.length - 1));
		total += gaps;
		
		return total;
	}
	
	/**
	 * Returns the preferred "depth" of this layout for the provided components.
	 */
	private int calculatePreferredDepth(Component[] components, int orientation) {
		if (orientation == HORIZONTAL) return getMaxPrefHeight(components);
		else if (orientation == VERTICAL) return getMaxPrefWidth(components);
		else
			return 0;
	}
	
	private int countSeparators(Component[] components) {
		int count = 0;
		
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof JSeparator) count++;
		}
		
		return count;
	}
	
}
