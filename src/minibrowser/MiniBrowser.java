
package minibrowser;

/**
 *
 * @author Sohel Rana
 * http://www.cuet.ac.bd/
 * http://www.prothom-alo.com/
 * http://localhost/
 */
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.html.*;
public class MiniBrowser extends JFrame implements HyperlinkListener {

    // These are the buttons for iterating through the page list.
private JButton backButton, forwardButton; 
    // Page location text field.
    private JTextField locationTextField;
    // Editor pane for displaying pages.
    private JEditorPane displayEditorPane;
    // Browser's list of pages that have been visited.
    private ArrayList pageList = new ArrayList();
    // Constructor for Mini Web Browser.
    public MiniBrowser() {
        // Set application title.
        super("CUETCSE'11");  
        // Set window size.
        setSize(640, 480);   
        // Handle closing events.
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                actionExit();
            }
        });      
        // Set up file menu.
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenuItem fileExitMenuItem = new JMenuItem("Exit",
                KeyEvent.VK_X);
        fileExitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionExit();
            }
        });
        fileMenu.add(fileExitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
      
        // Set up button panel.
      JPanel buttonPanel = new JPanel();
     
        locationTextField = new JTextField(35);
        locationTextField.addKeyListener(new KeyAdapter() { // interface addKeyListener, class KeyAdapter
            public void keyReleased(KeyEvent e) { //Invoked when a key has been released.
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    actionGo();
                }
            }
        });
        buttonPanel.add(locationTextField);
        JButton goButton = new JButton("GO");
        goButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionGo();
            }
        });
        buttonPanel.add(goButton);
         
        // Set up page display.
        displayEditorPane = new JEditorPane();
        displayEditorPane.setContentType("text/html");
        displayEditorPane.setEditable(false);
        displayEditorPane.addHyperlinkListener(this);
         
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(displayEditorPane),
                BorderLayout.CENTER);
    }
     
    // Exit this program.
    private void actionExit() {
        System.exit(0);
    }
     
    // Go back to the page viewed before the current page.
   
     
    // Load and show the page specified in the location text field.
    private void actionGo() {
        URL verifiedUrl = verifyUrl(locationTextField.getText());
        if (verifiedUrl != null) {
            showPage(verifiedUrl, true);
        } else {
            showError("Invalid URL");
        }
    }
     
    // Show dialog box with error message.
    private void showError(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage,
                "Error", JOptionPane.ERROR_MESSAGE);
    }
     
    // Verify URL format.
    private URL verifyUrl(String url) {
        // Only allow HTTP URLs.
        if (!url.toLowerCase().startsWith("http://"))
            return null;
         
        // Verify format of URL.
        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(url);
        } catch (Exception e) {
            return null;
        }
         
        return verifiedUrl;
    }
     
  /* Show the specified page and add it to
     the page list if specified. */
    private void showPage(URL pageUrl, boolean addToList) {
        // Show hour glass cursor while crawling is under way.
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         
        try {
            // Get URL of page currently being displayed.
            URL currentUrl = displayEditorPane.getPage();
             
            // Load and display specified page.
            displayEditorPane.setPage(pageUrl);
             
            // Get URL of new page being displayed.
            URL newUrl = displayEditorPane.getPage();
             
            // Add page to list if specified.
            if (addToList) {
                int listSize = pageList.size();
                if (listSize > 0) {
                    int pageIndex =
                            pageList.indexOf(currentUrl.toString());
                    if (pageIndex < listSize - 1) {
                        for (int i = listSize - 1; i > pageIndex; i--) {
                            pageList.remove(i);
                        }
                    }
                }
                pageList.add(newUrl.toString());
            }
             
            // Update location text field with URL of current page.
            locationTextField.setText(newUrl.toString());
             
            // Update buttons based on the page being displayed.
            updateButtons();
        } catch (Exception e) {
            // Show error messsage.
            //showError("Unable to load page");
            System.out.println(e);
        } finally {
            // Return to default cursor.
            setCursor(Cursor.getDefaultCursor());
        }
    }
     

     
    // Handle hyperlink's being clicked.
    public void hyperlinkUpdate(HyperlinkEvent event) {
        HyperlinkEvent.EventType eventType = event.getEventType();
        if (eventType == HyperlinkEvent.EventType.ACTIVATED) {
            if (event instanceof HTMLFrameHyperlinkEvent) {
                HTMLFrameHyperlinkEvent linkEvent =
                        (HTMLFrameHyperlinkEvent) event;
                HTMLDocument document =
                        (HTMLDocument) displayEditorPane.getDocument();
                document.processHTMLFrameHyperlinkEvent(linkEvent);
            } else {
                showPage(event.getURL(), true);
            }
        }
    }
     
    // Run the Mini Browser.
    public static void main(String[] args) {
        // TODO code application logic here
         MiniBrowser browser = new MiniBrowser();
        browser.show();
    }
    
}
