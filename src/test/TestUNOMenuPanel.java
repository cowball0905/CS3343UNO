package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.UNOMenuPanel;

public class TestUNOMenuPanel {
    
    private UNOMenuPanel menuPanel;
    
    @BeforeEach
    public void setUp() {
        menuPanel = new UNOMenuPanel(null);
    }
    
    @Test
    public void testMenuPanelCreation() {
        assertNotNull(menuPanel, "Menu panel should be created");
        assertTrue(menuPanel.getComponents().length > 0, "Menu panel should have components");
    }
    
    @Test
    public void testPanelSize() {
        assertNotNull(menuPanel.getPreferredSize(), "Menu panel should have a preferred size");
    }
}