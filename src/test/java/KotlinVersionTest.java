import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KotlinVersionTest {
    @Test
    public void parse() {
        assertArrayEquals(MCKTResolver.KotlinVersion.parse("1.4.20"), new int[] { 1, 4, 20 });
    }

    @Test
    public void compare() {
        // both null -> same
        assertEquals(MCKTResolver.KotlinVersion.compareVersion(null, null), 0);
        // null is old
        assertEquals(MCKTResolver.KotlinVersion.compareVersion(null, new int[] { 1, 4, 20}), -1);
        assertEquals(MCKTResolver.KotlinVersion.compareVersion(new int[] { 1, 4, 20}, null), 1);
        // normal
        assertEquals(MCKTResolver.KotlinVersion.compareVersion(new int[] { 1, 4, 20}, new int[] { 1, 4, 30}), -1);
        assertEquals(MCKTResolver.KotlinVersion.compareVersion(new int[] { 1, 4, 30}, new int[] { 1, 4, 20}), 1);
    }
}
