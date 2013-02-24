package javax.usb.tck.sigtest;

/**
 * Copyright (c) 2003,2004 International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

/**
 * This class stores the name and modifiers for a given member (class or
 * method).
 *
 * @author Matthew J. Duftler 
 */
@SuppressWarnings("all")
public class MemberDesc
{
  protected String name = null;
  protected int modifiers = 0;

  public MemberDesc(String name, int modifiers)
  {
    this.name = name;
    this.modifiers = modifiers;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public void setModifiers(int modifiers)
  {
    this.modifiers = modifiers;
  }

  public int getModifiers()
  {
    return modifiers;
  }
}