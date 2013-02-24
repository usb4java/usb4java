package javax.usb.tck;

/**
 * Copyright (c) 2004, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.tck.sigtest.*;
import java.io.*;
import junit.framework.*;

/**
 * This is the API signature test for the entire javax.usb.* package.  
 * This test verifies the existence of all expected public classes and 
 * interfaces.  In those public classes and interfaces it verifies the 
 * existence, correct modifiers, arguments, and throws clauses of 
 * public constructors and methods.  It also verifies the existence and 
 * correct modifiers of public fields.
 * 
 * @author Bob Rossi
 */
@SuppressWarnings("all")
public class SignatureTest extends TestCase
{
    /**
     * Test the signatures of all the classes and methods for compliance
     * with the Javax.usb specification.
     */
    public void testSignatures() throws IOException
    {
        String javaxusbtck_home =
            System.getProperty("javaxusbtck_home", System.getProperty("user.dir"));
        File dir = new File(javaxusbtck_home, "src/test/resources");
        File file = new File(dir, "javax.usb-1.0.sig");
        String result = ProjectDesc.compareProjectFile(file);

        Assert.assertNull(result, result);
    }
}