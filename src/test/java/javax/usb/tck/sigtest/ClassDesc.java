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
import java.lang.reflect.*;
import java.util.*;

/**
 * This class stores all the information needed to compare the signature
 * of one class to another.
 *
 * @author Matthew J. Duftler 
 */
@SuppressWarnings("all")
public class ClassDesc extends MemberDesc
{
  private String superClassName = null;
  private List interfaceNames = new Vector();
  private List constructorDescs = new Vector();
  private List methodDescs = new Vector();

  public ClassDesc(String name, int modifiers)
  {
    super(name, modifiers);
  }

  public void setSuperClassName(String superClassName)
  {
    this.superClassName = superClassName;
  }

  public String getSuperClassName()
  {
    return superClassName;
  }

  public void setInterfaceNames(List interfaceNames)
  {
    this.interfaceNames = interfaceNames;
  }

  public List getInterfaceNames()
  {
    return interfaceNames;
  }

  public void addConstructorDesc(MethodDesc constructorDesc)
  {
    constructorDescs.add(constructorDesc);
  }

  public void setConstructorDescs(List constructorDescs)
  {
    this.constructorDescs = constructorDescs;
  }

  public List getConstructorDescs()
  {
    return constructorDescs;
  }

  public void addMethodDesc(MethodDesc methodDesc)
  {
    methodDescs.add(methodDesc);
  }

  public void setMethodDescs(List methodDescs)
  {
    this.methodDescs = methodDescs;
  }

  public List getMethodDescs()
  {
    return methodDescs;
  }

  public static ClassDesc parseClassDesc(BufferedReader classDescReader)
    throws IOException
  {
    String classDescStr = classDescReader.readLine();
    List tokens = SigTestUtils.stringToList(classDescStr, " ");
    int size = tokens.size();
    int i = 0;
    int modifiers = Integer.parseInt((String)tokens.get(i++));
    String className = (String)tokens.get(i++);
    String superClassName = null;
    String interfaceNamesListStr = null;
    List interfaceNames = null;

    while (size > i)
    {
      String tempToken = (String)tokens.get(i++);

      if (tempToken.equals("extends"))
      {
        superClassName = (String)tokens.get(i++);
      }
      else if (tempToken.equals("implements"))
      {
        interfaceNamesListStr = (String)tokens.get(i++);
      }
    }

    if (interfaceNamesListStr != null)
    {
      interfaceNames = SigTestUtils.stringToList(interfaceNamesListStr, ",");
    }

    ClassDesc classDesc = new ClassDesc(className, modifiers);

    classDesc.setSuperClassName(superClassName);

    if (interfaceNames != null)
    {
      classDesc.setInterfaceNames(interfaceNames);
    }

    classDescReader.readLine();

    String tempLine = null;

    while (!(tempLine = classDescReader.readLine()).equals("}"))
    {
      MethodDesc methodDesc = MethodDesc.parseMethodDesc(tempLine);

      if (methodDesc.getReturnTypeName() == null)
      {
        classDesc.addConstructorDesc(methodDesc);
      }
      else
      {
        classDesc.addMethodDesc(methodDesc);
      }
    }

    return classDesc;
  }

  public boolean equals(Object obj)
  {
    if (obj == null)
    {
      return false;
    }
    else if (obj == this)
    {
      return true;
    }
    else
    {
      ClassDesc that = (ClassDesc)obj;

      if (!SigTestUtils.objectsEqual(name, that.name))
      {
        return false;
      }
      else if (modifiers != that.modifiers)
      {
        return false;
      }
      else if (!SigTestUtils.objectsEqual(superClassName, that.superClassName))
      {
        return false;
      }
      else if (!SigTestUtils.collectionsMatch(interfaceNames,
                                              that.interfaceNames))
      {
        return false;
      }
      else if (!SigTestUtils.collectionsMatch(constructorDescs,
                                              that.constructorDescs))
      {
        return false;
      }
      else if (!SigTestUtils.collectionsMatch(methodDescs, that.methodDescs))
      {
        return false;
      }
      else
      {
        return true;
      }
    }
  }

