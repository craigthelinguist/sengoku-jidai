import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class Editor {

	private List<Clan> clans;
	private List<Character> characters;
	
	private List<Clan> clansSorted;
	private List<Character> charactersSorted;

	private Map<Integer, Integer> clanIndex2clanSortedIndex;

	final JLabel labelOVR = new JLabel();
	
	private Character character;
	
	private final static Dimension DIMENSIONS = new Dimension(1000, 800);
	private final static Dimension DIMENSION_DROPDOWN = new Dimension(80, 25);
	private final static Dimension DIMENSION_FIELD_SMALL = new Dimension(30, 25);
	private final static Dimension DIMENSION_FIELD_MED = new Dimension(60, 25);
	private final static Dimension DIMENSION_FIELD_LARGE = new Dimension(140, 25);
	
	public Editor (List<Clan> clans, List<Character> characters) {	
		
		this.clans = clans;
		this.characters = characters;
		this.clansSorted = new ArrayList<>(clans);
		Collections.sort(clansSorted, Clan.NameComparator());
		this.charactersSorted = new ArrayList<>(characters);
		Collections.sort(charactersSorted, Character.NameComparator());
		
		clanIndex2clanSortedIndex = new HashMap<>();
		
		nextClan: for (int i = 0; i < clans.size(); i++) {
			Clan clan = clans.get(i);
			for (int j = 0; j < clansSorted.size(); j++) {
				Clan clan2 = clansSorted.get(j);
				if (clan2 == clan) {
					clanIndex2clanSortedIndex.put(i, j);
					continue nextClan;
				}
			}
		}
		
		
		JFrame frame = new JFrame();
		JTabbedPane pane = new JTabbedPane();
		pane.setPreferredSize(DIMENSIONS);
	
		setup(pane);
		
		frame.add(pane, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	private void setup (JTabbedPane tabPane) {
		JPanel panel1 = new ClanView();
		JPanel panel2 = new CharView();
		JPanel panel3 = new DataView();
		tabPane.addTab("Clans", null, panel1, "View clans.");
		tabPane.addTab("Officers", null, panel2, "View officers.");
		tabPane.addTab("Data", null, panel3, "Save and load.");
	}
	
	private class DataView extends JPanel {
		
		public DataView () {
			JButton button = new JButton("Save");
			button.setPreferredSize(new Dimension(60,20));
			button.setMaximumSize(new Dimension(60, 20));
			button.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent event) {
					System.out.println("save button");
				}
			});

			GroupLayout layout = new GroupLayout(this);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			this.setLayout(layout);
			
			GroupLayout.SequentialGroup horizontal = layout.createSequentialGroup();
			GroupLayout.SequentialGroup vertical = layout.createSequentialGroup();
			layout.setHorizontalGroup(horizontal);
			layout.setVerticalGroup(vertical);
			
			horizontal.addComponent(button);
			vertical.addComponent(button);
			
		}
		
	}
	
	private class ClanView extends JPanel {
		
		public ClanView () {
			setPreferredSize(DIMENSIONS);
			
			final JLabel labelClan = new JLabel();
			final JComboBox dropdown = new JComboBox(clansSorted.toArray());
			dropdown.setPreferredSize(DIMENSION_DROPDOWN);
			dropdown.setMaximumSize(DIMENSION_DROPDOWN);
			
			dropdown.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed (ActionEvent e) {
					Clan clan = (Clan)dropdown.getSelectedItem();
					labelClan.setText("Clan: " + clan.toString());
				}
			});
			
			GroupLayout layout = new GroupLayout(this);
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			this.setLayout(layout);
			
			GroupLayout.SequentialGroup horizontal = layout.createSequentialGroup();
			GroupLayout.SequentialGroup vertical = layout.createSequentialGroup();
			layout.setHorizontalGroup(horizontal);
			layout.setVerticalGroup(vertical);
			
			vertical.addComponent(dropdown);
			vertical.addComponent(labelClan);
			
			horizontal.addGroup(layout.createParallelGroup()
				.addComponent(dropdown)
				.addComponent(labelClan)
			);
			
		}
		
	}
	
	private class LoseFocus implements FocusListener {
		
		private String name;
		private JTextField field;
		
		public LoseFocus (String name_, JTextField field_) {
			name = name_;
			field = field_;
		}
		
		public void focusGained(FocusEvent arg0) {}
		
		public void focusLost(FocusEvent arg0) {
			int value;
			Stats stats = character.stats;
			switch (name) {
			case "WAR":
				value = stats.WAR; break;
			case "CHR":
				value = stats.CHR; break;
			case "POL":
				value = stats.POL; break;
			case "INT":
				value = stats.INT; break;
			case "CUL":
				value = stats.CUL; break;
			case "SPI":
				value = stats.SPI; break;
			default:
				throw new NullPointerException();
			}
			field.setText("" + value);
		}
		
	}
	
	private class UpdateListener implements DocumentListener {
		
		private String name;
		private JTextField field;
		
		public UpdateListener (String name_, JTextField field_) {
			this.name = name_;
			this.field = field_;
		}
		
		public void changedUpdate(DocumentEvent e) { update(); }
		public void insertUpdate(DocumentEvent e) { update(); }
		public void removeUpdate(DocumentEvent e) { update(); }
		
		public void update () {
			try {
				int newstat = Integer.parseInt(field.getText());
				if (newstat >= 0 && newstat <= 100) {
					Stats stats = character.stats;
					switch (name) {
					case "WAR":
						stats.WAR = newstat; break;
					case "CHR":
						stats.CHR = newstat; break;
					case "POL":
						stats.POL = newstat; break;
					case "INT":
						stats.INT = newstat; break;
					case "CUL":
						stats.CUL = newstat; break;
					case "SPI":
						stats.SPI = newstat; break;
					}
					labelOVR.setText("Overall: " + character.stats.overall());
				}
			}
			catch (NumberFormatException nfe) {}
		}
		
	}
	
	private class CharView extends JPanel {
		
		public CharView () {
			setPreferredSize(DIMENSIONS);
			setPreferredSize(DIMENSIONS);

			final JLabel labelName = new JLabel("Name ");
			final JTextField fieldName = new JTextField();
			fieldName.setMaximumSize(DIMENSION_FIELD_LARGE);
			fieldName.setMaximumSize(DIMENSION_FIELD_LARGE);

			final JLabel labelClan = new JLabel("Clan ");
			final JComboBox<Clan> dropdownClan = new JComboBox(clansSorted.toArray());
			dropdownClan.setPreferredSize(DIMENSION_DROPDOWN);
			dropdownClan.setMaximumSize(DIMENSION_DROPDOWN);
			
			
			
			final JLabel labelWAR = new JLabel("WAR ");
			final JTextField fieldWAR = new JTextField();
			fieldWAR.setMaximumSize(DIMENSION_FIELD_SMALL);
			fieldWAR.setPreferredSize(DIMENSION_FIELD_SMALL);
			fieldWAR.getDocument().addDocumentListener(new UpdateListener("WAR", fieldWAR));
			fieldWAR.addFocusListener(new LoseFocus("WAR", fieldWAR));
			
			final JLabel labelINT = new JLabel("INT ");
			final JTextField fieldINT = new JTextField();
			fieldINT.setMaximumSize(DIMENSION_FIELD_SMALL);
			fieldINT.setPreferredSize(DIMENSION_FIELD_SMALL);
			fieldINT.getDocument().addDocumentListener(new UpdateListener("INT", fieldINT));
			fieldINT.addFocusListener(new LoseFocus("INT", fieldINT));
			
			final JLabel labelCHR = new JLabel("CHR ");
			final JTextField fieldCHR = new JTextField();
			fieldCHR.setMaximumSize(DIMENSION_FIELD_SMALL);
			fieldCHR.setPreferredSize(DIMENSION_FIELD_SMALL);
			fieldCHR.getDocument().addDocumentListener(new UpdateListener("CHR", fieldCHR));
			fieldCHR.addFocusListener(new LoseFocus("CHR", fieldCHR));
			
			final JLabel labelPOL = new JLabel("POL ");
			final JTextField fieldPOL = new JTextField();
			fieldPOL.setMaximumSize(DIMENSION_FIELD_SMALL);
			fieldPOL.setPreferredSize(DIMENSION_FIELD_SMALL);
			fieldPOL.getDocument().addDocumentListener(new UpdateListener("POL", fieldPOL));
			fieldPOL.addFocusListener(new LoseFocus("POL", fieldPOL));
			
			final JLabel labelSPI = new JLabel("SPI ");
			final JTextField fieldSPI = new JTextField();
			fieldSPI.setMaximumSize(DIMENSION_FIELD_SMALL);
			fieldSPI.setPreferredSize(DIMENSION_FIELD_SMALL);
			fieldSPI.getDocument().addDocumentListener(new UpdateListener("SPI", fieldSPI));
			fieldSPI.addFocusListener(new LoseFocus("SPI", fieldSPI));
			
			final JLabel labelCUL = new JLabel("CUL ");
			final JTextField fieldCUL = new JTextField();
			fieldCUL.setMaximumSize(DIMENSION_FIELD_SMALL);
			fieldCUL.setPreferredSize(DIMENSION_FIELD_SMALL);
			fieldCUL.getDocument().addDocumentListener(new UpdateListener("CUL", fieldCUL));
			fieldCUL.addFocusListener(new LoseFocus("CUL", fieldCUL));
			
			// Layout for stats.
			JPanel panelStats = new JPanel();
			GroupLayout layoutStats = new GroupLayout(panelStats);
			layoutStats.setAutoCreateGaps(true);
			layoutStats.setAutoCreateContainerGaps(true);
			panelStats.setLayout(layoutStats);
			GroupLayout.SequentialGroup horizontalStats = layoutStats.createSequentialGroup();
			GroupLayout.SequentialGroup verticalStats = layoutStats.createSequentialGroup();
			layoutStats.setHorizontalGroup(horizontalStats);
			layoutStats.setVerticalGroup(verticalStats);
			
			verticalStats.addGroup(layoutStats.createParallelGroup()
				.addComponent(labelWAR).addComponent(fieldWAR).addComponent(labelPOL).addComponent(fieldPOL));
			verticalStats.addGroup(layoutStats.createParallelGroup()
				.addComponent(labelCHR).addComponent(fieldCHR).addComponent(labelCUL).addComponent(fieldCUL));
			verticalStats.addGroup(layoutStats.createParallelGroup()
				.addComponent(labelINT).addComponent(fieldINT).addComponent(labelSPI).addComponent(fieldSPI));
			
			horizontalStats.addGroup(layoutStats.createParallelGroup()
				.addComponent(labelWAR).addComponent(labelCHR).addComponent(labelINT));
			horizontalStats.addGroup(layoutStats.createParallelGroup()
				.addComponent(fieldWAR).addComponent(fieldCHR).addComponent(fieldINT));
			horizontalStats.addGroup(layoutStats.createParallelGroup()
				.addComponent(labelPOL).addComponent(labelCUL).addComponent(labelSPI));
			horizontalStats.addGroup(layoutStats.createParallelGroup()
				.addComponent(fieldPOL).addComponent(fieldCUL).addComponent(fieldSPI));
			
			// Layout for info.
			JPanel panelInfo = new JPanel();
			GroupLayout layoutInfo = new GroupLayout(panelInfo);
			layoutInfo.setAutoCreateGaps(true);
			layoutInfo.setAutoCreateContainerGaps(true);
			panelInfo.setLayout(layoutInfo);
			GroupLayout.SequentialGroup horizontalInfo = layoutInfo.createSequentialGroup();
			GroupLayout.SequentialGroup verticalInfo = layoutInfo.createSequentialGroup();
			layoutInfo.setHorizontalGroup(horizontalInfo);
			layoutInfo.setVerticalGroup(verticalInfo);

			verticalInfo.addGroup(layoutInfo.createParallelGroup()
				.addComponent(labelName).addComponent(fieldName));
			verticalInfo.addGroup(layoutInfo.createParallelGroup()
				.addComponent(labelClan).addComponent(dropdownClan));
			
			horizontalInfo.addGroup(layoutInfo.createParallelGroup()
				.addComponent(labelName).addComponent(labelClan));
			horizontalInfo.addGroup(layoutInfo.createParallelGroup()
				.addComponent(fieldName).addComponent(dropdownClan));
			
			// Character select.
			final JComboBox dropdown = new JComboBox(charactersSorted.toArray());
			dropdown.setPreferredSize(new Dimension(80, 25));
			dropdown.setMaximumSize(new Dimension(80, 25));
			
			dropdown.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					character = (Character)dropdown.getSelectedItem();
					fieldName.setText(character.toString());
					Stats stats = character.stats;
					fieldWAR.setText(""+stats.WAR);
					fieldINT.setText(""+stats.INT);
					fieldCHR.setText(""+stats.CHR);
					fieldCUL.setText(""+stats.CUL);
					fieldPOL.setText(""+stats.POL);
					fieldSPI.setText(""+stats.SPI);
					labelOVR.setText("Overall: " + stats.overall());
					int clanID = stats.clan.ID;
					int indexSortedByName = clanIndex2clanSortedIndex.get(clanID);
					dropdownClan.setSelectedIndex(indexSortedByName);	
				}
			});
			dropdown.setSelectedIndex(0);
		
			// Layout for everything.	
			GroupLayout layout = new GroupLayout(this);
			layout.setAutoCreateContainerGaps(true);
			this.setLayout(layout);
			
			GroupLayout.SequentialGroup horizontal = layout.createSequentialGroup();
			GroupLayout.SequentialGroup vertical = layout.createSequentialGroup();
			layout.setHorizontalGroup(horizontal);
			layout.setVerticalGroup(vertical);
			
			vertical.addComponent(dropdown);
			vertical.addComponent(panelInfo);
			vertical.addComponent(panelStats);
			vertical.addComponent(labelOVR);
			
			horizontal.addGroup(layout.createParallelGroup()
				.addComponent(dropdown)
				.addComponent(panelInfo)
				.addComponent(panelStats)
				.addComponent(labelOVR));
		}
		
	}
	
	
	
	
	
	
	public static void main (String[] args) {
		List<Clan> clans = null;
		List<Character> chars = null;
		try {
			clans = Loader.LoadClans("data" + File.separatorChar + "clans.dat");
			chars = Loader.LoadCharacters("data" + File.separatorChar + "characters.dat", clans);
		}
		catch (IOException ioe) {
			System.err.println("Error loading data files.");
			System.err.println(ioe.getStackTrace());
			System.exit(1);
		}
		Editor editor = new Editor(clans, chars);
	}
	
}
