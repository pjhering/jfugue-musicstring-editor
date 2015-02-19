package midieditor;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import org.jfugue.Player;
import org.jfugue.examples.Midi2WavRenderer;

public class Main {

    private final JFrame frame;
    private JTabbedPane tabs;
    private final JFileChooser chooser;
    private final JFileChooser midiChooser;
    private int count;
    private final Player player;
    
    public Main(){
        frame = new JFrame();
        chooser = new JFileChooser();
        midiChooser = new JFileChooser();
        count = 1;
        player = new Player();
    }
    
    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }
    
    private void start() {
        
        initFrame();
        initTabs();
        initMenubar();
        initChooser();
        
        frame.setVisible(true);
    }
    
    private void initChooser() {
        
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".jfm");
            }

            @Override
            public String getDescription() {
                return "JFugue MusicString Files";
            }
        };
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileHidingEnabled(true);
        chooser.setFileFilter(filter);
        
        FileFilter midiFilter = new FileFilter(){

            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".midi");
            }

            @Override
            public String getDescription() {
                return "MIDI Files";
            }
        };
        midiChooser.setAcceptAllFileFilterUsed(false);
        midiChooser.setFileHidingEnabled(true);
        midiChooser.setFileFilter(midiFilter);
    }
    
    private void initFrame() {
        
        frame.setTitle("JFugue MusicString Editor");
        
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                file_exit();
            }
        });
        
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = size.width * 4 / 5;
        int height = size.height * 4 / 5;
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
    }
    
    private void initTabs() {
        tabs = new JTabbedPane();
        tabs.addTab("unsaved #" + count, new EditorPanel());
        count += 1;
        
        frame.getContentPane().setLayout(new GridLayout(1,1));
        frame.setContentPane(tabs);
    }
    
    private void initMenubar() {

        JMenu fileMenu = new JMenu("file");
        
        JMenuItem fileNew = new JMenuItem("new");
        fileNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK, true));
        fileNew.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                file_new();
            }
        });
        fileMenu.add(fileNew);
        
        JMenuItem fileOpen = new JMenuItem("open");
        fileOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK, true));
        fileOpen.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                file_open();
            }
        });
        fileMenu.add(fileOpen);
        
        JMenuItem fileClose = new JMenuItem("close");
        fileClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK, true));
        fileClose.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                file_close();
            }
        });
        fileMenu.add(fileClose);
        
        JMenuItem fileSave = new JMenuItem("save");
        fileSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK, true));
        fileSave.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                file_save();
            }
        });
        fileMenu.add(fileSave);
        
        JMenuItem fileSaveAs = new JMenuItem("save as");
        fileSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK, true));
        fileSaveAs.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                file_saveAs();
            }
        });
        fileMenu.add(fileSaveAs);
        
        JMenuItem fileExportMidi = new JMenuItem("export midi");
        fileExportMidi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK, true));
        fileExportMidi.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                file_exportMidi();
            }
        });
        fileMenu.add(fileExportMidi);
        
        JMenuItem fileExit = new JMenuItem("exit");
        fileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK, true));
        fileExit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                file_exit();
            }
        });
        fileMenu.add(fileExit);
        
        JMenu editMenu = new JMenu("edit");
        
        JMenuItem editPlay = new JMenuItem("play");
        editPlay.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.ALT_DOWN_MASK, true));
        editPlay.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                edit_play();
            }
        });
        editMenu.add(editPlay);
        
        JMenuItem editStop = new JMenuItem("stop");
        editStop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.ALT_DOWN_MASK, true));
        editStop.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                edit_stop();
            }
        });
        editMenu.add(editStop);
        
        JMenuBar menubar = new JMenuBar();
        menubar.add(fileMenu);
        menubar.add(editMenu);
        
        frame.setJMenuBar(menubar);
    }
    
    private void file_new(){
        
        EditorPanel editor = new EditorPanel();
        String title = "unsaved #" + count;
        count += 1;
        tabs.addTab(title, editor);
    }
    
    private void file_open() {
        
        int r = chooser.showOpenDialog(frame);
        
        if(r == JFileChooser.APPROVE_OPTION) {
            
            File file = chooser.getSelectedFile();
            
            EditorPanel panel = new EditorPanel();
            
            try {
                
                panel.open(file);
                tabs.addTab(file.getName(), panel);
                tabs.setSelectedComponent(panel);
                
            } catch(IOException ex) {
                showErrorDialog(ex);
            }
        }
    }
    
    private void file_close() {
        
        EditorPanel editor = (EditorPanel) tabs.getSelectedComponent();
        
        if(editor == null) return;
        
        if(editor.isChanged()) {
            
            int r = JOptionPane.showConfirmDialog(frame, "Save changes?");
            
            if(r == JOptionPane.OK_OPTION) {
                
                file_save();
                
            } else if(r == JOptionPane.NO_OPTION) {
                
                tabs.remove(editor);
            }
        } else {
            
            tabs.remove(editor);
        }
    }
    
    private void file_save() {
        
        EditorPanel editor = (EditorPanel) tabs.getSelectedComponent();
        
        if(editor == null) return;
        
        File file = editor.getFile();
        
        if(file == null) {
            
            file_saveAs();
            
        } else {
            
            try {
                
                editor.save();
                
            } catch(IOException ex) {
                
                showErrorDialog(ex);
            }
        }
    }
    
    private void file_saveAs() {
        
        int r = chooser.showSaveDialog(frame);
        
        if(r == JFileChooser.APPROVE_OPTION) {
            
            File file = chooser.getSelectedFile();
            
            if(!file.getName().endsWith(".jmf")) {
                
                file.renameTo(new File(file.getName() + ".jmf"));
            }
            
            EditorPanel editor = (EditorPanel) tabs.getSelectedComponent();
            
            if(editor == null) return;
            
            try {
                
                editor.saveAs(file);
                
                int i = tabs.getSelectedIndex();
                tabs.setTitleAt(i, file.getName());
                
            } catch(IOException ex) {
                
                showErrorDialog(ex);
            }
        }
    }
    
    private void file_exportMidi() {
        
        EditorPanel editor = (EditorPanel) tabs.getSelectedComponent();
        
        if(editor == null) return;
        
        String text = editor.getText();
        
        if(text == null || text.length() == 0) return;
        
        midiChooser.setCurrentDirectory(editor.getFile());
        
        int r = midiChooser.showSaveDialog(frame);
        
        if(r == JFileChooser.APPROVE_OPTION) {
            
            Player temp = new Player();
            
            File file = midiChooser.getSelectedFile();
            try {
                
                temp.saveMidi(text, file);
                
            } catch (IOException ex) {
                
                showErrorDialog(ex);
            }
        }
    }
    
    private void file_exportWav(){
        
        try {
            
            Midi2WavRenderer m2w = new Midi2WavRenderer();
            
        } catch (MidiUnavailableException | InvalidMidiDataException | IOException ex) {
            
            showErrorDialog(ex);
        }
    }
    
    private void file_exit() {
        
        int tabCount = tabs.getTabCount();
        boolean ok = true;
        
        for(int i = 0; i < tabCount; i++) {
            
            EditorPanel editor = (EditorPanel) tabs.getComponent(i);
            
            if(editor != null) {
                
                if(editor.isChanged()) {
                    
                    ok = false;
                }
            }
        }
        
        if(!ok) {
            
            int r = JOptionPane.showConfirmDialog(frame, "Some files have changed.  Close anyway?");
            
            if(r != JOptionPane.OK_OPTION) {
                
                return;
            }
        }
        
        if(player.isPlaying()) {
            
            player.stop();
        }
        
        player.close();
        
        frame.setVisible(false);
        frame.dispose();
        System.exit(0);
    }
    
    private void edit_play() {
        
        EditorPanel editor = (EditorPanel) tabs.getSelectedComponent();
        
        if(editor == null) return;
        
        String text = editor.getText();
        
        if(text == null || text.length() == 0) return;
        
        if(player.isPlaying()) {
            
            player.stop();
        }
        
        player.play(text);
    }
    
    private void edit_stop() {
        
        if(player.isPlaying()) {
            
            player.stop();
        }
    }
    
    private void showErrorDialog(Exception ex) {
        
        JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
