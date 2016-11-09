package midieditor;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class EditorPanel extends JPanel implements DocumentListener
{

    private static final long serialVersionUID = 1L;
    private final JTextArea view;
    private File file;
    private boolean changed;

    public EditorPanel()
    {

        this.setLayout(new GridLayout());
        view = new JTextArea();
        view.setFont(Font.decode("Monospaced-PLAIN-16"));

        Document doc = view.getDocument();
        doc.addDocumentListener(this);

        JScrollPane scroll = new JScrollPane(view);
        this.add(scroll);
        changed = false;
    }

    public File getFile()
    {
        return file;
    }

    public boolean isChanged()
    {
        return changed;
    }

    public void open(File file) throws IOException
    {

        this.file = file;

        try (FileReader reader = new FileReader(file))
        {
            view.read(reader, file);
            view.getDocument().addDocumentListener(this);
        }
    }

    public void save() throws IOException
    {

        try (FileWriter writer = new FileWriter(file))
        {
            view.write(writer);
            changed = false;
        }
    }

    public void saveAs(File other) throws IOException
    {

        try (FileWriter writer = new FileWriter(other))
        {
            view.write(writer);
            this.file = other;
            changed = false;
        }
    }

    public String getText()
    {

        return view.getText();
    }

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        changed = true;
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
        changed = true;
    }

    @Override
    public void changedUpdate(DocumentEvent e)
    {
        changed = true;
    }
}
