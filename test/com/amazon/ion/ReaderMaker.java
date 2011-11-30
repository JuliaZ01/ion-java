// Copyright (c) 2011 Amazon.com, Inc.  All rights reserved.

package com.amazon.ion;

import static com.amazon.ion.TestUtils.ensureBinary;
import static com.amazon.ion.TestUtils.ensureText;

import com.amazon.ion.impl.IonImplUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Abstracts the various ways that {@link IonReader}s can be created, so test
 * cases can cover all the APIs.
 */
public enum ReaderMaker
{
    /**
     * Invokes {@link IonSystem#newReader(String)}.
     */
    FROM_STRING(true, false)
    {
        @Override
        public IonReader newReader(IonSystem system, String ionText)
        {
            return system.newReader(ionText);
        }
    },


    /**
     * Invokes {@link IonSystem#newReader(byte[])} with Ion binary.
     */
    FROM_BYTES_BINARY(false, true)
    {
        @Override
        public IonReader newReader(IonSystem system, byte[] ionData)
        {
            ionData = ensureBinary(system, ionData);
            return system.newReader(ionData);
        }
    },


    /**
     * Invokes {@link IonSystem#newReader(byte[])} with Ion text.
     */
    FROM_BYTES_TEXT(true, false)
    {
        @Override
        public IonReader newReader(IonSystem system, byte[] ionData)
        {
            ionData = ensureText(system, ionData);
            return system.newReader(ionData);
        }
    },


    /**
     * Invokes {@link IonSystem#newReader(byte[],int,int)} with Ion binary.
     */
    FROM_BYTES_OFFSET_BINARY(false, true)
    {
        @Override
        public int getOffset() { return 37; }

        @Override
        public IonReader newReader(IonSystem system, byte[] ionData)
        {
            ionData = ensureBinary(system, ionData);
            byte[] padded = new byte[ionData.length + 70];
            System.arraycopy(ionData, 0, padded, 37, ionData.length);
            return system.newReader(padded, 37, ionData.length);
        }
    },


    /**
     * Invokes {@link IonSystem#newReader(byte[],int,int)} with Ion text.
     */
    FROM_BYTES_OFFSET_TEXT(true, false)
    {
        @Override
        public int getOffset() { return 37; }

        @Override
        public IonReader newReader(IonSystem system, byte[] ionData)
        {
            ionData = ensureText(system, ionData);
            byte[] padded = new byte[ionData.length + 70];
            System.arraycopy(ionData, 0, padded, 37, ionData.length);
            return system.newReader(padded, 37, ionData.length);
        }
    },


    /**
     * Invokes {@link IonSystem#newReader(InputStream)} with Ion binary.
     */
    FROM_INPUT_STREAM_BINARY(false, true)
    {
        @Override
        public IonReader newReader(IonSystem system, byte[] ionData)
        {
            ionData = ensureBinary(system, ionData);
            InputStream in = new ByteArrayInputStream(ionData);
            return system.newReader(in);
        }
    },


    /**
     * Invokes {@link IonSystem#newReader(InputStream)} with Ion text.
     */
    FROM_INPUT_STREAM_TEXT(true, false)
    {
        @Override
        public IonReader newReader(IonSystem system, byte[] ionData)
        {
            ionData = ensureText(system, ionData);
            InputStream in = new ByteArrayInputStream(ionData);
            return system.newReader(in);
        }
    },


    FROM_DOM(false, false)
    {
        @Override
        public IonReader newReader(IonSystem system, String ionText)
        {
            IonDatagram dg = system.getLoader().load(ionText);
            return system.newReader(dg);
        }

        @Override
        public IonReader newReader(IonSystem system, byte[] ionData)
        {
            IonDatagram dg = system.getLoader().load(ionData);
            return system.newReader(dg);
        }
    };


    //========================================================================

    private final boolean mySourceIsText;
    private final boolean mySourceIsBinary;

    private ReaderMaker(boolean sourceIsText, boolean sourceIsBinary)
    {
        mySourceIsText   = sourceIsText;
        mySourceIsBinary = sourceIsBinary;
    }

    public boolean sourceIsText()
    {
        return mySourceIsText;
    }

    public boolean sourceIsBinary()
    {
        return mySourceIsBinary;
    }

    public int getOffset()
    {
        return 0;
    }


    public IonReader newReader(IonSystem system, String ionText)
    {
        byte[] utf8 = IonImplUtils.utf8(ionText);
        return newReader(system, utf8);
    }


    public IonReader newReader(IonSystem system, byte[] ionData)
    {
        IonDatagram dg = system.getLoader().load(ionData);
        String ionText = dg.toString();
        return newReader(system, ionText);
    }


    public static ReaderMaker[] valuesExcluding(ReaderMaker... exclusions)
    {
        ReaderMaker[] all = values();
        ArrayList<ReaderMaker> retained =
            new ArrayList<ReaderMaker>(Arrays.asList(all));
        retained.removeAll(Arrays.asList(exclusions));
        return retained.toArray(new ReaderMaker[retained.size()]);
    }
}
