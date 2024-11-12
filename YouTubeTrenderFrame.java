import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;



public class YouTubeTrenderFrame extends JFrame {

    private JTextField jTextFieldDataFile;
    private JTextField jTextFieldChannel;
    private JTextField jTextFieldDate;
    private JTextField jTextFieldTitle;
    private JTextArea jTextAreaVideoDescription;
    private JTextField jTextFieldViewCount;
    private JList<YouTubeVideo> jListVideo;
    private YouTubeVideoIndexer videoIndexer;
    private DefaultListModel<String> trendingModel;
    private JTextArea trendingTopicsArea;
    private JTextArea associatedVideosArea;
    private DefaultListModel<YouTubeVideo> videoModel;
    private JButton indexVideosButton;

    private List<YouTubeVideo> list;

    public YouTubeTrenderFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(650, 300));
        setResizable(false);
        initComponents();
        videoIndexer = new YouTubeVideoIndexer();

        trendingTopicsArea = new JTextArea(20, 40);
        associatedVideosArea = new JTextArea(20, 40);
        indexVideosButton = new JButton("Index Videos");
        indexVideosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                indexVideos();
            }
        });
        setVisible(true);
    }

    private void indexVideos() {
        trendingModel.clear(); // Clear the list model first

        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No videos loaded to index!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (YouTubeVideo video : list) {
            videoIndexer.indexVideo(video);
        }

        List<Map.Entry<String, YouTubeWordItem>> sortedEntries = new ArrayList<>(videoIndexer.getIndexedWords().entrySet());

        sortedEntries.sort((entry1, entry2) -> Integer.compare(entry2.getValue().getCount(), entry1.getValue().getCount()));

        for (Map.Entry<String, YouTubeWordItem> entry : sortedEntries) {
            YouTubeWordItem wordItem = entry.getValue();
            String wordEntry = entry.getKey() + ": [" + wordItem.getCount() + "]";
            trendingModel.addElement(wordEntry);
        }
    }

    private void displayAssociatedVideos(String word) {
        associatedVideosArea.setText("");  // Clear the area first
        YouTubeWordItem wordItem = videoIndexer.findWord(word);

        if (wordItem != null) {
            StringBuilder sb = new StringBuilder();
            for (YouTubeVideo video : wordItem.getAssociatedVideos()) {
                sb.append(video.getTitle()).append(" (").append(video.getViewCount()).append(" views)\n");
            }
            associatedVideosArea.setText(sb.toString());
        } else {
            associatedVideosArea.setText("No videos found for this word.");
        }
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException ex) {
            Logger.getLogger(YouTubeTrenderFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(() -> new YouTubeTrenderFrame().setVisible(true));
    }

    private void initComponents() {
        JPanel jPanelContainer = new JPanel(new GridBagLayout());
        jPanelContainer.setBorder(new MatteBorder(10, 10, 10, 10, new Color(0.0f, 0.66f, 0.42f)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Top Panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        jPanelContainer.add(createTopPanel(), gbc);

        // Sorting Panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        jPanelContainer.add(createSortingPanel(), gbc);

        // Main Video Panel
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        jPanelContainer.add(createVideoPanel(), gbc);

        // Video Details Panel
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jPanelContainer.add(createVideoDetailsPanel(), gbc);

        // Trending Panel
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        jPanelContainer.add(createTrendingPanel(), gbc);

        add(jPanelContainer);
        pack();

    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setMaximumSize(new Dimension(550, 25));
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        jTextFieldDataFile = new JTextField();
        jTextFieldDataFile.setPreferredSize(new Dimension(200, 25));
        jTextFieldDataFile.setText("data/youtubedata_15_50.json");
        JButton jButtonParse = new JButton("Load");
        jButtonParse.addActionListener(this::jButtonParseActionPerformed);

        topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        topPanel.add(jTextFieldDataFile);
        topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        topPanel.add(jButtonParse);

        return topPanel;
    }

    private JPanel createSortingPanel() {
        JPanel sortingPanel = new JPanel();
        sortingPanel.setBorder(BorderFactory.createTitledBorder("Sort Criteria"));

        // Create a ButtonGroup to manage radio buttons
        ButtonGroup buttonGroup = new ButtonGroup();

        // Create radio buttons for sorting criteria
        JRadioButton channelButton = new JRadioButton("Channel");
        JRadioButton dateButton = new JRadioButton("Date");
        JRadioButton viewsButton = new JRadioButton("Views");
        JRadioButton descriptionButton = new JRadioButton("Description");

        // Add radio buttons to ButtonGroup
        buttonGroup.add(channelButton);
        buttonGroup.add(dateButton);
        buttonGroup.add(viewsButton);
        buttonGroup.add(descriptionButton);

        // Add radio buttons to panel
        sortingPanel.add(channelButton);
        sortingPanel.add(dateButton);
        sortingPanel.add(viewsButton);
        sortingPanel.add(descriptionButton);

        // Create a sort button
        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(e -> {
            if (channelButton.isSelected()) {
                sortVideos("Channel");
            } else if (dateButton.isSelected()) {
                sortVideos("Date");
            } else if (viewsButton.isSelected()) {
                sortVideos("Views");
            } else if (descriptionButton.isSelected()) {
                sortVideos("Description");
            }
        });

        sortingPanel.add(sortButton);
        return sortingPanel;
    }

    private JPanel createVideoPanel() {
        JPanel videoPanel = new JPanel();
        videoPanel.setPreferredSize(new Dimension(600, 300));
        videoPanel.setBorder(BorderFactory.createTitledBorder("Videos"));
        JScrollPane jScrollPaneListVideo = new JScrollPane();
        jScrollPaneListVideo.setPreferredSize(new Dimension(575, 250));

        videoModel = new DefaultListModel<>();
        jListVideo = new JList<>(videoModel);
        jScrollPaneListVideo.setViewportView(jListVideo);
        jListVideo.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    YouTubeVideo selectedVideo = jListVideo.getSelectedValue();
                    if (selectedVideo != null) {
                        updateVideoDetails(selectedVideo); // Update video details when a video is selected
                    }
                }
            }
        });

        videoPanel.add(jScrollPaneListVideo);
        return videoPanel;
    }

    private JPanel createTrendingPanel() {
        JPanel jpT = new JPanel();
        jpT.setLayout(new BorderLayout());
        jpT.setPreferredSize(new Dimension(200, 300));
        jpT.setBorder(BorderFactory.createTitledBorder("Trending"));

        trendingModel = new DefaultListModel<>();  // Model for trending words
        JList<String> trendingList = new JList<>(trendingModel);
        trendingList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedWord = trendingList.getSelectedValue();
                    if (selectedWord != null && !selectedWord.isEmpty()) {
                        String word = selectedWord.split(":")[0].trim();  // Extract the word part
                        displayAssociatedVideos(word);
                    }
                }
            }
        });

        JScrollPane jscrlpnTrendingPanel = new JScrollPane(trendingList);
        jscrlpnTrendingPanel.setPreferredSize(new Dimension(130, 200));

        indexVideosButton = new JButton("Index Videos");
        indexVideosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                indexVideos();
            }
        });

        // Thêm nút và danh sách trending vào panel
        jpT.add(indexVideosButton, BorderLayout.NORTH);
        jpT.add(jscrlpnTrendingPanel, BorderLayout.CENTER);

        return jpT;
    }


    private void sortVideos(String criterion) {
        if (list == null || list.isEmpty()) {
            return; // No videos to sort
        }

        switch (criterion) {
            case "Channel":
                list.sort(new YouTubeVideoChannelComparator());
                break;
            case "Date":
                list.sort(new YouTubeVideoDateComparator());
                break;
            case "Views":
                list.sort(new YouTubeVideoViewsComparator());
                break;
            case "Description":
                list.sort(new YouTubeVideoDescriptionComparator());
                break;
        }

        videoModel.clear(); // Clear the current model
        for (YouTubeVideo video : list) {
            videoModel.addElement(video); // Add sorted videos back to the model
        }
    }

    private JPanel createVideoDetailsPanel() {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setPreferredSize(new Dimension(600, 300));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Video Details"));

        JLabel jLabelChannel = new JLabel("Channel:");
        jTextFieldChannel = new JTextField();
        jTextFieldChannel.setEditable(false);

        JLabel jLabelDate = new JLabel("Date Posted:");
        jTextFieldDate = new JTextField();
        jTextFieldDate.setEditable(false);

        JLabel jLabelTitle = new JLabel("Title:");
        jTextFieldTitle = new JTextField();
        jTextFieldTitle.setEditable(false);

        JLabel jLabelViewCount = new JLabel("View Count:");
        jTextFieldViewCount = new JTextField();
        jTextFieldViewCount.setEditable(false);

        JLabel jLabelDescription = new JLabel("Description:");
        JScrollPane jScrollPaneVideoDescription = new JScrollPane();
        jTextAreaVideoDescription = new JTextArea();
        jTextAreaVideoDescription.setEditable(false);
        jTextAreaVideoDescription.setColumns(20);
        jTextAreaVideoDescription.setLineWrap(true);
        jTextAreaVideoDescription.setRows(5);
        jTextAreaVideoDescription.setWrapStyleWord(true);
        jScrollPaneVideoDescription.setViewportView(jTextAreaVideoDescription);
        jScrollPaneVideoDescription.setPreferredSize(new Dimension(500, 120));

        detailsPanel.add(createHorizontalBox(jLabelChannel, jTextFieldChannel));
        detailsPanel.add(createHorizontalBox(jLabelDate, jTextFieldDate));
        detailsPanel.add(createHorizontalBox(jLabelTitle, jTextFieldTitle));
        detailsPanel.add(createHorizontalBox(jLabelViewCount, jTextFieldViewCount));
        detailsPanel.add(createHorizontalBox(jLabelDescription, new JTextField()));

        Box descriptionTextBox = Box.createHorizontalBox();
        descriptionTextBox.setPreferredSize(new Dimension(500, 120));
        descriptionTextBox.add(jScrollPaneVideoDescription);

        detailsPanel.add(Box.createHorizontalGlue());
        detailsPanel.add(descriptionTextBox);

        return detailsPanel;
    }

    private Box createHorizontalBox(JLabel jLabel, JTextField jTextField) {
        Box b = Box.createHorizontalBox();
        b.setPreferredSize(new Dimension(500, 25));
        jLabel.setPreferredSize(new Dimension(80, 25));
        jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        b.add(jLabel);
        b.add(Box.createRigidArea(new Dimension(5, 0)));
        b.add(jTextField);

        return b;
    }

    private void updateVideoDetails(YouTubeVideo video) {
        jTextFieldChannel.setText(video.getTitle());
        jTextFieldDate.setText(video.getDate());
        jTextFieldTitle.setText(video.getTitle());
        jTextFieldViewCount.setText(String.valueOf(video.getViewCount()));
        jTextAreaVideoDescription.setText(video.getDescription());
    }

    private void jButtonParseActionPerformed(ActionEvent evt) {
        String dataFile = jTextFieldDataFile.getText();
        YouTubeDataParser parser = new YouTubeDataParser();
        try {
            list = parser.parse(dataFile);
            videoModel.clear();
            for (YouTubeVideo video : list) {
                videoModel.addElement(video);
            }
        } catch (YouTubeDataParserException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Parsing Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
