package com.modwiz.ld31.leveleditor;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JList;

import com.modwiz.ld31.entities.*;
import com.modwiz.ld31.world.*;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ScrollPaneConstants;
import javax.swing.JScrollPane;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListDataListener;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JFileChooser;
import java.io.File;
import javax.swing.JPanel;
import javax.swing.JMenuBar;

/**
	The JFrame that is the level editor, create a new instance to have the level editor be created.
*/
public class LevelEditorMain extends JFrame {
	
	private Viewport viewport;
	private PropertyPanel propertyPanel;
	private FileManager fileManager;
	private JScrollPane dimListViewport;
	private JScrollPane objectLibViewport;
	private JList<Dimension> dimList;
	private JList<GameObject> objectLib;
	private JButton addObject;
	private JButton removeObject;
	private JButton dimAdd;
	private JButton dimRemove;
	private JMenu fileMenu;
	private JMenu editorMenu;
	private JMenuItem newButton;
	private JMenuItem saveButton;
	private JMenuItem saveAsButton;
	private JMenuItem openButton;
	private JCheckBoxMenuItem snapToGrid;
	private JCheckBoxMenuItem snapToObjects;
	private JCheckBoxMenuItem gridVisible;
	private GameObject selecting;
	private GameWorld currentLevel;
	private JPanel rightPanel;
	private JPanel southPanel;
	private JLabel cursorPos;
	private JFileChooser fc;
	private boolean snappingToGrid;
	private boolean snappingToObjects;
	private GameObjectFactory gameObjectFactory;
	
	public LevelEditorMain() {
		super("Ludum Dare 31 Level Editor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void init() {
		addWindowListener(
			new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					onClose();
				}
			}
		);
		gameObjectFactory = new GameObjectFactory();
		snappingToGrid = false;
		snappingToObjects = true;
		currentLevel = null;
		fc = new JFileChooser();
		viewport = new Viewport(this, 600, 400);
		fileManager = new FileManager();
		propertyPanel = new PropertyPanel(this);
		rightPanel = new JPanel();
		rightPanel.add(propertyPanel);
		southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
		JMenuBar bar = new JMenuBar();
		makeFileMenu();
		makeEditorMenu();
		bar.add(fileMenu);
		bar.add(editorMenu);
		setJMenuBar(bar);
		makeEverythingElse();
		cursorPos = new JLabel();
		add(cursorPos, BorderLayout.NORTH);
		add(rightPanel, BorderLayout.EAST);
		add(viewport, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		setJMenuBar(bar);
		pack();
		viewport.setupListeners(this);
		setLocationRelativeTo(null);
	}
	
	/**
	 * Add objects to the Object Library for the level editor; the more we stuff
	 * in here the better. DOn't forget to change the object's name
	 */
	private GameObject[] makeObjectsInLibrary() {
		return new GameObject[] {
			gameObjectFactory.createWall(),
			gameObjectFactory.createMeleeEnemy(),
			gameObjectFactory.createRangedEnemy(),
			gameObjectFactory.createTextBlock()
		};
	}
	
	public void updateCursorLabel(float x, float y) {
		cursorPos.setText("2D Cursor position (" + x + ", " + y + ")");
		cursorPos.repaint();
	}
	
	private void makeEverythingElse() {
		makeAddDim();
		makeRemoveDim();
		makeAddObject();
		makeRemoveObject();
		makeObjectLib();
		makeDimList();
	}
	
	private boolean confirmAction(String message) {
		return JOptionPane.showConfirmDialog(this, message) == JOptionPane.YES_OPTION;
	}

	private void makeFileMenu() {
		fileMenu = new JMenu("File");
		makeNewButton();
		makeSaveAsButton();
		makeSaveButton();
		makeOpenButton();
		fileMenu.add(newButton);
		fileMenu.add(saveAsButton);
		fileMenu.add(saveButton);
		fileMenu.add(openButton);
	}
	