  public String compare(ClassDesc that)
  {
    StringBuffer strBuf = new StringBuffer();

    if (modifiers != that.modifiers)
    {
      strBuf.append("On class '" + name + "', the modifiers are '" +
                    Modifier.toString(that.modifiers) +
                    "', when they should be '" +
                    Modifier.toString(modifiers) + "'.");
    }

    if (!SigTestUtils.objectsEqual(superClassName, that.superClassName))
    {
      strBuf.append("\nClass '" + name + "' " +
                    (that.superClassName != null
                     ? "extends '" + that.superClassName + "'"
                     : "does not extend any class") +
                    ", when it should" +
                    (superClassName != null
                     ? " extend '" + superClassName + "'."
                     : "n't extend any class."));
    }

    if (!SigTestUtils.collectionsMatch(interfaceNames, that.interfaceNames))
    {
      strBuf.append("\nClass '" + name + "' " +
                    (that.interfaceNames.size() > 0
                    ? "implements '" + that.interfaceNames + "'"
                    : "does not implement any interfaces") +
                   ", when it should" +
                   (interfaceNames.size() > 0
                    ? " implement '" + interfaceNames + "'."
                    : "n't implement any interfaces."));
    }

    List referenceExtras = new Vector();
    List candidateExtras = new Vector();

    SigTestUtils.findExtras(constructorDescs,
                            that.constructorDescs,
                            referenceExtras,
                            candidateExtras);

    if (referenceExtras.size() > 0 || candidateExtras.size() > 0)
    {
      strBuf.append("\nClass '" + name + "'" +
                    (candidateExtras.size() > 0
                     ? " has extraneous constructor" +
                       (candidateExtras.size() > 1 ? "s" : "") + " " +
                       SigTestUtils.getExpandedMethodList(candidateExtras)
                     : "") +
                    (referenceExtras.size() > 0
                     ? (candidateExtras.size() > 0 ? " and" : "") +
                       " is missing constructor" +
                       (referenceExtras.size() > 1 ? "s" : "") + " " +
                       SigTestUtils.getExpandedMethodList(referenceExtras)
                     : "") + ".");
    }

    referenceExtras = new Vector();
    candidateExtras = new Vector();

    SigTestUtils.findExtras(methodDescs,
                            that.methodDescs,
                            referenceExtras,
                            candidateExtras);

    if (referenceExtras.size() > 0 || candidateExtras.size() > 0)
    {
      strBuf.append("\nClass '" + name + "'" +
                    (candidateExtras.size() > 0
                     ? " has extraneous method" +
                       (candidateExtras.size() > 1 ? "s" : "") + " " +
                       SigTestUtils.getExpandedMethodList(candidateExtras)
                     : "") +
                    (referenceExtras.size() > 0
                     ? (candidateExtras.size() > 0 ? " and" : "") +
                       " is missing method" +
                       (referenceExtras.size() > 1 ? "s" : "") + " " +
                       SigTestUtils.getExpandedMethodList(referenceExtras)
                     : "") + ".");
    }

    return (strBuf.length() > 0)
            ? strBuf.toString()
            : null;
  }

  public String toString()
  {
    StringBuffer strBuf = new StringBuffer();

    strBuf.append(modifiers + " " + name +
                  (superClassName != null
                   ? " extends " + superClassName
                   : "") +
                  (interfaceNames.size() > 0
                   ? " implements " + SigTestUtils.listToString(interfaceNames)
                   : "") +
                  "\n{\n");

    Iterator constrIterator = constructorDescs.iterator();

    while (constrIterator.hasNext())
    {
      strBuf.append("  " + constrIterator.next() + "\n");
    }

    Iterator methodIterator = methodDescs.iterator();

    while (methodIterator.hasNext())
    {
      strBuf.append("  " + methodIterator.next() + "\n");
    }

    strBuf.append("}");

    return strBuf.toString();
  }

  public int hashCode()
  {
    return toString().hashCode();
  }
}