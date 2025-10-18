package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import controller.UNOController;

public class TestWildCard {
    private WildCard wildCard;
    
    @BeforeEach
    void setUp() {
        wildCard = new WildCard(true);
    }
    
    @Test
    void testWildCardInitialization() {
        assertNotNull(wildCard, "WildCard should be initialized");
        assertNull(wildCard.getColor(), "WildCard color should be null initially");
        assertTrue(wildCard.isRevealed(), "WildCard should be revealed");
        assertEquals(Type.Wild, wildCard.getType(), "Card type should be Wild");
    }
    
    @Test
    void testSetColor() {
        // Test setting different colors
        wildCard.setColor(Color.Red);
        assertEquals(Color.Red, wildCard.getColor(), "Color should be set to RED");
        
        wildCard.setColor(Color.Blue);
        assertEquals(Color.Blue, wildCard.getColor(), "Color should be set to BLUE");
    }
    
    @Test
    void testToString() {
        // Test before color is set
        assertEquals("Wild Card", wildCard.toString(),
                    "toString should return 'Wild Card' when no color is set");
            
        // Test after color is set
        wildCard.setColor(Color.Red);
        assertEquals("Wild Card (RED)", wildCard.toString(),
                    "toString should include color when set");
    }
}
