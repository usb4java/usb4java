package javax.usb.tck.sigtest;

/**
 * Copyright (c) 2003,2004 International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import java.lang.reflect.*;
import java.io.*;
import java.util.*;

/**
 * This class implements static utility methods for use by the signature
 * test tool.
 *
 * @author Matthew J. Duftler 
 */
@SuppressWarnings("all")
public class SigTestUtils
{
  /**
   * This method builds a new project description by using an existing one
   * as a reference. The new project description is built by resolving each
   * class specified in the reference description, and adding their signatures
   * to the newly built project.
   *
   * @param referencePD the reference project description
   *
   * @return the new project description
   */
  public static ProjectDesc getProjectDesc(ProjectDesc referencePD)
  {
    ProjectDesc candidatePD = new ProjectDesc("CandidateImpl");
    Iterator iterator = referencePD.getClassDescs().iterator();

    while (iterator.hasNext())
    {
      ClassDesc referenceCD = (ClassDesc)iterator.next();
      String className = referenceCD.getName();
      ClassDesc candidateCD = null;

      try
      {
        Class candidateClass = Class.forName(className);

        candidateCD = getClassDesc(candidateClass);
      }
      catch (ClassNotFoundException e)
      {
      }

      if (candidateCD != null)
      {
        candidatePD.addClassDesc(candidateCD);
      }
    }

    return candidatePD;
  }

  public static ClassDesc getClassDesc(Class theClass)
  {
    String className = getClassName(theClass);
    ClassDesc classDesc = new ClassDesc(className,
                                        theClass.getModifiers());
    Class superClass = theClass.getSuperclass();

    if (superClass != null)
    {
      classDesc.setSuperClassName(getClassName(superClass));
    }

    List interfaceNames = getTypeNames(theClass.getInterfaces());

    classDesc.setInterfaceNames(interfaceNames);

    List constructorDescs =
      getConstructorDescs(className, theClass.getDeclaredConstructors());

    classDesc.setConstructorDescs(constructorDescs);

    List methodDescs =
      getMethodDescs(theClass.getDeclaredMethods());

    classDesc.setMethodDescs(methodDescs);

    return classDesc;
  }

  public static List getConstructorDescs(String className,
                                         Constructor[] constructors)
  {
    List constructorDescs = new Vector();

    for (int i = 0; i < constructors.length; i++)
    {
      MethodDesc constructorDesc =
        new MethodDesc(className,
                       constructors[i].getModifiers(),
                       null,
                       getTypeNames(constructors[i].getParameterTypes()),
                       getTypeNames(constructors[i].getExceptionTypes()));

      constructorDescs.add(constructorDesc);
    }

    return constructorDescs;
  }

  public static List getMethodDescs(Method[] methods)
  {
    List methodDescs = new Vector();

    for (int i = 0; i < methods.length; i++)
    {
      MethodDesc methodDesc =
        new MethodDesc(methods[i].getName(),
                       methods[i].getModifiers(),
                       getClassName(methods[i].getReturnType()),
                       getTypeNames(methods[i].getParameterTypes()),
                       getTypeNames(methods[i].getExceptionTypes()));

      methodDescs.add(methodDesc);
    }

    return methodDescs;
  }

  public static List getTypeNames(Class[] types)
  {
    List typeNames = new Vector();

    for (int i = 0; i < types.length; i++)
    {
      typeNames.add(getClassName(types[i]));
    }

    return typeNames;
  }

  public static String listToString(List list)
  {
    StringBuffer strBuf = new StringBuffer();
    int size = list.size();

    for (int i = 0; i < size; i++)
    {
      strBuf.append((i > 0 ? "," : "") +
                    list.get(i));
    }

    return strBuf.toString();
  }

  public static List stringToList(String str, String delim)
  {
    return Arrays.asList(tokenize(str, delim));
  }

  public static String[] tokenize(String tokenStr, String delim)
  {
    StringTokenizer strTok = new StringTokenizer(tokenStr, delim);
    String[] tokens = new String[strTok.countTokens()];

    for (int i = 0; i < tokens.length; i++)
    {
      tokens[i] = strTok.nextToken();
    }

    return tokens;
  }

  public static boolean objectsEqual(Object obj1, Object obj2)
  {
    if (obj1 == null)
    {
      return (obj2 == null);
    }
    else
    {
      return obj1.equals(obj2);
    }
  }

  public static boolean collectionsMatch(Collection c1, Collection c2)
  {
    if (c1 == null)
    {
      return (c2 == null);
    }
    else
    {
      return c1.containsAll(c2) && c2.containsAll(c1);
    }
  }

  public static String getExpandedMethodList(List list)
  {
    StringBuffer strBuf = new StringBuffer("[");
    int size = list.size();

    for (int i = 0; i < size; i++)
    {
      strBuf.append((i > 0 ? ", " : "") +
                    ((MethodDesc)list.get(i)).toString(true));
    }

    strBuf.append("]");

    return strBuf.toString();
  }

  public static String getCondensedClassList(List list)
  {
    StringBuffer strBuf = new StringBuffer("[");
    int size = list.size();

    for (int i = 0; i < size; i++)
    {
      strBuf.append((i > 0 ? ", " : "") +
                    ((ClassDesc)list.get(i)).getName());
    }

    strBuf.append("]");

    return strBuf.toString();
  }

