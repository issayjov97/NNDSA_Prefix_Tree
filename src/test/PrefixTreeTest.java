package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PrefixTreeTest {

    private main.PrefixTree<Character> prefixTree;

    @BeforeEach
    void setUp() {
        this.prefixTree = new main.PrefixTree();
    }

    @Test
    void insert() {
        prefixTree.insert("abc");
        prefixTree.insert("abgl");

        main.PrefixTree<Character>.TreeNode a = prefixTree.getRoot().getChildren().get('a');
         main.PrefixTree<Character>.TreeNode b = a.getChildren().get('b');
         main.PrefixTree<Character>.TreeNode c = b.getChildren().get('c');
         main.PrefixTree<Character>.TreeNode g = b.getChildren().get('g');
         main.PrefixTree<Character>.TreeNode l = g.getChildren().get('l');

        assertNotNull(a);
        assertNotNull(b);
        assertNotNull(c);
        assertNotNull(g);
        assertNotNull(l);
        assertTrue(c.getChildren().isEmpty());
        assertTrue(l.getChildren().isEmpty());
    }

    @Test
    void shouldReturnFalseIfTreeContainsWord() {
        final String word = "abgl";
        prefixTree.insert(word);
        assertTrue(prefixTree.wordSearch(word));
    }

    @Test
    void shouldReturnFalseIfSymbolIsNotEnd() {
        final String word = "abgld";
        final String search = "abgl";

        prefixTree.insert(word);
        assertFalse(prefixTree.wordSearch(search));
    }

    @Test
    void shouldReturnFalseIfSymbolDoesNotExists() {
        final String search = "abgl";

        assertFalse(prefixTree.wordSearch(search));
    }

    @Test
    void shouldReturnTrueIfTreeContainsPrefix() {
        final String word = "abgld";
        final String search = "abgl";

        prefixTree.insert(word);

        assertTrue(prefixTree.prefixSearch(search.substring(0,2)).isEmpty());
        assertTrue(prefixTree.prefixSearch(search).isEmpty());

    }

    @Test
    void shouldReturnFalseIfPrefixDoesNotExists() {
        final String search = "abgl";

        assertFalse(prefixTree.prefixSearch(search).isEmpty());
    }

    @Test
    void prefixSearch() {
        prefixTree.insert("luk");
        prefixTree.insert("rek");
        prefixTree.insert("sek");
        prefixTree.insert("brek");
        prefixTree.insert("marek");
        prefixTree.insert("bor");
        prefixTree.insert("dar");
        prefixTree.insert("zmar");
        prefixTree.insert("magor");
        prefixTree.insert("tabor");

        System.out.println(prefixTree.wordSearch("rek"));
        System.out.println(prefixTree.prefixSearch("ek"));
        prefixTree.delete("rek");
        System.out.println(prefixTree.wordSearch("rek"));
        System.out.println(prefixTree.prefixSearch("ek"));
        System.out.println(prefixTree.hasPrefix("ek"));
        System.out.println(prefixTree.getRoot());

    }

    @Test
    void filePrefixSearch() {
        List<String> words = main.FileService.readWords("src/words.txt");
        words.forEach(it->{
            prefixTree.insert(it);
        });
        System.out.println(prefixTree.prefixSearch("fii"));
        System.out.println(prefixTree.wordSearch("etnografii"));
        prefixTree.delete("rek");
    }

    @Test
    void shouldBFSTraverseTrieTree() {
        prefixTree.insert("abc");
        prefixTree.insert("dfh");
        prefixTree.insert("abgl");
    }

    @Test
    void shouldSetEndOFWordIfKeyContainsChildren() {
        prefixTree.insert("abc");
        prefixTree.insert("abgl");
        prefixTree.insert("abcd");

        prefixTree.delete("abc");

         main.PrefixTree<Character>.TreeNode a = prefixTree.getRoot().getChildren().get('a');
         main.PrefixTree<Character>.TreeNode b = a.getChildren().get('b');
         main.PrefixTree<Character>.TreeNode c = b.getChildren().get('c');


        assertFalse(prefixTree.wordSearch("abc"));
        assertFalse(c.isEndOfWord());
        assertTrue(c.getChildren().containsKey('d'));

    }

    @Test
    void shouldDeleteWholeWord() {
        prefixTree.insert("abc");
        prefixTree.insert("def");

        prefixTree.delete("abc");

        assertFalse(prefixTree.wordSearch("abc"));
        assertNull(prefixTree.getRoot().getChildren().get('a'));
    }
}