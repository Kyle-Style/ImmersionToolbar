package vip.superbrain.immersion.toolbar.java;

import org.junit.Test;

import vip.superbrain.immersion.toolbar.EmojiReader;
import vip.superbrain.immersion.toolbar.utils.EmojiHelper;

public class CharsetTest {

    @Test
    public void testCodePoint() {
        String content = "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC67";
        System.out.println(EmojiHelper.getInstance().getVisionCharCount(content));

//        EmojiUtils.INSTANCE.unicode(content);
        
        System.out.println("666\t" + EmojiReader.INSTANCE.subSequence(content, 0, 4));
//        System.out.println(EmojiUtils.INSTANCE.decodeUnicode2(content));

//        for (String s : EmojiReader.INSTANCE.transToUnicode(content)) {
//            System.out.println(s);
//        }
    }
}
