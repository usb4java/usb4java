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
import java.util.*;

/**
 * This class stores all the information needed to identify a method
 * signature.
 *
 * @author Matthew J. Duftler 
 */
@SuppressWarnings("all")
public class MethodDesc extends MemberDesc
{
  private String returnTypeName = null;
  private List paramTypeNames = new Vector();
  private List exceptionTypeNames = new Vector();

  public MethodDesc(String name,
                    int modifiers,
                    String returnTypeName,
                    List paramTypeNames,
                    List exceptionTypeNames)
  {
    super(name, modifiers);

    this.returnTypeName = returnTypeName;
    this.paramTypeNames = paramTypeNames;
    this.exceptionTypeNames = exceptionTypeNames;
  }

  public void setReturnTypeName(String returnTypeName)
  {
    this.returnTypeName = returnTypeName;
  }

  public String getReturnTypeName()
  {
    return returnTypeName;
  }

  public void setParameterTypeNames(List paramTypeNames)
  {
    this.paramTypeNames = paramTypeNames;
  }

  public List getParameterTypeNames()
  {
    return paramTypeNames;
  }

  public void setExceptionTypeNames(List exceptionTypeNames)
  {
    this.exceptionTypeNames = exceptionTypeNames;
  }

  public List getExceptionTypeNames()
  {
    return exceptionTypeNames;
  }

  public static MethodDesc parseMethodDesc(String methodDescStr)
  {
    String[] tokens = SigTestUtils.tokenize(methodDescStr, " ");

    int modifiers = Integer.parseInt(tokens[0]);
    String returnTypeName = null;
    int offset = 0;

    if (!tokens[2].startsWith("("))
    {
      returnTypeName = tokens[1];
      offset = 1;
    }

    String methodName = tokens[1 + offset];
    String parameterTypeNamesStr = tokens[2 + offset];

    parameterTypeNamesStr =
      parameterTypeNamesStr.substring(1, parameterTypeNamesStr.length() - 1);

    List parameterTypeNames =
      SigTestUtils.stringToList(parameterTypeNamesStr, ",");
    List exceptionTypeNames;

    if (tokens.length > (3 + offset))
    {
      String exceptionTypeNamesStr = tokens[4 + offset];

      exceptionTypeNames =
        SigTestUtils.stringToList(exceptionTypeNamesStr, ",");
    }
    else
    {
      exceptionTypeNames = new Vector();
    }

    MethodDesc methodDesc =
      new MethodDesc(methodName,
                     modifiers,
                     returnTypeName,
                     parameterTypeNames,
                     exceptionTypeNames);

    return methodDesc;
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
      MethodDesc that = (MethodDesc)obj;

      if (!SigTestUtils.objectsEqual(name, that.name))
      {
        return false;
      }
      else if (modifiers != that.modifiers)
      {
        return false;
      }
      else if (!SigTestUtils.objectsEqual(returnTypeName, that.returnTypeName))
      {
        return false;
      }
      else if (!SigTestUtils.objectsEqual(paramTypeNames, that.paramTypeNames))
      {
        return false;
      }
      else if (!SigTestUtils.collectionsMatch(exceptionTypeNames,
                                              that.exceptionTypeNames))
      {
        return false;
      }
      else
      {
        return true;
      }
    }
  }

  public int hashCode()
  {
    return toString().hashCode();
  }

  public String toString(boolean expandModifiers)
  {
    return (expandModifiers
            ? Modifier.toString(modifiers)
            : modifiers + "") + " " +
           (returnTypeName != null ? returnTypeName + " " : "") +
           name + " (" + SigTestUtils.listToString(paramTypeNames) + ")" +
           (exceptionTypeNames.size() > 0
            ? " throws " + SigTestUtils.listToString(exceptionTypeNames)
            : "");
  }

  public String toString()
  {
    return toString(false);
  }
}