	private void makeNewButton() {
		newButton = new JMenuItem("New Level");
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newLevel();
			}
		});
	}
	
	private void makeSaveButton() {
		saveButton = new JMenuItem("Save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileManager.hasCurrent()) {
					saveLevel();
				} else {
					saveLevelAs();
				}
			}
		});
	}
	
	private void makeSaveAsButton() {
		saveAsButton = new JMenuItem("Save As...");
		saveAsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveLevelAs();
			}
		});
	}
	
	private void makeOpenButton() {
		openButton = new JMenuItem("Open...");
		openButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openLevel();
			}
		});
	}
	
	private void makeSnapToGrid() {
		snapToGrid = new JCheckBoxMenuItem("Snap to Grid");
		snapToGrid.setState(snappingToGrid);
		snapToGrid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (snapToGrid.getState()) {
					startSnappingToGrid();
				} else {
					stopSnappingToGrid();
				}
			}
		});
	}
	
	private void makeSnapToObjects() {
		snapToObjects = new JCheckBoxMenuItem("Snap to Object");
		snapToObjects.setState(snappingToObjects);
		snapToObjects.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (snapToObjects.getState()) {
					startSnappingToObjects();
				} else {
					stopSnappingToObjects();
				}
			}
		});
	}
	
	private void makeShowGrid() {
		gridVisible = new JCheckBoxMenuItem("Grid visible");
		gridVisible.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gridVisible.getState()) {
					showGrid();
				} else {
					hideGrid();
				}
			}
		});
	}
	
	private void makeDimList() {
		dimList = new JList();
		dimListViewport = new JScrollPane(
			dimList,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
		);
		dimList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				choseDimension();
			}
		});
		JPanel dimPanel = new JPanel();
		dimPanel.setLayout(new BoxLayout(dimPanel, BoxLayout.X_AXIS));
		JPanel dimPanelRight = new JPanel();
		dimPanelRight.setLayout(new BoxLayout(dimPanelRight, BoxLayout.Y_AXIS));
		dimPanelRight.add(dimAdd);
		dimPanelRight.add(dimRemove);
		dimPanel.add(dimListViewport);
		dimPanel.add(dimPanelRight);
		southPanel.add(dimPanel);
	}
	
	private void makeObjectLib() {
		objectLib = new JList();
		objectLibViewport = new JScrollPane(
			objectLib,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
		);
		JPanel objectPanel = new JPanel();
		objectPanel.setLayout(new BoxLayout(objectPanel, BoxLayout.X_AXIS));
		JPanel objectPanelRight = new JPanel();
		objectPanelRight.setLayout(new BoxLayout(objectPanelRight, BoxLayout.Y_AXIS));
		objectPanelRight.add(addObject);
		objectPanelRight.add(removeObject);
		objectPanel.add(objectLibViewport);
		objectPanel.add(objectPanelRight);
		southPanel.add(objectPanel);
		/* Add objects into the object library  */
		GameObject[] objects = makeObjectsInLibrary();
		
		objectLib.setListData(objects);
		objectLibViewport.revalidate();
		objectLibViewport.repaint();
	}
	
	private void makeAddDim() {
		dimAdd = new JButton("Add");
		dimAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addNewDimension();
			}
		});
	}
	
	private void makeRemoveDim() {
		dimRemove = new JButton("Remove");
		dimRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeSelectedDimension();
			}
		});
	}
	
	private void makeAddObject() {
		addObject = new JButton("Insert into world");
		addObject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addToWorldFromObjectLib();
			}
		});
	}
	
	private void makeRemoveObject() {
		removeObject = new JButton("Remove object");
		removeObject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeSelectedObject();
			}
		});
	}
	
	private void removeSelectedDimension() {
		System.out.println("Removing selected dimension");
		if (this.currentLevel!=null && dimList.getSelectedValue()!=null) {
			currentLevel.removeDimension(dimList.getSelectedValue());
			dimList.setListData(currentLevel.getDimensions());
			dimListViewport.revalidate();
			dimList.repaint();
		}
	}
	
	private void addNewDimension() {
		String dimName;
		if (this.currentLevel!= null) {
			dimName = JOptionPane.showInputDialog(this, "Enter the name of the new dimension");
			if (dimName!=null && !dimName.equals("")) {
				Dimension dim = new Dimension();
				dim.setName(dimName);
				currentLevel.addDimension(dim);
				Dimension[] dimensions = currentLevel.getDimensions();
				dimList.setListData(dimensions);
				dimListViewport.revalidate();
				dimList.repaint();
				viewport.repaint();
				System.out.println("Added a dimension (" + dimList.getModel().getSize() + " dimensions)");
			}
		}
	}
	
	private void makeEditorMenu() {
		editorMenu = new JMenu("Editor");
		makeSnapToGrid();
		editorMenu.add(snapToGrid);
		makeSnapToObjects();
		editorMenu.add(snapToObjects);
		makeShowGrid();
		editorMenu.add(gridVisible);
	}
	
	private void choseDimension() {
		//Set the current dimension to the chosen one
		if (currentLevel!=null && dimList.getSelectedValue()!=null) {
			clearSelectedGameObject();
			currentLevel.setActiveDimension(dimList.getSelectedValue().getName());
			System.out.println("Selected a dimension");
			viewport.repaint();
		}
	}
	
	private void addToWorldFromObjectLib() {
		System.out.println("Add to world from object lib");
		if (currentLevel!=null && currentLevel.getActiveDimension()!=null) {
			GameObject obj = (GameObject)objectLib.getSelectedValue();
			if (obj!=null) {
				obj.setX(viewport.get2DCursor().getX());
				obj.setY(viewport.get2DCursor().getY());
				if (obj instanceof Player) {
					currentLevel.getActiveDimension().addObject((Player)obj.clone());
				} else if (obj instanceof Enemy) {
					currentLevel.getActiveDimension().addObject((Enemy)obj.clone());
				} else if (obj instanceof Creature) {
					currentLevel.getActiveDimension().addObject((Creature)obj.clone());
				} else if (obj instanceof GameBlock) {
					currentLevel.getActiveDimension().addObject((GameBlock)obj.clone());
				} else if (obj instanceof GameObject) {
					currentLevel.getActiveDimension().addObject((GameObject)obj.clone());
				}
				viewport.repaint();
			}
		}
	}
	
	private void newLevel() {
		if (currentLevel!=null) {
			if (confirmAction("Save current level?")) {
				saveLevel();
			}
			clearLevel();
		}
		currentLevel = new GameWorld();
		Dimension dim = new Dimension("Dimension1");
		currentLevel.addDimension(dim);
		Dimension[] dimensions = currentLevel.getDimensions();
		dimList.setListData(dimensions);
		dimListViewport.revalidate();
		dimList.repaint();
		viewport.repaint();
		viewport.setLevel(currentLevel);
		System.out.println("Create a new level");
	}
	
	private void saveLevel() {
		if (currentLevel!=null) {
			System.out.println("Save the current level");
			if (fileManager.hasCurrent()) {
				fileManager.save(currentLevel);
			} else {
				saveLevelAs();
			}
			viewport.repaint();
		}
	}
	
	private void saveLevelAs() {
		if (currentLevel!=null) {
			System.out.println("Save the current level as");
			if (fileManager.hasCurrent()) {
				fc.setCurrentDirectory(fileManager.getCurrent().getParentFile());
			} else {
				File loc = new File("assets/levels");
				fc.setCurrentDirectory(loc.getAbsoluteFile());
			}
			int confirm = fc.showSaveDialog(this);
			if (confirm == JFileChooser.APPROVE_OPTION) {
				fileManager.saveAs(fc.getSelectedFile(), currentLevel);
			}
			viewport.repaint();
		}
	}
	
	public void repaintViewport() {
			viewport.repaint();
		}
	
	private void openLevel() {
		System.out.println("Open a level");
		if (currentLevel!=null) {
			if (confirmAction("Save current level?")) {
				saveLevel();
			}
			clearLevel();
		}
		if (fileManager.hasCurrent()) {
				fc.setCurrentDirectory(fileManager.getCurrent().getParentFile());
		} else {
			File loc = new File("assets/levels");
			fc.setCurrentDirectory(loc.getAbsoluteFile());
		}
		int confirm = fc.showOpenDialog(this);
		if (confirm == JFileChooser.APPROVE_OPTION) {
			GameWorld world = fileManager.open(fc.getSelectedFile());
			if (world!=null) {
				setLevel(world);
			} else {
				System.err.println("The world is unable to be loaded");
			}
		}
		viewport.repaint();
	}
	
	private void selectGameObject(GameObject selecting) {
		if (selecting!=null) {
			viewport.setSelected(selecting);
			this.selecting = selecting;
			propertyPanel.loadObject(selecting);
			viewport.repaint();
		}
	}

	private void clearSelectedGameObject() {
		viewport.setSelected(null);
		this.selecting = null;
		propertyPanel.clear();
		viewport.repaint();
	}

	public void checkSelection(Cursor2D cursor) {
		if (currentLevel!=null) {
			Dimension d = currentLevel.getActiveDimension();
			if (d!=null) {
				boolean found = false;
				for (GameObject go : d.getObjects()) {
					if (go instanceof GameBlock) {
						GameBlock gb = (GameBlock)go;
						if (
							cursor.getX() > gb.getX() &&
							cursor.getX() < gb.getX() + gb.getWidth() &&
							cursor.getY() > gb.getY() &&
							cursor.getY() < gb.getY() + gb.getHeight()
						) {
							clearSelectedGameObject();
							selectGameObject(gb);
							found = true;
						}
					}
				}
				if (!found) {
					clearSelectedGameObject();
				}
			}
			viewport.repaint();
		}
	}
	
	private void showGrid() {
		System.out.println("Showing grid");
		viewport.setGridVisible(true);
		viewport.repaint();
	}
	
	private void hideGrid() {
		System.out.println("Hiding grid");
		viewport.setGridVisible(false);
		viewport.repaint();
	}
	
	private void startSnappingToGrid() {
		System.out.println("Snapping to grid");
		snappingToObjects = false;
        snappingToGrid = true;
		gridVisible.setState(true);
		viewport.setGridVisible(true);
		viewport.repaint();
	}
	
	private void stopSnappingToGrid() {
		System.out.println("Not snapping to grid");
        snappingToGrid = false;
	}
	
	private void startSnappingToObjects() {
		System.out.println("Snapping to objects");
		snappingToObjects = true;
        snappingToGrid = false;
	}
	
	private void stopSnappingToObjects() {
		System.out.println("Not snapping to objects");
		snappingToObjects = false;
	}
	
	private void removeSelectedObject() {
		System.out.println("Removing the selected object");
		if (selecting!=null) {
			currentLevel.getActiveDimension().removeObject(selecting);
			viewport.repaint();
		}
	}
	
	public void onClose() {
		if (fileManager.hasCurrent()) {
			if (confirmAction("Save current work?")) {
				fileManager.save(currentLevel);
				dispose();
			}
		}
	}
	
	public void setLevel(GameWorld level) {
		System.out.println("Set level");
		this.currentLevel = level;
		viewport.setLevel(this.currentLevel);
		propertyPanel.clear();
		dimList.setListData(level.getDimensions());
		dimListViewport.repaint();
		viewport.repaint();
	}	
	
	public void clearLevel() {
		viewport.setLevel(null);
		currentLevel = null;
		propertyPanel.clear();
		//Remove all dimensions in list
		dimList.setListData(new Dimension[0]);
		dimListViewport.repaint();
		viewport.repaint();
	}	
	
	public void mouseDragged(Cursor2D cursor) {
		if (currentLevel!=null && selecting!=null) {
			if (!cursor.isDragging()) {
				cursor.startDragging(selecting);
			} else {
				cursor.updateDrag();
				if (snappingToGrid) {
					if(selecting.getX()%50 > 25){
						selecting.setX(selecting.getX() - selecting.getX()%50 + 50);
					}else{
						selecting.setX(selecting.getX() - selecting.getX()%50);
					}
					if(selecting.getY()%50 > 25){
						selecting.setY(selecting.getY() - selecting.getY()%50 + 50);
					}else{
						selecting.setY(selecting.getY() - selecting.getY()%50);
					}
				}
				if (snappingToObjects) {
					for (GameObject obj : currentLevel.getActiveDimension().getObjects()) {
						if (obj instanceof GameBlock) {
							checkSnappageWith((GameBlock)selecting, (GameBlock)obj);
						}
					}
				}
			}
			propertyPanel.updateFields();
			if (selecting instanceof GameBlock && ((GameBlock)selecting).hasImage()) {
				((GameBlock)selecting).resetImage();
			}
		}
	}
	
	private void checkSnappageWith(GameBlock sel, GameBlock other) {
		if (!sel.equals(other)) {
			//Check snappage in the x direction
			int dis;
			//Right side of selecting with left side of other
			dis = Math.abs((int)( sel.getX()+sel.getWidth() - other.getX()));
			if (dis < 9) {
				//close enough, snap to it!
				sel.setX(other.getX()-sel.getWidth());
			}
			//Left side of selecting with right side of other
			dis = Math.abs((int)( sel.getX() - (other.getX()+other.getWidth()) ));
			if (dis < 9) {
				sel.setX(other.getX()+other.getWidth());
			}
			//Top side of selecting with bottom side of other
			dis = Math.abs((int)( sel.getY() - (other.getY()+other.getHeight()) ));
			if (dis < 9) {
				sel.setY(other.getY()+other.getHeight());
			}
			//Bottom side of selecting with top side of other
			dis = Math.abs((int)( sel.getY()+sel.getHeight() - other.getY() ));
			if (dis < 9) {
				sel.setY(other.getY()-sel.getHeight());
			}
		}
	}
}