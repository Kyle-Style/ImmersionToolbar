package vip.superbrain.immersion.toolbar.java;

import org.junit.Test;

import java.util.ArrayList;

import vip.superbrain.immersion.toolbar.data.DataTest;

public class DataInfoTest {

    @Test
    public void testData() {
        DataTest test = new DataTest();
        test.setData(new ArrayList<String>(){{
            add("666");
        }});
        test.getData222();
        System.out.println();
        System.out.println();
        test.setData(new ArrayList<String>(){{
            add("888");
        }});
        test.getData222();
    }
}