  public static void findExtras(Collection reference,
                                Collection candidate,
                                List referenceExtras,
                                List candidateExtras)
  {
    referenceExtras.addAll(reference);
    referenceExtras.removeAll(candidate);
    candidateExtras.addAll(candidate);
    candidateExtras.removeAll(reference);

    if (candidateExtras.size() > 0)
    {
      Iterator memberDescs = candidateExtras.iterator();

      while (memberDescs.hasNext())
      {
        MemberDesc memberDesc = (MemberDesc)memberDescs.next();
        int modifiers = memberDesc.getModifiers();

        if (Modifier.isPrivate(modifiers)
            || !(Modifier.isProtected(modifiers)
                 || Modifier.isPublic(modifiers)))
        {
          memberDescs.remove();
        }
      }
    }
  }

  public static void findExtraClasses(ProjectDesc referencePD,
                                      ProjectDesc candidatePD,
                                      List referenceExtras,
                                      List candidateExtras)
  {
    referenceExtras.addAll(referencePD.getClassDescs());
    candidateExtras.addAll(candidatePD.getClassDescs());

    Iterator iterator = referenceExtras.iterator();

    while (iterator.hasNext())
    {
      ClassDesc referenceCD = (ClassDesc)iterator.next();
      ClassDesc candidateCD =
        candidatePD.getClassDesc(referenceCD.getName());

      if (candidateCD != null)
      {
        iterator.remove();
      }
    }

    iterator = candidateExtras.iterator();

    while (iterator.hasNext())
    {
      ClassDesc candidateCD = (ClassDesc)iterator.next();
      ClassDesc referenceCD =
        referencePD.getClassDesc(candidateCD.getName());

      if (referenceCD != null)
      {
        iterator.remove();
      }
    }
  }

  /*
    This method will return the correct name for a class object representing
    a primitive, a single instance of a class, as well as n-dimensional arrays
    of primitives or instances. This logic is needed to handle the string returned
    from Class.getName(). If the class object represents a single instance (or
    a primitive), Class.getName() returns the fully-qualified name of the class
    and no further work is needed. However, if the class object represents an
    array (of n dimensions), Class.getName() returns a Descriptor (the Descriptor
    grammar is defined in section 4.3 of the Java VM Spec). This method will
    parse the Descriptor if necessary.
  */
  public static String getClassName(Class targetClass)
  {
    String className = targetClass.getName();

    return targetClass.isArray() ? parseDescriptor(className) : className;
  }

  /*
    See the comment above for getClassName(targetClass)...
  */
  private static String parseDescriptor(String className)
  {
    char[] classNameChars = className.toCharArray();
    int    arrayDim       = 0;
    int    i              = 0;

    while (classNameChars[i] == '[')
    {
      arrayDim++;
      i++;
    }

    StringBuffer classNameBuf = new StringBuffer();

    switch (classNameChars[i++])
    {
      case 'B' : classNameBuf.append("byte");
                 break;
      case 'C' : classNameBuf.append("char");
                 break;
      case 'D' : classNameBuf.append("double");
                 break;
      case 'F' : classNameBuf.append("float");
                 break;
      case 'I' : classNameBuf.append("int");
                 break;
      case 'J' : classNameBuf.append("long");
                 break;
      case 'S' : classNameBuf.append("short");
                 break;
      case 'Z' : classNameBuf.append("boolean");
                 break;
      case 'L' : classNameBuf.append(classNameChars,
                                     i, classNameChars.length - i - 1);
                 break;
    }

    for (i = 0; i < arrayDim; i++)
      classNameBuf.append("[]");

    return classNameBuf.toString();
  }

  private static OutputStream getOutputStream(String root,
                                              String name,
                                              boolean overwrite,
                                              boolean verbose)
                                                throws IOException
  {
    if (root != null)
    {
      File directory = new File(root);

      if (!directory.exists())
      {
        if (!directory.mkdirs())
        {
          throw new IOException("Failed to create directory '" + root + "'.");
        }
        else if (verbose)
        {
          System.out.println("Created directory '" +
                             directory.getAbsolutePath() + "'.");
        }
      }
    }

    File file = new File(root, name);
    String absolutePath = file.getAbsolutePath();

    if (file.exists())
    {
      if (!overwrite)
      {
        throw new IOException("File '" + absolutePath + "' already exists. " +
                              "Please remove it or enable the " +
                              "overwrite option.");
      }
      else
      {
        file.delete();

        if (verbose)
        {
          System.out.println("Deleted file '" + absolutePath + "'.");
        }
      }
    }

    FileOutputStream fos = new FileOutputStream(absolutePath);

    if (verbose)
    {
      System.out.println("Created file '" + absolutePath + "'.");
    }

    return fos;
  }

  public static void generateProjectFile(String classListFile,
                                         String projectFile,
                                         boolean overwrite)
                                           throws IOException,
                                                  ClassNotFoundException
  {
    FileReader in = new FileReader(classListFile);
    BufferedReader buf = new BufferedReader(in);
    OutputStream out = getOutputStream(null, projectFile, overwrite, true);
    PrintWriter pw = new PrintWriter(out);
    String tempLine;

    while ((tempLine = buf.readLine()) != null)
    {
      pw.println(getClassDesc(Class.forName(tempLine)));
    }

    pw.flush();
    pw.close();
    buf.close();
    in.close();
  }
}
