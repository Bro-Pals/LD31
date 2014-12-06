package com.modwiz.ld31.leveleditor;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JList;

import com.modwiz.ld31.entities.GameObject;
import com.modwiz.ld31.world.*;
import javax.swing.JOptionPane;
import javax.swing.JButton;
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
	private DimensionListSelectionModel dimListModel;
	private ObjectLibrarySelectionModel objectLibModel;
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
	private JFileChooser fc;
	private boolean snappingToGrid;
	private boolean snappingToObjects;
	
	public LevelEditorMain() {
		super("Ludum Dare 31 Level Editor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
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
		snappingToGrid = false;
		snappingToObjects = true;
		currentLevel = null;
		fc = new JFileChooser();
		viewport = new Viewport(600, 400);
		fileManager = new FileManager();
		propertyPanel = new PropertyPanel();
		rightPanel = new JPanel();
		southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
		JMenuBar bar = new JMenuBar();
		makeFileMenu();
		makeEditorMenu();
		bar.add(fileMenu);
		bar.add(editorMenu);
		setJMenuBar(bar);
		makeEverythingElse();
		add(rightPanel, BorderLayout.EAST);
		add(viewport, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
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
	
	public void checkSelection(Cursor2D cursor) {
		//Check to see if the cursor is selecting something
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
		dimListModel = new DimensionListSelectionModel();
		dimList = new JList(dimListModel);
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
		objectLibModel = new ObjectLibrarySelectionModel();
		objectLib = new JList(objectLibModel);
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
		
	}
	
	private void addNewDimension() {
		
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
	
	}
	
	private void addToWorldFromObjectLib() {
	
	}
	
	private void newLevel() {
		if (fileManager.hasCurrent()) {
			if (confirmAction("Save current level before overwriting with a new level?")) {
				//Make a new level
				fileManager.save(currentLevel);
				fileManager.clear();
				currentLevel = new GameWorld();
				dimListModel.clear();
				dimList.revalidate();
				viewport.setLevel(currentLevel);
			}
		}
	}
	
	private void saveLevel() {
		fileManager.save(currentLevel);
	}
	
	private void saveLevelAs() {
		if (fileManager.hasCurrent()) {
			fc.setSelectedFile(fileManager.getCurrent());
		}
		int chooser = fc.showSaveDialog(this);
		if (chooser == JFileChooser.APPROVE_OPTION) {
			fileManager.saveAs(fc.getSelectedFile(), currentLevel);
		}
	}
	
	private void openLevel() {
		if (fileManager.hasCurrent()) {
			fc.setSelectedFile(fileManager.getCurrent());
		}
		int chooser = fc.showOpenDialog(this);
		if (chooser == JFileChooser.APPROVE_OPTION) {
			currentLevel = fileManager.open(fc.getSelectedFile());
		}
	}
	
	private void selectGameObject(GameObject selecting) {
		
	}
	
	private void showGrid() {
		viewport.setGridVisible(true);
	}
	
	private void hideGrid() {
		viewport.setGridVisible(true);
	}
	
	private void startSnappingToGrid() {
		
	}
	
	private void stopSnappingToGrid() {
		
	}
	
	private void startSnappingToObjects() {
		
	}
	
	private void stopSnappingToObjects() {
		
	}
	
	private void removeSelectedObject() {
		
	}
	
	public void onClose() {
		if (fileManager.hasCurrent()) {
			if (confirmAction("Save current work?")) {
				fileManager.save(currentLevel);
				dispose();
			}
		}
	}
	
	/*
		List models
	*/
	class DimensionListSelectionModel implements ListModel<Dimension> {
		
		private List<Dimension> list;
		private List<ListDataListener> dataListeners;
		
		public DimensionListSelectionModel() {
			list = new ArrayList<Dimension>();
			dataListeners = new ArrayList<ListDataListener>();
		}

		@Override
		public int getSize() {
			return list.size();
		}
		
		@Override
		public Dimension getElementAt(int index) {
			return list.get(index);
		}
		
		@Override
		public void addListDataListener(ListDataListener l) {
			dataListeners.add(l);
		}
		
		@Override
		public void removeListDataListener(ListDataListener l) {
			dataListeners.remove(l);
		}
		
		public void clear() {
			list.clear();
		}
	}
	
	class ObjectLibrarySelectionModel implements ListModel<GameObject> {
		
		private List<ListDataListener> dataListeners;
		private List<GameObject> list;
		
		public ObjectLibrarySelectionModel() {
			list = new ArrayList<GameObject>();
			dataListeners = new ArrayList<ListDataListener>();
		}
		
		@Override
		public void addListDataListener(ListDataListener l) {
			dataListeners.add(l);
		}
		
		@Override
		public void removeListDataListener(ListDataListener l) {
			dataListeners.remove(l);
		}
		
		@Override
		public int getSize() {
			return list.size();
		}
		
		@Override
		public GameObject getElementAt(int index) {
			return list.get(index);
		}
		
		public void clear() {
			list.clear();
		}
	}
}