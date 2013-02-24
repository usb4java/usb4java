package javax.usb.tck.sigtest;

/**
 * Copyright (c) 2003,2004 International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.io.*;
import java.util.*;

/**
 * This class stores a name and a collection of class descriptions,
 * to form a project.
 *
 * @author Matthew J. Duftler 
 */
@SuppressWarnings("all")
public class ProjectDesc
{
  private String name;
  private List classDescs = new Vector();

  public ProjectDesc(String name)
  {
    this.name = name;
  }

  public void addClassDesc(ClassDesc classDesc)
  {
    classDescs.add(classDesc);
  }

  public ClassDesc getClassDesc(String className)
  {
    Iterator iterator = classDescs.iterator();

    while (iterator.hasNext())
    {
      ClassDesc classDesc = (ClassDesc)iterator.next();

      if (SigTestUtils.objectsEqual(classDesc.getName(), className))
      {
        return classDesc;
      }
    }

    return null;
  }

  public void setClassDescs(List classDescs)
  {
    this.classDescs = classDescs;
  }

  public List getClassDescs()
  {
    return classDescs;
  }

  /**
   * This method compares this project description to the specified one.
   *
   * @param that the project description to compare this one to
   *
   * @return a description of the differences, or null if they match perfectly
   */
  public String compare(ProjectDesc that)
  {
    StringBuffer strBuf = new StringBuffer();
    Iterator iterator = classDescs.iterator();

    while (iterator.hasNext())
    {
      ClassDesc thisCD = (ClassDesc)iterator.next();
      ClassDesc thatCD = that.getClassDesc(thisCD.getName());

      if (thatCD != null)
      {
        String result = thisCD.compare(thatCD);

        if (result != null)
        {
          strBuf.append("\n" + result);
        }
      }
    }

    List referenceExtras = new Vector();
    List candidateExtras = new Vector();

    SigTestUtils.findExtraClasses(this,
                                  that,
                                  referenceExtras,
                                  candidateExtras);

    if (referenceExtras.size() > 0 || candidateExtras.size() > 0)
    {
      strBuf.append("\nProject '" + that.name + "'" +
                    (candidateExtras.size() > 0
                     ? " has extraneous class" +
                       (candidateExtras.size() > 1 ? "es" : "") + " " +
                       SigTestUtils.getCondensedClassList(candidateExtras)
                     : "") +
                    (referenceExtras.size() > 0
                     ? (candidateExtras.size() > 0 ? " and" : "") +
                       " is missing class" +
                       (referenceExtras.size() > 1 ? "es" : "") + " " +
                       SigTestUtils.getCondensedClassList(referenceExtras)
                     : "") + ".");
    }

    return (strBuf.length() > 0)
            ? strBuf.toString()
            : null;
  }

  /**
   * This method compares the specified project file to the classes
   * currently available on the classpath.
   *
   * @param file the project file to read from
   *
   * @return a description of the differences, or null if they match perfectly
   */
  public static String compareProjectFile(File projectFile)
    throws IOException
  {
    ProjectDesc riPD = readProjectFile("ReferenceImpl", projectFile);
    ProjectDesc cdPD = SigTestUtils.getProjectDesc(riPD);

    return riPD.compare(cdPD);
  }

  public String toString()
  {
    StringBuffer strBuf = new StringBuffer("Project '" + name + "': ");
    int size = classDescs.size();

    for (int i = 0; i < size; i++)
    {
      strBuf.append((i > 0 ? ", " : "") +
                    ((ClassDesc)classDescs.get(i)).getName());
    }

    return strBuf.toString();
  }

  /**
   * This method reads the specified project file into memory.
   *
   * @param projectName the name to be used when referring to the project in
   * error messages
   * @param file the project file to read from
   *
   * @return a model of the project
   */
  public static ProjectDesc readProjectFile(String projectName,
                                            File file)
                                              throws IOException
  {
    ProjectDesc pd = new ProjectDesc(projectName);
    FileReader fileReader = new FileReader(file);
    BufferedReader buf = new BufferedReader(fileReader);

    while (buf.ready())
    {
      pd.addClassDesc(ClassDesc.parseClassDesc(buf));
    }

    buf.close();
    fileReader.close();

    return pd;
  }
}
