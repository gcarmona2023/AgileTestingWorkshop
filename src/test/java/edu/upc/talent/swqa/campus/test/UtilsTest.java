package edu.upc.talent.swqa.campus.test;

import static edu.upc.talent.swqa.test.utils.Asserts.assertEquals;
import static edu.upc.talent.swqa.util.Utils.union;
import org.junit.jupiter.api.Test;

import java.util.Set;

public final class UtilsTest {

  @Test
  public void testSetUnion() {
    final var set1 = Set.of(1, 2, 3);
    assertEquals(set1, union(set1, Set.of()));
    assertEquals(set1, union(Set.of(), set1));
    assertEquals(Set.of(1, 2, 3, 4), union(set1, Set.of(4)));
  }

}
