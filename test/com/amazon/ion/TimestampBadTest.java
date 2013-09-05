// Copyright (c) 2013 Amazon.com, Inc.  All rights reserved.

package com.amazon.ion;

import static com.amazon.ion.TestUtils.BAD_TIMESTAMP_IONTESTS_FILES;
import static com.amazon.ion.TestUtils.GLOBAL_SKIP_LIST;
import static com.amazon.ion.TestUtils.testdataFiles;

import com.amazon.ion.impl._Private_Utils;
import com.amazon.ion.junit.Injected.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 *
 */
public class TimestampBadTest
    extends IonTestCase
{
    @Inject("testFile")
    public static final File[] FILES =
        testdataFiles(GLOBAL_SKIP_LIST, BAD_TIMESTAMP_IONTESTS_FILES);

    private File myTestFile;

    public void setTestFile(File file)
    {
        myTestFile = file;
    }


    @Test
    public void testValueOf()
    throws IOException
    {
        String tsText;
        try
        {
            tsText = _Private_Utils.utf8FileToString(myTestFile);
        }
        catch (IonException e)
        {
            // Bad UTF-8 data, just ignore the file
            return;
        }

        tsText = new BufferedReader(new StringReader(tsText)).readLine();
        tsText = tsText.trim();  // Trim newlines and whitespace

        // TODO some bad files have comments in them

        try
        {
            Timestamp.valueOf(tsText);
            fail("Expected exception");
        }
        catch (IllegalArgumentException e) { }
    }
}
